package maestrovs.androidofficiant.photocube;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LightRenderer implements Renderer {
    private int rows;
    private int cols;

    private FloatBuffer vb;
    private FloatBuffer mab, mdb, msb;

    private FloatBuffer lPos;
    private FloatBuffer lab, ldb, lsb;


    Cylinder cylinder;

    public LightRenderer(Context context, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        cylinder = new Cylinder(context);

        vb = ByteBuffer.allocateDirect(4 * (rows - 1) * cols * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        for(int row = 0; row < (rows - 1); ++row)
            for(int col = 0; col < cols; ++col) {
                vb.put(col * 320.0f / (cols - 1)); vb.put((row + 1) * 480.0f / (rows - 1));
                vb.put(col * 320.0f / (cols - 1)); vb.put(row * 480.0f / (rows - 1));
            }
        vb.position(0);
//      vb = ByteBuffer.allocateDirect(24 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
//      vb.put(new float[]{
//          0.0f, 50.0f,
//          0.0f, 0.0f,
//          50.0f, 50.0f,
//          50.0f, 0.0f,
//          100.0f, 50.0f,
//          100.0f, 0.0f,
//          0.0f, 100.0f,
//          0.0f, 50.0f,
//          50.0f, 100.0f,
//          50.0f, 50.0f,
//          100.0f, 100.0f,
//          100.0f, 50.0f});
//      vb.position(0);
        mab = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mab.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});//цвет материала
        mab.position(0);
        mdb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mdb.put(new float[]{10.0f, 12.0f, 0.0f, 1.0f});//?
        mdb.position(0);
        msb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        msb.put(new float[]{10.0f, 0.0f, 10.0f, 1.0f});//?
        msb.position(0);

        lPos = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        lPos.put(new float[]{+180.0f, 90.0f, 50.0f, 1.0f});//положение светила
        lPos.position(0);
        lab = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        // THIS WORKS..
        lab.put(new float[]{0.1f, 0.0f, 0.0f, 1.0f});
        lab.position(0);
        ldb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        // SO DOES THIS..
        ldb.put(new float[]{0.0f, 0.1f, 0.0f, 1.0f});
        ldb.position(0);
        // BUT NOT THIS
        lsb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        lsb.put(new float[]{0.0f, 0.0f, 1.0f, 1.0f});
        lsb.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mab);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mdb);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, msb);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 128.0f);

        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lPos);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lab);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, ldb);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lsb);
        gl.glEnable(GL10.GL_LIGHT0);

        gl.glDisable(GL10.GL_TEXTURE);
        gl.glFrontFace(GL10.GL_CCW);

        cylinder.loadTexture(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
//      gl.glOrthof(0.0f, width, 0.0f, height, -1.0f, 1.0f);
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 999.0f, 1001.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl.glLoadIdentity();
        gl.glTranslatef(-160.0f, -240.0f, -1000.0f);

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vb);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        for(int row = 0; row < rows - 1; ++row) {
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 2 * row * cols, 2 * cols);
//          gl.glDrawArrays(GL10.GL_LINE_STRIP, 2 * row * cols, 2 * cols);
        }

       // cylinder.draw(gl);
//      gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 6);
//      gl.glDrawArrays(GL10.GL_LINE_STRIP, 6, 6);
    }

}