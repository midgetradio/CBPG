package ser322;

import java.util.Scanner;

public class ComicBookPG {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbclab";
        String driver = "com.mysql.cj.jdbc.Driver";
        String user = args[0];
        String pwd = args[1];

        // Create the scanner for input
        Scanner input = new Scanner(System.in);  

        // Create the data access layer
        CBPGDAL dal = new CBPGDAL(url, pwd, user, driver);

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
