import java.io.*;
import java.sql.*;
import org.testng.annotations.Test;
import org.junit.jupiter.api.Assertions;


public class OpenMeteoAPITest {

    // Checks if it is possible to establish a connection with the database.
    // It tries to connect to the database using the credentials "postgres" and "1234".
    // If the connection is established successfully, the test passes.
    @Test
    public void testConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/OpenMeteo", "postgres", "1234");
        Assertions.assertNotNull(connection);
        connection.close();
    }


    // Verifies if it is possible to insert a row in the "Master" table of the database.
    // It inserts a line with latitude equal to -23.5489 and longitude equal to -46.6388 and checks
    // if a row was affected (that is, if the insertion was successful).
    @Test
    public void testInsertionInMaster() throws SQLException {
        double latitude = -23.5489;
        double longitude = -46.6388;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/OpenMeteo", "postgres", "1234")) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Master (latitude, longitude) VALUES (?, ?)");
            statement.setDouble(1, latitude);
            statement.setDouble(2, longitude);
            int affectedRows = statement.executeUpdate();
            Assertions.assertEquals(1, affectedRows);
            statement.close();
        }
    }


    // Checks if CSV file is created after execution
    @Test
    public void verifyCSVFile() {
        OpenMeteoAPI.main(new String[]{});
        File file = new File("dados.csv");
        Assertions.assertTrue(file.exists());
    }
}