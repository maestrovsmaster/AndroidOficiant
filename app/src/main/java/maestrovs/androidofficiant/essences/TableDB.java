package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 28.04.2016.
 */
public abstract  class TableDB {

    private int id=0;
    private int grpId=0;



    public TableDB(int id, int grpId) {
        this.id = id;
        this.grpId=grpId;
    }

    public int getId() {
        return id;
    }

    public int getGrpId() {
        return grpId;
    }

}
