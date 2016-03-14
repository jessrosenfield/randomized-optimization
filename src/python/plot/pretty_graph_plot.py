from matrix_prototypes import *

from matplotlib.pyplot import show, ion, axis
import random
import networkx as nx

def display(matrix, points):
    axis((-5, 600, -5, 600))
    nx.draw(matrix.graph(), points)
    show()

def getRandom(vertices):
    return {vertex : (random.randrange(10*len(vertices)),
            random.randrange(10*len(vertices))) for vertex in vertices}

prettyNum = 1
matrixWeb = AdjacencyMatrix(24)
matrixWeb.addVertices(["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X"])
matrixWeb.addEdge("A", "B")
matrixWeb.addEdge("B", "C")
matrixWeb.addEdge("C", "D")
matrixWeb.addEdge("D", "E")
matrixWeb.addEdge("E", "F")
matrixWeb.addEdge("F", "G")
matrixWeb.addEdge("G", "H")
matrixWeb.addEdge("H", "A")
matrixWeb.addEdge("I", "J")
matrixWeb.addEdge("J", "K")
matrixWeb.addEdge("K", "L")
matrixWeb.addEdge("L", "M")
matrixWeb.addEdge("M", "N")
matrixWeb.addEdge("N", "O")
matrixWeb.addEdge("O", "P")
matrixWeb.addEdge("P", "I")
matrixWeb.addEdge("Q", "R")
matrixWeb.addEdge("R", "S")
matrixWeb.addEdge("S", "T")
matrixWeb.addEdge("T", "U")
matrixWeb.addEdge("U", "V")
matrixWeb.addEdge("V", "W")
matrixWeb.addEdge("W", "X")
matrixWeb.addEdge("X", "Q")
matrixWeb.addEdge("A", "I")
matrixWeb.addEdge("B", "J")
matrixWeb.addEdge("C", "K")
matrixWeb.addEdge("D", "L")
matrixWeb.addEdge("E", "M")
matrixWeb.addEdge("F", "N")
matrixWeb.addEdge("G", "O")
matrixWeb.addEdge("H", "P")
matrixWeb.addEdge("I", "Q")
matrixWeb.addEdge("J", "R")
matrixWeb.addEdge("K", "S")
matrixWeb.addEdge("L", "T")
matrixWeb.addEdge("M", "U")
matrixWeb.addEdge("N", "V")
matrixWeb.addEdge("O", "W")
matrixWeb.addEdge("P", "X")


randSolution = getRandom(matrixWeb.vertices)
display(matrixWeb, randSolution)
f = open(('../../../logs/pretty{}.txt'.format(prettyNum)))
lines = f.readlines()
for i in range(len(lines) - 15, len(lines)):
  dataset = None
  if 'RHC:' in lines[i]:
    dataset = 'RHC'
  elif 'GA:' in lines[i]:
    dataset = 'GA'
  elif 'SA:' in lines[i]:
    dataset = 'SA'
  elif 'MIMIC:' in lines[i]:
    dataset = 'MIMIC'
  if dataset is not None:
    solution = {}
    nums = lines[i + 1].strip().split(', ')
    for j in range(len(nums)):
      nums[j] = int(nums[j].replace('.000000', ''))
    for j in range(len(matrixWeb.vertices)):
      coords = (nums[2 * j], nums[2 * j + 1])
      solution[matrixWeb.vertices[j]] = coords
    display(matrixWeb, solution)