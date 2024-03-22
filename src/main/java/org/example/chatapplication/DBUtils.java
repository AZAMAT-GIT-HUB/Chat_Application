package org.example.chatapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.*;


import java.io.IOException;

public class DBUtils {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String favChannel){
        Parent root = null;
        if(username != null && favChannel != null){
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username, favChannel); // // set username and favChannel
            }catch (IOException e){
                e.printStackTrace(); // CALL printStackTrace() METHOD IF THERE IS ANY ISSUES
            }
        }else{ // IF THE USER WANTS TO SWITCH ON THE LOGIN TO SIGN UP
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        // HERE HOW WE CAN GET A STAGE AND THEN SET THE NEW SCENE
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }

    // WORKING WITH FUNCTIONALITY TO SIGN THEM UP
    public static void singUpUser(ActionEvent event, String username, String password, String favChannel){
        // CONNECTING JAVAFX WITH DATABASE AND THEN QUERY FROM DATABASE
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        // IF THE USER WANTS TO SIGN UP WE FIRST NEED TO ESTABLISH A CONNECTION TO A DATABASE
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx_shema", "root", "root");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE usernames = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            // THE isBeforeFirst() METHOD RETURN FALSE IF THE RESULTSET IS EMPTY
            // IF isBeforeFirst() METHOD RETURNS TRUE THEN THIS MEANS THAT THE USERNAME HAS ALREADY BEEN TAKEN
            if(resultSet.isBeforeFirst()){  // IF TRUE
                System.out.println("User already exists!");    // PRINT
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username!");
                alert.show();
            }else{ // IF FALSE THEN SET INSERT INTO THE USER TABLE
                psInsert = connection.prepareStatement("INSERT INTO users (usernames, passwords, favChannel) VALUES (?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, favChannel);
                psInsert.executeUpdate();

                // AFTER SIGN UP LOGGED-IN PAGE WILL BE OPEN AND TITLE WILL BE "WELCOME"
                changeScene(event, "logged-in.fxml", "Welcome!", username, favChannel);
            }
        // IT'S NOT INPUT OUTPUT EXCEPTION
        // WE ARE WORKING WITH SQL SO THIS WILL BE SQLEXCEPTION
        }catch (SQLException e){
            e.printStackTrace();
        // AFTER WE ARE DONE CONNECTION TO THE DATABASE WE HAVE TO CLOSE DATABASE CONNECTION
        // IF WE DON'T CLOSE OUR DATABASE CONNECTION IT COULD LEAD TO MEMORY LEAKAGE
        }finally {
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null){
                try {
                    psCheckUserExists.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(psInsert != null){
                try {
                    psInsert.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            // WE CLOSED EVERYTHING AT THE END THE CONNECTION
        }
    }

    // WORKING WITH FUNCTIONALITY TO LOG THEM IN
    public static void logInUser(ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx_shema", "root", "root");
            preparedStatement = connection.prepareStatement("SELECT passwords, favChannel FROM users WHERE usernames = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            //CHECK IF THE PROVIDED USERNAME IS IN THE DATABASE
            if(!resultSet.isBeforeFirst()){ // IF THERE IS NO ROWS FOUND
                System.out.println("User not found in the Database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            }else { // IF SOMETHING IS RETURNED, IF THE USERNAME IS FOUND AND THEN WE NEED COMPARE THE PASSWORD TO ENTER
                while (resultSet.next()){
                    // the password and favChannel type in String not Int
                    String retrievedPassword = resultSet.getString("passwords"); // retrieve from the Passwords column in the table
                    String retrievedFavChannel = resultSet.getString("favChannel"); // retrieve from the favChannel column in the table
                    if(retrievedPassword.equals(password)){
                        changeScene(event, "logged-in.fxml", "Welcome Logged in Page!", username, retrievedFavChannel);
                    }else{
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect");
                        alert.show();
                    }
                }

            }
        }catch (SQLException e ){
            e.printStackTrace();
        }finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
