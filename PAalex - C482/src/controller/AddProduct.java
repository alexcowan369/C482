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
 * The class below creates the AddProduct Controller.
 * @author Alexander Cowan
 */
public class AddProduct implements Initializable {

    private static int productID = 1000;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Product uniqueProduct = new Product(0, "", 0.00, 0, 0, 0, null);

    Stage stage;
    Parent scene;


    @FXML private TextField addproductMaxtext;
    @FXML private TextField addproductMintext;
    @FXML private TextField addproductNametext;
    @FXML private TextField addproductInvtext;
    @FXML private TextField addproductPricetext;
    @FXML private Label addproductIDlabel;
    @FXML private TextField addproductaddSearchtext;
    @FXML private TableView<Part> addproductaddTableView;
    @FXML private TableColumn<Part, Integer> addproductaddColumnPartID;
    @FXML private TableColumn<Part, String> addproductaddColumnPartName;
    @FXML private TableColumn<Part, Integer> addproductaddColumnInventory;
    @FXML private TableColumn<Part, Double> addproductaddColumnPrice;
    @FXML private TableView<Part> addproductdeleteTableView;
    @FXML private TableColumn<Part, Integer> addproductdeleteColumnPartID;
    @FXML private TableColumn<Part, String> addproductdeleteColumnPartName;
    @FXML private TableColumn<Part, Integer> addproductdeleteColumnInventory;
    @FXML private TableColumn<Part, Double> addproductdeleteColumnPrice;

    /**
     * Method below adds an associated part to a new product when user interacts with the "Add" button. From the inventory in the top table a part is taken the copied into the associated parts beneath it.
     * @param event user clicks said "Add" button
     */
    @FXML
    void onActionAdd(ActionEvent event) {
        Part selectedPart = addproductaddTableView.getSelectionModel().getSelectedItem();
        associatedParts.add(selectedPart);
        addproductdeleteTableView.setItems(associatedParts);
    }

    /**
     * In the method below when the "Search" button is clicked the inventory is searched. Partial names, full names, and/or exact part IDs are searched for when clicked.
     * @param event user clicks said "Search" button
     */
    @FXML
    void onActionSearch(ActionEvent event) {
        String searchBox = addproductaddSearchtext.getText();
        try {
            ObservableList<Part> invParts = Inventory.lookupPart(searchBox);
            if (invParts.size() == 0) {
                int findID = Integer.parseInt(searchBox);
                Part findPart = Inventory.lookupPart(findID);
                invParts.add(findPart);
                if (findPart == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("No parts found by specific ID");
                    alert.showAndWait();
                }
            }
            addproductaddTableView.setItems(invParts);
        }
        catch (NumberFormatException e){
            addproductaddTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No parts found by specific name inputted");
            alert.showAndWait();
        }
    }

    /**
     * In the method below, it allows a "refresh" of the top parts in the table and all parts in the inventory when said user deletes text from the specific search field.
     * @param event user deletes all text from the search field
     */
    @FXML
    void addProductSearchOnKeyTyped(KeyEvent event) {
        if (addproductaddSearchtext.getText().isEmpty()) {
            addproductaddTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * In the method below, it allows the removal of an associated part from said new part being created when the "Remove Associated Part" button is clicked. The part remains in the inventory but the associated part is removed from the "associated parts table"
     * @param event users clicks the "Remove Associated Part button"
     */
    @FXML
    void onActionDelete(ActionEvent event) {
        Part deletePart = addproductdeleteTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.show();
            associatedParts.remove(deletePart);
            addproductdeleteTableView.setItems(associatedParts);
        }
    }

    /**
     * In the method below the "Save" button allows a user to save a new product. Saved products are then inputted to the inventory via the information inputted on the "Add Product" screen if all attributes/boxes are filled properly. Note IException below maintaining the integrity of data inputted via "Warnings".
     * @param event user clicks said "Save" button
     * @throws IOException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            uniqueProduct.setId(Integer.parseInt(addproductIDlabel.getText()));
            uniqueProduct.setName(addproductNametext.getText());
            uniqueProduct.setPrice(Double.parseDouble(addproductPricetext.getText()));
            uniqueProduct.setStock(Integer.parseInt(addproductInvtext.getText()));
            uniqueProduct.setMin(Integer.parseInt(addproductMintext.getText()));
            uniqueProduct.setMax(Integer.parseInt(addproductMaxtext.getText()));
            uniqueProduct.setAssociatedParts(associatedParts);

            if (Integer.parseInt(addproductMintext.getText()) > Integer.parseInt(addproductMaxtext.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The minimum must be less than or equal to the maximum]");
                alert.showAndWait();
            } else if (Integer.parseInt(addproductInvtext.getText()) > Integer.parseInt(addproductMaxtext.getText()) || Integer.parseInt(addproductInvtext.getText()) < Integer.parseInt(addproductMintext.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The stock value must be within the minimum and maximum range]");
                alert.showAndWait();
            } else if (addproductNametext.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR [The name must not be empty]");
                alert.showAndWait();
            }
            else {
                Inventory.addProduct(uniqueProduct);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Super Secret Warning Dialog");
            alert.setContentText("Input valid values for all text fields");
            alert.showAndWait();
        }
    }

    /**
     * In the method below the "Cancel" button returns the user back to the main screen after accepting the warning message. Note the IOException is not being used for data integrity like above but just as a precautionary warning in order to make sure the user does not make an unwanted mistake deleting all data inputted.
     * @param event user clicks said "Cancel" button
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
     * In the method below it allows the new product ID starting at 1000.
     * @return the product ID
     */
    public static int getProductIDCount() {
        productID++;
        return productID;
    }

    /**
     * This is the method to set the top all parts table view and the bottom associated parts table view, get the new product id, and set the new product id to the uneditable id field.
     * In the method below it sets the top "All Parts" table views and the bottom "Associated Parts" table view. Also allows to get the "product ID" and set the "product ID" as an uneditable field to maintain its integrity within the application.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addproductaddTableView.setItems(Inventory.getAllParts());
        addproductaddColumnPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        addproductaddColumnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addproductaddColumnInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addproductaddColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        addproductdeleteColumnPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        addproductdeleteColumnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addproductdeleteColumnInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addproductdeleteColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productID = getProductIDCount();
        addproductIDlabel.setText(String.valueOf(productID));
    }
}
