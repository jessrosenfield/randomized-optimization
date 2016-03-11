import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdjacencyMatrix {
    private boolean[][] matrix;

    public AdjacencyMatrix(int vertexCount) {
        matrix = new boolean[vertexCount][vertexCount];
    }

    public void addEdge(int i, int j) {
        matrix[i][j] = true;
        matrix[j][i] = true;
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

    public Iterator<Edge> getEdgeIterator() {
        return new Iterator<Edge>() {
            int i = 0;
            int j = 0;

            @Override
            public boolean hasNext() {
                if (j < matrix.length) {
                    j += 1;
                    if (matrix[i][j - 1]) {
                        return true;
                    } else {
                        return hasNext();
                    }
                } else if (i < matrix.length - 1) {
                    i += 1;
                    j = i + 1;
                    if (j < matrix.length) {
                        j += 1;
                        if (matrix[i][j  - 1]) {
                            j -= 1;
                            return true;
                        } else {
                            return hasNext();
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            @Override
            public Edge next() {
                Edge next = new Edge(i, j);
                return next;
            }
        };
    }
}