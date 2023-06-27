package ser322;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
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
        System.out.println("6) Update a specific issue.");
        System.out.println("7) Remove a specific issue.");
        System.out.println("8) Quit.");
    }

    public void displayEndProgramMessage() {
        System.out.println("Thank you for using the Comic Book Price Guide.");
    }

    public int getMainMenuSelection() {
        int selection = 0;
        while (selection < 1 || selection > 8) {
            System.out.println("Please enter a number between 1 and 8.");
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
            System.out.println("Please enter the search term, or enter 'cancel' to return to the main menu.");
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

    public boolean handleMainMenuSelection(int selection) {
        
        if(selection == 1) {
            List<ComicIssue> comics = dal.getTopTenComics();
            printResults(comics);
        }
        else if(selection == 2) {
            String writer = getStringInput();
            if(writer == "cancel") {
                return true;
            }
            List<ComicIssue> comics = dal.getComicsByWriter(writer);
            printResults(comics);
        }
        else if(selection == 3) {
            String artist = getStringInput();
            if(artist == "cancel") {
                return true;
            }
            List<ComicIssue> comics = dal.getComicsByArtist(artist);
            printResults(comics);
        }
        else if(selection == 4) {
            String title = getStringInput();
            if(title == "cancel") {
                return true;
            }
            List<ComicIssue> comics = dal.getComicsByTitle(title);
            printResults(comics);
        }
        else if(selection == 5) {
            ComicIssue issue = handleInsert();
            dal.insertIssue(issue);
        }
        else if(selection == 6) {
            handleUpdate();
        }
        else if(selection == 8) {
            return false;
        }

        return true;
    }

    private void handleUpdate() {
        // get the issue to update based on issue id
        int id = 0;
        while(id == 0) {
            System.out.println("Enter the id number of the issue you want to update.");
            String idString = input.nextLine();
            try {
                id = Integer.parseInt(idString.trim());
            }
            catch (Exception e) {
                System.out.println("Enter a number or type 'cancel' to return to main menu.");
                id = 0;
            }
        }

        ComicIssue issueToUpdate = dal.getIssueById(id);

        // update issue number
        String msgToUser = "Update Issue Number, or press enter to keep current value (" + issueToUpdate.getIssueNumber() + "): ";
        String issueNumber = getIssueNumberInput(msgToUser, issueToUpdate.getIssueNumber());

        // update price
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        msgToUser = "Update price, or press enter to keep current value (" + formatter.format(issueToUpdate.getPrice()) + "): ";
        float price = getPriceInput(msgToUser, issueToUpdate.getPrice());


        List<ComicIssue> ci = new ArrayList<ComicIssue>();
        ci.add(issueToUpdate);
        printResults(ci);

    }

    private String getIssueNumberInput(String msgToUser, String currentIssueNumber) {
        String issueNumberInput = "";
        while (issueNumberInput.length() < 1 || issueNumberInput.length() > 25) {
            System.out.print(msgToUser);
            issueNumberInput = input.nextLine();

            if(!currentIssueNumber.isEmpty() && issueNumberInput.isEmpty()) {
                return currentIssueNumber;
            }
        }
        return issueNumberInput;
    }

    private float getPriceInput(String msgToUser, float price) {
        float priceInput = 0;
        String priceString = "";

        while (priceInput == 0) {
            System.out.print(msgToUser);
            priceString = input.nextLine();
            if (price > 0 && priceString.isEmpty()) {
                return price;
            } else {
                try {
                    priceInput = Float.parseFloat(priceString);
                } catch (Exception e) {
                    System.out.println("Please input a decimal value (no $)");
                }
            }
        }

        return priceInput;
    }

    private ComicIssue handleInsert() {
        ComicIssue issue = new ComicIssue();
        
        // get issue number
        String msgToUser = "Enter issue number: ";
        String inpuString = getIssueNumberInput(msgToUser, "");
        issue.setIssueNumber(inpuString);

        // get price
        msgToUser = "Enter a price: ";
        float price = getPriceInput(msgToUser, 0);
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

        return issue;
    }

    private void printResults(List<ComicIssue> comics) {
        System.out.println("*** RESULTS ***");
        for(var c : comics) {
            System.out.println(c);
        }
        System.out.println("Press enter to continue.");
        input.nextLine();
    }
    
}
