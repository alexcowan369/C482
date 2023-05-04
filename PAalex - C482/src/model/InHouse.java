package model;

/**
 * InHouse part objects is created by this class. Likewise, machineID is added to the instance and the abstract Part class is extended. "machineId" set to private.
 * InHouse part constructor below after "private int machineID;"
 * @author Alexander Cowan
 */
public class InHouse extends Part{
    private int machineId;
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Getter and Setter below
     * @return the machine ID
     */
    public int getMachineId() { return machineId; }

    /**
     * @param machineID the machine ID to set
     */
    public void setMachineId(int machineID) { this.machineId = machineId; }

}
