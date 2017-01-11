package maestrovs.androidofficiant.photocube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLUtils;
import android.os.Build;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/*
 * A photo cube with 6 pictures (textures) on its 6 faces.
 */
public class Sphere implements Telo{
    private FloatBuffer vertexBuffer;  // Vertex Buffer
    private FloatBuffer texBuffer;     // Texture Coords Buffer


    private float X=0;
    private float Y=0;
    private float Z=-4;

    private float radius =2f;

    private int numF = 10;
    private int numFaces = 100;

    private float r=1,g=1,b=1, a=1;


   // private int[] textureIDs = new int[numFaces];

    private Bitmap myBitmap;

    private int textureResId;


    private int[] textureId=new int[numFaces];




    // Constructor - Set up the vertex buffer
    public Sphere(Context context, Coord3d coord3d, float radius, int koef, int textureResId, boolean ring) {
        // Allocate vertex buffer. An float has 4 bytes

        X=coord3d.x;
        Y=coord3d.y;
        Z=coord3d.z;
        this.radius =radius;
        numF = koef;
        numFaces=numF*numF;
        this.ring=ring;

        textureId[0]=1;


        ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * numFaces);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();

        this.textureResId=textureResId;
        myBitmap = BitmapFactory.decodeStream(
                context.getResources().openRawResource(textureResId));


        calculateVertexes(0,0);

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

        float maxY=numF/2;

        for (int y = 0; y < maxY; y++) { ////!!!!!!!!!!!!!!!!!!!!!!!!!! TEXTURES
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


    private boolean ring = false;

    public void setRingType()
    {
        ring =true;
    }

    public void calculateVertexes(double fi,double teta) {

        double nfc = numFaces;
        double alph = 360 / numF;
       // Log.d("my", "sphere X = " + X + " Y = " + Y + "  Z = " + Z);
        // double w = 2 * radius * Math.cos(alph);

        //Log.d("my","numFaces = "+numFaces);




            for (int c = 0; c < numF / 2; c++) {


                for (int d = 0; d < numF; d++) {


                    double fi2 = fi + alph;
                    double teta2 = teta + alph;

                    // Log.d("my", "alp = " + alph + " rotateX = " + rotateX + "  fi2 = " + fi2);


                    float x1 = (float) (X + radius * Math.sin(Math.toRadians(teta2)) * Math.sin(Math.toRadians(fi)));
                    float y1 = (float) (Y - radius * Math.cos(Math.toRadians(teta2)));
                    float z1 = (float) (Z + radius * Math.sin(Math.toRadians(teta2)) * Math.cos(Math.toRadians(fi)));

                    float x2 = (float) (X + radius * Math.sin(Math.toRadians(teta2)) * Math.sin(Math.toRadians(fi2)));
                    float y2 = (float) (Y - radius * Math.cos(Math.toRadians(teta2)));
                    float z2 = (float) (Z + radius * Math.sin(Math.toRadians(teta2)) * Math.cos(Math.toRadians(fi2)));

                    float x3 = (float) (X + radius * Math.sin(Math.toRadians(teta)) * Math.sin(Math.toRadians(fi)));
                    float y3 = (float) (Y - radius * Math.cos(Math.toRadians(teta)));
                    float z3 = (float) (Z + radius * Math.sin(Math.toRadians(teta)) * Math.cos(Math.toRadians(fi)));

                    float x4 = (float) (X + radius * Math.sin(Math.toRadians(teta)) * Math.sin(Math.toRadians(fi2)));
                    float y4 = (float) (Y - radius * Math.cos(Math.toRadians(teta)));
                    float z4 = (float) (Z + radius * Math.sin(Math.toRadians(teta)) * Math.cos(Math.toRadians(fi2)));

                    // Log.d("my", "x== = " + x1 + " y = " + y1 + "  z = " + z1);

                    //if(x1==x2&&y1==y2&&z1==z2) Log.d("my","null vertex");


                    float[] vertices = {
                            x1, y1, z1,
                            x2, y2, z2,
                            x3, y3, z3,
                            x4, y4, z4,
                    };
                    vertexBuffer.put(vertices);  // Populate

                    fi += alph;
                }
                teta += alph;
            }



        vertexBuffer.position(0);    // Rewind
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
    public void rotateXYZ(double rx, double ry, double rz) {

    }

    @Override
    public int getBitmaps() {
        return 1;
    }

    public void setTextureId(int[] textureId) {
        this.textureId = textureId;
    }
}