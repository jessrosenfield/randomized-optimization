package org.jrosenfield3;

import shared.Trainer;

import java.io.PrintWriter;


/**
 * A fixed iteration trainer
 *
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FixedIterationTrainerMod implements Trainer {

    /**
     * The inner trainer
     */
    private Trainer trainer;

    /**
     * The number of iterations to train
     */
    private int iterations;

    /**
     * Make a new fixed iterations trainer
     *
     * @param t    the trainer
     * @param iter the number of iterations
     */
    public FixedIterationTrainerMod(Trainer t, int iter) {
        trainer = t;
        iterations = iter;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train(double start) {
        double sum = 0;
        double end = 0;
        double trainingTime = 0;
        PrintWriter pw = new PrintWriter(System.out);
        for (int i = 0; i < iterations; i++) {
            double fitness = trainer.train();
            if (i < 100 || i % 100 == 0) {
                end = System.nanoTime();
                trainingTime = end - start;
                trainingTime /= Math.pow(10, 9);
                pw.println(i + ", " + trainingTime + ", " + fitness);
            }
            if (fitness == -1) {
                pw.flush();
                return sum / i;
            }
            sum += fitness;
        }
        pw.flush();
        return sum / iterations;
    }

    @Override
    public double train() {
        return train(System.nanoTime());
    }
}