import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.Vector;

import java.util.List;

public class PrettyGraphsEvaluationFunction extends EvaluationFunction {

    protected double areaM = 0.2;
    protected double edgeVarM = 0.7;
    protected double edgeDistM = 1.0;
    protected double intersM = 1.7;
    protected double edgeLenM = 0.3;
    protected double overlapM = 2.0;

    protected int numVertices = 24;

    protected PrettyGraph graph = new PrettyGraph(numVertices);

    public double score(Instance d) {
        Vector coords = d.getData();

        double[] as = graph.areaScore(coords);
        double areaScore = as[0] * areaM;
        double diagonal = as[1];
        double edgeVarScore = getEdgeVariance(coords, diagonal);

    }

    @Override
    public double score(AdjacencyMatrix matrix, List<VertexLocation> solution, double percentComplete) {
        int numVertices = matrix.rowCount();
        percentComplete = Math.min(percentComplete, 1.0);
        double areaM = 0.2;
        double edgeVarM = 0.7;
        double edgeDistM = 1;
        double intersM = 1.7;
        double edgeLenM = 0.3;
        double overlapM = 2 * percentComplete;

        int xMin = solution.stream().min((v1, v2) -> Integer.compare(v1.x, v2.x)).get().x;
        int xMax = solution.stream().max((v1, v2) -> Integer.compare(v1.x, v2.x)).get().x;
        int yMin = solution.stream().min((v1, v2) -> Integer.compare(v1.y, v2.y)).get().y;
        int yMax = solution.stream().max((v1, v2) -> Integer.compare(v1.y, v2.y)).get().y;

        double areaScore = 1 - 1.0 * ((xMax - xMin) * (yMax - yMin)) / Math.pow(10 * numVertices, 2);
        double diagonal = Math.sqrt(Math.pow(xMax - xMin, 2) + Math.pow(yMax - yMin, 2));

        areaScore *= areaM;
        edgeVarScore = edgeVarM *
    }

    private double edgeLength(List<VertexLocation> solution, int v1, int v2) {
        return Math.sqrt(Math.pow(solution.get(v1).x - solution.get(v2).x, 2)
                + Math.pow(solution.get(v1).y - solution.get(v2).y, 2));
    }

    private double edgeLengthScore(List<VertexLocation> solution, AdjacencyMatrix matrix, double diagonal) {
        int desiredDist = 10;
        for (matrix.)
    }
}

