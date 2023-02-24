import com.opencsv.CSVWriter;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class OpenMeteoAPI {
    public static void main(String[] args) {

        // Set latitude, longitude and current weather parameters
        double latitude = -23.5489; // Latitude example for Sao Paulo, Brazil
        double longitude = -46.6388; // Longitude example for Sao Paulo, Brazil
        boolean currentWeather = true;

        // Building URL
        String urlString = buildApiUrl(latitude, longitude, currentWeather);

        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            System.err.println("Erro ao criar a URL: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set HTTP as GET
        if (connection != null) {
            try {
                connection.setRequestMethod("GET");

                // Check if the HTTP response was successful
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    System.out.println("Erro ao conectar com a API. Código de resposta HTTP: " + responseCode);
                    return;
                }

                // Get API response in JSON format
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                String response = responseBuilder.toString();

                // Print the response in the console
                System.out.println(response);

                // Close the connection and the reader
                reader.close();
                connection.disconnect();

                // Convert JSON response to Java objects
                JSONObject jsonResponse = new JSONObject(response);
                JSONObject currentWeatherJson = jsonResponse.getJSONObject("current_weather");

                // Get the values of temperature, wind speed and wind direction
                float temperatura = currentWeatherJson.getFloat("temperature");
                float velocidadeVento = currentWeatherJson.getFloat("windspeed");
                float direcaoVento = currentWeatherJson.getFloat("winddirection");

                // Get UTC Time
                LocalDateTime horaUtc = LocalDateTime.parse(currentWeatherJson.getString("time"));
                ZonedDateTime horaUtcZone = horaUtc.atZone(ZoneId.of("UTC"));
                ZonedDateTime horaLocalZone = horaUtcZone.withZoneSameInstant(ZoneId.systemDefault());
                LocalDateTime horaLocal = horaLocalZone.toLocalDateTime();


                // Open a connection to the Postgres database
                try (Connection connectionDb = DriverManager.getConnection("jdbc:postgresql://localhost:5432/OpenMeteo", "postgres", "1234")) {

                    // Enter latitude and longitude values into the Master table
                    PreparedStatement statementMaster = connectionDb.prepareStatement("INSERT INTO Master (latitude, longitude) VALUES (?, ?)");
                    statementMaster.setDouble(1, latitude);
                    statementMaster.setDouble(2, longitude);
                    statementMaster.executeUpdate();

                    // Get the ID generated from the Master table
                    PreparedStatement idgerado = connectionDb.prepareStatement("SELECT Max(id) FROM Master");
                    ResultSet valor = idgerado.executeQuery();
                    int idMaster = 0;
                    while (valor.next()) {
                        idMaster = valor.getInt(1);
                    }


                    // Enter values for temperature, wind speed, wind direction, UTC time and local time in the Detailed table
                    PreparedStatement statementDetalhada = connectionDb.prepareStatement("INSERT INTO Detailed (temperature, wind_speed, wind_direction, time_utc, local_time, id_master) VALUES (?, ?, ?, ?, ?, ?)");
                    statementDetalhada.setFloat(1, temperatura);
                    statementDetalhada.setFloat(2, velocidadeVento);
                    statementDetalhada.setFloat(3, direcaoVento);
                    statementDetalhada.setTimestamp(4, Timestamp.valueOf(horaUtc));
                    statementDetalhada.setTimestamp(5, Timestamp.valueOf(horaLocal));
                    statementDetalhada.setInt(6, idMaster);
                    statementDetalhada.executeUpdate();

                    // Close connections and statements
                    statementDetalhada.close();
                    statementMaster.close();
                    connectionDb.close();

                    // Generate a CSV file with data from the Detailed table
                    try (Connection connectionDb2 = DriverManager.getConnection("jdbc:postgresql://localhost:5432/OpenMeteo", "postgres", "1234")) {
                        PreparedStatement statementSelect = connectionDb2.prepareStatement("SELECT * FROM Detailed");
                        ResultSet resultSet = statementSelect.executeQuery();
                        CSVWriter writer = new CSVWriter(new FileWriter("dados.csv"));
                        writer.writeAll(resultSet, true);
                        writer.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e){
                System.out.println(e);
            }
        }
    }

    // Build JSON API URL with desired parameters
    public static String buildApiUrl(double latitude, double longitude, boolean currentWeather) {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                "&longitude=" + longitude + "&current_weather=" + currentWeather;
        return url;
    }
}




