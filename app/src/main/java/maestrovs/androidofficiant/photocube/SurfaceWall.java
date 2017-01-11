package maestrovs.androidofficiant.photocube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import maestrovs.androidofficiant.R;

/*
 * A photo cube with 6 pictures (textures) on its 6 faces.
 */
public class SurfaceWall implements Telo{
    private FloatBuffer vertexBuffer;  // Vertex Buffer
    private FloatBuffer texBuffer;     // Texture Coords Buffer


    private float X=0;
    private float Y=0;
    private float Z=0;

    float dx,dy,dz;



    private int numF = 10;
    private int numFaces = 100;

    private float r=1,g=1,b=1, a=1;


   // private int[] textureIDs = new int[numFaces];

    private Bitmap myBitmap;

    private int textureResId;


    private int[] textureId=new int[numFaces];

    float lenthY =0;
    float widhtX=0;

    Context context;




    // Constructor - Set up the vertex buffer
    public SurfaceWall(Context context, Coord3d coord3d, float widhtX, float lenthZ, int koef, int textureResId) {

        this.context=context;

        // Allocate vertex buffer. An float has 4 bytes

        X=coord3d.x;
        Y=coord3d.y;
        Z=coord3d.z;

        numF = koef;
        numFaces=numF*numF;
        this.widhtX=widhtX;
        this.lenthY =lenthZ;

        textureId[0]=1;


        ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * numFaces);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();

        this.textureResId=textureResId;
        myBitmap = BitmapFactory.decodeStream(
                context.getResources().openRawResource(textureResId));


        calculateSurface();
        calculateVertexes();

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

        float maxY=numF;

        for (int y = numF; y > 0; y--) { ////!!!!!!!!!!!!!!!!!!!!!!!!!! TEXTURES
            for (int face = 0; face < numF; face++) {

                double fc = face;
                double num = numF;
                float x1 = (float) (fc / num);
                float x2 = (float) ((fc + 1) / num);

                double fcy = y;
                double numy = maxY;
                float y1 = (float) (fcy / numy);
                float y2 = (float) ((fcy + 1) / numy);

               // Log.d("my", "x1=" + x1 + "  x2="+x2+" y1=" + y1 + "  y2=" + y2);

                float[] texCoords0 = {
                        x1, y2,  // A. left-bottom
                        x2, y2,  // B. right-bottom
                        x1, y1,  // C. left-top
                        x2, y1   // D. right-top
                };


                texBuffer.put(texCoords0);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            }
        }
        texBuffer.position(0);   // Rewind
        texBuffer.rewind();

        int scale = 512;
        if(numF>=30) scale = 1024;
        if(numF>=60) scale = 2048;

        myBitmap= scaleBitmap(myBitmap,scale,scale);
    }

    ArrayList<ArrayList<Float>> surfList = new ArrayList<>();

    private void calculateSurface()
    {


        GetPixelColor getPixelColor = new GetPixelColor(context, R.drawable.cafee1);
       // getPixelColor.getApproximatesList(0.1f);


        Random random = new Random();




        for(int iz=0;iz<numF+1;iz++)
        {

            float my =0;
            int rand = random.nextInt(3);
            if(rand==1) {
                int rnd3 = random.nextInt(4);
                float frnd3 = rnd3;
                my = frnd3 / 10;
            }

            ArrayList<Float> xlist = new ArrayList<>();
            for(int ix=0;ix<numF+1;ix++){

                float y=0;

                 rand = random.nextInt(20);
                if(rand==1)
                {

                int rnd3 = random.nextInt(100);
                float frnd3=rnd3;
                y=frnd3/100;
                }
                y+=my;

                xlist.add(y);


            }
            surfList.add(xlist);
            Log.d("my",xlist.toString());
        }

    }




    private float[] vx = new float[8];
    private float[] vy = new float[8];
    private float[] vz = new float[8];

    ArrayList<Coord3d> coordList = new ArrayList<>();

    private void calculateVertexes() {



        float num=numF;
        float dx=widhtX/num;
        float dy= lenthY /num;

        ArrayList<Float> ytempVert3= new ArrayList<>();
        ArrayList<Float> ytempVert4= new ArrayList<>();
       for(int iy=0;iy<numF;iy++)
       {



           for(int ix=0;ix<numF;ix++){

               float x = ix*dx;
               float y = iy*dy;


              // float y1=Y+surfList.get(iy).get(ix);
               //float y2=Y+surfList.get(iy).get(ix+1);


               float x1=X+x;

               float y1=Y+ y;

               float x2=X+x+dx;

               float y2=Y+ y;

               //float y3=Y+surfList.get(iy+1).get(ix);
               //float y4=Y+surfList.get(iy+1).get(ix+1);


               float z = Z;

               float x3=X+x;

               float y3=Y+ y +dy;

               float x4=X+x+dx;

               float y4=Y+ y +dy;

               float[] vertices = {
                       x1, y1, z,
                       x2, y2, z,
                       x3, y3, z,
                       x4, y4, z,
               };
               vertexBuffer.put(vertices);

              /* Log.d("my"," x1="+x1+" y1="+y1+" z1="+z1);
               Log.d("my"," x2="+x2+" y2="+y2+" z2="+z2);
               Log.d("my"," x3="+x3+" y3="+y3+" z3="+z3);
               Log.d("my"," x4="+x4+" y4="+y4+" z4="+z4);

               Log.d("my","   ");*/

           }
           ytempVert3.clear();
           ytempVert4.clear();
          // Log.d("my","-------------------------");
       }

        vertexBuffer.position(0);
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


    }















    // Render the shape
    public void draw(GL10 gl) {

       // Log.d("my","draw  sphere!!!");
        gl.glFrontFace(GL10.GL_CCW);


        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);

        for(int i=0;i<numFaces;i++){
            float angle = i;
            gl.glPushMatrix();
            gl.glTranslatef(0, 0f, 0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i*4, 4);
            gl.glPopMatrix();

        }


        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
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





    // Load images into 6 GL textures
    public void loadTexture(GL10 gl) {
       // gl.glGenTextures(4, textureIDs, 0); // Generate texture-ID array for 6 IDs

        // Generate OpenGL texture images
       // for (int face = 0; face < numFaces; face++)
        {

            Bitmap bm = myBitmap;

            gl.glBindTexture(GL10.GL_TEXTURE_2D,1/* textureIDs[face]*/);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            // Build Texture from loaded bitmap for the currently-bind texture ID
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
           // bitmap[face].recycle();
        }
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


   /* public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());


        return output;
    }*/

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
        return myBitmap;
    }

    public int getTextureId(int num) {
        return textureId[num];
    }



    @Override
    public int getBitmaps() {
        return 1;
    }

    public void setTextureId(int[] textureId) {
        this.textureId = textureId;
    }
}