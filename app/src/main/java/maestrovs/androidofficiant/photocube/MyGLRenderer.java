package maestrovs.androidofficiant.photocube;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import maestrovs.androidofficiant.R;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private PhotoCube cube;     // (NEW)



    private static int dalnostZ = 30;


   // Sphere mSphere;
   // Cylinder cylinder;

   ArrayList<Telo> planets = new ArrayList();
    private int[] textureIDs = new int[100];

    float step0=1.5f;
    float step1=2f;
    float step3=2.5f;
    private float currentOrbit=0;
    private float currentState=0;
    private float stepState=40;


    Planet space;

    Planet sun;
    Planet mercury;
    Planet venus;
    Planet earth;
    Planet mars;
    Planet jupiter;
    Planet saturn;
    Planet uranus;
    Planet neptune;
    Planet pluto;
    Planet charon;

    Context context;

    private FloatBuffer lPos;
    private FloatBuffer lab, ldb, lsb;


    // Constructor
    public MyGLRenderer(Context context) {

    this.context=context;







        float cx=0;
        float cy=-1;

        addStolik(0,-1);

      //  addStolik(-1,-1);

     //   addStolik(1.5f,-2);

      //  addStolik(0,3);

     //   addStolik(3,1);




       /* Cube kvadrat2 = new Cube(context, new Coord3d(2f,-2f,-0f),new Coord3d(0.3f,0.3f,0.3f),resId);
        kvadrat2.setRGB(3,1,1,1);
        setTextureId(kvadrat2);
        planets.add(kvadrat2);*/

      /* Cube kvadrat3 = new Cube(context, new Coord3d(-1.5f,-2.5f,-0f),new Coord3d(0.3f,0.3f,0.3f),resId);
        //kvadrat2.setRGB(3,1,1,1);
        setTextureId(kvadrat3);
        planets.add(kvadrat3);*/


       /* Surface surface = new Surface(context,new Coord3d(-10f,-2f,-20f),40,40,80,R.drawable.floor1);
        surface.setRGB(3,1,1,1);
        setTextureId(surface);
        planets.add(surface);*/

        SurfaceWall surfaceWall = new SurfaceWall(context,new Coord3d(-3f,-3f,-0.0f),6,6,80,R.drawable.floor1);
        surfaceWall.setRGB(3,1,1,1);
        setTextureId(surfaceWall);
        planets.add(surfaceWall);

       /* sun = new Planet(context, new Coord3d(0,1,-0.8f),30.0f,36,R.drawable.cafee1);
        sun.setFi(12,0.2f);
        sun.setTeta(180,0);
        sun.setRGB(0.5f,1,1,1);
        setTextureId(sun);
        planets.add(sun);*/





        lPos = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        lPos.put(new float[]{0.0f, 0.0f, -50f, 1.0f});//положение светила
        lPos.position(0);
        lab = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        // THIS WORKS..
        lab.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        lab.position(0);
        ldb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        // SO DOES THIS..
        ldb.put(new float[]{0.5f, 0.5f, 1.0f, 1.0f});
        ldb.position(0);
        // BUT NOT THIS
        lsb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        lsb.put(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
        lsb.position(0);

        // Blending (NEW)
        boolean blendingEnabled = false;  // Is blending on? (NEW)

    }


    private void addStolik(float cx, float cy)
    {
        float cz=0.3f;

        int[] resIdn = {
                R.drawable.derevo,//niz
                R.drawable.derevo,//verh
                R.drawable.derevo,
                R.drawable.derevo,
                R.drawable.derevo,//zad
                R.drawable.derevo//pered
        } ;

        int[] resId = {
                R.drawable.transparent,//niz
                R.drawable.table_white_cofee,//verh
                R.drawable.derevo,
                R.drawable.derevo,
                R.drawable.derevo,//zad
                R.drawable.derevo//pered
        } ;


        float wx=0.3f;
        float wy=0.3f;

        float wh=0.03f;

        Cube kvadrat = new Cube(context, new Coord3d(cx,cy,cz),new Coord3d(wx,wy,wh),resId);
        kvadrat.setRGB(3,1,1,1);
        setTextureId(kvadrat);
        planets.add(kvadrat);

        Cube n1 = new Cube(context, new Coord3d(cx-wx/2+wh/2,cy-wy/2+wh/2,cz/2),new Coord3d(wh,wh,cz),resIdn);
        setTextureId(n1);
        planets.add(n1);

        Cube n2 = new Cube(context, new Coord3d(cx+wx/2-wh/2,cy-wy/2+wh/2,cz/2),new Coord3d(wh,wh,cz),resIdn);
        setTextureId(n2);
        planets.add(n2);

        Cube n3 = new Cube(context, new Coord3d(cx-wx/2+wh/2,cy+wy/2-wh/2,cz/2),new Coord3d(wh,wh,cz),resIdn);
        setTextureId(n3);
        planets.add(n3);

        Cube n4 = new Cube(context, new Coord3d(cx+wx/2-wh/2,cy+wy/2-wh/2,cz/2),new Coord3d(wh,wh,cz),resIdn);
        setTextureId(n4);
        planets.add(n4);
    }



    int textureId=0;

    private void setTextureId(Telo telo)
    {

       // planet.setTextureId(textureId);
        int sdvig= telo.getBitmaps();
        int[] textMass=new int[sdvig];
        for(int i=0;i<sdvig;i++)
        {
            textMass[i]=textureId+i;
        }
        telo.setTextureId(textMass);

        textureId+=sdvig;
    }

    // Call back when the surface is first created or re-created.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

       // gl.glViewport(0, 0, 1, 1);
       // gl.glMatrixMode(GL10.GL_PROJECTION);
       // gl.glLoadIdentity();

        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

        // Setup Texture, each time the surface is created (NEW)
       // cube.loadTexture(gl);             // Load images into textures (NEW)
      //  cylinder.loadTexture(gl);
        //sun.loadTexture(gl);

        for( int f = 0; f<planets.size();f++)
        {
            //if(planets.get(f) instanceof  Planet)

                Telo planet =  planets.get(f);




              int imagesSize = planet.getBitmaps();

            for(int i=0;i<imagesSize;i++) {

                Bitmap bm = planet.getMyBitmap(i);


                if (bm != null/*&&!(planet instanceof PlanetRing)*/) {


                    Log.d("my", "bitmap ok");

                    gl.glBindTexture(GL10.GL_TEXTURE_2D, planet.getTextureId(i));
                    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
                    // Build Texture from loaded bitmap for the currently-bind texture ID
                    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);

                    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
                }
            }


        }


        gl.glFrontFace(GL10.GL_CCW);




        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lPos);

        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lab);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, ldb);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lsb);
        gl.glEnable(GL10.GL_LIGHT0);



        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture (NEW)


        // Setup Blending (NEW)
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);           // Full brightness, 50% alpha (NEW)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE); // Select blending function (NEW)


    }

    // Call back after onSurfaceCreated() or whenever the window's size changes
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;

        Log.d("my","?surf changed");

        //aspect=-3;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);



        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, dalnostZ, aspect, 0.1f, 200.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset

        // You OpenGL|ES display re-sizing code here
        // ......
    }

    float dZ=-0.0f;


    float angleRotate = 10;
    float rotateX=0.05f;
    float rotateY=0;
    float rotateZ=0;

    float dRotate=0.01f;


   /* double rotateX=0;
    double dfi=1.2;*/

    double drx=0, dry=0, drz=0;

    // Call back to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear color and depth buffers
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // ----- Render the Cube -----
        gl.glLoadIdentity();

       // gl.glEnable(GL10.GL_BLEND);       // Turn blending on (NEW)
       // gl.glDisable(GL10.GL_DEPTH_TEST);


        // Reset the model-view matrix
        gl.glTranslatef(X, -Y, -5.0f);   // Translate into the screen
        Z+=dZ;


        angleRotate+=dRotate;

       // gl.glRotatef(angleRotate, rotateX, rotateY, rotateZ); // Rotate
        //  gl.glRotatef(angleCube, 0.2f, 0.3f, 0.15f);

          gl.glRotatef(60, -0.2f, 0.05f, 0.01f);


        drx=0;
        dry=0;
        drz=0;


        for(Telo telo: planets) {
           // Log.d("my","draw all");
           // telo.rotateXYZ(drx,dry,drz);
            telo.draw(gl);
        }




        Z+= speedZ;
        speedZ = speedZ /koefdiv;

        if(Math.abs(speedZ)<=koefZ){
            speedZ =0;
        }


        X+= speedX;
        speedX = speedX /koefdiv;

        if(Math.abs(speedX)<=koefX){
            speedX =0;
        }

        Y+= speedY;
        speedY = speedY /koefdiv;

        if(Math.abs(speedY)<=koefY){
            speedY =0;
        }


    }

    float koefdiv=1.9f;

    float Y=0;
    float koefY=0.02f;
    float speedY=0;

    float Z=-10;
    float X=0;

    float speedZ = 0.0f;
    float koefZ=0.2f;


    float speedX=0;
    float koefX=0.02f;




    public void setAngle(float dx, float dy) {

        speedZ = koefZ*dy;

        speedY = koefY*dy;

        speedX = koefX*dx;



    }



}