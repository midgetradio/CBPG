package ser322;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CBPGUI {
    private CBPGDAL dal = null;
    private Scanner input = null;

    public CBPGUI(CBPGDAL d, Scanner s) {
        dal = d;
        input = s;
    }

    // This is the main menu for the user
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

    // This message displays when the user terminates the program
    public void displayEndProgramMessage() {
        System.out.println("Thank you for using the Comic Book Price Guide.");
    }

    // Get user input at the main menu selection screen
    public int getMainMenuSelection() {
        int selection = 0;
        while (selection < 1 || selection > 8) {
            System.out.print("Please enter a number between 1 and 8: ");
            try {
                selection = input.nextInt();
            } catch (InputMismatchException exception) {
                System.out.print("Input must be a number: ");
                input.nextLine();
                selection = 0;
            }
        }
        input.nextLine();
        return selection;
    }

    // Getting general string input from the user, used for entering search terms
    private String getStringInput() {
        String selection = "";
        boolean success = false;

        while (!success) {
            System.out.print("Please enter the search term, or enter 'cancel' to return to the main menu: ");
            try {
                selection = input.nextLine();
            } catch (InputMismatchException exception) {
                System.out.print("Input must be a string: ");
                input.nextLine();
                selection = "";
            }
            success = true;
        }
        return selection;
    }

    // This method executes the appropriate code depending on what the user selected from the main menu
    public boolean handleMainMenuSelection(int selection) {
        if(selection == 1) {
            List<ComicIssue> comics = dal.getTopTenComics();
            printResults(comics);
        }
        else if(selection == 2) {
            String writer = getStringInput();
            if(writer.equals("cancel")) {
                return true;
            }
            List<ComicIssue> comics = dal.getComicsByWriter(writer);
            printResults(comics);
        }
        else if(selection == 3) {
            String artist = getStringInput();
            if(artist.equals("cancel")) {
                return true;
            }
            List<ComicIssue> comics = dal.getComicsByArtist(artist);
            printResults(comics);
        }
        else if(selection == 4) {
            String title = getStringInput();
            if(title.equals("cancel")) {
                return true;
            }
            List<ComicIssue> comics = dal.getComicsByTitle(title);
            printResults(comics);
        }
        else if(selection == 5) {
            ComicIssue issue = handleInsert();
            if(issue.isValid()) {
                String msg = dal.insertIssue(issue);
                System.out.println(msg);
            }
        }
        else if(selection == 6) {
            ComicIssue issue = handleUpdate();
            if(issue.isValid()) {
                String msg = dal.updateIssue(issue);
                System.out.println(msg);
            }
        }
        else if(selection == 7) {
            int id = handleDelete();
            dal.deleteIssue(id);
        }
        else if(selection == 8) {
            return false;
        }

        return true;
    }

    // Get the id of the issue the user wants to delete, and execute the delete method in the DAL
    private int handleDelete() {
        int id = 0;
        while(id == 0) {
            System.out.print("Enter the id number of the issue you want to delete: ");
            String idString = input.nextLine();
            try {
                id = Integer.parseInt(idString.trim());
            }
            catch (Exception e) {
                System.out.print("Enter a number or type 'cancel' to return to main menu: ");
                id = 0;
            }
        }

        return id;
    }

    // Get the issue the user wants to update, and execute the update method in the DAL
    private ComicIssue handleUpdate() {
        // get the issue to update based on issue id
        int id = 0;
        while(id == 0) {
            System.out.print("Enter the id number of the issue you want to update (type 'cancel' to return to the main menu): ");
            String idString = input.nextLine();

            if(idString.trim().equals("cancel")) {
                ComicIssue issue = new ComicIssue();
                issue.setIsValid(false);
                return issue;
            }

            try {
                id = Integer.parseInt(idString.trim());
            }
            catch (Exception e) {
                System.out.print("Enter a number or type 'cancel' to return to main menu:");
                id = 0;
            }
        }

        ComicIssue issueToUpdate = dal.getIssueById(id);

        // update issue number
        String msgToUser = "Update Issue Number, or press enter to keep current value (" + issueToUpdate.getIssueNumber() + "): ";
        issueToUpdate.setIssueNumber(getIssueNumberInput(msgToUser, issueToUpdate.getIssueNumber()));

        // update price
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        msgToUser = "Update price, or press enter to keep current value (" + formatter.format(issueToUpdate.getPrice()) + "): ";
        issueToUpdate.setPrice(getPriceInput(msgToUser, issueToUpdate.getPrice()));

        // update description
        int index = 20;
        if(issueToUpdate.getDescription().length() < index) {
            index = issueToUpdate.getDescription().length();
        }
        msgToUser = "Update description, or press enter to keep current value (" + issueToUpdate.getDescription().substring(0, index) + "... ): ";
        issueToUpdate.setDescription(getDescriptionInput(msgToUser, issueToUpdate.getDescription()));

        // update artist
        msgToUser = "Update artist, or press enter to keep current value (" + issueToUpdate.getArtistName() + "): ";
        issueToUpdate.setArtistName(getArtistInput(msgToUser, issueToUpdate.getArtistName()));

        // update artist
        msgToUser = "Update writer, or press enter to keep current value (" + issueToUpdate.getWriterName() + "): ";
        issueToUpdate.setWriterName(getWriterInput(msgToUser, issueToUpdate.getWriterName()));

        // update publisher
        msgToUser = "Update publisher, or press enter to keep current value (" + issueToUpdate.getPublisherName() + "): ";
        issueToUpdate.setPublisherName(getPublisherInput(msgToUser, issueToUpdate.getPublisherName()));

        // update volume title
        msgToUser = "Update volume title, or press enter to keep current value (" + issueToUpdate.getVolumeTitle() + "): ";
        issueToUpdate.setVolumeTitle(getVolumeTitleInput(msgToUser, issueToUpdate.getVolumeTitle()));

        // update publication year
        msgToUser = "Update publication year, or press enter to keep current value (" + issueToUpdate.getPublicationYear() + "): ";
        issueToUpdate.setPublicationYear(getPublicationYearInput(msgToUser, issueToUpdate.getPublicationYear()));

        issueToUpdate.setIsValid(true);

        return issueToUpdate;
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

    private String getDescriptionInput(String msgToUser, String currentDescription) {
        String descriptionInput = "";
        while (descriptionInput.length() < 1 || descriptionInput.length() > 1000) {
            System.out.print(msgToUser);
            descriptionInput = input.nextLine();

            if(!currentDescription.isEmpty() && descriptionInput.isEmpty()) {
                return currentDescription;
            }
        }
        return descriptionInput;
    }

    private String getArtistInput(String msgToUser, String currentArtist) {
        String artistInput = "";
        while (artistInput.length() < 1 || artistInput.length() > 45) {
            System.out.print(msgToUser);
            artistInput = input.nextLine();

            if(!currentArtist.isEmpty() && artistInput.isEmpty()) {
                return currentArtist;
            }
        }
        return artistInput;
    }

    private String getWriterInput(String msgToUser, String currentWriter) {
        String writerInput = "";
        while (writerInput.length() < 1 || writerInput.length() > 45) {
            System.out.print(msgToUser);
            writerInput = input.nextLine();

            if(!currentWriter.isEmpty() && writerInput.isEmpty()) {
                return currentWriter;
            }
        }
        return writerInput;
    }

    private String getPublisherInput(String msgToUser, String currentPublisher) {
        String publisherInput = "";
        while (publisherInput.length() < 1 || publisherInput.length() > 45) {
            System.out.print(msgToUser);
            publisherInput = input.nextLine();

            if(!currentPublisher.isEmpty() && publisherInput.isEmpty()) {
                return currentPublisher;
            }
        }
        return publisherInput;
    }

    private String getVolumeTitleInput(String msgToUser, String currentVolumeTitle) {
        String volumeTitleInput = "";
        while (volumeTitleInput.length() < 1 || volumeTitleInput.length() > 45) {
            System.out.print(msgToUser);
            volumeTitleInput = input.nextLine();

            if(!currentVolumeTitle.isEmpty() && volumeTitleInput.isEmpty()) {
                return currentVolumeTitle;
            }
        }
        return volumeTitleInput;
    }

    private int getPublicationYearInput(String msgToUser, int year) {
        int yearInput = 0;
        String yearString = "";

        while (yearInput == 0) {
            System.out.print(msgToUser);
            yearString = input.nextLine();
            if (year > 0 && yearString.isEmpty()) {
                return year;
            } else {
                try {
                    yearInput = Integer.parseInt(yearString);
                    if(yearInput < 1800 || yearInput > 2023) {
                        System.out.println("Year must be between 1800 and no later than 2023");
                        yearInput = 0;
                    }
                } catch (Exception e) {
                    System.out.println("Year must be between 1800 and no later than 2023");
                }
            }
        }

        return yearInput;
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
        msgToUser = "Enter story description: ";
        inpuString = getDescriptionInput(msgToUser, "");
        issue.setDescription(inpuString);

        // get artist
        msgToUser = "Enter artist name: ";
        inpuString = getArtistInput(msgToUser, "");
        issue.setArtistName(inpuString);

        // get writer
        msgToUser = "Enter writer name: ";
        inpuString = getWriterInput(msgToUser, "");
        issue.setWriterName(inpuString);

        // get publisher
        msgToUser = "Enter publisher name: ";
        inpuString = getPublisherInput(msgToUser, "");
        issue.setPublisherName(inpuString);

        // get volume title
        msgToUser = "Enter volume title: ";
        inpuString = getVolumeTitleInput(msgToUser, "");
        issue.setVolumeTitle(inpuString);

        // get publication year
        msgToUser = "Enter publication year: ";
        int publicationYear = getPublicationYearInput(msgToUser, 0);
        issue.setPublicationYear(publicationYear);

        issue.setIsValid(true);
        
        // System.out.println(issue);

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
