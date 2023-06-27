package ser322;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
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
