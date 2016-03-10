import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.Vector;

public class PrettyGraphsEvalutationFunction implements EvaluationFunction {

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

}