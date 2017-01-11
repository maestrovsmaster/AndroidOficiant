package maestrovs.androidofficiant.photocube;

import android.content.Context;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by userd088 on 24.10.2016.
 */

public class Planet extends Sphere {

    private float fi=0.0f;
    private float teta=0.0f;
    private float speedFi=0;
    private float speedTeta=0;

    public float r=0,g=0,b=0,a=1;


    private float rOrbity=0;
    private float satelliteSpeed =0;
    float satelliteAlph=0;







    ArrayList<Planet> satellitesList = new ArrayList<>();


    public Planet(Context context, Coord3d coord3d, float radius, int koef, int textureResId, boolean ring) {
        super(context, coord3d, radius, koef, textureResId, ring);
    }

    public Planet(Context context, Coord3d coord3d, float radius, int koef, int textureResId) {
        super(context, coord3d, radius, koef, textureResId, false);
    }

    public void addSatellite(Planet planet , float rOrbity, float satelliteAlph, float satelliteSpeed)
    {
        if(!this.equals(planet)) {
            planet.setSatelliteAlph(satelliteAlph);
            planet.setSatelliteSpeed(satelliteSpeed);
            planet.setrOrbity(rOrbity);
            calculateSatelliteCoord(planet);

            satellitesList.add(planet);
        }
        //if(ring!=null) calculateRingCoord(ring);

    }

    PlanetRing ring=null;

    public PlanetRing getRing() {
        return ring;
    }

    public void setRing(PlanetRing ring) {
        this.ring = ring;
        calculateRingCoord(this.ring);
    }

    @Override
    public void draw(GL10 gl) {
        loadColor(gl);
       // loadTexture(gl);
        super.draw(gl);

        fi+=speedFi;
        teta+=speedTeta;
        calculateVertexes(fi,teta);

        for(Planet satellite : satellitesList)
        {
            satellite.loadColor(gl);
           // loadTexture(gl);
            satellite.draw(gl);
            calculateSatelliteCoord(satellite);
            float alp = satellite.getSatelliteAlph();
            float speed = satellite.getSatelliteSpeed();
            alp+=speed;
            satellite.setSatelliteAlph(alp);
        }

        if(ring!=null)
        {
            calculateRingCoord(ring);
            //Log.d("my","planet draw ring");
            ring.calculateVertexes(0,0);
            ring.draw(gl);
        }
    }


    public int getBitmaps() {
        return 1;
    }


    private void calculateSatelliteCoord(Planet planet)
    {
        float X=getCoord().x;
        float Y=getCoord().y;
        float Z=getCoord().z;
        float alp = planet.getSatelliteAlph();
        float r= planet.getrOrbity();

        float sy=Y;
        float sx = (float) (X+ r*Math.cos(Math.toRadians(alp)));
        float sz = (float) (Z+ r*Math.sin(Math.toRadians(alp)));

        planet.setCoord(new Coord3d(sx,sy,sz));
    }

    private void calculateRingCoord(PlanetRing planet)
    {
        float X=getCoord().x;
        float Y=getCoord().y;
        float Z=getCoord().z;
        float alp = planet.getSatelliteAlph();
        float r= 0;//planet.getrOrbity();

        float sy=Y;
        float sx = (float) (X+ r*Math.cos(Math.toRadians(alp)));
        float sz = (float) (Z+ r*Math.sin(Math.toRadians(alp)));

        planet.setCoord(new Coord3d(X,Y,Z));
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
        for (Planet planet : satellitesList)
        {
            //planet.loadColor(planet.r,planet.g,planet.b, planet.a);
        }
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
