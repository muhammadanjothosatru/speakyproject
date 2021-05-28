from prepare_dataset import *
from tensorflow.keras import layers, models

model_filename = 'disfluency_model.h5'

model = models.load_model(model_filename)

mfccs = np.expand_dims(x_test[2], 0)
print(mfccs.shape)

mfcc_reshape = mfccs.reshape(1, 256)
np.savetxt("mfcc", mfcc_reshape)
print(mfcc_reshape.shape)

mfcc_reshape = mfcc_reshape.reshape(1, 16, 16, 1)
print(mfcc_reshape.shape)
