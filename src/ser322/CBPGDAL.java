package ser322;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CBPGDAL {
    String user;
    String pwd;
    String url;
    String driver;
    Statement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    public CBPGDAL() {
        super();
    }

    public CBPGDAL(String url, String pwd, String user, String driver) {
        this.user = user;
        this.pwd = pwd;
        this.url = url;
        this.driver = driver;

        createConnection();
    }

    private void createConnection() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pwd);
            conn.setAutoCommit(false);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<ComicIssue> getTopTenComics() {
        List<ComicIssue> comics = new ArrayList<ComicIssue>();
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT cb.id, cb.issue_number, v.title, cb.description, w.name as writer, a.name as artist, p.name as publisher, v.year, cb.price " +
                                    "FROM comicbooks.comic_books cb " +
                                    "JOIN comicbooks.artists a ON a.id = cb.artist_id " +
                                    "JOIN comicbooks.writers w ON w.id = cb.writer_id " +
                                    "JOIN comicbooks.publishers p ON p.id = cb.publisher_id " +
                                    "JOIN comicbooks.volumes v ON v.id = cb.volume_id " +
                                    "ORDER BY cb.price DESC " +
                                    "LIMIT 10;");

            comics = convertResultSetToComicIssues(rs);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return comics;
    }

    public List<ComicIssue> getComicsByWriter(String writerName) {
        List<ComicIssue> comics = new ArrayList<ComicIssue>();
        writerName = "%" + writerName + "%";
        String sql = "SELECT cb.id, cb.issue_number, v.title, cb.description, w.name as writer, a.name as artist, p.name as publisher, v.year, cb.price " +
                                    "FROM comicbooks.comic_books cb " +
                                    "JOIN comicbooks.artists a ON a.id = cb.artist_id " +
                                    "JOIN comicbooks.writers w ON w.id = cb.writer_id " +
                                    "JOIN comicbooks.publishers p ON p.id = cb.publisher_id " +
                                    "JOIN comicbooks.volumes v ON v.id = cb.volume_id " +
                                    "WHERE w.Name LIKE ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, writerName);

            rs = pstmt.executeQuery();

            comics = convertResultSetToComicIssues(rs);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return comics;
    }

    public List<ComicIssue> getComicsByArtist(String artistName) {
        List<ComicIssue> comics = new ArrayList<ComicIssue>();
        artistName = "%" + artistName + "%";
        String sql = "SELECT cb.id, cb.issue_number, v.title, cb.description, w.name as writer, a.name as artist, p.name as publisher, v.year, cb.price " +
                                    "FROM comicbooks.comic_books cb " +
                                    "JOIN comicbooks.artists a ON a.id = cb.artist_id " +
                                    "JOIN comicbooks.writers w ON w.id = cb.writer_id " +
                                    "JOIN comicbooks.publishers p ON p.id = cb.publisher_id " +
                                    "JOIN comicbooks.volumes v ON v.id = cb.volume_id " +
                                    "WHERE a.Name LIKE ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, artistName);

            rs = pstmt.executeQuery();

            comics = convertResultSetToComicIssues(rs);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return comics;
    }

    public List<ComicIssue> getComicsByTitle(String title) {
        List<ComicIssue> comics = new ArrayList<ComicIssue>();
        title = "%" + title + "%";
        String sql = "SELECT cb.id cb.issue_number, v.title, cb.description, w.name as writer, a.name as artist, p.name as publisher, v.year, cb.price " +
                                    "FROM comicbooks.comic_books cb " +
                                    "JOIN comicbooks.artists a ON a.id = cb.artist_id " +
                                    "JOIN comicbooks.writers w ON w.id = cb.writer_id " +
                                    "JOIN comicbooks.publishers p ON p.id = cb.publisher_id " +
                                    "JOIN comicbooks.volumes v ON v.id = cb.volume_id " +
                                    "WHERE v.Title LIKE ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);

            rs = pstmt.executeQuery();

            comics = convertResultSetToComicIssues(rs);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return comics;
    }

    public ComicIssue getIssueById(int id) {
        List<ComicIssue> comics = new ArrayList<ComicIssue>();

        String sql = "SELECT cb.id, cb.issue_number, v.title, cb.description, w.name as writer, a.name as artist, p.name as publisher, v.year, cb.price " +
                                    "FROM comicbooks.comic_books cb " +
                                    "JOIN comicbooks.artists a ON a.id = cb.artist_id " +
                                    "JOIN comicbooks.writers w ON w.id = cb.writer_id " +
                                    "JOIN comicbooks.publishers p ON p.id = cb.publisher_id " +
                                    "JOIN comicbooks.volumes v ON v.id = cb.volume_id " +
                                    "WHERE cb.id = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            comics = convertResultSetToComicIssues(rs);

        } catch(Exception e) {
            e.printStackTrace();
        }

        if(comics.size() > 0) {
            return comics.get(0);
        }

        return new ComicIssue();
    }

    public void insertIssue(ComicIssue issue) {
        Random random = new Random();
        boolean success = false;
        
        // get writer id
        int writerId = getExistingWriterId(issue.getWriterName());
        if(writerId == 0) {
            writerId = generateRandomId(random);
            // create entry in db
            success = insertWriter(writerId, issue.getWriterName());
        }

        // get artist id
        int artistId = getExistingArtistId(issue.getArtistName());
        if(artistId == 0) {
            artistId = random.nextInt();
            // create entry in db
            success = insertArtist(artistId, issue.getArtistName());
        }

        // get volume id
        int volumeId = getExistingVolumeId(issue.getVolumeTitle());
        if(volumeId == 0) {
            volumeId = random.nextInt();
            // create entry in db
            success = insertVolume(volumeId, issue.getVolumeTitle(), issue.getPublicationYear());
        }

        // get publisher id
        int publisherId = getExistingPublisherId(issue.getVolumeTitle());
        if(publisherId == 0) {
            publisherId = random.nextInt();
            // create entry in db
            success = insertPublisher(publisherId, issue.getPublisherName());
        }

        // insert the issue
        try {
            pstmt = conn.prepareStatement("INSERT INTO comicbooks.comic_books VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, generateRandomId(random));
            pstmt.setString(2, issue.getIssueNumber());
            pstmt.setFloat(3, issue.getPrice());
            pstmt.setString(4, issue.getDescription());
            pstmt.setInt(5, artistId);
            pstmt.setInt(6, writerId);
            pstmt.setInt(7, publisherId);
            pstmt.setInt(8, volumeId);

            if(pstmt.executeUpdate() > 0) {
                conn.commit();
                System.out.println("Issue inserted successfully.");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        
    }

    public void updateIssue(ComicIssue issue) {
        Random random = new Random();
        boolean success = false;
        
        // get writer id
        int writerId = getExistingWriterId(issue.getWriterName());
        if(writerId == 0) {
            writerId = generateRandomId(random);
            // create entry in db
            success = insertWriter(writerId, issue.getWriterName());
        }

        // get artist id
        int artistId = getExistingArtistId(issue.getArtistName());
        if(artistId == 0) {
            artistId = random.nextInt();
            // create entry in db
            success = insertArtist(artistId, issue.getArtistName());
        }

        // get volume id
        int volumeId = getExistingVolumeId(issue.getVolumeTitle());
        if(volumeId == 0) {
            volumeId = random.nextInt();
            // create entry in db
            success = insertVolume(volumeId, issue.getVolumeTitle(), issue.getPublicationYear());
        }

        // get publisher id
        int publisherId = getExistingPublisherId(issue.getVolumeTitle());
        if(publisherId == 0) {
            publisherId = random.nextInt();
            // create entry in db
            success = insertPublisher(publisherId, issue.getPublisherName());
        }

        // update the issue
        try {
            String sql = "UPDATE comicbooks.comic_books cb SET cb.issue_number = ?, cb.price = ?, cb.description = ?, cb.artist_id = ?, cb.writer_id = ?, cb.publisher_id = ?, cb.volume_id = ? WHERE cb.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, issue.getIssueNumber());
            pstmt.setFloat(2, issue.getPrice());
            pstmt.setString(3, issue.getDescription());
            pstmt.setInt(4, artistId);
            pstmt.setInt(5, writerId);
            pstmt.setInt(6, publisherId);
            pstmt.setInt(7, volumeId);
            pstmt.setInt(8, issue.getId());

            if(pstmt.executeUpdate() > 0) {
                conn.commit();
                System.out.println("Issue updated successfully.");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        
    }

    // delete an issue
    public void deleteIssue(int id) {
        try {
            String sql = "DELETE FROM comicbooks.comic_books cb WHERE cb.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

             if(pstmt.executeUpdate() > 0) {
                conn.commit();
                System.out.println("Issue deleted successfully.");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    // insert publisher
    private boolean insertPublisher(int id, String publisherName) {
        try {
            pstmt = conn.prepareStatement("INSERT INTO comicbooks.publishers VALUES (?, ?)");
            pstmt.setInt(1, id);
            pstmt.setString(2, publisherName);

            if(pstmt.executeUpdate() > 0) {
                conn.commit();
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // insert volume
    private boolean insertVolume(int id, String volumeTitle, int publicationYear) {
        try {
            pstmt = conn.prepareStatement("INSERT INTO comicbooks.volumes VALUES (?, ?, ?)");
            pstmt.setInt(1, id);
            pstmt.setString(2, volumeTitle);
            pstmt.setInt(3, publicationYear);

            if(pstmt.executeUpdate() > 0) {
                conn.commit();
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // insert artist
    private boolean insertArtist(int id, String artistName) {
        try {
            pstmt = conn.prepareStatement("INSERT INTO comicbooks.artists VALUES (?, ?)");
            pstmt.setInt(1, id);
            pstmt.setString(2, artistName);

            if(pstmt.executeUpdate() > 0) {
                conn.commit();
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // insert writer
    private boolean insertWriter(int id, String writerName) {
        try {
            pstmt = conn.prepareStatement("INSERT INTO comicbooks.writers VALUES (?, ?)");
            pstmt.setInt(1, id);
            pstmt.setString(2, writerName);

            if(pstmt.executeUpdate() > 0) {
                conn.commit();
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // check for existing publisher
    private int getExistingPublisherId(String publisherName) {
        int publisherId = 0;

        try {
            String sql = "SELECT id FROM comicbooks.publishers WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, publisherName);
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
                publisherId = rs.getInt("id");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return publisherId;
    }

    // check for existing volume
    private int getExistingVolumeId(String volumeTitle) {
        int volumeId = 0;

        try {
            String sql = "SELECT id FROM comicbooks.volumes WHERE title = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, volumeTitle);
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
                volumeId = rs.getInt("id");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return volumeId;
    }

    // check for existing artist
    private int getExistingArtistId(String artistName) {
        int artistId = 0;

        try {
            String sql = "SELECT id FROM comicbooks.artists WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, artistName);
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
                artistId = rs.getInt("id");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return artistId;
    }

    // check for existing writer
    private int getExistingWriterId(String writerName) {
        int writerId = 0;

        try {
            String sql = "SELECT id FROM comicbooks.writers WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, writerName);
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
                writerId = rs.getInt("id");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return writerId;
    }

    // generate random id
    private int generateRandomId(Random random) {
        return random.nextInt(Integer.MAX_VALUE - 1);
    }

    private List<ComicIssue> convertResultSetToComicIssues(ResultSet rs) {
        List<ComicIssue> comics = new ArrayList<ComicIssue>();
        
        try {
            while (rs.next()) {
                ComicIssue comicIssue = new ComicIssue();
                comicIssue.setId(rs.getInt("Id"));
                comicIssue.setIssueNumber(rs.getString("Issue_Number"));
                comicIssue.setWriterName(rs.getString("Writer"));
                comicIssue.setArtistName(rs.getString("Artist"));
                comicIssue.setDescription(rs.getString("Description"));
                comicIssue.setVolumeTitle(rs.getString("Title"));
                comicIssue.setPublisherName(rs.getString("Publisher"));
                comicIssue.setPublicationYear(rs.getInt("Year"));
                comicIssue.setPrice(rs.getFloat("Price"));

                comics.add(comicIssue);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return comics;
    }
    
}
