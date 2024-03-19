package org.example.chatapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

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
        }else{ // IF THE USER WANTS TO SWITCH ON THT LOGIN TO SIGN UP
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
