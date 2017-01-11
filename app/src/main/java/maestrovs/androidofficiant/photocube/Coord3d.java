package maestrovs.androidofficiant.photocube;

/**
 * Created by userd088 on 20.10.2016.
 */

public class Coord3d {

    public float x=0;
    public float y=0;
    public float z=0;


    public Coord3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
