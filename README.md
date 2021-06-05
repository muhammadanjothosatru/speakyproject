# SPEAKY PROJECT
Speaky is a self learning public speaking machine learning based. The machine learning is used to assess disfluency, eye gazing, and eye blinking. After that the model deployed in google cloud platform with cloud functions and integrated with firebase. Android application trigger send mfcc to storage and trigger API to pass input to the model. After that, the result is sent to application again. 

## Machine Learning
- Make datasets or find public datasets for disfluent speech, eye blink, and eye gaze. 
- For the disfluent speech dataset, we use xxxxxxxxxxxxx
- For the eye blink dataset, we use a public dataset from MRL Eye Dataset as shown in ``Dataset info.md``
- For the eye gaze dataset, we use datasets from a conference paper entitled “EYE CONTROLLED VIRTUAL KEYBOARD USING CONVOLUTIONAL NEURAL NETWORKS” as shown in the ``Dataset       info.md``
- Make model for speech disfluency, eye blink, and eye gaze detection
- Save and export the existing model to .h5 file format
- Deploy the existing model using Flask 

## Android
- Define database
- Make User Interface and User experience in kotlin
- Create a basic features
- Record video and sound to upload in cloud for machine learning processing to get point
- Connect to API with Retrofit
- Save point and assessment history

## Cloud
- Use Google Cloud Platform as foundational architecture.
- Create a project in Google Cloud Platform.
- Add team members and manage roles.
- Create a storage bucket that is close enough to our market service. In our case it's in asia-southeast2 (Jakarta). It is a regional bucket so the service can serve fast enough.
- Create Cloud Computing Architecture for web server. We choose cloud computing service because we can freely design the architecture.
- Create Flask to make API work for deployed model on Virtual Machine.
- Testing the API.
- Deploy flask in virtual machine

# Deployment

```bash
import os

from tensorflow.keras import models
from flask import Flask, request
from datetime import datetime
import numpy as np
import json
import time

from pydub import AudioSegment
import librosa
import requests
import moviepy.editor as mp
import sklearn.preprocessing as sp

from imutils import face_utils
import cv2
import dlib

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


# create JSON
def createJSON(timestamp, score, dis_count, gaze_count, blink_count, urlvideo):
    value = {
        "timestamp": timestamp,
        "score": score,
        "blink": {
            "value": blink_count
        },
        "gaze": {
            "value": gaze_count
        },
        "disfluency": {
            "value": dis_count
        },
        "urlvideo": urlvideo
    }
    return json.dumps(value)


# scoring
def calc_score(dis_count, blink_count, gaze_count):
    dis_score = 0
    gaze_score = 0
    blink_score = 0

    if dis_count < 100:
        dis_score = 500
    elif dis_count < 150 & dis_count > 100:
        dis_score = 375
    elif dis_count < 200 & dis_count > 150:
        dis_score = 250
    elif dis_count < 250 & dis_count > 200:
        dis_score = 125
    else:
        dis_score = 0

    if blink_count < 7:
        blink_score = 500
    elif blink_count == 8:
        blink_score = 375
    elif blink_count == 9:
        blink_score = 250
    elif blink_count == 10:
        blink_score = 125
    else:
        blink_score = 0

    if gaze_count < 10:
        gaze_score = 500
    elif gaze_count < 20 & gaze_count > 10:
        gaze_score = 375
    elif gaze_count < 30 & gaze_count > 20:
        gaze_score = 250
    elif gaze_count < 50 & gaze_count > 40:
        gaze_score = 125
    else:
        gaze_score = 0

    score = ((33 / 100) * dis_score) + ((33 / 100) * blink_score) + ((33 / 100) * gaze_score)
    print(dis_count, gaze_count, blink_count, score)

    return score


# Flask webapp
@app.route("/api", methods=['GET'])
def api():
    filepath = request.args.get('link')
    token = request.args.get('token')
    uid = request.args.get('uid')

    url = filepath + "&token=" + token
    url = url.replace('video/', 'video%2F').replace('/video-', '%2Fvideo-')

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
    gaze_count, blink_count = video_process(video_path)
    score = calc_score(dis_count, gaze_count, blink_count)
    timestamp = datetime.now()
    os.remove(video_path)
    os.rmdir(parent_path)
    return createJSON(str(timestamp), score, dis_count, gaze_count, blink_count, url)


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
    frames_to_gaze = 30
    blink_frame = 0
    gaze_frame = 0
    blink_count = 0
    gaze_count = 0
    while (cap.isOpened()):

        ret, frame = cap.read()
        if ret == True:

            resize = cv2.flip(frame, flipCode=1)
            global gray
            gray = cv2.cvtColor(resize, cv2.COLOR_BGR2GRAY)

            faces = detector(gray)

            for face in faces:
                shapes = predictor(gray, face)

                shapes = face_utils.shape_to_np(shapes)
                eye_img_l, eye_rect_l = crop_eye(gray, eye_points=shapes[36:42])
                eye_blink_left = cv2.resize(eye_img_l.copy(), B_SIZE)
                eye_blink_left_i = eye_blink_left.reshape((1, B_SIZE[1], B_SIZE[0], 1)).astype(np.float32) / 255.
                eye_img_l = cv2.resize(eye_img_l, dsize=IMG_SIZE)
                eye_input_g = eye_img_l.copy().reshape((1, IMG_SIZE[1], IMG_SIZE[0], 1)).astype(np.float32) / 255.

                status_l = detect_blink(eye_blink_left_i)
                gaze = detect_gaze(eye_input_g)
                if gaze == class_labels[1]:
                    gaze_frame += 1
                    if gaze_frame == frames_to_gaze:
                        gaze_count += 1
                elif gaze == class_labels[2]:
                    gaze_frame += 1
                    if gaze_frame == frames_to_gaze:
                        gaze_count += 1
                else:
                    gaze_frame = 0

                if status_l < 0.1:
                    blink_frame += 1
                    if blink_frame == frames_to_blink:
                        blink_count += 1
                else:
                    blink_frame = 0
        else:
            break

    cap.release()
    return gaze_count, blink_count


def normalize(audio, axis=0):
    return sp.minmax_scale(audio, axis=axis)


# Disfluency
def calc_mfcc(path):
    signal, fs = librosa.load(path, sr=8000)

    signal = normalize(signal)

    mfccs = librosa.feature.mfcc(y=signal, sr=fs, n_fft=2048, n_mfcc=16, fmin=0, fmax=int(fs / 2),
                                 n_mels=26, hop_length=520, htk=False)

    return mfccs


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
    for i in range(300):
        mfcc = slicesound(audio, st, sp)
        if mfcc.shape[1] < 16:
            x = 16 - mfcc.shape[1]
            zero = np.zeros((16, x))
            mfcc = np.append(mfcc, zero)
        st += 100
        sp += 100
        mfcc = mfcc.reshape(1, 16, 16, 1)
        det = model_disfluency.predict(mfcc)
        if det == 1.0:
            dis_count += 1
    return dis_count


def audioprocess(path):
    sound = mp.VideoFileClip(path)
    sound_path = parent_path + "/sound.wav"
    sound.audio.write_audiofile(filename=sound_path, fps=8000, nbytes=2, buffersize=2000, codec='pcm_s32le',
                                ffmpeg_params=["-ac", "1"])
    sound = None
    time.sleep(2)
    dis_count = audiopredict(sound_path)
    os.remove(sound_path)
    return dis_count


@app.route("/")
def home():
    return "Nothing here bro"


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
```


# VM Setting

```bash
sudo apt-get update
sudo apt-get install python3.9 python3-pip
git clone https://github.com/mluthfi3d/speaky_deploy
cd speaky_deploy

sudo pip3 install virtualenv
virtualenv venv
source venv/bin/activate
pip install cmake
sudo apt install python-testresources
pip install --upgrade pip setuptools wheel
pip install -r requirements.txt

change pyasn1-modules~=0.2.8 to 0.2.1

sudo apt-get install libsndfile1
sudo apt install libgl1-mesa-glx

cd
sudo apt-get install nginx-full
sudo /etc/init.d/nginx start

sudo rm /etc/nginx/sites-enabled/default

sudo touch /etc/nginx/sites-available/flask_project
sudo ln -s /etc/nginx/sites-available/flask_project /etc/nginx/sites-enabled/flask_project

sudo nano /etc/nginx/sites-enabled/flask_project

server {
  location / {
    proxy_pass http://0.0.0.0:5000/;
  }
}

sudo /etc/init.d/nginx restart

cd speaky_deploy
gunicorn --bind 0.0.0.0:5000 app:app --daemon
```


