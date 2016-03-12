package org.jrosenfield3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdjacencyMatrix {
    protected boolean[][] matrix;
    private Set<Edge> edges;

    public AdjacencyMatrix(int vertexCount) {
        matrix = new boolean[vertexCount][vertexCount];
        edges = new HashSet<>();
    }

    public Edge[] getEdges() {
        return edges.toArray(new Edge[]{});
    }

    public void addEdge(int i, int j) {
        matrix[i][j] = true;
        matrix[j][i] = true;
        edges.add(new Edge(i, j));
    }

    public List<Integer> getNeighbors(int v) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][v]) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    public void removeEdge(int i, int j) {
        matrix[i][j] = false;
        matrix[j][i] = false;
        edges.remove(new Edge(i, j));
    }

    public int rowCount() {
        return matrix.length;
    }

    public static class Edge {
        public final int v1;
        public final int v2;

        public Edge(int v1, int v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Edge)) {
                return false;
            }
            Edge e = (Edge) o;
            if (v1 == e.v1 && v2 == e.v2) {
                return true;
            } else if (v1 == e.v2 && v2 == e.v1) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return v1 * v2;
        }
    }
}