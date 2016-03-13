import numpy as np
import matplotlib.pyplot as plt

data = {}
dataset = ''
max_error = 3

f = open(('../logs/logs-time{}.txt'.format(max_error)))
lines = f.readlines()
for i in range(len(lines)):
  if lines[i].find('Error results for') != -1:
    if 'RHC' in lines[i]:
      dataset = 'RHC'
    elif 'GA' in lines[i]:
      dataset = 'GA'
    else:
      dataset = 'SA'
    if dataset not in data.keys():
      data[dataset] =([], [])
    n, _, b = lines[i + 2].strip().split(', ')
    data[dataset][1].append(int(n))
    data[dataset][0].append(float(b))

print(data)
fig, ax = plt.subplots()
for dataset, entries in data.iteritems():
  print(entries)
  if dataset == 'RHC':
    color = 'r'
  elif dataset == 'SA':
    color = 'g'
  else:
    color = 'b'
  ax.scatter(entries[0], entries[1], marker='o', color=color, label=dataset)
title = 'Neural Network Optimization Under {}% Error'.format(max_error)
ax.legend(loc='upper left', scatterpoints = 1)
ax.set_title(title)
ax.set_ylim(0, ax.get_ylim()[1])
ax.set_xlim(0, ax.get_xlim()[1])
ax.set_ylabel('# of Iterations')
ax.set_xlabel('Total Runtime')
plt.show()
