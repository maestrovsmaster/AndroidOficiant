package maestrovs.androidofficiant.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import maestrovs.androidofficiant.R;
import maestrovs.androidofficiant.essences.Check;
import maestrovs.androidofficiant.essences.Stolik;
import maestrovs.androidofficiant.essences.User;
import maestrovs.androidofficiant.essences.Zal;
import maestrovs.androidofficiant.photocube.MySurface;
import maestrovs.androidofficiant.requests.Request;
import maestrovs.androidofficiant.requests.RequestDBVersion;
import maestrovs.androidofficiant.requests.RequestShiftCashId;
import maestrovs.androidofficiant.requests.RequestSignIn;
import maestrovs.androidofficiant.requests.RequestTablesHalls;
import maestrovs.androidofficiant.requests.RequestWSOptions;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MainActivity";


    public static  MySurface glView;

    private Menu menu;

    private String wsName = "";
    private String dbversion = "";


    TextView userInfo;
    static User currentUser = null;

    // public static  ArrayList<Zal> zal_set = new ArrayList<>();
    public static HashMap<Integer, Zal> zal_set = new HashMap<>();
    public static List<Zal> zal_list = new ArrayList<>();


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ImageView hallImage;
    android.support.design.widget.FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getScreenResolution();
        Log.d("my", "screen size = " + screenWidth + " - " + screenHeight);

       // RelativeLayout surfRel=(RelativeLayout) findViewById(R.id.surfRel);

       // glView = new MySurface(this);           // Allocate a GLSurfaceView.

       // glView.setRenderer(new MyGLRenderer(this)); // Use a custom renderer
        // glView.setRenderer(new LightRenderer(this,40,40));

        Log.d("my","start");

        //surfRel.addView(glView);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);


       mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        mViewPager.setAdapter(mSectionsPagerAdapter);


        userInfo = (TextView) toolbar.findViewById(R.id.userInfo);

        hallImage = (ImageView) findViewById(R.id.imageView);


        addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this, CheckDetailsActivity.class);

                Bundle arg = new Bundle();
                arg.putInt("USER_ID", currentUser.getEmplId());
                arg.putString("USER_NAME", currentUser.getName());
                arg.putInt("SHIFT_ID", currentShiftCashId);
                arg.putString("TABLE", "");
                intent.putExtras(arg);
                startActivity(intent);
            }
        });


    }


   /* @Override
    protected void onResume() {
        super.onResume();
        if (pinCode.length() == 0) updateStatus();
    }*/

    private void updateStatus() {
        startH = new Handler();
        startH.postAtTime(new Runnable() {
            @Override
            public void run() {
                LoadStatusThread loadStatusThread = new LoadStatusThread();
                loadStatusThread.start();
            }
        }, 1000);
    }


    Handler startH = new Handler();

    class LoadStatusThread extends Thread {

        @Override
        public void run() {
            RequestDBVersion requestDBVersion = new RequestDBVersion();
            JSONObject verObj = requestDBVersion.getDBVersion();
            Log.d(TAG, " ver = " + verObj.toString());


            RequestWSOptions requestWSOptions = new RequestWSOptions();
            JSONObject wsObj = requestWSOptions.getWSOptions();
            Log.d(TAG, " wsopt = " + wsObj.toString());

            try {
                dbversion = verObj.getString("ver");
            } catch (Exception e) {
            }

            try {
                wsName = wsObj.getString("wsname");
            } catch (Exception e) {
            }


            h.post(new Runnable() {
                @Override
                public void run() {


                    if (dbversion.length() > 0 && wsName.length() > 0) {
                        updateConnectStatus();
                        // showPinCodeDialog();//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        pinCode = "7";
                        GetUserThread getUserThread = new GetUserThread();
                        getUserThread.start();
                    } else {
                        updateConnectDialog();
                    }
                }
            });
        }
    }

    ;

    private void updateConnectStatus() {
        if (menu == null) Log.d(TAG, "menu === null");
        else {
            Log.d(TAG, "menu === ok");
        }
        MenuItem statusMenuItem = menu.findItem(R.id.action_status);

        if (statusMenuItem == null) Log.d(TAG, "menu item null");
        else {
            Log.d(TAG, "menu item ok");
            Log.d(TAG, "===ws=" + wsName + "  ver=" + dbversion);
            if (dbversion.length() > 0 && wsName.length() > 0) {
                statusMenuItem.setTitle("v. " + dbversion + ", " + wsName);
            }
        }
    }

    private void updateConnectDialog() {
        if (MainActivity.this != null) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Не удалось подключиться к серверу. Проверьте настройки подключения. ");
            adb.setIcon(android.R.drawable.ic_dialog_alert);
            adb.setPositiveButton("Повтор", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    updateStatus();
                }
            });


            adb.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });
            adb.show();
        }
    }

    String pinCode = "";
    String userId = "";
    String userName = "";

    static int currentShiftCashId = -1;

    private void showPinCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите PIN сотрудника:");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pinCode = input.getText().toString();
                GetUserThread getUserThread = new GetUserThread();
                getUserThread.start();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //finish();
            }
        });

        builder.show();
    }

    Handler getUserH = new Handler();

    class GetUserThread extends Thread {
        @Override
        public void run() {


            RequestSignIn requestSignIn = new RequestSignIn();
            JSONObject userInfoJSON = requestSignIn.signIn(pinCode);
            if (userInfoJSON == null) {
                Log.d(TAG, "user json = null");

            } else {
                Log.d(TAG, " user info: " + userInfoJSON.toString());

                try {
                    JSONArray data = userInfoJSON.getJSONArray("data");
                    if (data.length() == 0) {
                        getUserH.post(new Runnable() {
                            @Override
                            public void run() {
                                showNoUserDialog();
                            }
                        });

                    } else {
                        JSONObject jsu = data.getJSONObject(0);
                        int id = jsu.getInt("ID");
                        String name = jsu.getString("CODE_NAME");
                        String job = "";
                        try {
                            job = jsu.getString("JOB");
                        } catch (Exception e) {

                        }


                        currentUser = new User(id, name, job);
                        getUserH.post(new Runnable() {
                            @Override
                            public void run() {
                                updateUserInfo();
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showNoUserDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Пользователь не найден. ");
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setNegativeButton("Ок", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        adb.show();
    }


    private void updateUserInfo() {
        String text = "Сотрудник: " + currentUser.getJob() + " " + currentUser.getName();
        userInfo.setText(text);

           CheckShiftCashId checkShiftCashId = new CheckShiftCashId();/////////////////////////////!!!!!!!!!!!!!!!!!!///////////////////////////////////////////////////////////
          checkShiftCashId.start();

        UpdateChekiNaStolah updateChekiNaStolah = new UpdateChekiNaStolah();
        updateChekiNaStolah.start();


    }

    Handler checkCashHandler = new Handler();

    class CheckShiftCashId extends Thread {
        @Override
        public void run() {
            RequestShiftCashId requestShiftCashId = new RequestShiftCashId();
            JSONObject cashJS = requestShiftCashId.getCashShiftId();
            try {
                JSONArray arr = cashJS.getJSONArray("data");
                if (arr.length() > 0) {
                    JSONObject obj = arr.getJSONObject(0);
                    currentShiftCashId = obj.getInt("ID");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "shift: " + cashJS.toString());
            checkCashHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateShiftStatus();
                }
            });
        }
    }

    Handler readyTablesH = new Handler();

    class UpdateZalsThread extends Thread {
        @Override
        public void run() {
            String query = "select  tg.id,  tg.name from dic_tables_grp tg  where tg.id>0";

            Request request = new Request();
            JSONObject zalObjs = request.getJSON(query);
            Log.d("my", "we get zal list = " + zalObjs.toString());
            if (zalObjs != null) {
                try {
                    JSONArray data = zalObjs.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        int id = obj.getInt("ID");
                        String name = obj.getString("NAME");
                        Log.d("my", ">>zal>>" + name);
                        Zal zal = new Zal(id, name);
                        zal_set.put(id, zal);

                    }
                    UpdateTablesThread updateTablesThread = new UpdateTablesThread();
                    updateTablesThread.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else { //net stolov, idem dalshe
                CheckShiftCashId checkShiftCashId = new CheckShiftCashId();/////////////////////////////
                checkShiftCashId.start();
            }
        }
    }

    class UpdateTablesThread extends Thread {
        @Override
        public void run() {
            String query = "select * from dic_tables";

            Request request = new Request();
            JSONObject zalObjs = request.getJSON(query);
            Log.d("my", "we get zal list = " + zalObjs.toString());
            if (zalObjs != null) {
                try {
                    JSONArray data = zalObjs.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);

                        int ID = obj.getInt("ID");
                        int GRP_ID = obj.getInt("GRP_ID");
                        String NAME = obj.getString("NAME");

                        int T_LEFT = obj.getInt("T_LEFT");
                        int T_TOP = obj.getInt("T_TOP");
                        int T_HEIGHT = obj.getInt("T_HEIGHT");
                        int T_WIDTH = obj.getInt("T_WIDTH");
                        Log.d("my", ">>stol>>" + NAME);
                        Stolik stolik = new Stolik(ID, GRP_ID, NAME);
                        stolik.setLocation(T_LEFT, T_TOP);
                        stolik.setSize(T_HEIGHT, T_WIDTH);

                        Zal currentZal = zal_set.get(GRP_ID);
                        if (currentZal != null) currentZal.addStol(stolik);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                readyTablesH.post(new Runnable() {
                    @Override
                    public void run() {
                        updateZalStoly();
                    }
                });

            }
        }
    }

    private void updateZalStoly() {
        Log.d("my", "update karty zala");


        zal_list = new ArrayList<Zal>(zal_set.values());
        Log.d("my", "list z=" + zal_list);
        mSectionsPagerAdapter.notifyDataSetChanged();//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        mViewPager.setCurrentItem(0);

        CheckShiftCashId checkShiftCashId = new CheckShiftCashId();/////////////////////////////
        checkShiftCashId.start();



    }


    public static ArrayList<Check> listChekovNaStolah = new ArrayList<>();

    Handler updateChekiNaStH = new Handler();

    class UpdateChekiNaStolah extends  Thread
    {
        @Override
        public void run() {

            listChekovNaStolah.clear();

            String updCheckiString = "select jc.id, jc.num, jc.table_id ,  jc.employee_id, jc.client_id, jc.preprint_cnt, " +
                    " (select sum(jcd.sum_) from jor_checks_dt jcd where jcd.hd_id=jc.id  ) as CASH_SUM " +
                    " from jor_checks jc where jc.doc_state=0";

            Request request = new Request();
            JSONObject jsonObj = request.getJSON(updCheckiString);
            Log.d("my","update tables checks json = "+jsonObj.toString());
            try {
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                if(jsonArray!=null){
                    Log.d("my","js check data ok");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    int id = obj.getInt("ID");
                    String num = obj.getString("NUM");
                    double cash_sum = obj.getDouble("CASH_SUM");
                    int employee_id = obj.getInt("EMPLOYEE_ID");
                    int client_id = obj.getInt("CLIENT_ID");


                    int preprint_cnt = obj.getInt("PREPRINT_CNT");

                    Check check = new Check(id,num);
                    check.setTotalSum(cash_sum);
                    check.setClientId(client_id);

                    check.setPreprintCnt(preprint_cnt);
                    check.setEmployeeId(employee_id);

                    try{
                        int table_id = obj.getInt("TABLE_ID");
                        check.setTableId(table_id);
                    }catch (Exception e){

                    }


                    listChekovNaStolah.add(check);
                    Log.d("my","-----------we add check + "+listChekovNaStolah.size());
                }}else {
                    Log.d("my","js check data null");
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("my","js check data except!!!"+e.toString());
            }


            updateChekiNaStH.post(new Runnable() {
                @Override
                public void run() {
                    updateChekiStoliki();
                }
            });

        }
    }


    private void updateChekiStoliki()
    {
        UpdateZalsThread updateZalsThread = new UpdateZalsThread();
        updateZalsThread.start();
    }


    private void updateShiftStatus() {
        if (currentUser != null && currentShiftCashId >= 0) {
            addButton.setVisibility(View.VISIBLE);
        }


    }

    Handler buildTablesHandler = new Handler();


    Handler h = new Handler();

    Handler hHalls = new Handler();

    JSONObject jsnhall = null;

    class LoadHalls extends Thread {
        @Override
        public void run() {

            RequestTablesHalls requestTablesHalls = new RequestTablesHalls();
            jsnhall = requestTablesHalls.getTableHalls();
            // Log.d(TAG," js th = "+jsnhall.toString());

            hHalls.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONArray data = jsnhall.getJSONArray("data");
                        int len = data.length();
                        Log.d(TAG, "data hall size = " + len);
                        JSONObject js2 = data.getJSONObject(1);
                        String strmap = js2.getString("NAME");
                        String key = UUID.randomUUID().toString().replaceAll("-", "");


                        // String tag1 = Crypto.encrypt(strmap, key);
                        // Log.d(TAG,"tag1"+tag1);
                        // String tag2=Crypto.decrypt(tag1,key);
                        // Log.d(TAG,"tag2 "+tag2);
                        //Bitmap bm = convertBase64StringToBitmap(tag1);

                        String[] safe = strmap.split("=");
                        byte[] recvpicbyte = Base64.decode(safe[0], Base64.NO_PADDING);
                        //byte [] encodeByte=Base64.decode(strmap.getBytes(),Base64.DEFAULT);
                        Bitmap bm = BitmapFactory.decodeByteArray(recvpicbyte, 0, recvpicbyte.length);
                        if (bm != null) {
                            Log.d(TAG, "bm!=null");
                            hallImage.setImageBitmap(bm);
                        } else {
                            Log.d(TAG, " bitmap null");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "222 crypt " + e.toString());
                    }
                }
            });

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Log.d(TAG, "settings");
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_exit) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ZalFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public ZalFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ZalFragment newInstance(int sectionNumber) {
            ZalFragment fragment = new ZalFragment();
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

            if (ZalFragment.this == null) Log.d("my", "zal fragm =null");
            else Log.d("my", "zal fragm ok");
            // if(rootView==null) Log.d("my","rootview=null");


            //  View inflatedLayout = inflater.inflate(R.layout.table_layout, null, false);

            // Object objTag = rootView.getTag();
            // if (objTag != null)
            //if (objTag instanceof Zal)
            //  Zal zal = (Zal) objTag;


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

                for(Check check:listChekovNaStolah)
                {
                    int stolId = check.getTableId();
                    if(stolId==stol.getId()){
                        stol.addCheck(check);
                       // listChekovNaStolah.remove(check);
                    }
                }

                View inflatedLayout = inflater.inflate(R.layout.table_layout, null, false);
                TextView tableNameTV = (TextView) inflatedLayout.findViewById(R.id.tableNameTV);
                RelativeLayout relativeZal = (RelativeLayout) inflatedLayout.findViewById(R.id.relativeZal);
                ImageView imageView = (ImageView) inflatedLayout.findViewById(R.id.tableImage);
                TextView tableInfoTV = (TextView) inflatedLayout.findViewById(R.id.tableInfoTV);


                relativeZal.setTag(stol);


                String name = stol.getName();
                tableNameTV.setText(name);

                ArrayList<Check> checkList = stol.getCheckList();
                Log.d("my","check list size = "+checkList.size());
                if(checkList.size()==0) tableInfoTV.setText("");
                if(checkList.size()==1){
                    double sum = checkList.get(0).getTotalSum();
                  //  String sum_ = Double.toString(sum);
                    String sum_f= String.format( "%.2f", sum);
                    tableInfoTV.setText(sum_f);

                }
                if(checkList.size()>1) tableInfoTV.setText("Σ");



                float tableX = stol.getX();
                float tableY = stol.getY();
                float tableW = 150;
                float tableH = 150;



                tableX=tableX*koefX-minTableX*koefX;
                tableY=tableY*koefY-minTableY*koefY;

                Log.d("my","originX= "+stol.getX()+"  new tableX = "+tableX);



                imageView.getLayoutParams().height = dpToPx((int)tableH);
                imageView.getLayoutParams().width = dpToPx((int)tableW);



                relativeZal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        Intent intent = new Intent(getActivity(), CheckDetailsActivity.class);

                        Stolik stolik = (Stolik) view.getTag();


                        Bundle arg = new Bundle();
                        arg.putInt("USER_ID", currentUser.getEmplId());
                        arg.putString("USER_NAME", currentUser.getName());
                        arg.putInt("SHIFT_ID", currentShiftCashId);
                        arg.putString("TABLE", stolik.getName());
                        arg.putInt("TABLE_ID", stolik.getId());
                        intent.putExtras(arg);
                        startActivity(intent);
                    }
                });

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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ZalFragment (defined as a static inner class below).
            return ZalFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return zal_list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return zal_list.get(position).getName();
        }
    }


    public static Bitmap convertBase64StringToBitmap(String source) {
        byte[] rawBitmap = Base64.decode(source.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawBitmap, 0, rawBitmap.length);
        return bitmap;
    }


    public static int screenWidth = 1;
    public static int screenHeight = 1;

    private void getScreenResolution() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    public int dpToPx(int dp) {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        //Log.d("my","dp ="+dp+" dp="+px);

        return px;
    }

   /* @Override
    protected void onPause() {
        super.onPause();
        if(glView!=null)    glView.onPause();
    }*/

    // Call back after onPause()
    @Override
    protected void onResume() {
        super.onResume();
        if (pinCode.length() == 0) updateStatus();
//if(glView!=null)        glView.onResume();
    }


  /*  @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("my", "___"+event.toString());
        return true;
    }*/


}
