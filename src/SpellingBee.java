import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        ArrayList<String> substrings = new ArrayList<String>();
        // First and last must start at zero to be able to capture all substrings
        words = generateHelper(substrings, 0, 0, letters);
        for (String s : words) {
            System.out.println(s);
        }
    }

    public ArrayList<String> generateHelper(ArrayList<String> arr, int first, int last, String word) {
        // Base case: If we have nothing more to check, return the array of substrings
        if (first == word.length()) {
            return arr;
        }
        // Second base case: If the last index exceeds the length of the word then move onto the next starting index and reset the last index
        if (last >= word.length()) {
            return generateHelper(arr, first + 1, first + 1, word);
        }
        // Add current substring to arraylist making sure to include last index
        arr.add(word.substring(first, last + 1));
        // Keep moving on in the word by increasing the last index
        return generateHelper(arr, first, last + 1, word);
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        int[] sorted = new int[words.size()];
        sortHelper(sorted, 0, words.size() -1);
    }

    public int[] sortHelper(int[] arr, int low, int high) {
        if (high == low) {
            int[] newArr = new int[1];
            newArr[0] = arr[low];
            return newArr;
        }
        int med = (high + low)/2;
        int[] arr1 = sortHelper(arr, low, med);
        int[] arr2 = sortHelper(arr, med+1, high);
        return merge(arr1, arr2);
    }

    private int[] merge(int[] arr1, int[] arr2) {
        int[] merged = new int[arr1.length + arr2.length];
        int i = 0, j =0;
        while (i < arr1.length && j < arr2.length) {
            if (arr1[i] < arr2[j]) {
                merged[i + j] = arr1[i++];
            }
            else {
                merged[i+j] = arr2[j++];
            }
        }
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for (String w: words) {
            checkWordsHelper(w, 0, DICTIONARY_SIZE -1);
        }
    }

    public boolean checkWordsHelper(String word, int first, int last) {
        int mid = (first + last)/2 + first;
        if (first == last) {
            return false;
        }
        if (DICTIONARY[mid].equals(word)) {
            return true;
        }
        if (1 ==1) {// If dictionary[mid] >= word
            return checkWordsHelper(word, first, mid -1);
        }
        return checkWordsHelper(word, mid + 1, last);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
