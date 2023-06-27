package ser322;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CBPGUI {
    CBPGDAL dal = null;
    Scanner input = null;

    public CBPGUI(CBPGDAL d, Scanner s) {
        dal = d;
        input = s;
    }

    public void displayMainMenu() {
        System.out.println("Welcome to Comic Book Price Guide. Please choose an option below.");
        System.out.println("1) View Top 10 Most Expensive Books.");
        System.out.println("2) Search for comics by a particular writer.");
        System.out.println("3) Search for comics by a particular artist.");
        System.out.println("4) Search for comics by title.");
        System.out.println("5) Insert a new comic book issue.");
        System.out.println("6) Remove a specific issue.");
    }

    public int getMainMenuSelection() {
        int selection = 0;
        while (selection < 1 || selection > 6) {
            System.out.println("Please enter a number between 1 and 6.");
            try {
                selection = input.nextInt();
            } catch (InputMismatchException exception) {
                System.out.println("Input must be a number.");
                input.nextLine();
                selection = 0;
            }
        }
        input.nextLine();
        return selection;
    }

    private String getStringInput() {
        String selection = "";
        boolean success = false;

        while (!success) {
            System.out.println("Please enter the search term.");
            try {
                selection = input.nextLine();
            } catch (InputMismatchException exception) {
                System.out.println("Input must be a string.");
                input.nextLine();
                selection = "";
            }
            success = true;
        }
        return selection;
    }

    public void handleMainMenuSelection(int selection) {
        if(selection == 1) {
            dal.getTopTenComics();
        }
        else if(selection == 2) {
            String writer = getStringInput();
            dal.getComicsByWriter(writer);
        }
        else if(selection == 3) {
            String artist = getStringInput();
            dal.getComicsByArtist(artist);
        }
        else if(selection == 4) {
            String title = getStringInput();
            dal.getComicsByTitle(title);
        }
        else if(selection == 5) {
            handleInsert();
        }
    }

    private void handleInsert() {
        ComicIssue issue = new ComicIssue();
        
        // get issue number
        String inpuString = "";
        while(inpuString.length() < 1 || inpuString.length() > 25) {
            System.out.print("Enter issue number: ");
            inpuString = input.nextLine();
        }
        issue.setIssueNumber(inpuString);

        // get price
        float price = 0;
        boolean success = false;
        while(!success) {
            try {
                System.out.print("Enter price: ");
                price = input.nextFloat();
            } catch(InputMismatchException e) {
                System.out.println("Please enter a decimal.");
            }
            if(price != 0) {
                success = true;
            }
            input.nextLine();
        }
        issue.setPrice(price);

        // get story description
        inpuString = "";
        while(inpuString.length() > 1000 || inpuString.length() == 0) {
            System.out.print("Enter story description: ");
            inpuString = input.nextLine();
        }
        issue.setDescription(inpuString);

        // get artist
        inpuString = "";
        while(inpuString.length() > 45 || inpuString.length() == 0) {
            System.out.print("Enter artist name: ");
            inpuString = input.nextLine();
        }
        issue.setArtistName(inpuString);

        // get writer
        inpuString = "";
        while(inpuString.length() > 45 || inpuString.length() == 0) {
            System.out.print("Enter writer name: ");
            inpuString = input.nextLine();
        }
        issue.setWriterName(inpuString);

        // get publisher
        inpuString = "";
        while(inpuString.length() > 45 || inpuString.length() == 0) {
            System.out.print("Enter publisher name: ");
            inpuString = input.nextLine();
        }
        issue.setPublisherName(inpuString);

        // get volume title
        inpuString = "";
        while(inpuString.length() > 45 || inpuString.length() == 0) {
            System.out.print("Enter volume title: ");
            inpuString = input.nextLine();
        }
        issue.setVolumeTitle(inpuString);

        // get publication year
        int publicationYear = 0;
        while(publicationYear < 1800 || publicationYear > 2023) {
            try {
                System.out.print("Enter publication year: ");
                publicationYear = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Enter the year as digits.");
                publicationYear = 0;
            }
        }
        issue.setPublicationYear(publicationYear);
        
        System.out.println(issue);
    }
    
}
