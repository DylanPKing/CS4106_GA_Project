import java.util.*;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Graphics;

public class GraphOrdering extends JFrame{

    //I need chunk for fitness function

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    

    static int[][] adjMatrix;
    static int[] currentOrdering;
    static int populationSize;

    public static double chunk = 0;

    public GraphOrdering() {
        setTitle("AI");
        setSize(960, 960);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void paint(Graphics g) {
        int radius = 100;
        int mov = 200;
        
        for (int i = 0; i < populationSize; i++) {
            for (int j = i + 1; j < populationSize; j++) {
                if (adjMatrix[currentOrdering[i]][currentOrdering[j]] == 1) {
                    g.drawLine(
                        (int)(((double) Math.cos(i * chunk)) * radius + mov),
                        (int)(((double) Math.sin(i * chunk)) * radius + mov),
                        (int)(((double) Math.cos(j * chunk)) * radius + mov),
                        (int)(((double) Math.cos(j * chunk)) * radius + mov)
                    );
                }
            }
        }
    }

    public static void main(String [] args) throws IOException
	{
		 //Szymon needs these for input
        int numberOfGenerations = -1;
        int crossoverRate = -1;
        int mutationRate = -1;

		//Get the welcome message
		welcome();
        
        /*-------------------------------Szymon is is doing validation and input-------------------------------*/
        //get the population size using loops to back step
		while(true){
            populationSize = validator(crossoverRate, 0, "Please enter a population size.", "Your hand must have slipped. Enter a positive integer.");
            //If you cancel at first window, you want to quit.
			if(populationSize == -1)
                System.exit(0);
            //Get number of generations
			while(numberOfGenerations == -1){
                numberOfGenerations = validator(crossoverRate, 0, "Please enter the number of generations.", "Well, it would appear you did something wrong, YOU, not us.");
                //If you cancel here, go back one window
                if(numberOfGenerations == -1)
			        break;
                //Get crossover rate
                while(crossoverRate == -1){
                    crossoverRate = validator(crossoverRate, 1, "Enter the crossover rate (0-100)", "Were the instructions unclear? A number in range....");
                    //If you cancel here, go back one window
                    if(crossoverRate == -1){
                        numberOfGenerations = -1;
                        break;
                    }
                    //Get mutation rate
                    while(mutationRate == -1){
                        mutationRate = validator(crossoverRate, 1, "Enter the mutation rate (0-100)", "*sigh* Can you please actually try to follow the instructions?");
                        //If you cancel here, go back one window
                        if(mutationRate == -1){
                            crossoverRate = -1;
                            break;
                        }
                    }
                }
            }
            //If everything is correct, exit the loop and be happy about being done with validation
            if(numberOfGenerations != -1 && crossoverRate != -1 && mutationRate != -1)
                break;
        }
        /*-------------------------------End Szymon is is doing validation and input-------------------------------*/
        //The main continues here
        chunk = (2 * Math.PI) / populationSize;
        adjMatrix = convertEdgeTable();
        int[][] currentPopulation = new int[adjMatrix.length][populationSize];
        int[][] nextPopulation = new int[adjMatrix.length][populationSize];
        double[] fitness = new double[adjMatrix.length];
        initialisation(currentPopulation, adjMatrix.length);
        for (int i = 0; i < currentPopulation.length; i++) {
            currentOrdering = currentPopulation[i];
            fitness[i] = fitnessFunction();
        }
        
        for (int i = 0; i < numberOfGenerations; i++) {
            selectionProcess(currentPopulation, fitness);
            //Crossover here
            
            for (int j = 0; j < nextPopulation.length; j++) {
                currentOrdering = currentPopulation[j];
                fitness[j] = fitnessFunction();
            }

            for (int j = 0; j < nextPopulation.length; j++) {
                currentPopulation[j] = Arrays.copyOf(nextPopulation[j], nextPopulation[j].length);
            }
            currentOrdering = currentPopulation[0];
            GraphOrdering visualisation = new GraphOrdering();
        }
    }
	
	/*-------------------------------Szymon is is doing validation and input-------------------------------*/
	
	/**
	 * Just a quick welcome message
	 * @author Szymon Sztyrmer
	 */
	public static void welcome()
	{
		JOptionPane.showMessageDialog(null, "Welcome, press OK to start, use the cancel button to return to the previous screen during input.");
	}
	
    /**
     * The function that validates all the user input
     * @author Szymon Sztyrmer
     * @param crossoverRate Keeping track of the crossover rate for 1 check at the end
     * @param check An int telling the method whether its dealing with the 2 range bound variables
     * @param enterMessage A string containing the message to be displayed on the input window
     * @param errorMessage A string containing the custom error should the user not supply the correct information
     * @return userInput is parsed into an integer and returned back to the main to be stored in the corresponding variable
     */
    public static int validator(int crossoverRate, int check, String enterMessage, String errorMessage){
        //Get the variables I need
        String pattern = "[0-9]{1,}";
        String userInput = "";

        //Get the user to enter a number
        while(true){
            //User input
            userInput = (String) JOptionPane.showInputDialog(null, enterMessage);
			//If you press cancel
			if(userInput == null){
				JOptionPane.showMessageDialog(null, "Process Aborted", "Ha, I got you!", 0);
				return -1;
			}
            //Check if integer
            if(!(userInput.matches(pattern))){
                //If not, repeat
                JOptionPane.showMessageDialog(null, errorMessage, "Oops, Something went wrong.", 0);
                continue;
            }
            //If integer, move on
            else{
                //If it's crossover or mutation
                //If it's greater than 100 because it can't be less than 0 from up above, trust me I tried.
                if(check == 1 && Integer.parseInt(userInput) > 100){
                    //Back to the top to get a number that's not greater than 100
                    JOptionPane.showMessageDialog(null, "Really? The instructions said (0-100) right? How about you try to follow that", "Numbers are hard", 0);
                    continue;
                }
                //If the sum of crossover and mutation is greater than 100
                else if(check == 1 && (crossoverRate + Integer.parseInt(userInput)) > 100){
                    //Back to the previous window to select new crossover and mutation
                    JOptionPane.showMessageDialog(null, "The sum of the crossover rate and the mutation rate cannot be bigger than 100...", "You like big numbers a little too much", 0);
                    return -1;
                }
            }   
            //If you got here, you have a nice validated number as a string to be converted to int.
            break;
        }
        //Return the number
        return Integer.parseInt(userInput);
    }

    public static int[][] convertEdgeTable() throws IOException {
        File inputFile = new File("input.txt");
        Scanner in = new Scanner(inputFile);
        int maxCol = 0;
        int maxRow = 0;
        int temp = 0;
        ArrayList<ArrayList<Integer>> edgeTable = new ArrayList<ArrayList<Integer>>();
        edgeTable.add(new ArrayList<Integer>());
        edgeTable.add(new ArrayList<Integer>());
        String[] split;
        System.out.println(inputFile.exists());
        /*
        Split file contents into temp variable, then fill matrix.
        */
        while(in.hasNext()){
            split = in.nextLine().split(" ");
            for(int i = 0; i<split.length;i++){
                temp = Integer.parseInt(split[i]);
                if(i%2==0)
                    edgeTable.get(0).add(temp);
                else
                    edgeTable.get(1).add(temp);
            }
        }
        for(int i=0;i<edgeTable.size();i++){
            for(int j=0;j<edgeTable.get(i).size();j++)
                System.out.print(edgeTable.get(i).get(j) + " ");
            System.out.println();
        }
        /*
        Assign Max Row value
        */
        for(int j =0; j<edgeTable.get(0).size() - 1;j++)
            maxRow = Math.max(maxRow, Math.max(edgeTable.get(0).get(j), edgeTable.get(0).get(j+1)));
        System.out.println("Max Row: " + maxRow);
        /*     
        Assign Max Coloumn value
        */
        for(int j =0; j<edgeTable.get(1).size() - 1;j++)
            maxCol = Math.max(maxCol, Math.max(edgeTable.get(1).get(j), edgeTable.get(1).get(j+1)));
        System.out.println("Max Column: "+maxCol);
        /*
        Create the adjacency Matrix
        Convert edge list to adjacency Matrix
        */
        int[][] matrix = new int[maxRow + 1][maxCol + 1];

        for (int i = 0; i < edgeTable.get(0).size(); i++) {
            matrix[edgeTable.get(0).get(i)][edgeTable.get(1).get(i)] = 1;
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1)
                    matrix[j][i] = 1;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        in.close();
        return matrix;
    } 

    public static void initialisation(int[][] current, int maxRow)
    {
        int random = 0;
        boolean found = false;
        int populationCount = 0;

        //Let's make unique random values
        ArrayList<Integer> uniqueRandomNumbers = new ArrayList<Integer>();
        for(int row = 0; row < (maxRow+1); row++){
            for(int i = 0; i< (maxRow+1);){
                found = false;
                random = (int)(Math.random() * 18);
                for(int j = 0; j< uniqueRandomNumbers.size(); j++){
                    if(uniqueRandomNumbers.get(j) == random)
                        found = true;
                }
                if(!found){
                    uniqueRandomNumbers.add(random);
                    i++;
                }
            }
            for(int j = 0; j < uniqueRandomNumbers.size();j++){
                current[populationCount][j] =uniqueRandomNumbers.get(j);
            }
            uniqueRandomNumbers.clear();
            populationCount++;
        }

        System.out.println("Here begins the population initialisation");
        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[i].length; j++) {
                System.out.print(current[i][j] + " ");
            }
            System.out.println();
        }
    }
    

    /*-------------------------------End Szymon is doing validation and input-------------------------------*/

    /**
     * Entry function for selection process.
     * @param currentPop all the combinations stored
     * @param fitness corresponding fitness values for each member of the population
     */
    public static void selectionProcess(int[][] currentPop, double[] fitness) {
        selectionProcessSort(currentPop, fitness, 0, fitness.length);
        increaseFitness(currentPop, fitness);
    }

    /**
     * Replaces the lowest performing segment of the population with copies of the highest performing segment
     * @param currentPop all the combinations stored
     * @param fitness corresponding fitness values for each member of the population
     */
    private static void increaseFitness(int[][] currentPop, double[] fitness) {
        int segSize = fitness.length / 3;

        // Starting point for loops, and offset for fetching the index to replace with
        // Same as segSize * 2 but addition uses less CPU time
        int offset = segSize + segSize;

        // Iterates through both loops and replaces low-fitness orderings with high performance orderings,
        // as well as corresponding fitness scores
        for (int i = offset; i < fitness.length; i++) {
            currentPop[i] = currentPop[i - offset];
            fitness[i] = fitness[i - offset];
        }

        // In case the population is not divisible by 3, to avoid 1 or 2 low-fitness orderings surviving
        if (fitness.length % 3 != 0) {

            // New offset. Same as segSize * 3
            offset += segSize;

            // Same loop as above, but with new offset
            for (int i = segSize * 3; i < fitness.length; i++) {
                currentPop[i] = currentPop[i - offset];
                fitness[i] = fitness[i - offset];
            }
        }
    }

    /**
     * Recursive mergesort function for orderings.
     * @param currentPop all the combinations stored
     * @param fitness corresponding fitness values for each member of the population
     * @param left lower bound to use for array
     * @param right upper bound to use for array
     */
    private static void selectionProcessSort(int[][] currentPop, double[] fitness, final int left, final int right) {

        // Only execute if the bounds do not overlap
        if (left < right) {
            int middle = (left + right) / 2;

            // Recursive call for left half of current array
            selectionProcessSort(currentPop, fitness, left, middle);

            // and for the right half
            selectionProcessSort(currentPop, fitness, middle + 1, right);

            // then stitches them back together
            selectionProcessMerge(currentPop, fitness, left, middle, right);
        }
    }

    /**
     * Merges sorted array segments back together in ascending order of fitness
     * @param currentPop all the combinations stored
     * @param fitness corresponding fitness values for each member of the population
     * @param left lower bound to use for array split
     * @param middle midpoint to use for array split
     * @param right upper bound to use for array split
     */
    private static void selectionProcessMerge(int[][] currentPop, double[] fitness,
                                       final int left, final int middle, final int right) {

        // Creating copies of each half of both arrays
        double[] leftFitArr = Arrays.copyOfRange(fitness, 0, middle);
        double[] rightFitArr = Arrays.copyOfRange(fitness, middle + 1, right);
        int[][] leftPopArr = Arrays.copyOfRange(currentPop, 0, middle);
        int[][] rightPopArr = Arrays.copyOfRange(currentPop, middle + 1, right);

        // Declaring counters for the rest of the method
        int i = 0, j = 0, k = left;

        // Goes through the fitness arrays and places the ordering with the lowest fitness score first
        // Only increments the index that was chosen so neither half is skipped over
        for (; i < leftFitArr.length && j < rightFitArr.length; k++) {
            if (leftFitArr[i] <= rightFitArr[j]) {
                currentPop[k] = leftPopArr[i];
                fitness[k] = leftFitArr[i];
                i++;
            } else {
                currentPop[k] = rightPopArr[j];
                fitness[k] = rightFitArr[j];
                j++;
            }
        }

        // If the right half is fully placed first, assign remaining left half orderings
        for (; i < leftFitArr.length; i++, k++) {
            currentPop[k] = leftPopArr[i];
            fitness[k] = leftFitArr[i];
        }

        // and vice-versa
        for (; j < rightFitArr.length; j++, k++) {
            currentPop[k] = rightPopArr[j];
            fitness[k] = rightFitArr[j];
        }
    }

    /*------------------------------------Szymon is doing the fitness function----------------------------------------------*/
    
    /**
     * The function that evaluates the fitness of the any given ordering
     * @author Szymon Sztyrmer
     * @return distance The distance of the line "drawn" between all the points (Lower is better)
     */
    public static double fitnessFunction() {
        int radius = 100;
        int mov = 200;
        double x1 = 0.0;
        double x2 = 0.0; 
        double y1 = 0.0; 
        double y2 = 0.0;
        double distance = 0.0;
    

        for(int i = 0; i < adjMatrix.length; i++)
            for(int j = i; j < adjMatrix[i].length; j++){
                if(adjMatrix[currentOrdering[i]][currentOrdering[j]] == 1){
                    x1 = (((double) Math.cos(i * chunk)) * radius + mov);
                    y1 = (((double) Math.sin(i * chunk)) * radius + mov);
                    x2 = (((double) Math.cos(j * chunk)) * radius + mov);
                    y2 = (((double) Math.cos(j * chunk)) * radius + mov);
                }
                
                distance += Math.sqrt(Math.pow((x2 - x1), 2) + (Math.pow(y2 - y1, 2)));
            }
        return distance;
    }
    
    /*------------------------------------End Szymon is doing the fitness function----------------------------------------------*/
}