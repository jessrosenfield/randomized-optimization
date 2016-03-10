import java.util.Iterator;

public class AdjacencyMatrix {
    private boolean[][] matrix;

    public AdjacencyMatrix(int vertexCount) {
        matrix = new boolean[vertexCount][vertexCount];
    }

    public void addEdge(int i, int j) {
        addEdge(i, j, 1);
    }

    public void addEdge(int i, int j) {
        matrix[i][j] = true;
        matrix[j][i] = true;
    }

    public void removeEdge(int i, int j) {
        matrix[i][j] = false;
        matrix[j][i] = false;
    }

    public int rowCount() {
        return matrix.length;
    }

    public Iterator<Edge> getEdgeIterator() {
        return new Iterator<Edge>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Edge next() {

            }
        }
    }

    public static class Edge {
        public int v1;
        public int v2;
    }
}