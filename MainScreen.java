package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.lang.*;

/**
 * The class below creates the MainScreen controller.
 * @author Alexander Cowan
 */
public class MainScreen implements Initializable {

    Stage stage;
    Parent scene;
    @FXML private TextField mainmenupartsSearchtext;
    @FXML private TableView<Part> mainmenuPartsTableView;
    @FXML private TableColumn<Part, Integer> mainmenupartsColumnPartID;
    @FXML private TableColumn<Part, String> mainmenupartsColumnPartName;
    @FXML private TableColumn<Part, Integer> mainmenupartsColumnInventory;
    @FXML private TableColumn<Part, Double> mainmenupartsColumnPrice;
    @FXML private TextField mainmenuproductsSearchtext;
    @FXML private TableView<Product> mainmenuproductsTableView;
    @FXML private TableColumn<Product, Integer> mainmenuproductsColumnProductID;
    @FXML private TableColumn<Product, String> mainmenuproductsColumnProduct;
    @FXML private TableColumn<Product, Integer> mainmenuproductsColumnInventory;
    @FXML private TableColumn<Product, Double> mainmenuproductsColumnPrice;

    /**
     * This method loads the "Add Part" screen when the user clicks the Add button in the Parts section.
     * @param event user clicks the "Add button" in the Parts section
     * @throws IOException
     */
    @FXML
    void onActionAddParts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * In the method below the "Search" button when clicked searches the parts inventory specifically in the "Parts" section. Parts can be searhced  by exact part ID, exact name, or partial name. When inputted the inventory is checked for the requested part and returns an exact id match if one exists or returns  partial name if one or multiple exist.
     * Note: for the "Alert" warning box system we are using the pre-made fxscenecontrol public class of "Alert" matched with local variable "alert" as well. This follows the best practice outlined in the webinars of matching the two.
     * This is also congruent throughout the rest of the project/files.
     * @param event user clicks the "Search button" in the Parts section
     */
    @FXML
    void onActonSearchParts(ActionEvent event) {
        String searchBox = mainmenupartsSearchtext.getText();
        try {
            //First, try searching by name
            ObservableList<Part> tempParts = Inventory.lookupPart(searchBox);
            //If no results by name, then search by ID
            if (tempParts.size() == 0) {
                int findID = Integer.parseInt(searchBox);
                Part searchPart = Inventory.lookupPart(findID);
                tempParts.add(searchPart);
                //If no results by ID, then alert no parts found by ID
                if (searchPart == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("No parts found by specific ID");
                    alert.showAndWait();
                }
            }
            mainmenuPartsTableView.setItems(tempParts);
        }
        //Catch NumberFormatException and alert no parts found by name
        catch (NumberFormatException e){
            mainmenuPartsTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No parts found by specific name");
            alert.showAndWait();
        }
    }

    /**
     * Below is the method that refreshes all the "Parts Table" and ALL parts within the inventory when all text from the parts search field is deleted by said user.
     * @param event user deletes all text from the parts search field
     */
    @FXML
    void mainScreenPartSearchOnKeyTyped(KeyEvent event) {
        if (mainmenupartsSearchtext.getText().isEmpty()) {
            mainmenuPartsTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * Below is the method that when the "Modify" button is clicked allows the "Modify" parts screen to load up. IOException used to make sure a part is selected.
     * @param event user clicks the "Modify" button in the Parts section
     * @throws IOException
     */
    @FXML
    void onActionModifyParts(ActionEvent event) throws IOException {
        //If no parts are selected, alert and return to MainScreen
        if (mainmenuPartsTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR [No part selected]");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();

            ModifyPart ModController = loader.getController();
            ModController.sendPart(mainmenuPartsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * In the method below when the "Delete" button is selected a part is then removed from the "Parts" section. IOException to make sure the user does not make an unwanted delete.
     * @param event user clicks the "Delete" button in the Parts section
     * @throws IOException
     */
    @FXML
    void onActionDeleteParts(ActionEvent event) throws IOException {
        Part part = mainmenuPartsTableView.getSelectionModel().getSelectedItem();
        if (mainmenuPartsTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR [No part selected]");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you 100% sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                Inventory.deletePart(part);
            } else {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    /**
     * In the method below when the "Add" button in the "Products" section is clicked the "Add Product" screen is then loaded.
     * @param event user clicks the "Add" button in the "Products" section
     * @throws IOException
     */
    @FXML
    void onActionAddProducts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * In this method the product inventory is searched when the "Search" button is clicked. Very similar to above where it searched by exact "product ID", partial name, or exact product name. Current products in the inventory are searched and returns the exact ID of one exists or returns an exact or partial name match if one or multiple exist.
     * @param event user clicks the "Search" button in the Products section
     */
    @FXML //Logic below for searching products will be similar in parts)
    void onActionSearchProducts(ActionEvent event) {
        String searchProductField = mainmenuproductsSearchtext.getText();
        try {
            //First, try searching by name
            ObservableList<Product> invProducts = Inventory.lookupProduct(searchProductField);
            //If no results by name, then search by ID
            if (invProducts.size() == 0) {
                int findProductID = Integer.parseInt(searchProductField);
                Product searchProd = Inventory.lookupProduct(findProductID);
                invProducts.add(searchProd);
                //If no results by ID, then alert no products found by ID
                if (searchProd == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("No products found by specific ID");
                    alert.showAndWait();
                }
            }
            mainmenuproductsTableView.setItems(invProducts);
        }
        //Catch NumberFormatException and alert no products found by name if occurs
        catch (NumberFormatException e) {
            mainmenuproductsTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No products found by inputted name");
            alert.showAndWait();
        }
    }

    /**
     * In this method when the user deletes all text from the product search field all products within the "Products Table" inventory are refreshed.
     * @param event user deletes all text from the products search field
     */
    @FXML
    void mainScreenProductSearchOnKeyTyped(KeyEvent event) {
        if (mainmenuproductsSearchtext.getText().isEmpty()) {
            mainmenuproductsTableView.setItems(Inventory.getAllProducts());
        }
    }

    /**
     * In this method when the "Modify" button is pressed in the "Product" section the "Modify Product" screen is loaded. IOException forces the user to have a product selected in order to modify it similar to the IOException with "Delete"
     * @param event user clicks the "Modify" button in the Product section
     * @throws IOException
     */
    @FXML
    void onActionModifyProducts(ActionEvent event) throws IOException {
        //If no products selected, alert and return to MainScreen like above
        if (mainmenuproductsTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR [No product selected]");
            alert.showAndWait();
        } else {
            FXMLLoader fxloader = new FXMLLoader();
            fxloader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            fxloader.load();

            ModifyProduct ModController = fxloader.getController();
            ModController.sendProduct(mainmenuproductsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = fxloader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * In this method when the "Delete" button is clicked in the "Product" section and product from inventory is consequently deleted.
     * @param event user clicks the "Delete" button in the Product section
     * @throws IOException
     */
    @FXML
    void onActionDeleteProducts(ActionEvent event) throws IOException {
        Product product = mainmenuproductsTableView.getSelectionModel().getSelectedItem();
        if (mainmenuproductsTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR [No product selected]");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                if (!product.getAllAssociatedParts().isEmpty()) {
                    Alert assocAlert = new Alert(Alert.AlertType.WARNING);
                    assocAlert.setTitle("Warning Dialog");
                    assocAlert.setContentText("Product has at least 1 associated part");
                    assocAlert.showAndWait();
                } else {
                    Inventory.deleteProduct(product);
                }
            }
        }
    }

    /**
     * In this method when the "Exit" button is clicked the program is terminated. IOException displaying an exit warning.
     * @param event user clicks the "Exit" button
     */
    @FXML void onActionExitMainScreen(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * In this method the all parts and all products table views are set.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Below is "Parts" table section
        //Table loaded with default view of parts
        mainmenuPartsTableView.setItems(Inventory.getAllParts());

        //Load the columns with parts info
        mainmenupartsColumnPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainmenupartsColumnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainmenupartsColumnInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainmenupartsColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Below is "Product" table section
        //Loads table with default view of products
        mainmenuproductsTableView.setItems(Inventory.getAllProducts());

        //Loads columns with products info
        mainmenuproductsColumnProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainmenuproductsColumnProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainmenuproductsColumnInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainmenuproductsColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}

