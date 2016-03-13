import numpy as np
import matplotlib.pyplot as plt
import re

data = {}
current = ''
dataset = ''
nameRegex = re.compile('[a-z]+')

f = open('decision-tree.txt')
lines = f.readlines()
for line in lines:
  if line.find(' ') != -1:
    n, a, b = line.strip().split(' ')
    method = nameRegex.match(n.split('_')[1]).group(0)
    num = 10
    if n[-1].isdigit():
      num = int(n[-1])
    print(method)
    print(num)
    if data[dataset].get(method, None) == None:
      data[dataset][method] = ([], [], [])
    data[dataset][method][0].append(num)
    data[dataset][method][1].append(1.0 - float(a))
    data[dataset][method][2].append(1.0 - float(b))
  else:
    dataset = line.strip().replace('-', '')
    data[dataset] = {}

print(data)
for dataset, entries in data.iteritems():
  fig, ax = plt.subplots()
  ax.plot(entries['gini'][0], entries['gini'][1], marker='o', color='b', label='Gini Train')
  ax.plot(entries['gini'][0], entries['gini'][2], marker='o', color='r', label='Gini Test')
  ax.plot(entries['entropy'][0], entries['entropy'][1], marker='*', markersize=10, linestyle='--', color='b', label='Entropy Train')
  ax.plot(entries['entropy'][0], entries['entropy'][2], marker='*', markersize=10, linestyle='--', color='r', label='Entropy Test')
  ax.set_ylim(0, ax.get_ylim()[1] * 1.1)
  ax.set_xlim(10, 1)
  ax.set_xticklabels([1, 2, 3, 4, 5, 6, 7, 8, 9, u'\u221E'])
  title = 'Decision Tree'
  if dataset == 'bc':
    title += ' (Breast Cancer)'
  else:
    title += ' (Vowel Recognition)'
  ax.set_title(title)
  ax.set_ylabel('% Error')
  ax.set_xlabel('Maximum Depth')
  plt.legend(loc='upper left')
  plt.show()
