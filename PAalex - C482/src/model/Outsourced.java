package model;

/**
 * Outsourced Part objects are created by this class. Likewise, the ability to add companyName to the instance and extend the abstract class is also shown. Note: "companyName" designated as private
 * Outsourced part constructor below after "private String companyName;"
 * @author Alexander Cowan
 */
public class Outsourced extends Part{
    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Getter and Setter below like the other classes
     * @return the company name
     */
    public String getCompanyName() { return this.companyName; }

    /**
     * @param companyName the company name to set
     */
    public void setCompanyName(String companyName) { this.companyName = companyName; }

}
