package org.example.chatapplication;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button button_logout;  //  link to the log-out button
    @FXML
    private Label label_welcome;    // label welcome
    @FXML
    private Label label_fav_channel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // CLICK THE BUTTON LOG OUT TO GO MAIN PAGE
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });

    }

    // TO SHOW WELCOME + USERNAME DASHBOARD
    public void setUserInformation(String username, String favChannel){
        label_welcome.setText("Welcome" + username + "!");
        label_fav_channel.setText("Your favorite YouTube channel is " + favChannel + "!");
    }
}
