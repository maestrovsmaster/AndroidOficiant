package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 26.10.2015.
 */
public class Device {

    private String id="";
    private String name="";

    public Device(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
