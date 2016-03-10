import java.util.List;

public abstract class EvaluationFunction {
    public double score(AdjacencyMatrix matrix, List<VertexLocation> solution) {
        return score(matrix, solution, 1.0);
    }

    public abstract double score(AdjacencyMatrix matrix, List<VertexLocation> solution, double percentComplete);
}
