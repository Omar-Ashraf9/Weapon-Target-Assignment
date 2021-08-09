import java.util.*;

public class initialize
{
    public static void initPopulation()
    {
        for(int i = 0; i < Main.parentsNum; i++)
        {
            /** Generate a random number to assign each weapon to a target and repeat this process n times , (n = # of weapons) **/

            Random rand = new Random();
            Vector<Integer> parent = new Vector<Integer>();
            for(int j = 0; j < Main.weaponsNum; j++)
            {
                parent.add(rand.nextInt(Main.targetNum));
            }
            Main.population.add(parent);
        }

    }
    public static void prepMatrices()
    {
        Main.successProb = new Double[Main.weaponsNum + 1][Main.targetNum + 1];
        Main.invProb = new Double[Main.weaponsNum + 1][Main.targetNum + 1];

        int x , row = 0;
        double z;
        Vector<Double> tempRow;
        for (Map.Entry<String,Integer> entry : Main.weapons.entrySet())
        {
            tempRow = new Vector <Double>();
            for(int i = 0; i < Main.targetNum; i++)
            {
                z = Main.input.nextDouble();
                tempRow.add(z);
            }
            x = entry.getValue();
            for(int i = row; i < row + x; i++)
            {
                for(int j = 0; j < Main.targetNum; j++)
                {
                    Main.successProb[i][j] = tempRow.get(j);
                }
            }
            row += x;
        }
        for(int i = 0; i < Main.weaponsNum; i++)
        {
            for(int j = 0; j < Main.targetNum; j++)
            {
                Main.invProb[i][j] = 1 - Main.successProb[i][j];
            }
        }

    }
}
