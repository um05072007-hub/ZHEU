import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private static final double S_peshkom = 0.1;
    private static final double Cp_s = 0.5;

    private List<Brigade> brigades;
    private List<Vehicle> freeVehicles;
    private List<House> houses;
    private List<Equipment> allEquip;
    private List<Equipment> requests;

    private double sum_Time, Time_С_Inctpum, Time_net_Inctpum;
    private int Count_Remont, Count_C_Inctpum, Count_bez_Inctpum;
    private double basicTime, electricTime, plumbingTime, constructionTime;
    private int basicCnt, electricCnt, plumbingCnt, constructionCnt;

    public Simulation(int brigadesCount, int vehiclesCount, int houseCount) {
        this.brigades = new ArrayList<Brigade>();
        this.freeVehicles = new ArrayList<Vehicle>();
        this.houses = new ArrayList<House>();
        this.allEquip = new ArrayList<Equipment>();
        this.requests = new ArrayList<Equipment>();

        resetStats();

        List<Vehicle> fleet = Vehicle.createFleet(vehiclesCount);
        for (int i = 0; i < fleet.size(); i++) { this.freeVehicles.add(fleet.get(i));
        }
        List<Brigade> genBrigades = Brigade.generateBrigades(brigadesCount);
        for (int i = 0; i < genBrigades.size(); i++) { this.brigades.add(genBrigades.get(i));
        }
        Apartment.generateDistrict(this.houses, this.allEquip, houseCount);
    }

    private void resetStats() {
        sum_Time = 0; Time_С_Inctpum = 0; Time_net_Inctpum = 0;
        Count_Remont = 0; Count_C_Inctpum = 0; Count_bez_Inctpum = 0;
        basicTime = 0; electricTime = 0; plumbingTime = 0; constructionTime = 0;
        basicCnt = 0; electricCnt = 0; plumbingCnt = 0; constructionCnt = 0;
    }

    public void run() {
        Zajavki_Remont_Ochered();
        Zajavki_poick_Br();
        Otchet();
    }

    private void Zajavki_Remont_Ochered() {
        for (int i = 0; i < allEquip.size(); i++) { Equipment e = allEquip.get(i);
            if (e.getWear() > 99.0) { requests.add(e);
            }
        }
    }

    private void Zajavki_poick_Br() {
        for (int i = 0; i < requests.size(); i++) { Equipment eq = requests.get(i);
            Brigade assigned = null;

            for (int j = 0; j < brigades.size(); j++) { Brigade b = brigades.get(j);
                if (b.getType().equals(eq.getType())) { boolean ectli_skil = true;
                    if (eq.need_Spec_Inctpum()) {
                        if (!b.need_Skill() || freeVehicles.size() == 0) { ectli_skil = false;
                        }
                    }if (ectli_skil) {assigned = b; break;}
                }
            }
            if (assigned != null) {
                double dist = eq.getApartment().getS_ot_ZHEU();
                double speed;
                if (freeVehicles.size() > 0) {speed = freeVehicles.get(0).getSpeed();
                } else {speed = S_peshkom;
                }
                double travel = (dist * 2) / Cp_s;
                double vremi = eq.getT_Remont() + travel;
                Statictika(eq, vremi);
                eq.setWear(0);
                if (freeVehicles.size() > 0 && eq.need_Spec_Inctpum() && assigned.need_Skill()) {
                    freeVehicles.remove(0);
                } assigned.setState("Ремонтирует");
            }
        }
        for (int i = 0; i < brigades.size(); i++) { brigades.get(i).setState("Ожидает");
        }
    } private void Statictika(Equipment e, double vremi) { sum_Time += vremi;
        Count_Remont++;
        if (e.getType().equals(WorkType.BASIC)) { basicTime += vremi;
            basicCnt++;
        } else if (e.getType().equals(WorkType.ELECTRIC)) { electricTime += vremi;
            electricCnt++;
        } else if (e.getType().equals(WorkType.PLUMBING)) { plumbingTime += vremi;
            plumbingCnt++;
        } else if (e.getType().equals(WorkType.CONSTRUCTION)) { constructionTime += vremi;
            constructionCnt++;
        }


        if (e.need_Spec_Inctpum()) { Time_С_Inctpum += vremi;Count_C_Inctpum++;
        } else { Time_net_Inctpum += vremi;Count_bez_Inctpum++;
        }
    }
    public void Otchet() { System.out.println("1. Среднее время, ремонта одного оборудования:");
        if (Count_Remont == 0) {System.out.println("Нет данных");
        } else {System.out.printf("Всего: %.2f мин\n", sum_Time / Count_Remont);
            if (basicCnt > 0) System.out.printf("   BASIC: %.2f мин\n", basicTime / basicCnt);
            if (electricCnt > 0) System.out.printf("   ELECTRIC: %.2f мин\n", electricTime / electricCnt);
            if (plumbingCnt > 0) System.out.printf("   PLUMBING: %.2f мин\n", plumbingTime / plumbingCnt);
            if (constructionCnt > 0) System.out.printf("   CONSTRUCTION: %.2f мин\n", constructionTime / constructionCnt);
        }
        System.out.println("\n2. Среднее время,ремонта одного оборудования спец.инструм. , без инструмента:");

        double Cr_vr_odn_oborud_cpec;
        if (Count_C_Inctpum > 0) { Cr_vr_odn_oborud_cpec = Time_С_Inctpum / Count_C_Inctpum;
        } else { Cr_vr_odn_oborud_cpec = 0;
        } System.out.printf("С инструментом: %.2f мин\n", Cr_vr_odn_oborud_cpec);

        double avgNoInstr;
        if (Count_bez_Inctpum > 0) { avgNoInstr = Time_net_Inctpum / Count_bez_Inctpum;
        } else { avgNoInstr = 0;
        }
        System.out.printf("Без инструмента: %.2f мин\n", avgNoInstr);
        System.out.println("\n3. Список всех бригад:");
        List<Brigade> bl = new ArrayList<Brigade>(brigades);
        Sort_Brigad_Sostoyanie(bl);
        System.out.println("ID , Тип , Инстр. , ТС , Состояние");
        for (int i = 0; i < bl.size(); i++) { Brigade b = bl.get(i);
            String v;
            if (b.getVehicle() != null) { v = "Да";
            } else { v = "Нет";
            }String s;
            if (b.need_Skill()) { s = "Да";
            } else { s = "Нет";
            }System.out.printf("%d , %s , %s , %s , %s\n",
                    b.getId(), b.getType(), s, v, b.getState());
        }
        System.out.println("\n4. Список оборудования (проц. износа > 50%):");
        List<Equipment> el = new ArrayList<Equipment>();
        for (int i = 0; i < allEquip.size(); i++) { Equipment e = allEquip.get(i);
            if (e.getWear() > 50.0) {
                el.add(e);
            }
        }
        SortEquipment_S(el);
        System.out.println("ID , Дом(км) , Тип , Износ");
        for (int i = 0; i < el.size(); i++) { Equipment e = el.get(i);
            System.out.printf("%d , %.2f , %s , %.1f%%\n",
                    e.getId(), e.getApartment().getS_ot_ZHEU(), e.getType(), e.getWear());
        }
    }
    public void Sort_Brigad_Sostoyanie(List<Brigade> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) { Brigade a = list.get(j);
                Brigade b = list.get(j + 1);
                if (a.getState().compareTo(b.getState()) > 0) {list.set(j, b);
                    list.set(j + 1, a);
                }
            }
        }
    }

    public void SortEquipment_S(List<Equipment> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                Equipment a = list.get(j);
                Equipment b = list.get(j + 1);
                if (a.getApartment().getS_ot_ZHEU() > b.getApartment().getS_ot_ZHEU()) {
                    list.set(j, b);
                    list.set(j + 1, a);
                }
            }
        }
    }
}