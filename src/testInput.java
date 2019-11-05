public static void main String[] args
{
    File inputFile = new File("input.txt");
    Scanner in = new Scanner(System.in);
    int maxCol = 0;
    int maxRow = 0;
    int temp = 0;
    String toParse = "";
    int[][] matrix = new int[maxCol][maxRow];

        while(inputFile.hasNextLine())
        {
            toParse = inputFile.readLine();
            temp = Integer.parseInt(toParse.split(" "));
                    for(int r = 0;r<1; r++)
                    {
                         for(int c = 0;c<1; c++)
                        {
                            matrix[r][c] = temp;
                            System.out.print(matrix[r][c]);
                            if(r%2 == 0)
                            System.out.print("\n");
                             if(matrix[r] < matrix[r+1])
                                maxRow = matrix[r+1];
                             if(matrix[c] < matrix[c+1])
                                maxCol = matrix[c+1];
                            System.out.print("Max Coloumn: " + maxCol + " " + "Max Row: " + maxRow);
                        }
                    }
        }
}