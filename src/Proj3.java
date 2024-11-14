
//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*
       // ∗ @file: Proj3.java
       //
       // ∗ @author: Ananya Rajgaria
       // ∗ @date: November 14, 2024
       // ∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗//


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Proj3 {
    // Sorting Method declarations
    // Merge Sort
    public static <T extends Comparable> void mergeSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, mid, right);
        }
    }

    public static <T extends Comparable> void merge(ArrayList<T> a, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        ArrayList<T> L = new ArrayList<>(n1);
        ArrayList<T> R = new ArrayList<>(n2);

        for (int i = 0; i < n1; i++)
            L.add(a.get(left + i));
        for (int j = 0; j < n2; j++)
            R.add(a.get(mid + 1 + j));

        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (L.get(i).compareTo(R.get(j)) <= 0) {
                a.set(k, L.get(i));
                i++;
            } else {
                a.set(k, R.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            a.set(k, L.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            a.set(k, R.get(j));
            j++;
            k++;
        }
    }

    // Quick Sort
    public static <T extends Comparable> void quickSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(a, left, right);
            quickSort(a, left, pivotIndex - 1);
            quickSort(a, pivotIndex + 1, right);
        }
    }

    public static <T extends Comparable> int partition(ArrayList<T> a, int left, int right) {
        T pivot = a.get(right);
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (a.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, right);
        return i + 1;
    }

    // Heap Sort
    public static <T extends Comparable> void heapSort(ArrayList<T> a, int left, int right) {
        int n = right - left + 1;
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(a, n, i);
        for (int i = n - 1; i > 0; i--) {
            swap(a, 0, i);
            heapify(a, i, 0);
        }
    }

    public static <T extends Comparable> void heapify(ArrayList<T> a, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && a.get(left).compareTo(a.get(largest)) > 0)
            largest = left;

        if (right < n && a.get(right).compareTo(a.get(largest)) > 0)
            largest = right;

        if (largest != i) {
            swap(a, i, largest);
            heapify(a, n, largest);
        }
    }

    static <T> void swap(ArrayList<T> a, int i, int j) {
        T temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    // Bubble Sort
    public static <T extends Comparable> int bubbleSort(ArrayList<T> a, int size) {
        int comparisons = 0;
        boolean swapped;
        for (int i = 0; i < size - 1; i++) {
            swapped = false;
            for (int j = 0; j < size - i - 1; j++) {
                comparisons++;
                if (a.get(j).compareTo(a.get(j + 1)) > 0) {
                    swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return comparisons;
    }

    // Odd-Even Transposition Sort
    public static <T extends Comparable> int transpositionSort(ArrayList<T> a, int size) {
        int comparisons = 0;
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            // Odd indexed elements
            for (int i = 1; i < size - 1; i += 2) {
                comparisons++;
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    isSorted = false;
                }
            }
            // Even indexed elements
            for (int i = 0; i < size - 1; i += 2) {
                comparisons++;
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    isSorted = false;
                }
            }
        }
        return comparisons;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java Proj3 <filename> <algorithm> <numLines>");
            return;
        }

        String filename = args[0];
        String algorithm = args[1];
        int numLines = Integer.parseInt(args[2]);

        ArrayList<Integer> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null && data.size() < numLines) {
                data.add(Integer.parseInt(line.trim()));
            }
        }

        long startTime, endTime, duration;
        int comparisons = 0;

        switch (algorithm.toLowerCase()) {
            case "bubble":
                startTime = System.nanoTime();
                comparisons = bubbleSort(data, data.size());
                endTime = System.nanoTime();
                break;
            case "merge":
                startTime = System.nanoTime();
                mergeSort(data, 0, data.size() - 1);
                endTime = System.nanoTime();
                break;
            case "quick":
                startTime = System.nanoTime();
                quickSort(data, 0, data.size() - 1);
                endTime = System.nanoTime();
                break;
            case "heap":
                startTime = System.nanoTime();
                heapSort(data, 0, data.size() - 1);
                endTime = System.nanoTime();
                break;
            case "transposition":
                startTime = System.nanoTime();
                comparisons = transpositionSort(data, data.size());
                endTime = System.nanoTime();
                break;
            default:
                System.out.println("Unknown algorithm: " + algorithm);
                return;
        }

        duration = endTime - startTime;
        System.out.println("Lines: " + data.size() + ", Time: " + duration + " ns, Comparisons: " + comparisons);

        try (FileWriter fw = new FileWriter("analysis.txt", true)) {
            fw.write(data.size() + "," + algorithm + "," + duration + "," + comparisons + "\n");
        }

        try (FileWriter fw = new FileWriter("sorted.txt")) {
            for (int num : data) {
                fw.write(num + "\n");
            }
        }
    }
}
