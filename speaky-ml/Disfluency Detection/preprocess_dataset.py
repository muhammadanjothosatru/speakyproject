from os import listdir
from os.path import isdir, join
import librosa
import random
import numpy as np
import sklearn.preprocessing as sp

dataset_path = "dataset2"
target_all = [name for name in listdir(dataset_path) if isdir(join(dataset_path, name))]
print(target_all)

num_samples = 0
for target in target_all:
    print(len(listdir(join(dataset_path, target))))
    num_samples += len(listdir(join(dataset_path, target)))
print("Total samples: ", num_samples)

target_list = target_all
feature_save_file = 'mfcc_set.npz'
perc_keep_samples = 1.0
val_ratio = 0.3
test_ratio = 0.1
sample_rate = 8000
num_mfcc = 32

filenames = []
y = []
for index, target in enumerate(target_list):
    print(join(dataset_path, target))
    filenames.append(listdir(join(dataset_path, target)))
    y.append(np.ones(len(filenames[index])) * index)

filenames = [item for sublist in filenames for item in sublist]
y = [item for sublist in y for item in sublist]

filenames_y = list(zip(filenames, y))
random.shuffle(filenames_y)
filenames, y = zip(*filenames_y)

print(len(filenames))
filenames = filenames[:int(len(filenames) * perc_keep_samples)]
print(len(filenames))

val_set_size = int(len(filenames) * val_ratio)
test_set_size = int(len(filenames) * test_ratio)

filenames_val = filenames[:val_set_size]
filenames_test = filenames[val_set_size:(val_set_size + test_set_size)]
filenames_train = filenames[(val_set_size + test_set_size):]

y_orig_val = y[:val_set_size]
y_orig_test = y[val_set_size:(val_set_size + test_set_size)]
y_orig_train = y[(val_set_size + test_set_size):]


def normalize(audio, axis=0):
    return sp.minmax_scale(audio, axis=axis)


def calc_mfcc(path):
    signal, fs = librosa.load(path, sr=sample_rate)

    signal = normalize(signal)

    mfccs = librosa.feature.mfcc(y=signal, sr=fs, n_fft=2048, n_mfcc=num_mfcc, fmin=0, fmax=int(fs / 2),
                                 n_mels=32, hop_length=255, htk=False)

    # mfccs = python_speech_features.base.mfcc(signal,
    #                                          samplerate=fs,
    #                                          winlen=0.256,
    #                                          winstep=0.050,
    #                                          numcep=num_mfcc,
    #                                          nfilt=26,
    #                                          nfft=2048,
    #                                          preemph=0.0,
    #                                          ceplifter=0,
    #                                          appendEnergy=False,
    #                                          winfunc=np.hanning)
    return mfccs


def extract_feature(in_files, in_y):
    out_x = []
    out_y = []

    for ind, filename in enumerate(in_files):

        path = join(dataset_path, target_list[int(in_y[ind])], filename)

        if not path.endswith('.wav'):
            continue

        mfccs = calc_mfcc(path)

        out_x.append(mfccs)
        out_y.append(in_y[ind])

    return out_x, out_y


x_train, y_train = extract_feature(filenames_train, y_orig_train)
x_val, y_val = extract_feature(filenames_val, y_orig_val)
x_test, y_test = extract_feature(filenames_test, y_orig_test)

print(x_test[0].shape)

np.savez(feature_save_file,
         x_train=x_train,
         y_train=y_train,
         x_val=x_val,
         y_val=y_val,
         x_test=x_test,
         y_test=y_test)
