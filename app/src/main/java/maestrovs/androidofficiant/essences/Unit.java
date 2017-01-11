package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 28.04.2016.
 */
public class Unit extends DicTable {

    String fullName="";

    public Unit(int id, int grpId, String name) {
        super(id, grpId, name);
    }

    public String getFullName() {
        return fullName;
    }
}
