
public class AdjacencyMatrix {
    protected int[][] matrix;

    public AdjacencyMatrix(int numEdges) {
        matrix = new int[numEdges][numEdges];
    }

    public void addEdge(int i, int j) {
        matrix[i][j] = 1;
        matrix[j][i] = 1;
    }

    public void removeEdge(int i, int j) {
        matrix[i][j] = 0;
        matrix[j][i] = 0;
    }

    public boolean hasEdge(int i, int j) {
        return matrix[i][j] == 1;
    }
}