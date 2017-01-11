package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 28.04.2016.
 */
public class DicTable extends TableDB {

    String name="";

    public DicTable(int id, int grpId) {
        super(id, grpId);
    }

    public DicTable(int id, int grpId, String name) {
        super(id, grpId);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
