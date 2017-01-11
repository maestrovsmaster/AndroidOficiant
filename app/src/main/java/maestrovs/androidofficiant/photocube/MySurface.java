package maestrovs.androidofficiant.photocube;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by userd088 on 10.10.2016.
 */

public class MySurface extends  GLSurfaceView {
    public MySurface(Context context) {
        super(context);
        Log.d("my","gogogo");
    }


    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        //Log.d("my","fdhdgdf");

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
              /*  if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }*/


              /*  mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));*/

                //Log.d("my","dx = "+dx);
                mRenderer.setAngle(dx, dy);
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    MyGLRenderer mRenderer;


    public void setRenderer(MyGLRenderer renderer) {
        this.mRenderer=renderer;
        super.setRenderer(mRenderer);

    }
}
