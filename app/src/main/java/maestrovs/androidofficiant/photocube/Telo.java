package maestrovs.androidofficiant.photocube;

import android.graphics.Bitmap;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by userd088 on 27.10.2016.
 */

public interface Telo {

    void draw(GL10 gl);

    Bitmap getMyBitmap(int num);

    void setTextureId(int[] textureId);

    int getTextureId(int num);

    void rotateXYZ(double rx, double ry, double rz);

    int getBitmaps();
}
