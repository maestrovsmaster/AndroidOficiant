package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 28.04.2016.
 */
public class Employee extends DicTable {

    String codeName="";
    String surName="";
    String secName="";
    JobTitle jobTitle=null;


    public Employee(int id, int grpId, String name) {
        super(id, grpId, name);
    }

    public String getCodeName() {
        return codeName;
    }

    public String getSurName() {
        return surName;
    }

    public String getSecName() {
        return secName;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }
}
