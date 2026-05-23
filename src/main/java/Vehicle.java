import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private int id;
    private double speed;

    public Vehicle(int id, double speed) {
        this.id = id;
        this.speed = speed;
    }

   public double getSpeed() { return speed; }

    public static List<Vehicle> createFleet(int count) {
        List<Vehicle> fleet = new ArrayList<Vehicle>();
        for (int i = 0; i < count; i++) {
            fleet.add(new Vehicle(i + 1, 0.4 + (Math.random() * 0.6)));
        }
        return fleet;
    }
}