import numpy as np
import matplotlib.pyplot as plt

data = {}
current = ''
dataset = ''

f = open('knn.txt')
lines = f.readlines()
for line in lines:
  if line.find(' ') != -1:
    n, a, b = line.strip().split(' ')
    data[current][dataset][0].append(n)
    data[current][dataset][1].append(1.0 - float(a))
    data[current][dataset][2].append(1.0 - float(b))
  elif line.startswith('-'):
    dataset = line.strip().replace('-', '')
    data[current][dataset] = ([], [], [])
  else:
    current = line.strip()
    data[current] = {}

print(data)
for thing, datasets in data.iteritems():
  print(datasets)
  for dataset, entries in datasets.iteritems():
    print(entries)
    fig, ax = plt.subplots()
    ax.scatter(entries[0], entries[1], marker='o', color='b', label='Train')
    ax.scatter(entries[0], entries[2], marker='o', color='r', label='Test')
    ax.set_ylim(0, ax.get_ylim()[1] * 1.1)
    title = ''
    if thing == 'knn_k_neighbors':
      title = 'k-Nearest-Neighbors'
    else:
      title = 'k-Nearest-Neighbors with Varied Training Size'
    if dataset == 'bc':
      title += ' (Breast Cancer)'
    else:
      title += ' (Vowel Recognition)'
    ax.set_title(title)
    ax.set_ylabel('% Error')
    if thing == 'knn_k_neighbors':
      ax.set_xlabel('# of Neighbors')
    else:
      ax.set_xlabel('% of Training Set')
    plt.legend()
    plt.show()
