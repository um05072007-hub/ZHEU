import java.util.ArrayList;
import java.util.List;

public class Brigade {
    private int id;
    private String type;
    private boolean need_Skill;
    private String state;
    private Vehicle vehicle;

    public Brigade(int id, String type, boolean need_Skill) {
        this.id = id;
        this.type = type;
        this.need_Skill = need_Skill;
        this.state = "Ожидает";
        this.vehicle = null;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public boolean need_Skill() { return need_Skill; }
    public String getState() { return state; }
    public Vehicle getVehicle() { return vehicle; }


    public void setState(String state) { this.state = state; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public static List<Brigade> generateBrigades(int count) {
        List<Brigade> list = new ArrayList<Brigade>();
        String[] types = WorkType.values();
        for (int i = 0; i < count; i++) { list.add(new Brigade(i + 1, types[i % types.length], Math.random() < 0.7));
        } return list;
    }
}