package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * Inventory for parts and products is held in this class. Methods to delete parts & products, add, search via ID/full/partial names, and update are also available.
 * Note: allParts and allProducts are the fields as required by UML.
 * @author Alexander Cowan
 * */
public class Inventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * In the method below, this allows for a new part to be added to "Inventory". On the "Main Screen" the part is displayed when correctly added.
     * @param newPart the new part to be added
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * In the method below, it allows to search parts by EXACT part IDs utilizing the "Lookup Method". An exact match is returned if one exist after the method is called. Note: When it is called, the lookup parses the current parts in inventory and provides the basis for all future lookups in the application.
     * Lookup part is a required static method as noted in the UML diagram. We will see the other methods below follow the same pathway within this class.
     * @param partId the part id to be searched for
     * @return The part matching the search ID criteria (only if a part is found).
     */
    public static Part lookupPart(int partId) {
        Part searchForPartID = null;
        for (Part part : getAllParts()) {
            if (part.getId() == partId) {
                searchForPartID = part;
            }
        }
        return searchForPartID;
    }

    /**
     * In the method below, it allows to search by partial or exact part names. Current parts in the inventory are checked when this method is called then returns an exact or partial match if one or more exist.
     * Similar to the alert statements, we are matching the class "Part" with the local variable of "part" for clarity.
     * @param partName the part name to be searched for
     * @return The part or parts matching the search text criteria, either exact matches or if the string is found at any point within part names (only if at least one part name or part of a part name is found).
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                returnedParts.add(part);
            }
        }
        return returnedParts;
    }

    /**
     * In the method below, another variant of lookup is used for updating parts. When this is called it takes an existing part which is being modified and replaces the part in the inventory using a part ID.
     * @param uniqueId the id of the part to be updated
     * @param selectedPart is the chosen part to be updated
     */
    public static void updatePart(int uniqueId, Part selectedPart) {
        int index = -1;
        for (Part Part : getAllParts()) {
            index++;
            if (Part.getId() == uniqueId) {
                getAllParts().set(index, selectedPart);
            }
        }
    }

    /**
     * In the method below, we delete a part. A part is removed from the all parts list when called.
     * @param selectedPart the chosen part to be deleted
     */
    public static boolean deletePart(Part selectedPart) { return allParts.remove(selectedPart);}


    /**
     * In the method below, we see the operation to grab all parts via the getter. All parts in the inventory are returned when this method is called.
     * @return All parts currently in the inventory.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * In the method below, we see the ability to add a new product to inventory. In the table on the main screen, the product is displayed once successfully added.
     * @param newProduct the new product to be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * In the method below, we see the ability to search products using the exact "product ID". Similar to above, the inventory is parsed for an exact ID match when called.
     * Note: class "Product" is matched to variable/parameter "product" for clarity.
     * @param productId the product id to be searched for
     * @return The product matching the search ID criteria (only if a product is found).
     */
    public static Product lookupProduct(int productId) {
        Product searchForProductID = null;
        for (Product product : Inventory.getAllProducts()) {
            if (product.getId() == productId) {
                searchForProductID = product;
            }
        }
        return searchForProductID;
    }

    /**
     * In the method below, we see the ability to search for partial or exact names (product names). The inventory is parsed, then returns an exact or partial match if one or more exist when called.
     * @param productName the product name to be searched for
     * @return The product or products matching the search text criteria, either exact matches or if the string is found at any point within product names (only if at least one product name or part of a product name is found).
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> returnedProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                returnedProducts.add(product);
            }
        }
        return returnedProducts;
    }

    /**
     *
     *
     *
     * In the method below, the ability to update a product is shown. Similar to code above, the existing product which is being modified then replaces the corresponding existing product in the inventory via the 'Product ID".
     * @param uniqueId the id of the product to be updated
     * @param newProduct the selected product to be updated
     */
    public static void updateProduct(int uniqueId, Product newProduct) {
        int index = -1;
        for (Product Product : Inventory.getAllProducts()) {
            index++;
            if (Product.getId() == uniqueId) {
                Inventory.getAllProducts().set(index, newProduct);
            }
        }
    }

    /**
     * In the method below, we see the ability to delete a product. In the inventory a product is deleted when this method is called.
     * @param selectedProduct is the selected product to be deleted
     */
    public static boolean deleteProduct(Product selectedProduct) { return allProducts.remove(selectedProduct);}

    /**
     * In the method below, we see the ability to grab all get products. All items in the inventory are returned once this method is called.
     * @return All products currently in the inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
