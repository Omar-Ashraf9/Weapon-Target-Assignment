import java.util.*;

public class GeneticAlgorithm
{
    public static Vector<Vector<Integer>> offSpring = new Vector<Vector<Integer>>();
    public static void fitnessCalc()
    {
        Main.fitValues = new Vector<Double>();
        for(int p = 0; p < Main.parentsNum; p++)
        {
            double totalScore = 0.0;
            int[][] weaponToTarget    = new int[Main.weaponsNum][Main.targetNum];    /** Assign each weapon to specific target. **/
            Double[][] resultMatrix   = new Double[Main.weaponsNum][Main.targetNum]; /**  will contain probability of surviving of each target against that weapon. **/

            /** Initialize matrices. **/
            for(int i = 0; i < Main.weaponsNum; i++)
            {
                for(int j = 0; j < Main.targetNum; j++)
                {
                    weaponToTarget[i][j] = 0;
                    resultMatrix[i][j]   = 1.0;
                }
            }

            /** fill the weapon to target matrix. **/
            Vector <Integer> tempParent = Main.population.get(p);
            for(int i = 0; i < Main.weaponsNum; i++)
            {
                weaponToTarget[i][tempParent.get(i)] = 1;
            }

            /** fill the result matrix. **/
            for(int i = 0; i < Main.weaponsNum; i++)
            {
                for(int j = 0 ; j < Main.targetNum; j++)
                {
                    if(weaponToTarget[i][j] == 1)
                    {
                        resultMatrix[i][j] = Main.invProb[i][j];
                    }
                }
            }

            /** multiply each column of the result matrix by the threat coefficient. **/
            for(int i = 0; i < Main.targetNum; i++)
            {
                double x = 1.0;
                for(int j = 0; j < Main.weaponsNum; j++)
                {
                    x *= resultMatrix[j][i];
                }
                x *= Main.threats.get(i);
                totalScore += x;
            }
            Main.fitValues.add(totalScore);
        }
    }
    public static void selection()
    {
        /** Apply tournament selection method to choose the appropriate parents **/
        int k = 2;
        Main.matingPool = new Vector<Vector<Integer>>();
        for(int i = 0; i < Main.parentsNum; i++)
        {
            double best = 1000000000;
            int idx , bestIdx = -1;
            for(int j = 0; j < k; j++)
            {
                Random rand = new Random();
                idx = rand.nextInt(Main.parentsNum);
                double individual = Main.fitValues.get(idx);
                if(individual < best)
                {
                    best = individual;
                    bestIdx = idx;
                }
            }
            Main.matingPool.add(Main.population.get(bestIdx));
        }
    }
    public static boolean isDiffrent()
    {
        /** utility function to check if all selected chromosomes are the same or not **/
        Vector <Integer> toCheck = Main.matingPool.get(0);
        for (int i = 1; i < Main.parentsNum; i++)
        {
            if(!toCheck.equals(Main.matingPool.get(i)))
            {
                return true;
            }
        }
        return false;
    }
    public static void crossOver()
    {
        Random rand = new Random();
        double pc = 0.6;
        int idxOfP1 , idxOfP2;
        for(int i = 0; i < Main.parentsNum / 2; i++)
        {
            idxOfP1 = 0; idxOfP2 = 0;

            /** loop until we select two different parents **/
            while (Main.matingPool.get(idxOfP1).equals(Main.matingPool.get(idxOfP2)))
            {
                idxOfP1 = rand.nextInt(Main.parentsNum);
                idxOfP2 = rand.nextInt(Main.parentsNum);
            }

            Vector<Integer> parent1 = Main.matingPool.get(idxOfP1);
            Vector<Integer> parent2 = Main.matingPool.get(idxOfP2);
            double rc = rand.nextDouble();
            if(rc <= pc)
            {
                 /** crossover occurs

                    P1: 1 2 1 1 0
                    P2: 2 0 1 1 1

                    child1:  1 | 0 1 1 1
                    child2:  2 | 2 1 1 0
                 **/

                /** Generate a random number to determine at which point we will perform crossover **/
                int xc = (int)(Math.random() * ((Main.weaponsNum-1) - (1) + 1) + 1);
                List child1fromP1 = parent1.subList(0,xc);
                List child1fromP2 = parent2.subList(xc,Main.weaponsNum);

                Vector<Integer> finalChild1 = new Vector<Integer>();
                finalChild1.addAll(child1fromP1);
                finalChild1.addAll(child1fromP2);




                List child2fromP2 =  parent2.subList(0,xc);
                List child2fromP1 =  parent1.subList(xc,Main.weaponsNum);

                Vector<Integer> finalChild2 = new Vector<Integer>();
                finalChild2.addAll(child2fromP2);
                finalChild2.addAll(child2fromP1);

                offSpring.add(finalChild1);
                offSpring.add(finalChild2);

            }else
            {
                /** add the parents to offspring **/
                offSpring.add(parent1);
                offSpring.add(parent2);
            }
        }
    }
    public static void mutation()
    {
        Random rand = new Random();
        double pm = 0.05;
        for(int i = 0; i < offSpring.size(); i++)
        {
            for(int j = 0; j < offSpring.get(i).size(); j++)
            {
                double r = rand.nextDouble();
                if(r <= pm)
                {
                    int newDigit = rand.nextInt(Main.targetNum);
                    offSpring.get(i).set(j,newDigit);
                }
            }
        }
    }
    public static void replacement()
    {
        /** Generational replacement: reproduce enough offspring to replace all population. **/
        Main.population = offSpring;
    }

    public static void printResult()
    {
        int idx = Main.fitValues.indexOf(Collections.min(Main.fitValues));
        int row = 0;
        Vector <Integer> res = Main.population.get(idx);
        System.out.println("The result is: " + res);
        for (Map.Entry<String,Integer> entry : Main.weapons.entrySet())
        {
            int cnt = 1;
            int amount = entry.getValue();
            String weaponName = entry.getKey();
            for(int i = row; i < row + amount; i++)
            {
                //Tank #1 is assigned to target #1,
                System.out.println(weaponName + " #" + cnt + " is assigned to target" + " #" + (res.get(i)+1));
                cnt++;
            }
            row += amount;
        }
        System.out.println("The expected total threat of the surviving targets is " + Main.fitValues.get(idx));
    }
    public static void GA()
    {
        for(int i = 0; i < Main.generationNum; i++)
        {
            /** calculate the fitness for each member in population. **/
            fitnessCalc();

            selection();

            /** if the mating pool all the same go back and re-calculate this iteration. **/
            if(!isDiffrent())
            {
                --i;
                continue;
            }
            crossOver();

            mutation();

            replacement();
        }
        printResult();
    }
}
