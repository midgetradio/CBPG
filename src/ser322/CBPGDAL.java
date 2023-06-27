package ser322;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Random;
import java.sql.SQLException;

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

    public void getTopTenComics() {
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT cb.issue_number, v.title, cb.description, w.name as writer, a.name as artist, p.name as publisher, v.year, cb.price " +
                                    "FROM comicbooks.comic_books cb " +
                                    "JOIN comicbooks.artists a ON a.id = cb.artist_id " +
                                    "JOIN comicbooks.writers w ON w.id = cb.writer_id " +
                                    "JOIN comicbooks.publishers p ON p.id = cb.publisher_id " +
                                    "JOIN comicbooks.volumes v ON v.id = cb.volume_id " +
                                    "ORDER BY cb.price DESC " +
                                    "LIMIT 10;");

            printResultSet(rs);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void getComicsByWriter(String writerName) {
        writerName = "%" + writerName + "%";
        String sql = "SELECT cb.issue_number, v.title, cb.description, w.name as writer, a.name as artist, p.name as publisher, v.year, cb.price " +
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

            printResultSet(rs);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void getComicsByArtist(String artistName) {
        artistName = "%" + artistName + "%";
        String sql = "SELECT cb.issue_number, v.title, cb.description, w.name as writer, a.name as artist, p.name as publisher, v.year, cb.price " +
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

            printResultSet(rs);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void getComicsByTitle(String title) {
        title = "%" + title + "%";
        String sql = "SELECT cb.issue_number, v.title, cb.description, w.name as writer, a.name as artist, p.name as publisher, v.year, cb.price " +
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

            printResultSet(rs);

        } catch(Exception e) {
            e.printStackTrace();
        }
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
        if(success) {
            System.out.println("Writer inserted successfully.");
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
        }

        // get publisher id
        int publisherId = getExistingPublisherId(issue.getVolumeTitle());
        if(publisherId == 0) {
            publisherId = random.nextInt();
            // create entry in db
        }

        
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
        return random.nextInt(Integer.MAX_VALUE) + 1;
    }

    private void printResultSet(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.print(rs.getString("Issue_Number") + "\t");
                System.out.print(rs.getString("Title") + "\t\t");
                System.out.print(rs.getString("Publisher") + "\t");
                System.out.print(rs.getInt("Year") + "\t");
                System.out.print(rs.getFloat("Price") + "\t");
                System.out.println();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
