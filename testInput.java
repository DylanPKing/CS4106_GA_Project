import java.util.*;
import java.io.*;
public class testInput{
    public static void main(String[] args) throws IOException{
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
        
    }
}