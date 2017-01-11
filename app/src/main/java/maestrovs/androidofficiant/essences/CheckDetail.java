package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 01.12.2016.
 */

public class CheckDetail {

    int id;

    Double quantity;
    Good good;
    int gruppa_praisa;

    public CheckDetail(Good good, Double quantity, int gruppa_praisa) {
        this.good=good;
        this.quantity=quantity;
        this.gruppa_praisa=gruppa_praisa;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Good getGood() {
        return good;
    }

    public Double getQuantity() {
        return quantity;
    }

    public int getGruppa_praisa() {
        return gruppa_praisa;
    }
}
