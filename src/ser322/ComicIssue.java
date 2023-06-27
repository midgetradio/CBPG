package ser322;

public class ComicIssue {
    private String issueNumber;
    private String writerName;
    private String artistName;
    private String description;
    private String volumeTitle;
    private String publisherName;
    private int publicationYear;
    private float price;

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

    @Override
    public String toString() {
        String returnString = issueNumber + " \n";
        returnString += volumeTitle + " \n";
        returnString += description + " \n";
        returnString += writerName + " \n";
        returnString += artistName + " \n";
        returnString += publisherName + " \n";
        returnString += publicationYear + " \n";
        returnString += price + " \n";

        return returnString;
    }

    
}
