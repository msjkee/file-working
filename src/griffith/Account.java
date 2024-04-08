package griffith;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Account {
    String username;
    String password;

    // Constructor to initialize username and password
    public Account(String account_username, String account_password) {
        username = account_username;
        password = account_password;
    }

    // Getter method to retrieve username
    public String getUsername() {
        return username;
    }

    // Getter method to retrieve password
    public String getPassword() {
        return password;
    }

    // Method for user registration (sign-up)
    public void signUp(String username_signup, String password_signup) {
        try {
            // Read from accounts.csv to check if the username is already taken
            BufferedReader reader = new BufferedReader(new FileReader("accounts.csv"));
            String line;
            boolean username_taken = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 1 && parts[0].equals(username_signup)) {
                    username_taken = true;
                    break;
                }
            }
            reader.close();

            // If username is already taken, inform the user
            if (username_taken) {
                System.out.println("Username is already taken! Try another one!");
            }
            else {
                // If username is available, write the new user data to accounts.csv
                FileWriter writer = new FileWriter("accounts.csv", true);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YY");
                String last_login = formatter.format(date);
                writer.write(username_signup + ", " + password_signup + ", " + last_login + "\n");
                writer.close();
                System.out.println("Perfect! User is now registered!");
            }
        }
        catch (IOException e) {
            System.err.println("Error! " + e.getMessage());
        }
    }

    // Method for user login
    public void login(String username_login, String password_login) {
        ArrayList<String> new_data = new ArrayList<>();
        try {
            // Read from accounts.csv to validate the username and password
            BufferedReader reader = new BufferedReader(new FileReader("accounts.csv"));
            String line;
            boolean username_exists = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 2 && parts[0].equals(username_login)) {
                    username_exists = true;
                    if (parts[1].equals(password_login)) {
                        // Successful login: update last login date and calculate days since last login
                        int last_login_before = Integer.parseInt(parts[2].substring(0, parts[2].indexOf("-")));
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YY");
                        String last_login = formatter.format(date);
                        int last_login_after = Integer.parseInt(last_login.substring(0, last_login.indexOf("-")));
                        parts[2] = last_login;
                        String updatedLine = String.join(", ", parts);
                        new_data.add(updatedLine);
                        int last_login_difference = Math.abs(last_login_before - last_login_after);
                        System.out.println("Welcome, " + username_login + "! " + last_login_difference + " days passed since your last login!");
                    }
                    else {
                        // Incorrect password: inform the user and retain existing data
                        System.out.println("Password is incorrect! Try again.");
                        new_data.add(line); // Retain the original data line
                    }
                }
                else {
                    new_data.add(line); // Retain the original data line
                }
            }
            reader.close();

            // Write updated data (if any) back to accounts.csv
            BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.csv"));
            for (String updatedLine : new_data) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

            // If username does not exist in the file, inform the user
            if (!username_exists) {
                System.out.println("Username doesn't exist! You need to sign up first!");
            }
        }
        catch (IOException e) {
            System.err.println("Error! " + e.getMessage());
        }
    }
}