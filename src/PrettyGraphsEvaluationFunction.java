import opt.EvaluationFunction;
import shared.Instance;
import AdjacencyMatrix.Edge;
import util.linalg.Vector;

import java.util.*;


public class PrettyGraphsEvaluationFunction implements EvaluationFunction {

    protected final double areaM = 0.2;
    protected final double edgeVarM = 0.7;
    protected final double edgeDistM = 1.0;
    protected final double intersM = 1.7;
    protected final double edgeLenM = 0.3;
//    protected double overlapM = 2.0;
    protected final int numVertices;
    protected final PrettyGraph graph;
    protected final int desiredDist = 10;
    protected List<VertexLocation> solution;

    public PrettyGraphsEvaluationFunction(PrettyGraph graph) {
        this.graph = graph;
        numVertices = graph.matrix.rowCount();
    }

    @Override
    public double value(Instance d) {
        solution = getSolutionForGivenInstance(d);
        return score();
    }

    public double score() {
        int xMin = solution.stream().min((v1, v2) -> Integer.compare(v1.x, v2.x)).get().x;
        int xMax = solution.stream().max((v1, v2) -> Integer.compare(v1.x, v2.x)).get().x;
        int yMin = solution.stream().min((v1, v2) -> Integer.compare(v1.y, v2.y)).get().y;
        int yMax = solution.stream().max((v1, v2) -> Integer.compare(v1.y, v2.y)).get().y;

        double areaScore = 1 - 1.0 * ((xMax - xMin) * (yMax - yMin)) / Math.pow(10 * numVertices, 2);
        double diagonal = Math.sqrt(Math.pow(xMax - xMin, 2) + Math.pow(yMax - yMin, 2));

        areaScore *= areaM;
        double edgeVarScore = edgeVarM * edgeLengthVariance(diagonal);
        double edgeDistScore = edgeDistM * checkDistributedEdges();
        double intersectScore = intersM * intersectionScore();
        double edgeLenScore = edgeLenM * edgeLengthScore(diagonal);
        // TODO: overlap
        return (areaScore + edgeVarScore + edgeDistScore + intersectScore + edgeLenScore) / (areaM + edgeVarM + edgeDistM + intersM + edgeLenM);
    }

    double checkDistributedEdges() {
        double totalVariance = 0.0;
        for (int i = 0; i < solution.size(); i++) {
            List<Double> angles = new ArrayList<>(graph.edges.size());
            List<Double> angleDist = new ArrayList<>(graph.edges.size());
            List<Integer> neighbors = graph.matrix.getNeighbors(i);
            if (neighbors.size() > 1) {
                for (int neighbor : neighbors) {
                    int x = solution.get(neighbor).x - solution.get(i).x;
                    int y = solution.get(neighbor).y - solution.get(i).y;
                    angles.add(Math.atan2(y, x));
                }
                angles.sort((a1, a2) -> Double.compare(a1, a2));
                for (int a = angles.size() - 1; a > 0; a--) {
                    angleDist.add(angles.get(i) - angles.get(i - 1));
                }
                angleDist.add((2 * Math.PI) - (angles.get(angles.size() - 1) - angles.get(0)));

                totalVariance += getVariance(angleDist);
            }
        }
        return 1.0 / (totalVariance + 1.0);
    }

    double getMean(List<? extends Number> list) {
        double sum = 0.0;
        for(Number a : list)
            sum += a.doubleValue();
        return sum/list.size();
    }

    double getVariance(List<? extends Number> list) {
        double mean = getMean(list);
        double temp = 0;
        for(Number a : list)
            temp += (mean-a.doubleValue())*(mean-a.doubleValue());
        return temp/list.size();
    }

    private double edgeLength(int v1, int v2) {
        return Math.sqrt(Math.pow(solution.get(v1).x - solution.get(v2).x, 2)
                + Math.pow(solution.get(v1).y - solution.get(v2).y, 2));
    }

    private double edgeLengthScore(double diagonal) {
        List<Double> lengthDiffs = new ArrayList<>(graph.edges.size());
        for (Edge edge : graph.edges) {
            lengthDiffs.add(Math.abs(desiredDist - edgeLength(edge.v1, edge.v2)));
        }
        double maxSumPossible = graph.edges.size() * Math.abs(desiredDist - diagonal);
        double sum = lengthDiffs.stream().mapToDouble(Double::doubleValue).sum();
        return 1 - sum / maxSumPossible;
    }

    private double edgeLengthVariance(double diagonal) {
        List<Double> edgeLengths = new ArrayList<>(graph.edges.size());
        for (Edge edge : graph.edges) {
            edgeLengths.add(edgeLength(edge.v1, edge.v2));
        }
        return 1 - getVariance(edgeLengths)/getVariance(Arrays.asList(0, diagonal));
    }

    public List<VertexLocation> getSolutionForGivenInstance(Instance d) {
        Vector vector = d.getData();
        List<VertexLocation> solution = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            solution.add(new VertexLocation((int) vector.get(2 * i), (int) vector.get(2 * i + 1)));
        }
        return solution;
    }

    public double intersectionScore() {
        Set<Edge> checkedEdges = new HashSet<>(graph.edges.size());
        int intersections = 0;
        for (Edge edge : graph.edges) {
            checkedEdges.add(edge);
            for (Edge other : graph.edges) {
                if (!checkedEdges.contains(other) && intersects(edge, other)) {
                    intersections++;
                }
            }
        }
        return 1 - intersections / (double) maxIntersections(graph.edges.size());
    }

    public boolean intersects(Edge e1, Edge e2) {
        if (e1.v1 == e2.v1 || e1.v2 == e2.v2 || e1.v1== e2.v2 || e1.v2 == e2.v1) {
            return false;
        }

        Orientation o1 = orientation(e1.v1, e1.v2, e2.v1);
        Orientation o2 = orientation(e1.v1, e1.v2, e2.v2);
        Orientation o3 = orientation(e2.v1, e2.v2, e1.v1);
        Orientation o4 = orientation(e2.v1, e2.v2, e1.v2);

        // General case
        if (o1.equals(o2) && o3.equals(o4)) {
            return true;
        }

        // Colinear overlapping
        if ((o1.equals(Orientation.COLINEAR) && onSegment(e1.v1, e1.v1, e2.v1))
                || (o2.equals(Orientation.COLINEAR) && onSegment(e1.v1, e1.v2, e2.v2))
                || (o2.equals(Orientation.COLINEAR) && onSegment(e2.v1, e2.v2, e1.v1))
                || (o2.equals(Orientation.COLINEAR) && onSegment(e2.v1, e2.v2, e1.v2))) {
            return true;
        }

        return false;
    }

    private enum Orientation {COLINEAR, CLOCKWISE, COUNTERCLOCKWISE}

    private Orientation orientation(int v1, int v2, int v3) {
        VertexLocation a = solution.get(v1);
        VertexLocation b = solution.get(v2);
        VertexLocation c = solution.get(v3);
        int val = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
        if (val == 0) {
            return Orientation.COLINEAR;
        } else if (val >= 0) {
            return Orientation.CLOCKWISE;
        } else {
            return Orientation.COUNTERCLOCKWISE;
        }
    }

    private boolean onSegment(int v1, int v2, int v3) {
        VertexLocation a = solution.get(v1);
        VertexLocation b = solution.get(v2);
        VertexLocation c = solution.get(v3);
        if ((b.x <= Math.max(a.x, c.x))
                && (b.x >= Math.min(a.y, c.y))
                && (b.y <= Math.max(a.y, c.y))
                && (b.y >= Math.min(a.y, c.y))) {
            return true;
        }
        return false;
    }

    public int maxIntersections(int i) {
        if (i < 2) {
            return 0;
        }
        return i - 1 + maxIntersections(i - 1);
    }
}
