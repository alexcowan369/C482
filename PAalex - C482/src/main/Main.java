package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <h1>Inventory Management System for C482 (QKM2)</h1>
 * The class below creates an application which manages the inventory of various parts and products.
 * <p><b>
 * FUTURE ENHANCEMENT: I thought of two different functionalities to add and improve the inventory system. The first enhancement would be a form of an export "button"/controller on both parts and products which would allow for for you to export the current inventory in parts or products as files such as .csv and Excel for further analysis.
 * Especially if the data is exported in excel you can make a customer workbook with macros enabled to find some more trends or you could import the newly exported .csv into some other software which can utilize it. The second one is a adding a field of buying in scale and what those prices would be.
 * Example under parts "Price/Cost per X Units" this can be any int value, same can be applied for the products section too. I would imagine this could help the salesperson using this to identify the pricing for bulk orders and provide an incentive for the buyer. However, the second idea would most likely be better suited for a macros excel workbook or something of that nature.
 * </b></p>
 * RUNTIME ERROR is found above onActionSave in the AddPart.java controller.
 * After unzipping qkm2alexcowan.zip, the Javadocs files are found within folder PAalex - C482/Javadoc
 * @author Alexander Cowan
 */
public class Main extends Application {

    /**
     * Below starts the GUI and displays the initial "Main Screen" by loading MainScreen.fxml.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1200, 550));
        primaryStage.show();
    }

    /**
     * In the method below, the database is launched. Also the main method.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
