from os import listdir
from os.path import isdir, join
from tensorflow.keras import layers, models
import numpy as np
import matplotlib.pyplot as plt
import tensorflowjs as tfjs

dataset_path = 'dataset2'
target_all = [name for name in listdir(dataset_path) if isdir(join(dataset_path, name))]

feature_set_path = 'mfcc_set.npz'
model_filename = 'disfluency_model.h5'

feature_sets = np.load(feature_set_path)
print(feature_sets.files)

x_train = feature_sets['x_train']
y_train = feature_sets['y_train']
x_val = feature_sets['x_val']
y_val = feature_sets['y_val']
x_test = feature_sets['x_test']
y_test = feature_sets['y_test']

print(y_test)

x_train = x_train.reshape(x_train.shape[0],
                          x_train.shape[1],
                          x_train.shape[2],
                          1)
x_val = x_val.reshape(x_val.shape[0],
                      x_val.shape[1],
                      x_val.shape[2],
                      1)
x_test = x_test.reshape(x_test.shape[0],
                        x_test.shape[1],
                        x_test.shape[2],
                        1)

input_shape = (16, 16, 1)
model = models.Sequential()
model.add(layers.Conv2D(32,
                        (2, 2),
                        activation='relu',
                        input_shape=input_shape))
model.add(layers.MaxPooling2D(pool_size=(2, 2)))
model.add(layers.Conv2D(32, (2, 2), activation='relu'))
model.add(layers.MaxPooling2D(pool_size=(2, 2)))
model.add(layers.Conv2D(64, (2, 2), activation='relu'))
model.add(layers.MaxPooling2D(pool_size=(2, 2)))

model.add(layers.Flatten())
model.add(layers.Dense(64, activation='relu'))
model.add(layers.Dropout(0.5))
model.add(layers.Dense(1, activation='sigmoid'))

model.summary()

model.compile(loss='binary_crossentropy',
              optimizer='rmsprop',
              metrics=['acc'])

history = model.fit(x_train, y_train, epochs=30, batch_size=2, validation_data=(x_val, y_val))

acc = history.history['acc']
val_acc = history.history['val_acc']
loss = history.history['loss']
val_loss = history.history['val_loss']


epochs = range(1, len(acc) + 1)

plt.plot(epochs, acc, 'r', label='Training acc')
plt.plot(epochs, val_acc, 'b', label='Validation acc')
plt.title('Training and Validation accuracy')
plt.legend()

plt.figure()

plt.plot(epochs, loss, 'r', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and Validation loss')
plt.legend()

plt.show()

mfccs = np.expand_dims(x_test[2], 0)
print(mfccs.shape)
mfcc_reshape = mfccs.reshape(1, 256)
np.savetxt("mfcc", mfcc_reshape)
print(mfcc_reshape.shape)
mfcc_reshape = mfcc_reshape.reshape(1, 16, 16, 1)
print(mfcc_reshape.shape)

models.save_model(model, model_filename)
tfjs.converters.save_keras_model(model, "website_test")
