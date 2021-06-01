from flask import Flask, request
import librosa
import requests
import moviepy.editor as mp
import python_speech_features
from tensorflow.keras import models

app = Flask(__name__)

path_model_disfluency = 'model_disfluency.h5'
path_model_eyegaze = 'model_eyegaze.h5'

model_disfluency = models.load_model(path_model_disfluency)
model_eyegaze = models.load_model(path_model_eyegaze)


# Flask webapp
@app.route("/")
def disfluency():
    filepath = request.args.get('link')
    token = request.args.get('token')

    url = filepath + "&token=" + token
    url = url.replace('audiofile/', 'audiofile%2F').replace('/audioFile.wav', '%2FaudioFile.wav')

    r = requests.get(url)
    with open('video.mp4', 'wb') as f:
        f.write(r.content)

    mfcc = calc_mfcc('audioFile.wav')
    return "oke"

# Video process
def video_process(path_video):
    video = mp.VideoFileClip(path_video)

    video.audio.write_audiofile(r"audioonly.mp3")


# Disfluency
def calc_mfcc(path):
    signal, fs = librosa.load(path, sr=None)

    mfccs = librosa.feature.mfcc(y=signal, sr=fs, n_mfcc=32)
    return mfccs


@app.route("/")
def user():
    return "Nothing here bro"


if __name__ == '__main__':
    app.run()
