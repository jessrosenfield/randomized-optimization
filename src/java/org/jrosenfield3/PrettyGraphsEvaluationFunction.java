package org.jrosenfield3;

import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.Vector;

import java.util.*;


public class PrettyGraphsEvaluationFunction implements EvaluationFunction {

    protected static final int DESIRED_DIST = 10;
    protected static final int GAP = 10;
    protected final double areaM = 0.2;
    protected final double edgeVarM = 0.7;
    protected final double edgeDistM = 1.0;
    protected final double intersM = 1.7;
    protected final double edgeLenM = 0.3;
    protected final int numVertices;
    protected final PrettyGraph graph;
    protected double overlapM = 2.0;
    protected List<VertexLocation> solution;

    public PrettyGraphsEvaluationFunction(PrettyGraph graph) {
        this.graph = graph;
        numVertices = graph.numVertices;
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
        double areaScore = 1 - 1.0 * ((xMax - xMin) * (yMax - yMin)) / Math.pow(GAP * numVertices, 2);
        double diagonal = Math.sqrt(2 * Math.pow(graph.max, 2));

        areaScore = areaM * areaScore > 0 ? areaScore : 0;
        double edgeVarScore = edgeVarM * edgeLengthVariance(diagonal);
        double edgeDistScore = edgeDistM * checkDistributedEdges();
        double intersectScore = intersM * intersectionScore();
        double edgeLenScore = edgeLenM * edgeLengthScore(diagonal);
        double overlapScore = overlapM * checkOverlappingPoints();
        return (areaScore + edgeVarScore + edgeDistScore + intersectScore + edgeLenScore + overlapScore)
                / (areaM + edgeVarM + edgeDistM + intersM + edgeLenM + overlapM);
    }

    private double checkDistributedEdges() {
        double totalVariance = 0.0;
        for (int i = 0; i < solution.size(); i++) {
            List<Double> angles = new ArrayList<>(graph.edges.length);
            List<Double> angleDist = new ArrayList<>(graph.edges.length);
            List<Integer> neighbors = graph.matrix.getNeighbors(i);
            if (neighbors.size() > 1) {
                for (int neighbor : neighbors) {
                    int x = solution.get(neighbor).x - solution.get(i).x;
                    int y = solution.get(neighbor).y - solution.get(i).y;
                    angles.add(Math.atan2(y, x));
                }
                angles.sort(Double::compare);
                for (int a = angles.size() - 1; a > 0; a--) {
                    angleDist.add(angles.get(a) - angles.get(a - 1));
                }
                angleDist.add((2 * Math.PI) - (angles.get(angles.size() - 1) - angles.get(0)));

                totalVariance += getVariance(angleDist);
            }
        }
        return 1.0 / (totalVariance + 1.0);
    }

    private double getMean(List<? extends Number> list) {
        double sum = 0.0;
        for (Number a : list)
            sum += a.doubleValue();
        return sum / list.size();
    }

    private double getVariance(List<? extends Number> list) {
        double mean = getMean(list);
        double temp = 0;
        for (Number a : list)
            temp += (mean - a.doubleValue()) * (mean - a.doubleValue());
        return temp / list.size();
    }

    private double edgeLength(int v1, int v2) {
        return Math.sqrt(Math.pow(solution.get(v1).x - solution.get(v2).x, 2)
                + Math.pow(solution.get(v1).y - solution.get(v2).y, 2));
    }

    private double edgeLengthScore(double diagonal) {
        List<Double> lengthDiffs = new ArrayList<>(graph.edges.length);
        for (AdjacencyMatrix.Edge edge : graph.edges) {
            lengthDiffs.add(Math.abs(DESIRED_DIST - edgeLength(edge.v1, edge.v2)));
        }
        double maxSumPossible = graph.edges.length * Math.abs(DESIRED_DIST - diagonal);
        double sum = lengthDiffs.stream().mapToDouble(Double::doubleValue).sum();
        return 1 - sum / maxSumPossible;
    }

    private double edgeLengthVariance(double diagonal) {
        List<Double> edgeLengths = new ArrayList<>(graph.edges.length);
        for (AdjacencyMatrix.Edge edge : graph.edges) {
            edgeLengths.add(edgeLength(edge.v1, edge.v2));
        }
        return 1 - getVariance(edgeLengths) / getVariance(Arrays.asList(0, diagonal));
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
        Set<AdjacencyMatrix.Edge> checkedEdges = new HashSet<>(graph.edges.length);
        int intersections = 0;
        for (AdjacencyMatrix.Edge edge : graph.edges) {
            checkedEdges.add(edge);
            for (AdjacencyMatrix.Edge other : graph.edges) {
                if (!checkedEdges.contains(other) && intersects(edge, other)) {
                    intersections++;
                }
            }
        }
        return 1 - intersections / (double) maxIntersections(graph.edges.length);
    }

    public boolean intersects(AdjacencyMatrix.Edge e1, AdjacencyMatrix.Edge e2) {
        if (e1.v1 == e2.v1 || e1.v2 == e2.v2 || e1.v1 == e2.v2 || e1.v2 == e2.v1) {
            return false;
        }

        Orientation o1 = orientation(e1.v1, e1.v2, e2.v1);
        Orientation o2 = orientation(e1.v1, e1.v2, e2.v2);
        Orientation o3 = orientation(e2.v1, e2.v2, e1.v1);
        Orientation o4 = orientation(e2.v1, e2.v2, e1.v2);

        // General case
        if (!o1.equals(o2) && !o3.equals(o4)) {
            return true;
        }

        // Colinear overlapping
        return ((o1.equals(Orientation.COLINEAR) && onSegment(e1.v1, e1.v1, e2.v1))
                || (o2.equals(Orientation.COLINEAR) && onSegment(e1.v1, e1.v2, e2.v2))
                || (o3.equals(Orientation.COLINEAR) && onSegment(e2.v1, e2.v2, e1.v1))
                || (o4.equals(Orientation.COLINEAR) && onSegment(e2.v1, e2.v2, e1.v2)));
    }

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
        return ((b.x <= Math.max(a.x, c.x))
                && (b.x >= Math.min(a.y, c.y))
                && (b.y <= Math.max(a.y, c.y))
                && (b.y >= Math.min(a.y, c.y)));
    }

    public int maxIntersections(int i) {
        if (i < 2) {
            return 0;
        }
        return i - 1 + maxIntersections(i - 1);
    }

    private double checkOverlappingPoints() {
        double numFailures = 0;
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                double len = edgeLength(i, j);
                if (len < GAP) {
                    double addScore = GAP / (len + 0.001);
                    addScore = 1 - (1 / addScore);
                    if (addScore > 0) {
                        numFailures += 10 * addScore;
                    }
                }
            }
        }

        for (int i = 0; i < numVertices; i++) {
            for (AdjacencyMatrix.Edge edge : graph.edges) {
                if (!(edge.v1 == i || edge.v2 == i)) {
                    double dist = pointToLine(i, edge.v1, edge.v2);
                    if (dist > 0 && dist < GAP) {
                        double addScore = GAP / (dist + 0.001);
                        addScore = 1 - (1 / addScore);
                        if (addScore > 0) {
                            numFailures += 10 * addScore;
                        }
                    }
                }
            }
        }
        int n = numVertices + graph.edges.length;
        double rawScore = 1 - ((numFailures * 2) / (10 * n * (n - 1) / 2));
        return Math.pow(rawScore, 5);
    }

    public double pointToLine(int point, int start, int end) {
        VertexLocation p = solution.get(point);
        VertexLocation s = solution.get(start);
        VertexLocation e = solution.get(end);
        double edgeLength = edgeLength(start, end);
        if (edgeLength == 0) {
            return 0;
        }
        double edgeUnitX = (e.x - s.x) / edgeLength;
        double edgeUnitY = (e.y - s.y) / edgeLength;
        int pointX = p.x - s.x;
        int pointY = p.y - s.y;
        double pointUnitX = (p.x - s.x) / edgeLength;
        double pointUnitY = (p.y - s.y) / edgeLength;
        double t = edgeUnitX * pointUnitX + edgeUnitY * pointUnitY;
        if (t < 0 || t > 1) {
            return 0;
        }
        double nearestX = (e.x - s.x) / t;
        double nearestY = (e.y - s.y) / t;
        return Math.sqrt(Math.pow(pointX - nearestX, 2) + Math.pow(pointY - nearestY, 2));
    }

    private enum Orientation {COLINEAR, CLOCKWISE, COUNTERCLOCKWISE}


}
