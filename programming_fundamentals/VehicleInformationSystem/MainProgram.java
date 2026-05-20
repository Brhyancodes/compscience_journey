import java.util.InputMismatchException;
import java.util.Scanner;

public class MainProgram {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        try {

            // ================= CAR =================
            Car car = new Car();

            System.out.println("Enter Car Details");

            System.out.print("Make: ");
            car.setMake(input.nextLine());

            System.out.print("Model: ");
            car.setModel(input.nextLine());

            System.out.print("Year: ");
            car.setYear(input.nextInt());

            System.out.print("Number of Doors: ");
            car.setNumberOfDoors(input.nextInt());

            input.nextLine();

            System.out.print("Fuel Type (Petrol/Diesel/Electric): ");
            car.setFuelType(input.nextLine());

            // ================= MOTORCYCLE =================
            Motorcycle motorcycle = new Motorcycle();

            System.out.println("\nEnter Motorcycle Details");

            System.out.print("Make: ");
            motorcycle.setMake(input.nextLine());

            System.out.print("Model: ");
            motorcycle.setModel(input.nextLine());

            System.out.print("Year: ");
            motorcycle.setYear(input.nextInt());

            System.out.print("Number of Wheels: ");
            motorcycle.setNumberOfWheels(input.nextInt());

            input.nextLine();

            System.out.print("Motorcycle Type (Sport/Cruiser/Off-road): ");
            motorcycle.setMotorcycleType(input.nextLine());

            // ================= TRUCK =================
            Truck truck = new Truck();

            System.out.println("\nEnter Truck Details");

            System.out.print("Make: ");
            truck.setMake(input.nextLine());

            System.out.print("Model: ");
            truck.setModel(input.nextLine());

            System.out.print("Year: ");
            truck.setYear(input.nextInt());

            System.out.print("Cargo Capacity (tons): ");
            truck.setCargoCapacity(input.nextDouble());

            input.nextLine();

            System.out.print("Transmission Type (Manual/Automatic): ");
            truck.setTransmissionType(input.nextLine());

            // ================= DISPLAY DETAILS =================
            System.out.println("\n================ VEHICLE INFORMATION ================");

            car.displayDetails();
            motorcycle.displayDetails();
            truck.displayDetails();

        }

        catch (InputMismatchException error) {
            System.out.println("Invalid input entered.");
            System.out.println("Please enter the correct data type.");
        }

        catch (Exception error) {
            System.out.println("Unexpected Error: " + error.getMessage());
        }

        finally {
            input.close();
        }
    }
}