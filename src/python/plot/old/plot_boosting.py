import numpy as np
import matplotlib.pyplot as plt

datasets = {}
dataset = ''

f = open('boosting.txt')
lines = f.readlines()
for line in lines:
  if line.find(' ') != -1:
    n, a, b = line.strip().split(' ')
    datasets[dataset][0].append(n)
    datasets[dataset][1].append(1.0 - float(a))
    datasets[dataset][2].append(1.0 - float(b))
  else:
    dataset = line.strip().replace('-', '')
    datasets[dataset] = ([], [], [])

for dataset, entries in datasets.iteritems():
  print dataset
  fig, ax = plt.subplots()
  ax.plot(entries[0], entries[1], color='b', label='Train')
  ax.plot(entries[0], entries[2], color='r', label='Test')
  ax.set_ylim(0, ax.get_ylim()[1] * 1.1)
  title = 'Boosting'
  if dataset == 'bc':
    title += ' (Breast Cancer)'
    ax.legend()
  else:
    title += ' (Vowel Recognition)'
    ax.legend(loc='bottom right')
  ax.set_title(title)
  ax.set_ylabel('% Error')
  ax.set_xlabel('# of Estimators')
  plt.show()
