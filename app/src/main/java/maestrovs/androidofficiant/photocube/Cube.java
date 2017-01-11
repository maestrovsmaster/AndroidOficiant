package maestrovs.androidofficiant.photocube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import maestrovs.androidofficiant.R;

/*
 * A photo cube with 6 pictures (textures) on its 6 faces.
 */
public class Cube implements  Telo{
    private FloatBuffer vertexBuffer;  // Vertex Buffer
    private FloatBuffer texBuffer;     // Texture Coords Buffer


    private float X=0;
    private float Y=0;
    private float Z=0;

   // private float radius =2f;



    private float r=1,g=1,b=1, a=1;




   // private Bitmap myBitmap;

    private int textureResId;

    int numFaces=6;

    private int[] texturesIDs = new int[numFaces];
    private int[] resoursesIDs = new int[numFaces];
    private Bitmap[] textureBitmaps = new Bitmap[numFaces];


   // private int textureId=1;

    float dx,dy,dz;


    // Constructor - Set up the vertex buffer
    public Cube(Context context, Coord3d coord3d, Coord3d size,  int[] textureResId) {
        // Allocate vertex buffer. An float has 4 bytes

        X=coord3d.x;
        Y=coord3d.y;
        Z=coord3d.z;

        dx=size.x;
        dy=size.y;
        dz=size.z;




        ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * numFaces);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();



        int resL = textureResId.length;

        for(int i=0;i<numFaces;i++) {

            int resId=0;//R.drawable.car_big;
            if(i<resL){
               resId=textureResId[i];

            }
            else{
                resId= R.drawable.cafee1;
            }
            resoursesIDs[i]=resId;

            Bitmap bm =BitmapFactory.decodeStream(
                    context.getResources().openRawResource(resId));
            textureBitmaps[i] =bm;

            texturesIDs[i]=i;
        }


        calculateVertexes();
        createVertexesFloatArray();

        // Allocate texture buffer. An float has 4 bytes. Repeat for 6 faces.
        float[] texCoords = {
                0.0f, 1.0f,  // A. left-bottom
                1.0f, 1.0f,  // B. right-bottom
                0.0f, 0.0f,  // C. left-top
                1.0f, 0.0f   // D. right-top
        };


        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4 * numFaces);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();


        int scale = 512;

        for(int i=0;i<numFaces;i++) {

            float[] texCoords0 = {
                    0, 1,  // A. left-bottom
                    1, 1,  // B. right-bottom
                    0, 0,  // C. left-top
                    1, 0   // D. right-top
            };


            texBuffer.put(texCoords0);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            textureBitmaps[i]=scaleBitmap(textureBitmaps[i],scale,scale);

        }
         texBuffer.position(0);   // Rewind
        texBuffer.rewind();

    }





    private float[] vx = new float[8];
    private float[] vy = new float[8];
    private float[] vz = new float[8];

    float u=0;


    private void calculateVertexes() {


        float xx = dx / 2;
        float yy = dy / 2;
        float zz = dz / 2;

        float a = X + xx;
        float b = Y + yy;
        float c = Z + zz;

        vx[0] = X - xx;
        vy[0] = Y - yy;
        vz[0] = Z - zz;

        vx[1] = X + xx;
        vy[1] = Y - yy;
        vz[1] = Z - zz;

        vx[2] = X - xx;
        vy[2] = Y + yy;
        vz[2] = Z - zz;

        vx[3] = X + xx;
        vy[3] = Y + yy;
        vz[3] = Z - zz;


        vx[4] = X - xx;
        vy[4] = Y - yy;
        vz[4] = Z + zz;

        vx[5] = X + xx;
        vy[5] = Y - yy;
        vz[5] = Z + zz;

        vx[6] = X - xx;
        vy[6] = Y + yy;
        vz[6] = Z + zz;

        vx[7] = X + xx;
        vy[7] = Y + yy;
        vz[7] = Z + zz;

        //createVertexesFloatArray();


    }





    public void rotateXYZ(double rx, double ry, double rz)
    {
        calculateVertexes();

        double radius = 0;//(float) Math.sqrt( (X+a)*(X+a) + (Y+b)*(Y+b) + (Z+c)*(Z*c)  );

       // Log.d("my","---------------------------------------");
        for(int i=0;i<8;i++)
        {

            double x0=vx[i];
            double y0=vy[i];
            double z0=vz[i];


            radius =  Math.sqrt(x0*x0+y0*y0+z0*z0);

            double teta0 =  Math.toDegrees(Math.acos(  y0/ radius ));
            double fi0 = 90;
            if(z0>0) fi0=Math.toDegrees( Math.atan(x0/z0 ));
            if(z0<0&&x0>=0) fi0=Math.toDegrees( Math.atan(x0/z0 ))+180;
            if(z0<0&&x0<0) fi0=Math.toDegrees( Math.atan(x0/z0 ))-180;
            if(z0==0&&x0>0) fi0=90;
            if(z0==0&&x0<0) fi0=-90;
            if(z0==0&&x0==0) fi0=0;

            fi0+=rx;
           // Log.d("my","RX = "+rx);
            float vx0 = (float) ( radius * Math.sin(Math.toRadians(teta0)) * Math.sin(Math.toRadians(fi0)));
            float vy0 = (float) ( radius * Math.cos(Math.toRadians(teta0)));
           float vz0 = (float) ( radius * Math.sin(Math.toRadians(teta0)) * Math.cos(Math.toRadians(fi0)));
            x0=vx0; y0=vy0;z0=vz0;



            double teta1 =  Math.toDegrees(Math.acos(  z0/ radius ));
            double fi1 = 90;
            if(x0>0) fi1=Math.toDegrees( Math.atan(y0/x0 ));
            if(x0<0&&y0>=0) fi1=Math.toDegrees( Math.atan(y0/x0 ))+180;
            if(x0<0&&y0<0) fi1=Math.toDegrees( Math.atan(y0/x0 ))-180;
            if(x0==0&&y0>0) fi1=90;
            if(x0==0&&y0<0) fi1=-90;
            if(x0==0&&y0==0) fi1=0;

            fi1+=ry;
            float vx1 = (float) ( radius * Math.sin(Math.toRadians(teta1)) * Math.sin(Math.toRadians(fi1)));
            float vy1 = (float) ( radius * Math.cos(Math.toRadians(teta1)));
            float vz1 = (float) ( radius * Math.sin(Math.toRadians(teta1)) * Math.cos(Math.toRadians(fi1)));
            x0=vx1; y0=vy1;z0=vz1;




           double a=y0, b=x0, c=z0;

            double teta2 =  Math.toDegrees(Math.acos(  a/ radius ));
            double fi2 = 90;
            if(b>0) fi2=Math.toDegrees( Math.atan(c/b ));
            if(b<0&&c>=0) fi2=Math.toDegrees( Math.atan(c/b ))+180;
            if(b<0&&c<0) fi2=Math.toDegrees( Math.atan(c/b ))-180;
            if(b==0&&c>0) fi2=90;
            if(b==0&&c<0) fi2=-90;
            if(b==0&&c==0) fi2=0;

            fi2+=rz;
            float vx2 = (float) ( radius * Math.sin(Math.toRadians(teta2)) * Math.sin(Math.toRadians(fi2)));
            float vy2 = (float) ( radius * Math.cos(Math.toRadians(teta2)));
            float vz2 = (float) ( radius * Math.sin(Math.toRadians(teta2)) * Math.cos(Math.toRadians(fi2)));

            x0=vx2; y0=vy2;z0=vz2;



            vx[i]= (float) x0;
            vy[i]=(float) y0;
            vz[i]=(float) z0;

            // Log.d("my","i = "+i+" x="+x0+" y="+y0+" z="+z0 );
            // Log.d("my"," ==="+i+ " x="+vx[i]+" y="+vy[i]+" z="+vz[i]);

           // Log.d("my"," ");

        }
        createVertexesFloatArray();

    }

    private void createVertexesFloatArray(){


        float[] verticesF = {
                vx[0], vy[0], vz[0],
                vx[1], vy[1], vz[1],
                vx[2], vy[2], vz[2],
                vx[3], vy[3], vz[3],
        };
        vertexBuffer.put(verticesF);



        float[] verticese = {
                vx[4], vy[4], vz[4],
                vx[5], vy[5], vz[5],
                vx[6], vy[6], vz[6],
                vx[7], vy[7], vz[7],
        };
        vertexBuffer.put(verticese);



        float[] verticesL = {
                vx[0], vy[0], vz[0],
                vx[4], vy[4], vz[4],
                vx[2], vy[2], vz[2],
                vx[6], vy[6], vz[6],
        };
        vertexBuffer.put(verticesL);


        float[] verticesR = {
                vx[1], vy[1], vz[1],
                vx[5], vy[5], vz[5],
                vx[3], vy[3], vz[3],
                vx[7], vy[7], vz[7],
        };
        vertexBuffer.put(verticesR);

        float[] verticesT = {
                vx[2], vy[2], vz[2],
                vx[3], vy[3], vz[3],
                vx[6], vy[6], vz[6],
                vx[7], vy[7], vz[7],
        };
        vertexBuffer.put(verticesT);

        float[] verticesB = {
                vx[0], vy[0], vz[0],
                vx[1], vy[1], vz[1],
                vx[4], vy[4], vz[4],
                vx[5], vy[5], vz[5],
        };
        vertexBuffer.put(verticesB);
        vertexBuffer.position(0);    // Rewind

    }




    // Render the shape
    public void draw(GL10 gl) {/////////////////////////////////////------DRAW-------------------------------////

        //rotateX++;
        //rotateY++;
        //Log.d("my","FI = "+rotateX);
       //calculateVertexes();




       // Log.d("my","draw  cube!!!");

        //rotateXYZ(0.0,0,0);
        gl.glFrontFace(GL10.GL_CCW);


        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);

        // Enable blending using premultiplied alpha.
       gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);

       // gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
      //  gl.glEnable(GL10.GL_BLEND);

        for(int i=0;i<numFaces;i++){
            float angle = i;
            gl.glPushMatrix();
            gl.glTranslatef(0, 0f, 0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesIDs[i]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i*4, 4);
            gl.glPopMatrix();
            gl.glDisable(GL10.GL_BLEND);
        }


        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glDisable(GL10.GL_BLEND);
    }

    private FloatBuffer mab, mdb, msb;

    public void loadColor(GL10 gl)
    {
        mab = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mab.put(new float[]{r,g,b,a});//цвет материала
        mab.position(0);
        mdb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mdb.put(new float[]{10.0f, 12.0f, 0.0f, 1.0f});//?
        mdb.position(0);
        msb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        msb.put(new float[]{10.0f, 0.0f, 10.0f, 1.0f});//?
        msb.position(0);

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mab);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mdb);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, msb);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 128.0f);
    }





    protected int byteSizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return data.getByteCount();
        } else {
            return data.getAllocationByteCount();
        }
    }



    private Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {

        if(bitmapToScale == null)
            return null;
        //get the original width and height
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(newWidth / width, newHeight / height);

        // recreate the new Bitmap and set it back
        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);

    }


    public Coord3d getCoord()
    {
        return new Coord3d(X,Y,Z);
    }

    public void setCoord(Coord3d coord3d)
    {
        X=coord3d.x;
        Y=coord3d.y;
        Z=coord3d.z;


    }

    public void setRGB(float r, float g, float b , float a)
    {
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=a;
    }

    public int getTextureResId() {
        return textureResId;
    }

    public Bitmap getMyBitmap(int num) {

        return textureBitmaps[num];
    }

    public int getTextureId(int num) {
        return texturesIDs[num];
    }

    @Override
    public int getBitmaps() {
        return numFaces;
    }

    public void setTextureId(int[] textureId) {
        this.texturesIDs = textureId;
    }
}