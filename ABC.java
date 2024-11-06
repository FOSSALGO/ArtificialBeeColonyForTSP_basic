import java.util.Arrays;
import java.util.Random;


public class ABC {

    public static void main(String[] args) {

        double[][] adjacency = {
            {0, 2, 6, 4, 3, 3},
            {2, 0, 3, 4, 5, 2},
            {6, 3, 0, 4, 7, 5},
            {4, 4, 4, 0, 2, 3},
            {3, 5, 7, 2, 0, 1},
            {3, 2, 5, 3, 1, 0}

        };

        int popSize = 20;
        int limit = 3;
        int maxIteration = 100;

        int[] bestSolution = null;
        double bestDistance = 0;
        double bestFitness = 0;

        int[][] population = new int[popSize][];
        double[] distance = new double[popSize];
        double[] fitness = new double[popSize];
        int[] trial = new int[popSize];

        for (int i = 0; i < popSize; i++) {
            int[] solution = randomSolution(adjacency);
            population[i] = solution;
            double d = 0;
            int origin = solution[0];
            for (int j = 1; j < solution.length; j++) {
                int destination = solution[j];
                double jarak = adjacency[origin][destination];
                d += jarak;
                origin = destination;
            }
            distance[i] = d;
            fitness[i] = 1.0 / d;

            // ELITISM
            if (fitness[i] > bestFitness) {
                bestSolution = solution.clone();
                bestDistance = distance[i];
                bestFitness = fitness[i];
            }
        }

        for (int iteration = 1; iteration <= maxIteration; iteration++) {
            //FASE EMPLOYEE BEE-----------------------------------------------------
            for (int i = 0; i < popSize; i++) {
                int[] candidate = population[i].clone();
                int index1 = randomBetween(1, candidate.length - 2);
                int index2 = index1;
                while (index1 == index2) {
                    index2 = randomBetween(1, candidate.length - 2);
                }

                //SWAP OPERATION
                int temp = candidate[index1];
                candidate[index1] = candidate[index2];
                candidate[index2] = temp;

                // Hitung Fitness candidate
                double d = 0;
                int origin = candidate[0];
                for (int j = 1; j < candidate.length; j++) {
                    int destination = candidate[j];
                    double jarak = adjacency[origin][destination];
                    d += jarak;
                    origin = destination;
                }
                double distanceCandidate = d;
                double fitnessCandidate = 1.0 / d;

                if (fitnessCandidate > fitness[i]) {
                    population[i] = candidate;
                    distance[i] = distanceCandidate;
                    fitness[i] = fitnessCandidate;
                    trial[i] = 0;
                } else {
                    trial[i]++;
                }
            }//END OF FASE EMPLOYEE BEE

            //FASE ONLOOKER BEE-------------------------------------------------
            int k = 0;
            while (k < popSize) {
                double totalFitness = 0;
                for (int i = 0; i < popSize; i++) {
                    totalFitness += fitness[i];
                }
                double[] probability = new double[popSize];
                for (int i = 0; i < popSize; i++) {
                    probability[i] = fitness[i] / totalFitness;
                }
                for (int i = 0; i < popSize; i++) {
                    if (k < popSize) {
                        double r = new Random().nextDouble();
                        if (r < probability[i]) {
                            k++;
                            int[] candidate = population[i].clone();
                            int index1 = randomBetween(1, candidate.length - 2);
                            int index2 = index1;
                            while (index1 == index2) {
                                index2 = randomBetween(1, candidate.length - 2);
                            }

                            //SWAP OPERATION
                            int temp = candidate[index1];
                            candidate[index1] = candidate[index2];
                            candidate[index2] = temp;

                            // Hitung Fitness candidate
                            double d = 0;
                            int origin = candidate[0];
                            for (int j = 1; j < candidate.length; j++) {
                                int destination = candidate[j];
                                double jarak = adjacency[origin][destination];
                                d += jarak;
                                origin = destination;
                            }
                            double distanceCandidate = d;
                            double fitnessCandidate = 1.0 / d;

                            if (fitnessCandidate > fitness[i]) {
                                population[i] = candidate;
                                distance[i] = distanceCandidate;
                                fitness[i] = fitnessCandidate;
                                trial[i] = 0;
                            } else {
                                trial[i]++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }//END OF FASE ONLOOKER BEE

            // FASE SCOUT BEE --------------------------------------------------
            for (int i = 0; i < popSize; i++) {
                if (trial[i] > limit) {
                    int[] solution = randomSolution(adjacency);
                    population[i] = solution;
                    double d = 0;
                    int origin = solution[0];
                    for (int j = 1; j < solution.length; j++) {
                        int destination = solution[j];
                        double jarak = adjacency[origin][destination];
                        d += jarak;
                        origin = destination;
                    }
                    distance[i] = d;
                    fitness[i] = 1.0 / d;
                    trial[i] = 0;
                }
            }

            //SAVE BEST SOLUTION
            for (int i = 0; i < popSize; i++) {
                // ELITISM
                if (fitness[i] > bestFitness) {
                    bestSolution = population[i].clone();
                    bestDistance = distance[i];
                    bestFitness = fitness[i];
                }
            }
            
        }//END OF ITERATION
        
        System.out.println("BEST SOLUTION");
        System.out.println(Arrays.toString(bestSolution));
        System.out.println(bestDistance);
        System.out.println(bestFitness);

//System.out.println("--------------------------------------");
//        for (int i = 0; i < popSize; i++) {
//            for (int j = 0; j < population[i].length; j++) {
//                if (j > 0) {
//                    System.out.print(" - ");
//                }
//                System.out.print("V"+population[i][j]);
//            }
//            System.out.print(" distance = " + distance[i]);
//            System.out.println(" = "+fitness[i]);
//        }
//        System.out.println("--------------------------------------");
//        for (int i = 0; i < popSize; i++) {
//            for (int j = 0; j < population[i].length; j++) {
//                if (j > 0) {
//                    System.out.print(" - ");
//                }
//                System.out.print("V"+population[i][j]);
//            }
//            System.out.print(" distance = " + distance[i]);
//            System.out.println(" = "+fitness[i]);
//        }
    }

    public static int[] randomSolution(double[][] adjacency) {
        int[] solution = new int[adjacency.length + 1];
        int depot = 3;
        solution[0] = depot;
        solution[solution.length - 1] = depot;

        for (int i = 1; i < solution.length - 1; i++) {
            int random = randomBetween(0, adjacency.length - 1);
            boolean unique = false;
            while (unique == false) {
                unique = true;
                for (int j = 0; j < i; j++) {
                    if (random == solution[j]) {
                        random = randomBetween(0, adjacency.length - 1);
                        unique = false;
                        break;
                    }
                }
                if (unique == true) {
                    solution[i] = random;
                }
            }

        }
        return solution;
    }

    public static int randomBetween(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        Random random = new Random();
        int r = min + random.nextInt(max - min + 1);
        return r;
    }
}
