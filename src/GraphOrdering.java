import java.util.Arrays;

public class GraphOrdering {

    public void selectionProcess(int[][] currentPop, double[] fitness) {
        selectionSort(currentPop, fitness, 0, fitness.length);
        increaseFitness(currentPop, fitness);
    }

    private void increaseFitness(int[][] currentPop, double[] fitness) {
        int segSize = fitness.length / 3;
        for (int i = 0; i < segSize; i++) {
            
        }
    }

    private void selectionSort(int[][] currentPop, double[] fitness, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            selectionSort(currentPop, fitness, left, middle);
            selectionSort(currentPop, fitness, middle + 1, right);
            selectionMerge(currentPop, fitness, left, middle, right);
        }
    }

    private void selectionMerge(int[][] currentPop, double[] fitness, int left, int middle, int right) {
        double leftFitArr[] = Arrays.copyOfRange(fitness, 0, middle);
        double rightFitArr[] = Arrays.copyOfRange(fitness, middle + 1, right);
        int leftPopArr[][] = Arrays.copyOfRange(currentPop, 0, middle);
        int rightPopArr[][] = Arrays.copyOfRange(currentPop, middle + 1, right);

        int i = 0, j = 0, k = left;
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

        for (; i < leftFitArr.length; i++, k++) {
            currentPop[k] = leftPopArr[i];
            fitness[k] = leftFitArr[i];
        }
        for (; j < rightFitArr.length; j++, k++) {
            currentPop[k] = rightPopArr[j];
            fitness[k] = rightFitArr[j];
        }
    }
}
