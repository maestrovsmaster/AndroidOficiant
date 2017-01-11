package maestrovs.androidofficiant.essences;

import java.util.ArrayList;

/**
 * Created by userd088 on 08.12.2016.
 */

public class Stolik {

    private int id;
    private int grpId;
    private String name;

    private int x = 0, y = 0;
    private int w = 1, h = 1;

    ArrayList<Check> checkList = new ArrayList<>();

    public Stolik(int id, int grpId, String name) {
        this.id = id;
        this.grpId = grpId;
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getId() {
        return id;
    }

    public int getGrpId() {
        return grpId;
    }

    public String getName() {
        return name;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public void addCheck(Check check) {
        checkList.add(check);
    }

    public void removeCheck(Check check) {
        checkList.remove(check);
    }

    public ArrayList<Check> getCheckList() {
        return checkList;
    }

    public void clearCheckList() {
        checkList.clear();
    }


}
