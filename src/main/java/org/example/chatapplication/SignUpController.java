package org.example.chatapplication;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button button_sign_up;
    @FXML
    private Button button_log_in;
    @FXML
    private RadioButton rb_azamat_channel;
    @FXML
    private RadioButton rb_someone_else;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //CREATE A TOGGLE GROUP FOR EACH RADIO BUTTON CLICK
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_azamat_channel.setToggleGroup(toggleGroup);
        rb_someone_else.setToggleGroup(toggleGroup);

        // IF WE CLICK THE BUTTON IT'S GONNA BE TRUE
        rb_azamat_channel.setSelected(true);
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // IT'S GONNA BE INSERTED INTO THE DATABASE
                String toggleName = ((RadioButton) toggleGroup.getSelectedToggle()).getText();

                // WE DON'T WANT EMPTY PLACE IN THE USERNAME AND PASSWORD TEXT FIELD;
                if(! tf_username.getText().trim().isEmpty() && ! tf_password.getText().trim().isEmpty()){
                    DBUtils.singUpUser(event, tf_username.getText(), tf_password.getText(), toggleName);
                }else {
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });

        //CHANGE THE SCENE TO LOGIN PAGE WHEN WE CLICK THE BUTTON
        button_log_in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "Log in Page!", null, null );
            }
        });


    }
}
