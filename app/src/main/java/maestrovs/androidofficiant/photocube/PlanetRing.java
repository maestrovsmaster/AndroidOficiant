package maestrovs.androidofficiant.photocube;

import android.content.Context;
import android.graphics.Bitmap;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by userd088 on 24.10.2016.
 */

public class PlanetRing extends Ring implements Telo{

    private float fi=0.0f;
    private float teta=0.0f;
    private float speedFi=0;
    private float speedTeta=0;

    public float r=0,g=0,b=0,a=1;


    private float rOrbity=0;
    private float satelliteSpeed =0;
    float satelliteAlph=0;










    public PlanetRing(Context context,  float radius, int koef, int textureResId) {
        super(context,  radius, koef, textureResId);
    }



    @Override
    public void draw(GL10 gl) {
       // loadColor(gl);
       // loadTexture(gl);
        super.draw(gl);

        //rotateX+=speedFi;
        //rotateY+=speedTeta;
        //calculateVertexes(rotateX,rotateY);


    }

    @Override
    public Bitmap getMyBitmap(int num) {
        return super.getMyBitmap(0);
    }

    @Override
    public int getBitmaps() {
        return 1;
    }


    public float getFi() {
        return fi;
    }

    public float getTeta() {
        return teta;
    }

    public void setFi(float fi, float speedFi) {
        this.fi = fi;
        this.speedFi=speedFi;
    }

    public void setTeta(float teta, float speedTeta) {
        this.teta = teta;
        this.speedTeta=speedTeta;
    }

    public float getrOrbity() {
        return rOrbity;
    }


    public float getSatelliteSpeed() {
        return satelliteSpeed;
    }

    @Override
    public void loadColor(GL10 gl) {
        super.loadColor(gl);

    }

    private void setrOrbity(float rOrbity) {
        this.rOrbity = rOrbity;
    }

    private void setSatelliteSpeed(float satelliteSpeed) {
        this.satelliteSpeed = satelliteSpeed;
    }

    public float getSatelliteAlph() {
        return satelliteAlph;
    }

    public void setSatelliteAlph(float satelliteAlph) {
        this.satelliteAlph = satelliteAlph;
    }

    @Override
    public void setRGB(float r, float g, float b, float a) {
        super.setRGB(r, g, b, a);
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=a;
    }


}
