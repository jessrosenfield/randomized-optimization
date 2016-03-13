package org.jrosenfield3;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
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
    public static final int ITER1 = 200000;
    public static final int ITER2 = 1000;
    private static final double TIME_FACTOR = Math.pow(10, 9);

    public static void main(String[] args) {
        PrettyGraph graph = new PrettyGraph();
        PrettyGraphsEvaluationFunction ef = new PrettyGraphsEvaluationFunction(graph);
        int[] ranges = new int[graph.numVertices * 2];
        Arrays.fill(ranges, graph.max);
        Distribution dist = new DiscreteUniformDistribution(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new SingleCrossOver();
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, dist, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, dist, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, dist, df);

        double start, trainingTime, end = 0;

        System.out.println("---RHC---");
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainerMod fit = new FixedIterationTrainerMod(rhc, ITER1);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= TIME_FACTOR;
        PrintWriter pw = new PrintWriter(System.out);
        pw.println("RHC: " + ef.value(rhc.getOptimal()) + " Time: " + trainingTime);
        pw.println(rhc.getOptimal());

        System.out.println("---SA---");
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .9995, hcp);
        fit = new FixedIterationTrainerMod(sa, ITER1);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= TIME_FACTOR;

        pw.println("SA: " + ef.value(sa.getOptimal()) + " Time: " + trainingTime);
        pw.println(sa.getOptimal());

        System.out.println("---GA---");
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 25, gap);
        fit = new FixedIterationTrainerMod(ga, ITER2 * 3);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= TIME_FACTOR;


        pw.println("GA: " + ef.value(ga.getOptimal()) + " Time: " + trainingTime);
        pw.println(ga.getOptimal());

        System.out.println("---MIMIC---");
        MIMIC mimic = new MIMIC(200, 100, pop);
        fit = new FixedIterationTrainerMod(mimic, ITER2);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= TIME_FACTOR;

        pw.println("MIMIC: " + ef.value(mimic.getOptimal()) + " Time: " + trainingTime);
        pw.println(mimic.getOptimal());

        pw.flush();
    }
}
