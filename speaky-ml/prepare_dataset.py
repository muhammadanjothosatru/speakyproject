from os import listdir
from os.path import isdir, join
import numpy as np

dataset_path = 'dataset2'
target_all = [name for name in listdir(dataset_path) if isdir(join(dataset_path, name))]

feature_set_path = 'mfcc_set.npz'

feature_sets = np.load(feature_set_path)

x_train = feature_sets['x_train']
y_train = feature_sets['y_train']
x_val = feature_sets['x_val']
y_val = feature_sets['y_val']
x_test = feature_sets['x_test']
y_test = feature_sets['y_test']

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