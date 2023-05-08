package edu.guilford;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;

public class Data 
{
    public static void main( String[] args ) {

        Scanner scan = new Scanner(System.in);
        Scanner scanFile = null;
        Path dataLocation = null; 
        String [][] values = null; 
        String fileName =  null; 

    // Ask the user for the name of the file they would like to read
        System.out.println("Please enter the name of the file you would like to read: ");
        fileName = scan.nextLine();

    // get the location of this file 
        try {
            dataLocation = Paths.get(Data.class.getResource("/" + fileName).toURI());
            FileReader dataFile = new FileReader(dataLocation.toString());
            BufferedReader dataBuffer = new BufferedReader(dataFile);
            scanFile = new Scanner(dataBuffer);
            values = readData(scanFile);
        } catch (URISyntaxException | FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }

    // Write the data to file
        try { 
            writeData(values, "output.txt");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.exit(1); 
        }
    }

    public static String [][] readData(Scanner scan) {

        Stack<String> stack = new Stack<String>();
        TreeSet<String> organizedData = new TreeSet<String>();

    // Remove all special characters and numbers from the file
        while (scan.hasNext()) {
            String word = scan.next();
            word = word.toLowerCase();
            word = word.replaceAll("[0-9]", "");
            word = word.replaceAll("[^a-zA-Z0-9]", "");
            stack.push(word);
        }

    // Add the elements of the file to the stack
        while (scan.hasNext()) {
            stack.push(scan.next());
        }


    // Use the Occurance class to store the number of occurance of each string
        while (!stack.isEmpty()) {
            String word = stack.pop();
            int count = 1;
            for (int i = 1; i < stack.size(); i++) {
                if (word.equals(stack.get(i))) {
                    count++;
                }
            }
            Occurances occurance = new Occurances(word, count);
            organizedData.add(occurance.getWord() + " " + occurance.getCount());
            while (stack.contains(word)) {
                stack.remove(word);
            }
        }

    // Add the elements of the file to the TreeSet
        while (!stack.isEmpty()) {
            organizedData.add(stack.pop());
        }
        
    // Print the elements of the TreeSet
        System.out.println("Here are the elements of the TreeSet: ");
        for (String s : organizedData) {
            System.out.println(s);
        }

    // Ask the user for a word to search for
        System.out.println("Please enter a word to search: ");
        Scanner scan2 = new Scanner(System.in);
        String searchWord = scan2.nextLine();
    // Find the searchWord in the occurance class and print the number of the occurance
        for (String s : organizedData) {
            if (s.contains(searchWord)) {
    // Print the number of occurances of the searchWord
                System.out.println("The word " + searchWord + " appears " + s.substring(s.indexOf(" ") + 1) + " times.");
            }
        }

    // Try to read the data from the file and catch any exceptions
        try{
            while (scan.hasNext()) {
                organizedData.add(scan.next());
            }
        } catch (InputMismatchException ex) {
            System.out.println("Invalid input");
        } catch (NoSuchElementException ex) {
            System.out.println("Ran out of data");
        }  
        return organizedData.toArray(new String[organizedData.size()][]);
    }    

    // Now, write the strings to the file
    public static void writeData(String [][] values, String fileName) throws URISyntaxException, IOException {
        Path locationPath = Paths.get(Data.class.getResource("/edu/guilford/").toURI());
        FileWriter fileLocation = new FileWriter(locationPath.toString() + "/" + fileName);
        BufferedWriter bufferWrite = new BufferedWriter(fileLocation);

    // Write the data to the file
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                bufferWrite.write(values[i][j] + " ");
            }
            bufferWrite.newLine();
        }
        bufferWrite.close();
    }

    public static class ScannerException extends Exception {
        public ScannerException(String message) {
            super(message);
        }
    }

    // Create a class to store the number of occurances of each string
    public static class Occurances implements Comparable<Occurances> {
        private String word;
        private int count;

        public Occurances(String word, int count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return this.word;
        }

        public String setWord() {
            return this.word;
        }

        public int getCount() {
            return this.count;
        }

        public int setCount() {
            return this.count;
        }

        public int compareTo(Occurances occurance) {
            if (this.count > occurance.getCount()) {
                return 1;
            } else if (this.count < occurance.getCount()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}

