package maestrovs.androidofficiant.photocube;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Sphere_old {

    static private FloatBuffer sphereVertex;
    static private FloatBuffer sphereNormal;
    static float sphere_parms[]=new float[3];

    double mRaduis;
    double mStep;
    float mVertices[];
    private static double DEG = Math.PI/180;
    int mPoints;

    /**
     * The value of step will define the size of each facet as well as the number of facets
     *
     * @param radius
     * @param step
     */

    public Sphere_old(float radius, double step) {
        this.mRaduis = radius;
        this.mStep = step;

        //ByteBuffer bb = ByteBuffer.wrap(new byte[] { 0, 0, 0, 0, 0, 0, 0, 'a' });
       // ByteBuffer bb = ByteBuffer.wrap(new byte[40000]);
        //bb.rewind();
        //FloatBuffer fb = ((ByteBuffer) bb.rewind()).asFloatBuffer();
        //sphereVertex = FloatBuffer.allocate(40000);
       // sphereVertex =((ByteBuffer) bb.rewind()).asFloatBuffer();

       // sphereVertex.allocateDirect(40000).order(ByteOrder.nativeOrder()).asFloatBuffer();
       // sphereVertex = FloatBuffer.allocate(1000).

        int NrOfBytes = 4000;
        // in voorbeeld:
        // NrOfBytes =          2   *           2           *           3           *       4       =   48

        ByteBuffer bb = ByteBuffer.allocateDirect(NrOfBytes);       //!!!!!!!!!!
        bb.order(ByteOrder.nativeOrder());
        sphereVertex = bb.asFloatBuffer();
       // sphereVertex.put(arrayVertecesLines);
        bb.position(0);
      //  sphereVertex.position(0);


        mPoints = build();
        Log.d("ALIS CHECK!!!!!!", " COUNT:" + mPoints);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, sphereVertex);

        gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_POINTS, 0, mPoints);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    private int build() {

        /**
         * x = p * sin(phi) * cos(theta)
         * y = p * sin(phi) * sin(theta)
         * z = p * cos(phi)
         */
        double dTheta = mStep * DEG;
        double dPhi = dTheta;
        int points = 0;
        for(double phi = -(Math.PI); phi <= Math.PI; phi+=dPhi){
        //for(double phi = -(Math.PI/2); phi <= Math.PI/2; phi+=dPhi) {
            //for each stage calculating the slices
            for(double theta = 0.0; theta <= (Math.PI * 2); theta+=dTheta) {
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.cos(theta)) );
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.sin(theta)) );
                sphereVertex.put((float) (mRaduis * Math.cos(phi)) );
                points++;

            }
        }
        sphereVertex.position(0);
        return points;
    }
}