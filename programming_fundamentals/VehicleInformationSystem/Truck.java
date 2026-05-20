public class Truck implements Vehicle, TruckVehicle {

    private String make;
    private String model;
    private int year;
    private double cargoCapacity;
    private String transmissionType;

    // Vehicle Methods
    @Override
    public String getMake() {
        return make;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setMake(String make) {
        this.make = make;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    // TruckVehicle Methods
    @Override
    public void setCargoCapacity(double capacity) {
        this.cargoCapacity = capacity;
    }

    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    @Override
    public String getTransmissionType() {
        return transmissionType;
    }

    // Display Method
    public void displayDetails() {
        System.out.println("\n===== Truck Details =====");
        System.out.println("Make: " + make);
        System.out.println("Model: " + model);
        System.out.println("Year: " + year);
        System.out.println("Cargo Capacity: " + cargoCapacity + " tons");
        System.out.println("Transmission Type: " + transmissionType);
    }
}