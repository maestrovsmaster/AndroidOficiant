package maestrovs.androidofficiant.photocube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import maestrovs.androidofficiant.R;

/*
 * A photo cube with 6 pictures (textures) on its 6 faces.
 */
public class Cylinder {
    private FloatBuffer vertexBuffer;  // Vertex Buffer
    private FloatBuffer texBuffer;     // Texture Coords Buffer


    private float X=2;
    private float Y=0;
    private float Z=-4;

    private float r=3.8f;
    private int numFaces = 60;



    private int[] imageFileIDs = {  // Image file IDs
           // R.drawable.cat_512_256,
          //  R.drawable.pic2,
          //  R.drawable.pic3,
         //   R.drawable.september,
         //   R.drawable.car,
          //  R.drawable.car_big
    };
    private int[] textureIDs = new int[numFaces];
    private Bitmap[] bitmap = new Bitmap[numFaces];
    private float cubeHalfSize = 1.0f;

    private Bitmap myBitmap;





    // Constructor - Set up the vertex buffer
    public Cylinder(Context context) {
        // Allocate vertex buffer. An float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * numFaces);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();

        myBitmap = BitmapFactory.decodeStream(
                context.getResources().openRawResource(imageFileIDs[3]));

        calculateVertexes(0);



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
        for (int face = 0; face < numFaces; face++) {

            double fc=face;
            double num=numFaces;
            float x1= (float) (fc/num);
            float x2 = (float) ((fc+1)/num);

            float[] texCoords0 = {
                    x1, 1.0f,  // A. left-bottom
                    x2, 1.0f,  // B. right-bottom
                    x1, 0.0f,  // C. left-top
                    x2, 0.0f   // D. right-top
            };
            texBuffer.put(texCoords0);
        }
        texBuffer.position(0);   // Rewind
    }


    public void calculateVertexes(double fi)
    {

        double nfc=numFaces;
        double alph = 360/nfc;
       // double rotateX=0;
        double w= 2*r*Math.cos(alph);

        // for(double u = 0;u<90; u=u+1) Log.d("my"," u = "+u+" cos u = "+Math.cos(Math.toRadians(u)));



        for (int face = 0; face < numFaces; face++) {




            double fi2 = fi+alph;

            Log.d("my","alp = "+alph+" rotateX = "+fi+ "  fi2 = "+fi2);

            double a1=r*Math.cos(Math.toRadians(fi));
            double b1=r*Math.sin(Math.toRadians(fi));



            double a2=r*Math.cos(Math.toRadians(fi2));
            double b2=r*Math.sin(Math.toRadians(fi2));

            float x1 = (float) (X+a1);
            float y1 = (float) (Y-w/2);
            float z1 = (float) (Z+b1);

            float x2 = (float) (X+a2);
            float y2 = (float) (Y-w/2);
            float z2 = (float) (Z+b2);

            float x3 = (float) (X+a1);
            float y3 = (float) (Y+w/2);
            float z3 = (float) (Z+b1);

            float x4 = (float) (X+a2);
            float y4 = (float) (Y+w/2);
            float z4 = (float) (Z+b2);


            float[] vertices = {
                    x1,y1,z1,
                    x2,y2,z2,
                    x3,y3,z3,
                    x4,y4,z4,
            };
            vertexBuffer.put(vertices);  // Populate

            fi+=alph;


        }
        vertexBuffer.position(0);    // Rewind
    }

    // Render the shape
    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);


        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);

        for(int i=0;i<numFaces;i++){
            float angle = i;
            gl.glPushMatrix();
            gl.glTranslatef(0, 0f, 0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[i]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i*4, 4);
            gl.glPopMatrix();

        }


        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    // Load images into 6 GL textures
    public void loadTexture(GL10 gl) {
        gl.glGenTextures(4, textureIDs, 0); // Generate texture-ID array for 6 IDs

        // Generate OpenGL texture images
        for (int face = 0; face < numFaces; face++) {

            Bitmap bm = myBitmap;//bitmap[face];
           // if(face==0) bm= myBitmap0;
            bm = scaleBitmap(bm,1024,1024);


            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[face]);
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


    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }
}