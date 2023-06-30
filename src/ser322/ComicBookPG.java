package ser322;

import java.util.Scanner;

public class ComicBookPG {
    public static void main(String[] args) {
        // handle the cli arguments
        CPGCLI cpgcli = new CPGCLI(args);
        if(!cpgcli.IsValid()) {
            System.out.println(cpgcli.getBadArgument() + " is not a valid argument.");
            System.out.println(cpgcli.getValidArgumentsMsg());
            return;
        }

        // Create the scanner for input
        Scanner input = new Scanner(System.in);  

        // Create the data access layer
        CBPGDAL dal = new CBPGDAL(cpgcli.getUrl(), cpgcli.getPwd(), cpgcli.getUser(), cpgcli.getDriver());

        // Create the ui
        CBPGUI ui = new CBPGUI(dal, input);

        // loop through the main menu until the user quits
        boolean remainInMenu = true;
        while(remainInMenu) {
            ui.displayMainMenu();
            int selection = ui.getMainMenuSelection();
            remainInMenu = ui.handleMainMenuSelection(selection);
        }

        // end program and display the end program message
        ui.displayEndProgramMessage();
    }
}
