from joblib import Parallel, delayed
from sklearn import cross_validation
from sklearn.grid_search import GridSearchCV
from sknn.mlp import Classifier, Layer

import data_util as util
import matplotlib.pyplot as plt
import numpy as np

v_data_train, v_data_test, v_target_train, v_target_test = util.load_vowel()

UNITS = [2, 100]

def ann_n_iter():
    print "n_iter"
    print "---v---"
    for n_units in UNITS:
        _ann_n_iter(
                v_data_train,
                v_data_test,
                v_target_train,
                v_target_test, n_units) 


def _ann_n_iter(data, data_test, target, target_test, n_units):
    nn = Classifier(
        layers=[
            Layer("Sigmoid", units=n_units),
            Layer("Softmax")],
        n_iter=4000)
    nn.fit(data, target)
    test_score = nn.score(data_test, target_test)
    print n_units, test_score


if __name__ == "__main__":
    ann_n_iter()
