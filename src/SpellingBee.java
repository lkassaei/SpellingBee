import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
// Spelling Bee by Lily Kassaei
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
        // Generate all substrings for original word
        ArrayList<String> substrings = new ArrayList<String>();
        // Note that first and last must start at zero to be able to capture all substrings
        words = generateHelper(substrings, 0, 0, letters);

        // Generate all permutations of the word
        ArrayList<String> permutations = new ArrayList<String>();
        generatePermutations(permutations, "", letters);
        // For every permutation, find all possible substrings in that permutation
        for (String w: permutations) {
            ArrayList<String> permutationSubstrings = new ArrayList<String>();
            permutationSubstrings = generateHelper(permutationSubstrings, 0, 0, w);
            // For every substring in every permutation, if words does not have it already, then add it
            for (String s: permutationSubstrings) {
                if (!words.contains(s)) {
                    words.add(s);
                }
            }
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

    public void generatePermutations(ArrayList<String> permutations, String current, String word) {
        // Base Case: If the word is empty, a permutation has been completed and we can stop recursion
        if (word.length() == 0) {
            // Add the permutation to the array
            permutations.add(current);
            return;
        }
        // Iterate through ever character in the word
        for (int i = 0; i < word.length(); i++) {
            // Remove the current character and pass the remaining string through the next recursive iteration
            String newWord = word.substring(0, i) + word.substring(i + 1);
            generatePermutations(permutations, current + word.charAt(i), newWord);
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Sort the words
        words = sortHelper(words, 0, words.size() -1);
    }

    public ArrayList<String> sortHelper(ArrayList<String> arr, int low, int high) {
        // Base Case: If we have split everything down to only one letter
        if (high == low) {
            // Take that letter and make a new arrayList of only that letter and return
            ArrayList<String> newArr = new ArrayList<String>();
            newArr.add(arr.get(low));
            return newArr;
        }
        // Find the middle of the array
        int med = (high + low)/2;
        // Split the list in two and sort those halves
        ArrayList<String> arr1 = sortHelper(arr, low, med);
        ArrayList<String> arr2 = sortHelper(arr, med+1, high);
        // Merge the halves together
        return merge(arr1, arr2);
    }

    private ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        // Create a new array for the merged list to go into
        ArrayList<String> merged = new ArrayList<String>();
        int left = 0, right =0;
        // While there is still things to look at
        while (left < arr1.size() && right < arr2.size()) {
            // If the first index in the left array is less than the first index in the right array
            if (arr1.get(left).compareTo(arr2.get(right)) < 0) {
                // Add the index in the left array to the new sorted array
                merged.add(arr1.get(left++));
            }
            else {
                // Add the index in the right array to the new sorted array
                merged.add(arr2.get(right++));
            }
        }
        // After exiting the first while loop, while whichever the bigger array is has more elements left, add those elements
        while (left < arr1.size()) {
            merged.add(arr1.get(left++));
        }
        while (right < arr2.size()) {
            merged.add(arr2.get(right++));
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
        // If the word is not found in the dictionary, remove it from the array
        for (int i = 0; i < words.size(); i++) {
            if (!checkWordsHelper(words.get(i), 0, DICTIONARY_SIZE -1)) {
                words.remove(i);
                // Make sure to reset i so we do not skip anything
                i--;
            }
        }
    }

    public boolean checkWordsHelper(String word, int first, int last) {
        // Find the middle word in the dictionary
        int mid = (first + last)/2;
        // Base Case: If the word is not found return false
        if (first > last) {
            return false;
        }
        // Base Case: If the middle index equals the word then return true
        if (DICTIONARY[mid].equals(word)) {
            return true;
        }
        // If the word is alphabetically before the word in the dictionary
        if (word.compareTo(DICTIONARY[mid]) < 0) {
            // Check the lower half of the dictionary
            return checkWordsHelper(word, first, mid -1);
        }
        // Else check the higher half of the dictionary
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
