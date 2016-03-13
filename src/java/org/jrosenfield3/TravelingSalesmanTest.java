package org.jrosenfield3;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 *
 * @author Jessica Rosenfield from Andrew Guillory
 * @version 1.0
 */
public class TravelingSalesmanTest {
    /** The n value */
    private static final int N = 50;
    private static final double TIME_FACTOR = Math.pow(10, 9);

    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Random random = new Random();
        // create the random points
        double[][] points = new double[N][2];
        for (int i = 0; i < points.length; i++) {
            points[i][0] = random.nextDouble();
            points[i][1] = random.nextDouble();
        }
        // for rhc, sa, and ga we use a permutation based encoding
        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
        Distribution odd = new DiscretePermutationDistribution(N);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new TravelingSalesmanCrossOver(ef);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        PrintWriter pw = new PrintWriter(System.out);

        System.out.println("---RHC---");
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainerMod fit = new FixedIterationTrainerMod(rhc, 200000);
        double start = System.nanoTime();
        fit.train(start);
        double end = System.nanoTime();
        double trainingTime = end - start;
        trainingTime /= TIME_FACTOR;
        pw.println("RHC: " + ef.value(rhc.getOptimal()) + " Time: " + trainingTime);
        pw.println(rhc.getOptimal());

        System.out.println("---SA---");
        SimulatedAnnealing sa = new SimulatedAnnealing(1E12, .95, hcp);
        fit = new FixedIterationTrainerMod(sa, 200000);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= TIME_FACTOR;
        pw.println("SA: " + ef.value(sa.getOptimal()) + " Time: " + trainingTime);
        pw.println(sa.getOptimal());

        System.out.println("---GA---");
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 20, gap);
        fit = new FixedIterationTrainerMod(ga, 1000);
        start = System.nanoTime();
        fit.train(start);
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= TIME_FACTOR;
        pw.println("GA: " + ef.value(ga.getOptimal()) + " Time: " + trainingTime);
        pw.println(ga.getOptimal());

        // for mimic we use a sort encoding
        System.out.println("---MIMIC---");
        ef = new TravelingSalesmanSortEvaluationFunction(points);
        int[] ranges = new int[N];
        Arrays.fill(ranges, N);
        odd = new  DiscreteUniformDistribution(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        MIMIC mimic = new MIMIC(200, 100, pop);
        fit = new FixedIterationTrainerMod(mimic, 1000);
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
