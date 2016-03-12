import numpy as np
import matplotlib.pyplot as plt

data = ([], [], [])

f = open('bc-n-iter.txt')
lines = f.readlines()
lines.sort(key=lambda x: int(x.strip().split(' ')[0]))
print(lines)
for line in lines:
  if line.find(' ') != -1:
    n, a, b = line.strip().split(' ')
    data[0].append(n)
    data[1].append(1.0 - float(a))
    data[2].append(1.0 - float(b))

fig, ax = plt.subplots()
ax.plot(data[0], data[1], color='b', label='Train')
ax.plot(data[0], data[2], color='r', label='Test')
ax.set_ylim(0, ax.get_ylim()[1] * 1.1)
ax.set_title('Neural Network (Breast Cancer)')
ax.set_ylabel('% Error')
ax.set_xlabel('# of Epochs')
ax.legend()
plt.show()
