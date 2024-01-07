

import java.awt.desktop.OpenURIEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class GuessTheNumberServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSock = null;
        try {
            serverSock = new ServerSocket(50000);
        } catch (IOException ie) {
            System.out.println("Can't listen on 50000");
            System.exit(1);
        }
        Socket link = null;
        System.out.println("Listening for connection ...");
        try {
            link = serverSock.accept();
        } catch (IOException ie) {
            System.out.println("Accept failed");
            System.exit(1);
        }
        System.out.println("Connection successful");
        System.out.println("Listening for input ...");
        PrintWriter output = new PrintWriter(link.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
        String inputLine;
        int numberToGuess = new Random().nextInt(101); // Random number between 0 and 100

        while ((inputLine = input.readLine()) != null) {
            if (inputLine.equalsIgnoreCase("Play?")) {
                System.out.println("Message from client: " + inputLine );
                output.println("I hava a number between 0 and 100. Can you guess it?");
            } else if (inputLine.equalsIgnoreCase("Bye")) {
                output.println("Bye");
                break;
            }else {
                // Comparing client input with the random number
                try {
                    int guess = Integer.parseInt(inputLine);
                    if (guess < numberToGuess) { // if the client number is smaller it will redirect the client to enter a higher number
                        System.out.println("Message from client: " + inputLine );
                        output.println("Guess Higher");

                    } else if (guess > numberToGuess) { // if the client number is greater than random number it will redirect client to enter a lower number
                        System.out.println("Message from client: " + inputLine );
                        output.println("Guess Lower");
                    } else {//if user get the right number
                        System.out.println("Message from client: " + inputLine );
                        output.println("You got it!");
                        break;
                    }
                } catch (NumberFormatException e) {// this is a warning for the client if client entered letters.
                    output.println("Invalid input. Please enter a number.");
                }
            }
        }
        output.close();
        input.close();
        link.close();
        serverSock.close();
    }
}

