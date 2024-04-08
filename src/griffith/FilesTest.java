package griffith;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.URL;
import java.io.IOException;

public class FilesTest {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Task 1: Sorting data from a file and saving the result to a new file
        String inputFile = "myFile.txt";
        String outputFile = "myFile_sorted.txt";

        sortAndSave(inputFile, outputFile);

        // Task 2: Downloading a file from a given URL and saving it to a specified location with a specified filename
        System.out.println("Input the URL of the file you want to download: ");
        String url = input.nextLine();
        System.out.println("Input the filename you want to save it as (including extension): ");
        String fileName = input.nextLine();
        System.out.println("Input the location where you want to save the file: ");
        String location = input.nextLine();

        boolean downloadSuccess = false;
        while (!downloadSuccess) {
            try {
                download(url, fileName, location);
                downloadSuccess = true;
                System.out.println("File successfully saved in the directory: " + location);
            }
            catch (MalformedURLException e) {
                System.err.println("Error! Invalid URL! Please input a valid URL: ");
                url = input.nextLine();
            }
            catch (IOException e) {
                System.err.println("Error! Failed to download or save the file: " + e.getMessage());
                location = input.nextLine();
            }
        }

        // Task 3: Main menu for user registration and login
        System.out.println("\n" + "Welcome! What would you like to do today? ");
        displayMenu();

        while (true) {
            String command = input.nextLine();

            String username;
            String password;

            switch (command.toLowerCase()) {
                case "sign up":
                    System.out.println("Input the username and password you would like to register: ");
                    Account newUser = new Account(username = input.nextLine(), password = input.nextLine());
                    newUser.signUp(username, password);
                    return;

                case "login":
                    System.out.println("Input the username and password to log in: ");
                    Account existingUser = new Account(username = input.nextLine(), password = input.nextLine());
                    existingUser.login(username, password);
                    return;
            }
        }
    }

    // Methods for Task 1: Sorting data from a file and saving the result to a new file

    // Read data from a file into an ArrayList
    public static ArrayList<String> sortFile(String filename) {
        ArrayList<String> data = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
            reader.close();
        }
        catch (IOException e) {
            System.err.println("Error! " + e.getMessage());
        }
        return data;
    }

    // Sort data in an ArrayList using bubble sort
    public static void bubbleSort(ArrayList<String> values) {
        for (int i = 0; i < values.size() - 1; i++) {
            for (int j = 0; j < values.size() - i - 1; j++) {
                if (values.get(j).compareTo(values.get(j + 1)) > 0) {
                    String tmp = values.get(j);
                    values.set(j, values.get(j + 1));
                    values.set(j + 1, tmp);
                }
            }
        }
    }

    // Write sorted data from an ArrayList back to a file
    public static void fileWrite(String filename, ArrayList<String> data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException e) {
            System.err.println("Error! " + e.getMessage());
        }
    }

    // Perform sorting and save the result to a new file
    public static void sortAndSave(String inputFile, String outputFile) {
        ArrayList<String> data = sortFile(inputFile);
        bubbleSort(data);
        fileWrite(outputFile, data);
        System.out.println("File sorted and saved as: " + outputFile);
    }

    // Methods for Task 2: Downloading a file from a URL and saving it to a specified location

    // Download a file from a URL and save it to a specified location with the given filename
    public static void download(String url, String fileName, String location) throws IOException {
        URL fileURL = new URL(url);
        Path outputPath = Path.of(location, fileName);
        try (InputStream inputStream = fileURL.openStream()) {
            Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // Method for Task 3: Displaying the main menu for user registration and login

    // Display the main menu options
    public static void displayMenu() {
        System.out.println("\n" + "1. SIGN UP");
        System.out.println("2. LOGIN" + "\n");
    }
}