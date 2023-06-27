package ser322;

import java.util.Scanner;

public class ComicBookPG {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbclab";
        String driver = "com.mysql.cj.jdbc.Driver";
        String user = "root";
        String pwd = "";

        Scanner input = new Scanner(System.in);  
        CBPGDAL dal = new CBPGDAL(url, pwd, user, driver);
        CBPGUI ui = new CBPGUI(dal, input);

        ui.displayMainMenu();
        int selection = ui.getMainMenuSelection();
        ui.handleMainMenuSelection(selection);


    }
    
}
