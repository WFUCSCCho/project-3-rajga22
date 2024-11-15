//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗//
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Record class to store each record from the dataset
class Record implements Comparable<Record> {
    private int number;

    // Default constructor
    public Record() {
        this.number = 0;
    }

    // Parameterized constructor
    public Record(int number) {
        this.number = number;
    }

    // Copy constructor
    public Record(Record other) {
        this.number = other.number;
    }

    // toString() method
    @Override
    public String toString() {
        return "Record{number=" + number + "}";
    }

    // equals() method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Record record = (Record) obj;
        return number == record.number;
    }

    // compareTo() method
    @Override
    public int compareTo(Record other) {
        return Integer.compare(this.number, other.number);
    }

    // Getter for number
    public int getNumber() {
        return number;
    }
}

// Binary Search Tree (BST) class
class BST<T extends Comparable<T>> {
    private class Node {
        T data;
        Node left, right;

        Node(T data) {
            this.data = data;
            left = right = null;
        }
    }

    private Node root;

    // Insert method
    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node insertRec(Node root, T data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }
        if (data.compareTo(root.data) < 0) {
            root.left = insertRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = insertRec(root.right, data);
        }
        return root;
    }

    // In-order traversal to print the tree
    public void inorder() {
        inorderRec(root);
    }

    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.println(root.data);
            inorderRec(root.right);
        }
    }
}

// Main Proj3 class with sorting methods and data processing
public class Proj3 {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java Proj3 <filename> <algorithm> <numLines>");
            return;
        }

        String filename = args[0];
        String algorithm = args[1];
        int numLines = Integer.parseInt(args[2]);

        ArrayList<Record> data = new ArrayList<>();
        BST<Record> bst = new BST<>();

        // Reading data and filling the BST
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null && data.size() < numLines) {
                int number = Integer.parseInt(line.trim());
                Record record = new Record(number);
                data.add(record);
                bst.insert(record);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid format in dataset: " + e.getMessage());
        }

        // Print the BST (in-order traversal)
        System.out.println("BST in-order traversal:");
        bst.inorder();

        // Sorting methods
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
            for (Record record : data) {
                fw.write(record.getNumber() + "\n");
            }
        }
    }

    // Sorting methods
    public static <T extends Comparable<T>> int bubbleSort(ArrayList<T> a, int size) {
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

    public static <T extends Comparable<T>> void mergeSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, mid, right);
        }
    }

    public static <T extends Comparable<T>> void merge(ArrayList<T> a, int left, int mid, int right) {
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

    public static <T extends Comparable<T>> void quickSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(a, left, right);
            quickSort(a, left, pivotIndex - 1);
            quickSort(a, pivotIndex + 1, right);
        }
    }

    public static <T extends Comparable<T>> int partition(ArrayList<T> a, int left, int right) {
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

    public static <T extends Comparable<T>> void heapSort(ArrayList<T> a, int left, int right) {
        int n = right - left + 1;
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(a, n, i);
        for (int i = n - 1; i > 0; i--) {
            swap(a, 0, i);
            heapify(a, i, 0);
        }
    }

    public static <T extends Comparable<T>> void heapify(ArrayList<T> a, int n, int i) {
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

    public static <T extends Comparable<T>> int transpositionSort(ArrayList<T> a, int size) {
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
}