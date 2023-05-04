package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The class below creates the ModifyProduct controller.
 * @author Alexander Cowan
 */
public class ModifyProduct implements Initializable {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Product testProduct;

    Stage stage;
    Parent scene;

    @FXML private TextField modifyproductMaxtext;
    @FXML private TextField modifyproductMintext;
    @FXML private TextField modifyproductNametext;
    @FXML private TextField modifyproductInvtext;
    @FXML private TextField modifyproductPricetext;
    @FXML private Label modifyproductIDlabel;
    @FXML private TextField modifyproductaddSearchtext;
    @FXML private TableView<Part> modifyproductaddTableView;
    @FXML private TableColumn<Part, Integer> modifyproductaddColumnPartID;
    @FXML private TableColumn<Part, String> modifyproductaddColumnPartName;
    @FXML private TableColumn<Part, Integer> modifyproductaddColumnInventory;
    @FXML private TableColumn<Part, Double> modifyproductaddColumnPrice;
    @FXML private TableView<Part> modifyproductdeleteTableView;
    @FXML private TableColumn<Part, Integer> modifyproductdeleteColumnPartID;
    @FXML private TableColumn<Part, String> modifyproductdeleteColumnPartName;
    @FXML private TableColumn<Part, Integer> modifyproductdeleteColumnInventory;
    @FXML private TableColumn<Part, Double> modifyproductdeleteColumnPrice;

    /**
     * In the method below when the user clicks the "Add" button an associated part is added to the product being modified. The part from the inventory table at the top of the program is then copied to the associated parts below.
     * @param event user clicks the "Add" button
     */
    @FXML
    void onActionAdd(ActionEvent event) {
        Part selectedPart = modifyproductaddTableView.getSelectionModel().getSelectedItem();
        testProduct.addAssociatedPart(selectedPart);
        testProduct.setAssociatedParts(testProduct.getAllAssociatedParts());
    }

    /**
     * In the method below once the "Search" is clicked it allows the user to search for parts. Similar to other searches in the program for exact ID and full or partial names.
     * @param event user clicks the "Search" button
     */
    @FXML
    void onActionSearch(ActionEvent event) {
        String searchBox = modifyproductaddSearchtext.getText();
        try {
            ObservableList<Part> invParts = Inventory.lookupPart(searchBox);
            if (invParts.size() == 0) {
                int findID = Integer.parseInt(searchBox);
                Part searchPro = Inventory.lookupPart(findID);
                invParts.add(searchPro);
                if (searchPro == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("No parts found by specific ID");
                    alert.showAndWait();
                }
            }
            modifyproductaddTableView.setItems(invParts);
        }
        catch (NumberFormatException e) {
            modifyproductaddTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No parts found by name inputted");
            alert.showAndWait();
        }
    }

    /**
     * In the method below when text is deleted from the search field the top "Parts Table" and all parts in inventory are refreshed.
     * @param event user deletes all text from "Search" field
     */
    @FXML
    void modifyProductSearchOnKeyTyped(KeyEvent event) {
        if (modifyproductaddSearchtext.getText().isEmpty()) {
            modifyproductaddTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * In the method below when the "Remove Associated Part" button is clicked the associated part from the product being modified is removed. The part will remain in inventory but is removed from bottom associated parts table.
     * @param event users clicks the "Remove Associated Part" button
     */
    @FXML
    void onActionDelete(ActionEvent event) {
        Part deletePart = modifyproductdeleteTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.show();
            testProduct.deleteAssociatedPart(deletePart);
            testProduct.setAssociatedParts(testProduct.getAllAssociatedParts());
        }
    }

    /**
     * In the method below the "Save" button saves the modification to an existing product. The information inputted on the "Modify Product" screen updates the existing product then saves the newly modified product to the inventory. Next, the method product takes the product being modified and replaces the existing version within the inventory using the product ID.
     * @throws IOException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            testProduct.setId(Integer.parseInt(modifyproductIDlabel.getText()));
            testProduct.setName(modifyproductNametext.getText());
            testProduct.setPrice(Double.parseDouble(modifyproductPricetext.getText()));
            testProduct.setStock(Integer.parseInt(modifyproductInvtext.getText()));
            testProduct.setMin(Integer.parseInt(modifyproductMintext.getText()));
            testProduct.setMax(Integer.parseInt(modifyproductMaxtext.getText()));
            testProduct.setAssociatedParts(testProduct.getAllAssociatedParts());

            if (Integer.parseInt(modifyproductMintext.getText()) > Integer.parseInt(modifyproductMaxtext.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The minimum must be less than or equal to the maximum]");
                alert.showAndWait();
            } else if (Integer.parseInt(modifyproductInvtext.getText()) > Integer.parseInt(modifyproductMaxtext.getText()) || Integer.parseInt(modifyproductInvtext.getText()) < Integer.parseInt(modifyproductMintext.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The stock value must be within the minimum and maximum range]");
                alert.showAndWait();
            } else if (modifyproductNametext.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The name must not be empty]");
                alert.showAndWait();
            }
            else {
                Inventory.updateProduct(testProduct.getId(), testProduct);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please input valid values for all text fields");
            alert.showAndWait();
        }
    }

    /**
     * In the method below when the "Cancel" button is clicked the user returns to the main screen. Info is not saved.
     * @param event user clicks the "Cancel" button
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all changes, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * In the method below a product is selected on the "Main Screen" for modification and sends said product data to the fields of the "Modify Product" screen.
     * @param product the product selected on the "Main Screen" to be modified
     */
    public void sendProduct(Product product) {
        testProduct = product;

        modifyproductIDlabel.setText(String.valueOf(testProduct.getId()));
        modifyproductNametext.setText(testProduct.getName());
        modifyproductPricetext.setText(String.valueOf(testProduct.getPrice()));
        modifyproductInvtext.setText(String.valueOf(testProduct.getStock()));
        modifyproductMaxtext.setText(String.valueOf(testProduct.getMax()));
        modifyproductMintext.setText(String.valueOf(testProduct.getMin()));
        modifyproductdeleteTableView.setItems(testProduct.getAllAssociatedParts());
    }

    /**
     * In the method below, the top all parts and bottom associated parts table views are set.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Top table view
        modifyproductaddTableView.setItems(Inventory.getAllParts());
        modifyproductaddColumnPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyproductaddColumnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyproductaddColumnInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyproductaddColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Bottom table view
        modifyproductdeleteColumnPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyproductdeleteColumnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyproductdeleteColumnInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyproductdeleteColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
