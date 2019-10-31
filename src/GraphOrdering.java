import java.util.Arrays;

public class GraphOrdering {

    /**
     * Entry function for selection process.
     * @param currentPop all the combinations stored
     * @param fitness corresponding fitness values for each member of the population
     */
    public void selectionProcess(int[][] currentPop, double[] fitness) {
        selectionProcessSort(currentPop, fitness, 0, fitness.length);
        increaseFitness(currentPop, fitness);
    }

    /**
     * Replaces the lowest performing segment of the population with copies of the highest performing segment
     * @param currentPop all the combinations stored
     * @param fitness corresponding fitness values for each member of the population
     */
    private void increaseFitness(int[][] currentPop, double[] fitness) {
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
    private void selectionProcessSort(int[][] currentPop, double[] fitness, final int left, final int right) {

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
    private void selectionProcessMerge(int[][] currentPop, double[] fitness,
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
}
