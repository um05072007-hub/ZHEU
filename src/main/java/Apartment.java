import java.util.ArrayList;
import java.util.List;

public class Apartment {
    private int id;
    private House house;
    private List<Equipment> equipmentList;

    public Apartment(int id, House house) {
        this.id = id;
        this.house = house;
        this.equipmentList = new ArrayList<Equipment>();
    }

    public int getId() { return id; }
    public House getHouse() { return house; }
    public double getS_ot_ZHEU() { return house.getS_ot_ZHEU(); }
    public List<Equipment> getEquipmentList() { return equipmentList; }
    public void addEquipment(Equipment e) { equipmentList.add(e); }

    public static void generateDistrict(List<House> houses, List<Equipment> allEquip, int houseCount) {
        String[] types = WorkType.values();
        int eqIdCounter = 1000;
        int aptIdCounter = 101;

        for (int i = 0; i < houseCount; i++) {
            double dist = 1.0 + (Math.random() * 4.0);
            House h = new House(i + 1, dist);
            houses.add(h);

            int count_Apartment = 1 + (int)(Math.random() * 100);
            for (int j = 0; j < count_Apartment; j++) {
                Apartment apt = new Apartment(aptIdCounter, h);
                h.addApartment(apt);
                aptIdCounter++;

                int itemsInApt = 1 + (int)(Math.random() * 10);
                for (int k = 0; k < itemsInApt; k++) {
                    String type = types[(int)(Math.random() * types.length)];
                    boolean need_Inctpum = Math.random() < 0.3;
                    int Tt = 15 + (int)(Math.random() * 40);
                    Equipment eq = new Equipment(eqIdCounter, type, need_Inctpum, Tt, apt);
                    eq.setWear(Math.random() * 100.0);
                    apt.addEquipment(eq);
                    allEquip.add(eq);
                    eqIdCounter++;
                }
            }
        }
    }
}