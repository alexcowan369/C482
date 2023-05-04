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
 * The class below creates the AddPart controller.
 * @author Alexander Cowan
 */
public class AddPart implements Initializable {

    private static int id = 150;

    Stage stage;
    Parent scene;

    @FXML private RadioButton addpartInhouseradio;
    @FXML private RadioButton addpartOutsourceradio;
    @FXML private TextField addpartMaxtext;
    @FXML private TextField addpartMintext;
    @FXML private TextField addpartNametext;
    @FXML private TextField addpartInvtext;
    @FXML private TextField addpartPricetext;
    @FXML private TextField addpartCompanytext;
    @FXML private Label addpartIDlabel;
    @FXML private Label MachineOrCompany;

    /**
     * <p><b>
     * RUNTIME ERROR: In in the initial creation of the "AddPart" controller, earlier variants had issues with blank fields and/or number formats such as integers and doubles. This occurred with price integers and doubles in the min and max boxes. I then realized this is an issue with exceptions after googling this issue then prompted me to watch the webinar on said topic: "Exceptions and Exception Handling" which allowed me to to create try/catch statement that generates a "Warning" and utilize "Numberformatexception" in order to instruct the user to put correct inputs into said box. This then solved the issue and allowed me to implement into other classes down the road.
     * </b></p>
     * In the method below it allows for a user to save a new part when the "Save" button is selected. Likewise, as the information is inputted in the AddPart screen it is then saved to the inventory.
     * @param event user clicks the "Save" button
     * @throws IOException
     * @throws NumberFormatException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(addpartIDlabel.getText());
            String name = addpartNametext.getText();
            int stock = Integer.parseInt(addpartInvtext.getText());
            double price = Double.parseDouble(addpartPricetext.getText());
            int min = Integer.parseInt(addpartMintext.getText());
            int max = Integer.parseInt(addpartMaxtext.getText());

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The minimum must be less than or equal to the maximum]");
                alert.showAndWait();
            }
            else if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The stock value must be within the minimum and maximum range]");
                alert.showAndWait();
            }
            else if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The name cannot be empty]");
                alert.showAndWait();
            }
            else {
                //Below is what occurs when InHouse radio button is selected
                if (addpartInhouseradio.isSelected()) {
                    int machineID = Integer.parseInt(addpartCompanytext.getText());
                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineID));
                }
                //Below is what occurs Outsourced radio button is selected
                else if (addpartOutsourceradio.isSelected()) {
                    String companyName = addpartCompanytext.getText();
                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                }
                //Below is radio button to return to main screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Input valid values for all text fields");
            alert.showAndWait();
        }
    }

    /**
     * Method below allows for the user to return to the main screen after clicking the "Cancel" button. It will also clear any inputted data/values which have not been added through the proper channels.
     * @param event user clicks the Cancel button
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All values will be cleared, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Method below allows the Machine ID/Company Name field to change to Machine ID when the user clicks the InHouse radio button.
     * @param event user clicks "InHouse" radio button
     */
    @FXML
    void onActionInhouse(ActionEvent event){
        MachineOrCompany.setText("Machine ID:");
        addpartOutsourceradio.setSelected(false);
    }

    /**
     * Method below allows the Machine ID/Company Name field to change to Company Name when the user clicks the Outsourced radio button.
     * @param event user clicks "Outsourced" radio button
     */
    @FXML
    void onActionOutsourced(ActionEvent event){
        MachineOrCompany.setText("Company Name:");
        addpartInhouseradio.setSelected(false);
    }

    /**
     * Method below allows the application to select a new part id beginning at 150 then count upward from there.
     * @return the part id
     */
    public static int getPartIDCount() {
        id++;
        return id;
    }

    /**
     * Method below sets the radio button to a default of InHouse, acquire a new part ID, and set said part ID as an uneditable ID field.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addpartInhouseradio.setSelected(true);
        id = getPartIDCount();
        addpartIDlabel.setText(String.valueOf(id));
    }
}
