import opt.*;
import opt.example.*;
import opt.ga.*;
import shared.*;
import func.nn.backprop.*;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights` to a neural network that is classifying vowels.
 *
 * @author Jessica Rosenfield
 * @version 1.0
 */
public class VowelTest {
    private static Instance[] instances = initializeInstances("datasets/vowel/vowel.train", 528);
    private static Instance[] testing = initializeInstances("datasets/vowel/vowel.test", 462);

    private static int inputLayer = 10, hiddenLayer = 100, outputLayer = 1, trainingIterations = 5000;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();

    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(instances);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private static String[] oaNames = {"RHC", "SA", "GA"};
    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) {
        for(int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                    new int[] {inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
        }

        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);

        for(int i = 0; i < oa.length; i++) {
            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
            train(oa[i], networks[i], oaNames[i]); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());

            double predicted, actual;
            start = System.nanoTime();
            for(int j = 0; j < testing.length; j++) {
                networks[i].setInputValues(testing[j].getData());
                networks[i].run();

                predicted = Double.parseDouble(testing[j].getLabel().toString());
                actual = Double.parseDouble(networks[i].getOutputValues().toString());

                if (Math.abs(predicted - actual) < 0.5 ) {
                    correct++;
                } else {
                    incorrect++;
                }

            }
            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10,9);

            results +=  "\nResults for " + oaNames[i] + ": \nCorrectly classified " + correct + " instances." +
                    "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                    + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        }

        System.out.println(results);
    }

    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
        System.out.println("\nError results for " + oaName + "\n---------------------------");
        double start = System.nanoTime();
        for(int i = 0; i < trainingIterations; i++) {
            oa.train();

            double error = 0;
            for(int j = 0; j < testing.length; j++) {
                network.setInputValues(testing[j].getData());
                network.run();

                Instance output = testing[j].getLabel(), example = new Instance(network.getOutputValues());
                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                error += (Math.abs(Double.parseDouble(output.toString()) - Double.parseDouble(example.getLabel().toString())) < .5) ? 0 : 1;
            }
            if (error/testing.length*100 < 10) {
                double end = System.nanoTime();
                double totalTime = end - start;
                totalTime /= Math.pow(10,9);
                System.out.println(i + ", " + error/testing.length*100 + ", " + totalTime + " sec");
                return;
            }
        }
    }

    private static Instance[] initializeInstances(String filename, int numInstances) {

        double[][][] attributes = new double[numInstances][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            br.readLine();

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[10]; // 10 attributes
                attributes[i][1] = new double[1];
                scan.next();
                attributes[i][1][0] = Double.parseDouble(scan.next());
                for(int j = 0; j < attributes[i][0].length; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            // binary classification of single vowel 'i' from "heed"
            instances[i].setLabel(new Instance(attributes[i][1][0] == 1 ? 1 : 0));
        }

        return instances;
    }
}