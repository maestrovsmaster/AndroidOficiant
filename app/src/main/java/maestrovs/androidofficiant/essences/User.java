package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 28.04.2016.
 */
public class User  {


    int emplId;
    String name="";
    String job="";

    public User(int emplId, String name, String job) {
        this.emplId = emplId;
        this.name = name;
        this.job = job;
    }

    public int getEmplId() {
        return emplId;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
