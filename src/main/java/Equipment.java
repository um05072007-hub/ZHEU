public class Equipment {
    private int id;
    private String type;
    private double wear;
    private boolean need_Spec_Inctpum;
    private int T_Remont;
    private Apartment apartment;

    public Equipment(int id, String type, boolean need_Inctpum, int Tt, Apartment apt) {
        this.id = id;
        this.type = type;
        this.wear = 0;
        this.need_Spec_Inctpum = need_Inctpum;
        this.T_Remont = Tt;
        this.apartment = apt;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public double getWear() { return wear; }
    public boolean need_Spec_Inctpum() { return need_Spec_Inctpum; }
    public int getT_Remont() { return T_Remont; }
    public Apartment getApartment() { return apartment; }

    public void setWear(double wear) { this.wear = wear; }
}