package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The class below creates the ModifyPart controller.
 * @author Alexander Cowan
 */
public class ModifyPart implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private RadioButton modifypartInhouseradio;
    @FXML private RadioButton modifypartOutsourceradio;
    @FXML private TextField modifypartMaxtext;
    @FXML private TextField modifypartMintext;
    @FXML private TextField modifypartNametext;
    @FXML private TextField modifypartInvtext;
    @FXML private TextField modifypartPricetext;
    @FXML private TextField modifypartCompanytext;
    @FXML private Label modifypartIDlabel;
    @FXML private Label MachineIDorCompanyName;

    /**
     * In the method below when the "Save" button is clicked it will modify an existing part. With the information inputted into the specific fields on the "Modify Part" screen it will then update the existing part and save the newly modified part into the inventory. You also see the method replacing the existing part with the inventory ID.
     * @param event user clicks the "Save" button
     * @throws IOException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(modifypartIDlabel.getText());
            String name = modifypartNametext.getText();
            int stock = Integer.parseInt(modifypartInvtext.getText());
            double price = Double.parseDouble(modifypartPricetext.getText());
            int min = Integer.parseInt(modifypartMintext.getText());
            int max = Integer.parseInt(modifypartMaxtext.getText());

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The minimum must be less than or equal to the maximum]");
                alert.showAndWait();
            } else if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The stock value must be within the minimum and maximum range]");
                alert.showAndWait();
            } else if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The name must not be empty]");
                alert.showAndWait();
            } else {
                //InHouse radio button is selected
                if (modifypartInhouseradio.isSelected()) {
                    int machineID = Integer.parseInt(modifypartCompanytext.getText());
                    Inventory.updatePart(id, new InHouse(id, name, price, stock, min, max, machineID));
                }
                //Outsourced radio button is selected
                else if (modifypartOutsourceradio.isSelected()) {
                    String companyName = modifypartCompanytext.getText();
                    Inventory.updatePart(id, new Outsourced(id, name, price, stock, min, max, companyName));
                }
                //Return to Main Screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Input valid values for all text fields");
            alert.showAndWait();
        }
    }

    /**
     * In this method when the "Cancel" button is clicked the user is then returned to the main screen. Note: inputted information will not be saved.
     * @param event user clicks the "Cancel" button
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all changes, do you want to continue?");
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * In this method the Machine ID/Company name is field is changed to Machine ID when the radio button: "Inhouse" is selected.
     * @param event user clicks In-House radio button
     */
    @FXML
    void onActionInhouse(ActionEvent event) {
        MachineIDorCompanyName.setText("Machine ID");
        modifypartOutsourceradio.setSelected(false);
    }

    /**
     * In this method the Machine ID/Company name is field is changed to Company when the radio button: Outsourced is selected.
     * @param event user clicks Outsourced radio button
     */
    @FXML
    void onActionOutsourced(ActionEvent event) {
        MachineIDorCompanyName.setText("Company Name");
        modifypartInhouseradio.setSelected(false);
    }

    /**
     * In the method below, a part selected on the "Main Screen" to be modified is sent to the fields section on the "Modify Part" screen which also includes it pre-existing data.
     * @param part the part selected on the "Main Screen" to be modified
     */
    public void sendPart(Part part) {
        modifypartIDlabel.setText(String.valueOf(part.getId()));
        modifypartNametext.setText(part.getName());
        modifypartInvtext.setText(String.valueOf(part.getStock()));
        modifypartPricetext.setText(String.valueOf(part.getPrice()));
        modifypartMaxtext.setText(String.valueOf(part.getMax()));
        modifypartMintext.setText(String.valueOf(part.getMin()));

        //Get InHouse or Outsourced radio button
        if(part instanceof InHouse) {
            modifypartInhouseradio.setSelected(true);
            MachineIDorCompanyName.setText("Machine ID");
            modifypartCompanytext.setText(String.valueOf(((InHouse) part).getMachineId()));
        }
        else {
            modifypartOutsourceradio.setSelected(true);
            MachineIDorCompanyName.setText("Company Name");
            modifypartCompanytext.setText(String.valueOf(((Outsourced) part).getCompanyName()));
        }
    }

    /**
     * In the method below the "Modify Part" class is Initialized.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
