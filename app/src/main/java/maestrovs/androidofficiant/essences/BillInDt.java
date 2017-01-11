package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 28.04.2016.
 */
public class BillInDt extends JorDtTable
{
    private double inPrice=0;

    public BillInDt(int id, int grpId,  int hdId, Good good, double count, double inPrice) {
        super(id, grpId,hdId, good, count);
        this.inPrice = inPrice;
    }



    public double getInPrice() {
        return inPrice;
    }
}