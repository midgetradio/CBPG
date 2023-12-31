package ser322;

import java.text.DecimalFormat;

public class ComicIssue {
    // private member variables
    private int id;
    private String issueNumber;
    private String writerName;
    private String artistName;
    private String description;
    private String volumeTitle;
    private String publisherName;
    private int publicationYear;
    private float price;
    private boolean isValid = false;

    // getters
    public int getId() {
        return id;
    }
    public String getIssueNumber() {
        return issueNumber;
    }
    public String getWriterName() {
        return writerName;
    }
    public String getArtistName() {
        return artistName;
    }
    public String getDescription() {
        return description;
    }
    public String getVolumeTitle() {
        return volumeTitle;
    }
    public String getPublisherName() {
        return publisherName;
    }
    public int getPublicationYear() {
        return publicationYear;
    }
    public float getPrice() {
        return price;
    }
    public boolean isValid() {
        return isValid;
    }

    // setters
    public void setId(int i) {
        id = i;
    }
    public void setIssueNumber(String n) {
        issueNumber = n;
    }
    public void setWriterName(String w) {
        writerName = w;
    }
    public void setArtistName(String a) {
        artistName = a;
    }
    public void setDescription(String d) {
        description = d;
    }
    public void setVolumeTitle(String vt) {
        volumeTitle = vt;
    }
    public void setPublisherName(String pn) {
        publisherName = pn;
    }
    public void setPublicationYear(int py) {
        publicationYear = py;
    }
    public void setPrice(float p) {
        price = p;
    }
    public void setIsValid(boolean t) {
        isValid = t;
    }

    // override toString for pretty printing
    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        String returnString = "ID: " + id + "    ";
        returnString += "ISSUE NUMBER: " + issueNumber + "    \n";
        returnString += "TITLE: " + volumeTitle + "    \n";
        returnString += "WRITER: " + writerName + "    ";
        returnString += "ARTIST: " + artistName + "    \n";
        returnString += "PUBLISHER: " + publisherName + "    ";
        returnString += "YEAR: " + publicationYear + "    ";
        returnString += "PRICE: $" + formatter.format(price) + "\n";
        returnString += "DESC: " + description + "\n";

        return returnString;
    }
}
