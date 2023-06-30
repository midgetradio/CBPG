package ser322;

public class CPGCLI {
    private String url = "jdbc:mysql://localhost:3306/comicbooks";
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String user = "";
    private String pwd = "";
    private String badArgument = "";
    private String validArgumentsMessage = "Valid Arguments: \n -u <username> \n -p <pwd> \n -d <driver> \n -a <url>";
    private boolean valid = true;
    private String[] args = null;

    public CPGCLI(String[] args) {
        this.args = args;
        handleArgs();
    }

    public String getUrl() {
        return url;
    }
    public String getDriver() {
        return driver;
    }
    public String getUser() {
        return user;
    }
    public String getPwd() {
        return pwd;
    }
    public String getBadArgument() {
        return badArgument;
    }
    public boolean IsValid() {
        return valid;
    }
    public String getValidArgumentsMsg() {
        return validArgumentsMessage;
    }

    public boolean handleArgs() {
        for(int i = 0; i < args.length; i+=2) {
            if(args[i].equals("-u")) {
                user = args[i+1];
            }
            else if(args[i].equals("-p")) {
                pwd = args[i+1];
            }
            else if(args[i].equals("-d")) {
                driver = args[i+1];
            }
            else if(args[i].equals("-a")) {
                url = args[i+1];
            }
            else {
                badArgument = args[i];
                valid = false;
            }
        }

        return true;
    }

}
