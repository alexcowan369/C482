package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * The product objects are created with this class. Fields are displayed below until "public Product" method, then subsequent methods follow the fields below as directed in the UML diagram.
 * @author Alexander Cowan
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    //Product Constrictor below
    public Product(int id, String name, double price, int stock, int min, int max, ObservableList<Part> associatedParts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = associatedParts;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the associated parts
     */
    public ObservableList<Part> getAllAssociatedParts(){ return associatedParts; }

    /**
     *
     * @param associatedParts the associated part to set
     */

    public void setAssociatedParts(ObservableList<Part> associatedParts) { this.associatedParts = associatedParts; }

    /**
     * @param part the part to add
     */
    public void addAssociatedPart(Part part){ this.associatedParts.add(part); }

    /**
     * Method to delete associated part
     * @param selectedAssociatedPart the associated part to delete
     * @return When part is selected, then "No item is found. Cannot Delete" error message displays
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if(!(selectedAssociatedPart == null)) {
            return associatedParts.remove(selectedAssociatedPart);
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("No item is found. Cannot delete");
        alert.showAndWait();
        return false;}

    }