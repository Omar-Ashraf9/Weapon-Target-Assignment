import java.util.*;
public class Main
{
    public static Scanner input = new Scanner(System.in);
    public static Vector<Vector<Integer>> population = new Vector<Vector<Integer>>();
    public static Map <String, Integer> weapons = new LinkedHashMap <String, Integer>();
    public static Vector <Integer> threats = new Vector <Integer>();
    public static int targetNum , weaponsNum , quantity , generationNum = 50 , parentsNum = 6;
    public static Vector<Double> fitValues;
    public static Vector<Vector<Integer>> matingPool;

    public static Double[][] successProb , invProb;

    public static void takeInput()
    {
        System.out.println("Enter the weapon types and the number of instances of each type: (Enter “x” when you’re done)");
        weaponsNum = 0;
        while (true)
        {
            String name = input.next();
            if(name.equals("x"))
                break;
            quantity = input.nextInt();
            weapons.put(name , quantity);
            weaponsNum += quantity;
        }
        System.out.println("Enter the number of targets: ");
        targetNum = input.nextInt();
        System.out.println("Enter the threat coefficient of each target: ");
        for(int i = 0; i < targetNum; i++)
        {
            int x = input.nextInt();
            threats.add(x);
        }
        System.out.println("Enter the weapons success probabilities matrix: ");
        initialize init = new initialize();
        GeneticAlgorithm g = new GeneticAlgorithm();
        init.prepMatrices();
        init.initPopulation();
        System.out.println("Please wait while running the GA…");
        g.GA();
    }
    public static void main(String[] args)
    {
        takeInput();
    }
}
