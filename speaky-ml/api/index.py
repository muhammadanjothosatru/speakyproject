import os

from tensorflow.keras import models
from flask import Flask, request
from datetime import datetime
import numpy as np
import json

from pydub import AudioSegment
import librosa
import requests
import moviepy.editor as mp
import python_speech_features

from imutils import face_utils
import cv2, dlib

app = Flask(__name__)

version = '1'

path_model_disfluency = './model/disfluency/model_disfluency_v' + version + '.h5'
path_model_eyegaze = './model/gaze/model_gaze_v' + version + '.h5'
path_model_blink = './model/blink/model_blink_v' + version + '.h5'

parent_path = ""

model_disfluency = models.load_model(path_model_disfluency)
model_eyegaze = models.load_model(path_model_eyegaze)
model_blink = models.load_model(path_model_blink)

IMG_SIZE = (64, 56)
B_SIZE = (64, 64)
margin = 95
class_labels = ['center', 'left', 'right']
detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor('shape_predictor_68_face_landmarks.dat')
font_letter = cv2.FONT_HERSHEY_SIMPLEX


# create JSON
def createJSON(timestamp, score, dis_count, eye_count, urlvideo):
    value = {
        "timestamp": timestamp,
        "score": score,
        "blink": {
            "value": eye_count
        },
        "disfluency": {
            "value": dis_count
        },
        "urlvideo": urlvideo
    }
    return json.dumps(value)

# scoring
def calc_score(dis_count, eye_count):
    dis_score = 0
    eye_score = 0
    if dis_count < 10:
        dis_score = 500
    elif dis_count < 20 & dis_count > 10:
        dis_score = 375
    elif dis_count < 30 & dis_count > 20:
        dis_score = 250
    elif dis_count < 50 & dis_count > 40:
        dis_score = 125
    else:
        dis_score = 0

    if eye_count < 10:
        eye_score = 500
    elif eye_count < 20 & eye_count > 10:
        eye_score = 375
    elif eye_count < 30 & eye_count > 20:
        eye_score = 250
    elif eye_count < 50 & eye_count > 40:
        eye_score = 125
    else:
        eye_score = 0

    score = ((33 / 100) * dis_score) + ((67 / 100) * eye_score)

    return score

# Flask webapp
@app.route("/api")
def api():
    filepath = request.args.get('link')
    token = request.args.get('token')
    uid = request.args.get('uid')

    url = filepath + "&token=" + token
    url = url.replace('video/', 'video%2F').replace('/videoFile.mp4', '%2FvideoFile.mp4')

    global parent_path
    parent_path = "data/" + uid
    video_path = parent_path + "/video.mp4"

    if not os.path.exists(parent_path):
        os.makedirs(parent_path)

    with open(video_path, 'wb') as f:
        r = requests.get(url, stream=True)
        total_length = r.headers.get('content-length')
        if total_length is None:
            f.write(r.content)
        else:
            dl = 0
            for data in r.iter_content(chunk_size=4096):
                dl += len(data)
                f.write(data)

    dis_count = audioprocess(video_path)
    eye_count = video_process(video_path)
    score = calc_score(dis_count, eye_count)
    timestamp = datetime.now()
    os.remove(video_path)
    os.rmdir(parent_path)
    return createJSON(str(timestamp), score, dis_count, eye_count, url)


# Video process
def detect_gaze(eye_img):
    pred_l = model_eyegaze.predict(eye_img)
    gaze = class_labels[np.argmax(pred_l)]
    return gaze


def detect_blink(eye_img):
    pred_B = model_blink.predict(eye_img)
    status = pred_B[0][0]
    status = status * 100
    status = round(status, 3)
    return status


def crop_eye(img, eye_points):
    x1, y1 = np.amin(eye_points, axis=0)
    x2, y2 = np.amax(eye_points, axis=0)
    cx, cy = (x1 + x2) / 2, (y1 + y2) / 2

    w = (x2 - x1) * 1.2
    h = w * IMG_SIZE[1] / IMG_SIZE[0]

    margin_x, margin_y = w / 2, h / 2

    min_x, min_y = int(cx - margin_x), int(cy - margin_y)
    max_x, max_y = int(cx + margin_x), int(cy + margin_y)

    eye_rect = np.rint([min_x, min_y, max_x, max_y]).astype(np.int)

    eye_img = gray[eye_rect[1]:eye_rect[3], eye_rect[0]:eye_rect[2]]

    return eye_img, eye_rect


def video_process(path_video):
    cap = cv2.VideoCapture(path_video)
    frames_to_blink = 6
    blinking_frames = 0
    eye_count = 0
    while (cap.isOpened()):

        # Capture frame-by-frame
        ret, frame = cap.read()
        if ret == True:

            # Display the resulting frame
            frame = cv2.flip(frame, flipCode=1)
            resize = cv2.resize(frame, (240, 426))
            global gray
            gray = cv2.cvtColor(resize, cv2.COLOR_BGR2GRAY)

            faces = detector(gray)

            for face in faces:
                shapes = predictor(gray, face)

                for n in range(36, 42):
                    x = shapes.part(n).x
                    y = shapes.part(n).y
                    next_point = n + 1
                    if n == 41:
                        next_point = 36

                    x2 = shapes.part(next_point).x
                    y2 = shapes.part(next_point).y
                    cv2.line(resize, (x, y), (x2, y2), (0, 69, 255), 2)

                for n in range(42, 48):
                    x = shapes.part(n).x
                    y = shapes.part(n).y
                    next_point = n + 1
                    if n == 47:
                        next_point = 42

                    x2 = shapes.part(next_point).x
                    y2 = shapes.part(next_point).y
                    cv2.line(resize, (x, y), (x2, y2), (153, 0, 153), 2)
                shapes = face_utils.shape_to_np(shapes)
                # ~~~~~~~~~~~~~~~~~56,64 EYE IMAGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
                eye_img_l, eye_rect_l = crop_eye(gray, eye_points=shapes[36:42])
                eye_img_r, eye_rect_r = crop_eye(gray, eye_points=shapes[42:48])
                # ~~~~~~~~~~~~~~~~FOR THE EYE FINAL_WINDOW~~~~~~~~~~~~~~~~~~~~~~#
                eye_img_l_view = cv2.resize(eye_img_l, dsize=(128, 112))
                eye_img_l_view = cv2.cvtColor(eye_img_l_view, cv2.COLOR_BGR2RGB)
                eye_img_r_view = cv2.resize(eye_img_r, dsize=(128, 112))
                eye_img_r_view = cv2.cvtColor(eye_img_r_view, cv2.COLOR_BGR2RGB)
                # ~~~~~~~~~~~~~~~~~FOR THE BLINK DETECTION~~~~~~~~~~~~~~~~~~~~~~~
                eye_blink_left = cv2.resize(eye_img_l.copy(), B_SIZE)
                eye_blink_right = cv2.resize(eye_img_r.copy(), B_SIZE)
                eye_blink_left_i = eye_blink_left.reshape((1, B_SIZE[1], B_SIZE[0], 1)).astype(np.float32) / 255.
                eye_blink_right_i = eye_blink_right.reshape((1, B_SIZE[1], B_SIZE[0], 1)).astype(np.float32) / 255.
                # ~~~~~~~~~~~~~~~~FOR THE GAZE DETECTIOM~~~~~~~~~~~~~~~~~~~~~~~~#
                eye_img_l = cv2.resize(eye_img_l, dsize=IMG_SIZE)
                eye_input_g = eye_img_l.copy().reshape((1, IMG_SIZE[1], IMG_SIZE[0], 1)).astype(np.float32) / 255.
                # ~~~~~~~~~~~~~~~~~~PREDICTION PROCESS~~~~~~~~~~~~~~~~~~~~~~~~~~#

                status_l = detect_blink(eye_blink_left_i)
                gaze = detect_gaze(eye_input_g)
                if gaze == class_labels[1]:
                    blinking_frames += 1
                    if blinking_frames == frames_to_blink:
                        eye_count += 1
                elif gaze == class_labels[2]:
                    blinking_frames += 1
                    if blinking_frames == frames_to_blink:
                        eye_count += 1
                if status_l < 0.1:
                    blinking_frames += 1
                    if blinking_frames == frames_to_blink:
                        eye_count += 1
                else:
                    blinking_frames = 0
        else:
            break

    # When everything done, release
    # the video capture object
    cap.release()

    # Closes all the frames
    cv2.destroyAllWindows()

    return eye_count


# Disfluency
def calc_mfcc(path):
    signal, fs = librosa.load(path, sr=8000)

    mfccs = python_speech_features.base.mfcc(signal,
                                             samplerate=fs,
                                             winlen=0.256,
                                             winstep=0.050,
                                             numcep=16,
                                             nfilt=26,
                                             nfft=2048,
                                             preemph=0.0,
                                             ceplifter=0,
                                             appendEnergy=False,
                                             winfunc=np.hanning)
    return mfccs.transpose()


def slicesound(path, st, sp):
    audio = AudioSegment.from_wav(path)
    crop = audio[st: sp]
    subsound_path = parent_path + '/subsound.wav'
    crop.export(subsound_path, format="wav")
    mfcc = calc_mfcc(subsound_path)
    os.remove(subsound_path)
    return mfcc


def audiopredict(audio):
    st = 0
    sp = 1000
    dis_count = 0
    for i in range(60):
        mfcc = slicesound(audio, st, sp)
        if mfcc.shape[1] < 16:
            x = 16 - mfcc.shape[1]
            zero = np.zeros((16, x))
            mfcc = np.append(mfcc, zero)
        st += 500
        sp += 500
        mfcc = mfcc.reshape(1, 16, 16, 1)
        det = model_disfluency.predict(mfcc)
        if det == 1.0:
            dis_count += 1
    return dis_count


def audioprocess(path):
    sound = mp.VideoFileClip(path)
    sound_path = parent_path + "/sound.wav"
    sound.audio.write_audiofile(filename=sound_path, fps=16000, nbytes=2, buffersize=2000, codec='pcm_s16le',
                                ffmpeg_params=["-ac", "1"])
    dis_count = audiopredict(sound_path)
    os.remove(sound_path)
    return dis_count


@app.route("/")
def user():
    return "Nothing here bro"


if __name__ == '__main__':
    app.run()
