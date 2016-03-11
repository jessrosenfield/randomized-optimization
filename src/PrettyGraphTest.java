import dist.DiscreteDependencyTree;
import dist.Distribution;
import opt.*;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;

import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created by Jessica on 3/10/2016.
 */
public class PrettyGraphTest {
    public static final int ITER = 10000;

    public static void main(String[] args) {
        PrettyGraph graph = new PrettyGraph();
        PrettyGraphsEvaluationFunction ef = new PrettyGraphsEvaluationFunction(graph);
        Distribution dist = new PrettyGraphDistribution(ef.numVertices);
        Distribution df = new DiscreteDependencyTree(.1);
        // TODO: try with DiscreteChangeOne instead of Swap
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new SingleCrossOver();
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, dist, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, dist, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, dist, df);

        double start, trainingTime, end = 0;

        System.out.println("---RHC---");
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainerMod fit = new FixedIterationTrainerMod(rhc, ITER);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10,9);
        PrintWriter pw = new PrintWriter(System.out);
        for (AdjacencyMatrix.Edge edge : graph.edges) {
           pw.println(edge.v1 + ", " + edge.v2);
        }
        pw.println("RHC: " + ef.value(rhc.getOptimal()) + " Time: " + trainingTime);
        pw.println(rhc.getOptimal());

        System.out.println("---SA---");
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        fit = new FixedIterationTrainerMod(sa, ITER);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10,9);

        pw.println("SA: " + ef.value(sa.getOptimal()) + " Time: " + trainingTime);
        pw.println(sa.getOptimal());

        System.out.println("---GA---");
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
        fit = new FixedIterationTrainerMod(ga, ITER);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10,9);


        pw.println("GA: " + ef.value(ga.getOptimal()) + " Time: " + trainingTime);
        pw.println(ga.getOptimal());

        System.out.println("---MIMIC---");
        MIMIC mimic = new MIMIC(24, 20, pop);
        fit = new FixedIterationTrainerMod(mimic, ITER);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10, 9);

        pw.println("MIMIC: " + ef.value(mimic.getOptimal()) + " Time: " + trainingTime);
        pw.println(mimic.getOptimal());

        pw.flush();
    }
}
