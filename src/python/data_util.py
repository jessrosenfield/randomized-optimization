import numpy as np

_vowel_path = 'datasets/vowel/vowel.train'
_vowel_test_path = 'datasets/vowel/vowel.test'


def _is_nan(string):
    if string is not '?':
        return int(string)
    else:
        return np.nan

def load_vowel():
    """Load and return the vowel training dataset.

    Returns
    -------
    (X_train, X_test, y_train, y_test) Tuple
        A tuple of data and target

    The copy of the vowel dataset is downloaded from:
    http://statweb.stanford.edu/~tibs/ElemStatLearn/data.html
    """
    train = _load_vowel_train()
    test = _load_vowel_test()
    return (train[0], test[0], train[1], test[1])


def _load_vowel_train():
    vowel_data = np.loadtxt(_vowel_path, delimiter=',', skiprows=1)
    X = vowel_data[:, -10:]
    y = vowel_data[:, 1].astype(int)
    y = np.array([1 if elem == 1 else 0 for elem in y]).astype(int)
    return (X, y)


def _load_vowel_test():
    """Load and return the vowel testing dataset.

    Returns
    -------
    (X, y) Tuple
        A tuple of data and target

    The copy of the vowel dataset is downloaded from:
    http://statweb.stanford.edu/~tibs/ElemStatLearn/data.html
    """
    vowel_data = np.loadtxt(_vowel_test_path, delimiter=',', skiprows=1)
    X = vowel_data[:, -10:]
    y = vowel_data[:, 1].astype(int)
    y = np.array([1 if elem == 1 else 0 for elem in y]).astype(int)
    return (X, y)

