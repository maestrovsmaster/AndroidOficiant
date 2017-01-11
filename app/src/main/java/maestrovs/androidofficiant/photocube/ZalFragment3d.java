package maestrovs.androidofficiant.photocube;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import maestrovs.androidofficiant.R;
import maestrovs.androidofficiant.essences.Stolik;
import maestrovs.androidofficiant.essences.Zal;

import static maestrovs.androidofficiant.main.MainActivity.glView;
import static maestrovs.androidofficiant.main.MainActivity.screenHeight;
import static maestrovs.androidofficiant.main.MainActivity.screenWidth;
import static maestrovs.androidofficiant.main.MainActivity.zal_list;

/**
 * A placeholder fragment containing a simple view.
 */
public  class ZalFragment3d extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";



    public ZalFragment3d() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public ZalFragment3d newInstance(int sectionNumber) {
        ZalFragment3d fragment = new ZalFragment3d();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    RelativeLayout zalRelative;
    Handler handleBld = new Handler();
    Zal zal;

    View rootView;
    LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        //View
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        zalRelative = (RelativeLayout) rootView.findViewById(R.id.relativeZal);

      //  glView = new MySurface(getActivity());           // Allocate a GLSurfaceView.

        glView.setRenderer(new MyGLRenderer(getActivity())); // Use a custom renderer


        Log.d("my","start");

        zalRelative.addView(glView);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int myInt = bundle.getInt(ARG_SECTION_NUMBER);

            zal = zal_list.get(myInt);
            String name = zal.getName();
            Log.d("my", "ZAL section = " + zal.getName() + " id =" + zal.getId());
            rootView.setTag(zal);

            textView.setText(name);


            handleBld.postAtTime(new Runnable() {
                @Override
                public void run() {
                    calculateStolic();
                }
            }, 500);

        }
        return rootView;
    }

    void calculateStolic() {



        ArrayList<Stolik> stoliks = zal.getStolikList();

        float fscrx = screenWidth;
        float fscry = screenHeight;

        float minTableX=zal.getMinTableX();

        float minTableY=zal.getMinTableY();

        float maxTableX = zal.getMaxTableX();
        if (maxTableX == 0) maxTableX = 1;

        //  if(maxTableX-minTableX>0) maxTableX-=minTableX;

        float maxTableY = zal.getMaxTableY();
        if (maxTableY == 0) maxTableY = 1;

        float koefX = fscrx / (maxTableX*1.2f);

        float koefY = fscry/maxTableY;

        Log.d("my","scY = "+screenHeight+"  maxY = "+maxTableY+"  minY = "+minTableY+" koef = "+koefY);



        for (Stolik stol : stoliks) {
            Log.d("my", "stol===" + stol.getName());

            View inflatedLayout = inflater.inflate(R.layout.table_layout, null, false);
            TextView tableNameTV = (TextView) inflatedLayout.findViewById(R.id.tableNameTV);
            RelativeLayout relativeZal = (RelativeLayout) inflatedLayout.findViewById(R.id.relativeZal);
            ImageView imageView = (ImageView) inflatedLayout.findViewById(R.id.tableImage);




            String name = stol.getName();
            tableNameTV.setText(name);


            float tableX = stol.getX();
            float tableY = stol.getY();
            float tableW = 80;
            float tableH = 80;



            tableX=tableX*koefX-minTableX*koefX;
            tableY=tableY*koefY-minTableY*koefY;

            Log.d("my","originX= "+stol.getX()+"  new tableX = "+tableX);



            imageView.getLayoutParams().height = dpToPx((int)tableH);
            imageView.getLayoutParams().width = dpToPx((int)tableW);

            // ImageView imageView = (ImageView)
            zalRelative.addView(relativeZal);

            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) relativeZal.getLayoutParams();

            relativeParams.setMargins((int) tableX, (int) tableY, 0, 0);  // left, top, right, bottom
            relativeZal.setLayoutParams(relativeParams);
            //  relativeParams.height=150;


        }


    }

    public int dpToPx(int dp) {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        //Log.d("my","dp ="+dp+" dp="+px);

        return px;
    }



}