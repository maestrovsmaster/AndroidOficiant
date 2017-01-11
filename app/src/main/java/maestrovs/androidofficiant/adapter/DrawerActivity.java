package maestrovs.androidofficiant.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

@Deprecated
public class DrawerActivity extends DrawerLayout {
    public DrawerActivity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private final Handler mDrawerHandler = new Handler();

    private void scheduleLaunchAndCloseDrawer(final View v) {
        // Clears any previously posted runnables, for double clicks
        mDrawerHandler.removeCallbacksAndMessages(null); 

        mDrawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // onDrawerItemSelection(v);
            }
        }, 250);
        // The millisecond delay is arbitrary and was arrived at through trial and error

       // mDrawerLayout.closeDrawer();
    }
}