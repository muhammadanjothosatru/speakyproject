from prepare_dataset import *
from tensorflow.keras import models

model_filename = 'disfluency_model.h5'

# datasetbaru = 'dataset2/audioFile.wav'
# mfcc = calc_mfcc(datasetbaru)
# np.savetxt("mfcc2", mfcc)
# print(mfcc.shape)

model = models.load_model(model_filename)

mfccs = np.expand_dims(x_test[1], 0)

# mfcc_reshape = mfccs.reshape(1, 1024)
# np.savetxt("mfcc", mfcc_reshape)
# print(mfcc_reshape.shape)

#mfcc_reshape = mfccs.reshape(1, 16, 16, 1)
#print(mfcc_reshape.shape)

print(model.predict(mfccs))
print(y_test[1])
