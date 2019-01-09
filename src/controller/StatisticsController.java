package controller;

import GUI.StatisticsFrame;

import javax.swing.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A class that handles the statistics view and logic.
 * The class fetches the scores from the SQL database, and calculates the averages, then it prints the results.
 */
public class StatisticsController {

    private JTextArea textArea;
    private JLabel loadingLabel;
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;
    private Connection connection;
    private Statement statement;

    /**
     * Constructs the controller methods
     * @throws ClassNotFoundException if not mySql class have found
     * @throws SQLException if there was a problem with the connection
     */
    StatisticsController() throws ClassNotFoundException, SQLException {
        initGUI();
        initSqlCredentials();
        initConnection();
        calculateAndPrintStatistics();
        closeConnection();
    }

    /**
     * Fetchs the data from the databse, Filters scores by IDs and mapIds
     * @throws SQLException if there is a connection problem
     */
    private void calculateAndPrintStatistics() throws SQLException {
        String query = "SELECT * FROM logs";
        ResultSet resultSet = statement.executeQuery(query);
        final int MACHINE_PLAY_ID = 1234;
        final int FIRST_ID = 308566611;
        final int SECOND_ID = 312522329;

        int mapId;

        double humanSumScoreByMapID[] = new double[9];
        int humanNumOfgamesByMapId[] = new int[9];

        double machineSumScoreByMapID[] = new double[9];
        int machineNumOfgamesByMapId[] = new int[9];

        double studentsSumScoreByMapID[] = new double[9];
        int studentsNumOfgamesByMapId[] = new int[9];

        while(resultSet.next())
        {
            mapId = getMapId(resultSet.getString("SomeDouble"));

            // Validate this a valid Map Id, of the new JAR
            if (mapId != -1) {
                // Our Human Played scenario
                if ((resultSet.getInt("FirstID") == FIRST_ID &&  resultSet.getInt("SecondID") == SECOND_ID && resultSet.getInt("ThirdID") == 0)
                        || resultSet.getInt("FirstID") == SECOND_ID &&  resultSet.getInt("SecondID") == FIRST_ID && resultSet.getInt("ThirdID") == 0) {

                    humanSumScoreByMapID[mapId-1] += resultSet.getDouble("Point");
                    humanNumOfgamesByMapId[mapId-1]++;


                // Our Machine Played scenario
                } else if ((resultSet.getInt("FirstID") == FIRST_ID &&  resultSet.getInt("SecondID") ==  MACHINE_PLAY_ID)
                        || resultSet.getInt("FirstID") == SECOND_ID &&  resultSet.getInt("SecondID") ==  MACHINE_PLAY_ID) {

                    machineSumScoreByMapID[mapId-1] += resultSet.getDouble("Point");
                    machineNumOfgamesByMapId[mapId-1]++;

                // Another students results scenario
                } else {
                    studentsSumScoreByMapID[mapId-1] += resultSet.getDouble("Point");
                    studentsNumOfgamesByMapId[mapId-1]++;
                }

                if (resultSet.getInt("ThirdID") == MACHINE_PLAY_ID) {
                    System.out.println("Found Third ID: " + MACHINE_PLAY_ID);

                    System.out.println("The First ID Is: " + resultSet.getInt("FirstID"));
                }

            }

        }
        resultSet.close();

        calculateAndPrintAverages(humanSumScoreByMapID, humanNumOfgamesByMapId,
                             machineSumScoreByMapID, machineNumOfgamesByMapId,
                             studentsSumScoreByMapID, studentsNumOfgamesByMapId);
    }

    /**
     * Inits the GUI, building the Statistics Frame and takes the JTextArea and JLabel
     */
    private void initGUI() {
        StatisticsFrame frame = new StatisticsFrame();
        textArea = frame.getTextArea();
        loadingLabel = frame.getLoadingLabel();
    }

    /**
     * Setup for the database credentials
     */
    private void initSqlCredentials() {
        jdbcUrl = "jdbc:mysql://ariel-oop.xyz:3306/oop";
        jdbcUser = "student";
        jdbcPassword = "student";
    }

    /**
     * Setup the connection for the databse
     * @throws ClassNotFoundException if no MySql library was found
     * @throws SQLException if there was a connection problem
     */
    private void initConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        statement = connection.createStatement();
    }

    /**
     * Closing the Statement and Connection with database
     * @throws SQLException when there is a connection error
     */
    private void closeConnection() throws SQLException {
        statement.close();
        connection.close();
    }

    /**
     * Calculating the Map Id by the hash String
     * @param hash from the databse
     * @return the number of map in the CSV file, -1 if this not a valid hash
     */
    private int getMapId(String hash) {
        switch (hash) {
            case "2128259830":
                return 1;
            case "1149748017":
                return 2;
            case "-683317070":
                return 3;
            case "1193961129":
                return 4;
            case "1577914705":
                return 5;
            case "-1315066918":
                return 6;
            case "-1377331871":
                return 7;
            case "306711633":
                return 8;
            case "919248096":
                return 9;
            default:
                return -1;
        }
    }

    /**
     * Calculates the average scores between our sum of scores and another students sum of scores, then prints the results to the text area
     * @param humanScoreSumByMapId Our sum of scores
     * @param humanNumOfGamesByMapId Our number of games
     * @param machineScoreSumByMapId Our machine scores
     * @param machineNumOfGamesByMapId Our machine Number of games
     * @param studentsScoreSumByMapId Another students scores
     * @param studentsNumOfGamesByMapId Another students number of games
     */
    private void calculateAndPrintAverages(double[] humanScoreSumByMapId, int[] humanNumOfGamesByMapId,
                                           double[] machineScoreSumByMapId, int[] machineNumOfGamesByMapId,
                                           double[] studentsScoreSumByMapId, int[] studentsNumOfGamesByMapId) {
        loadingLabel.setVisible(false);
        NumberFormat formatter = new DecimalFormat("#0.000");
        double difference;

        textArea.append("Map ID\tNo. Of Games Human Played\tNo. Of Games Machine Played\tOur Human Average\tOur Machine Average\tAnother Students Average\tDifference\n\n");

        for (int i=0; i<9; i++) {

            // Calculate the Average for every map id
            if (humanNumOfGamesByMapId[i] != 0) humanScoreSumByMapId[i] = humanScoreSumByMapId[i] / humanNumOfGamesByMapId[i];
            if (machineNumOfGamesByMapId[i] != 0) machineScoreSumByMapId[i] = machineScoreSumByMapId[i] / machineNumOfGamesByMapId[i];
            if (studentsNumOfGamesByMapId[i] != 0) studentsScoreSumByMapId[i] = studentsScoreSumByMapId[i] / studentsNumOfGamesByMapId[i];

            // Calculate the difference between our scores and another student scores
            difference = (humanScoreSumByMapId[i] + machineScoreSumByMapId[i]) - studentsScoreSumByMapId[i];

            textArea.append(
                    (i+1) + "\t" +
                    humanNumOfGamesByMapId[i] + "\t\t" +
                    machineNumOfGamesByMapId[i] + "\t\t" +
                    formatter.format(humanScoreSumByMapId[i]) + "\t\t" +
                    formatter.format(machineScoreSumByMapId[i]) + "\t\t" +
                    formatter.format(studentsScoreSumByMapId[i]) + "\t\t" +
                    formatter.format(difference) + "\n");
        }
    }
}