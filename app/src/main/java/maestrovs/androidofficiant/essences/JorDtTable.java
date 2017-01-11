package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 28.04.2016.
 */
public class JorDtTable extends JorTable{

    int hdId=0;
    Good good=null;
    double count = 0;

    public JorDtTable(int id, int grpId, int hdId, Good good, double count) {
        super(id, grpId);
        this.hdId = hdId;
        this.good=good;
        this.count=count;
    }



    public int getHdId() {
        return hdId;
    }

    public Good getGood() {
        return good;
    }

    public double getCount() {
        return count;
    }
}
