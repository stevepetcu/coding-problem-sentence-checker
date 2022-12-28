package com.stefanpetcu.sentencechecker;

import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your sentence: ");
        String userInput = scanner.nextLine();
        scanner.close();

        if (!userInput.matches("^[A-Z] |^[A-Z][a-z]+.+")) {
            System.err.println("Invalid sentence. Hint: watch your capitalisation.");
            return;
        }

        if (!userInput.matches("^(?:(?:[a-zA-Z]+[,;:.?!]?) ?)+$")) {
            System.err.println("Invalid sentence. Hint: see the README file for allowed characters and watch your spaces.");
            return;
        }

        List<String> words = new java.util.ArrayList<>(List.of(userInput.split(" ")));
        String lastWord = words.get(words.size() - 1);
        words.remove(lastWord);

        String badSpaces = words
                .stream()
                .filter(word -> word.matches(".+[.?!]+"))
                .findFirst()
                .orElse(null);

        if (badSpaces != null) {
            System.err.println("Invalid sentence. Hint: watch your punctuation, one sentence only.");
            return;
        }

        if (!lastWord.matches("\\w+[.?!]")) {
            System.err.println("Invalid sentence. Hint: should end properly.");
            return;
        }

        System.out.println(userInput);
    }
}
