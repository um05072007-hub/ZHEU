import java.util.ArrayList;
import java.util.List;

public class House {
    private int id;
    private double S_ot_ZHEU;
    private List<Apartment> apartments;

    public House(int id, double S_ot_ZHEU) {
        this.id = id;
        this.S_ot_ZHEU = S_ot_ZHEU;
        this.apartments = new ArrayList<Apartment>();
    }

    public int getId() { return id; }
    public double getS_ot_ZHEU() { return S_ot_ZHEU; }
    public List<Apartment> getApartments() { return apartments; }
    public void addApartment(Apartment apt) { apartments.add(apt); }
}