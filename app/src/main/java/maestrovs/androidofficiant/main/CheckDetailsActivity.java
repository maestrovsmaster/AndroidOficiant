package maestrovs.androidofficiant.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import maestrovs.androidofficiant.R;
import maestrovs.androidofficiant.application.MainApplication;
import maestrovs.androidofficiant.essences.Check;
import maestrovs.androidofficiant.essences.CheckDetail;
import maestrovs.androidofficiant.essences.DicTable;
import maestrovs.androidofficiant.essences.Good;
import maestrovs.androidofficiant.goodslist.PriceListActivity;
import maestrovs.androidofficiant.goodslist.PriceListFragment;
import maestrovs.androidofficiant.goodslist.PriceListInterface;
import maestrovs.androidofficiant.requests.Request;
import maestrovs.androidofficiant.requests.RequestAddDetails;
import maestrovs.androidofficiant.requests.RequestCreateCheck;
import maestrovs.androidofficiant.requests.RequestPrintCheck;

public class CheckDetailsActivity extends AppCompatActivity implements PriceListInterface {

    private  int userId;
    private  String userName;
    private int shiftId;
    private String table="";
    int tableId=0;

    private ViewPager mViewPager;
    private CheckDetailsActivity.ZakazPagerAdapter mSectionsPagerAdapter;

    android.support.design.widget.AppBarLayout app_bar;



   // ListView listView;

    static RecyclerView rv;

    int checkId=-1;





    android.support.design.widget.CollapsingToolbarLayout collapsingToolbarLayout;

    ImageButton backButton;
    TextView checkNumTV;
    TextView userTV;
    TextView clientTV;
    TextView tableTV;

    ImageButton homeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("my","hello check details activity");
        //listView = (ListView) findViewById(R.id.listView);
        //rv = (RecyclerView)findViewById(R.id.rv);

        MainApplication.currentCheck=null;

        checkDetails = new ArrayList<>();

        Intent iin = getIntent();

        Bundle bundle = iin.getExtras();

        try {
            userId = bundle.getInt("USER_ID");
            userName = bundle.getString("USER_NAME");
            shiftId = bundle.getInt("id");

        } catch (Exception e) {
            Log.d("my", " no id =((( ");
        }

        try {
           table = bundle.getString ("TABLE");
            tableId=bundle.getInt("TABLE_ID");

        } catch (Exception e) {
            Log.d("my", " no table=((( ");
        }



        try {
            checkId = bundle.getInt ("CHECK_ID");
            Log.d("my", "<<< check id = "+checkId);
        } catch (Exception e) {
            Log.d("my", " no check id=((( ");
            checkId=-1;

        }

        app_bar = (AppBarLayout) findViewById(R.id.app_bar);


        mSectionsPagerAdapter = new CheckDetailsActivity.ZakazPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();

                Intent intent = new Intent(CheckDetailsActivity.this, PriceListActivity.class);

                Bundle arg = new Bundle();
                /*arg.putInt("USER_ID", currentUser.getEmplId());
                arg.putString("USER_NAME", currentUser.getName());
                arg.putInt("SHIFT_ID", currentShiftCashId);
                arg.putString("TABLE", "");
                intent.putExtras(arg);*/
                startActivityForResult(intent,7);
            }
        });


        FloatingActionButton fabPrint = (FloatingActionButton) findViewById(R.id.fabPrint);
        fabPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread printThrd = new Thread(){
                    @Override
                    public void run() {
                        RequestPrintCheck requestPrintCheck = new RequestPrintCheck();
                        requestPrintCheck.print(MainApplication.currentCheck.getId());
                    }
                };
                printThrd.start();
            }
        });



        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        collapsingToolbarLayout.setTitle("Create Delivery Personnel");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 240, 250));



        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if(position==0)
                {
                    Log.d("my","check list");
                    Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                   homeButton.setAnimation(animFadeOut);
                    homeButton.setVisibility(View.GONE);

                    backButton.setAnimation(animFadeIn);
                    backButton.setVisibility(View.VISIBLE);


                    app_bar.setExpanded(true);
                }
                if(position==1)
                {
                    Log.d("my"," price list");
                    Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                    homeButton.setAnimation(animFadeIn);
                    homeButton.setVisibility(View.VISIBLE);

                    backButton.setAnimation(animFadeOut);
                    backButton.setVisibility(View.GONE);


                    app_bar.setExpanded(false);

                    if(priceListFragment!=null) priceListFragment.setHomePriceTitle();

                }
            }
        });


        ImageView toolbarImage = (ImageView) findViewById(R.id.tollbarImage);
        toolbarImage.setOnClickListener(null);


        backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("my","FINISH");
                finish();
            }
        });

        homeButton = (ImageButton)findViewById(R.id.homeBt);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("my","home");
                if(priceListFragment!=null) priceListFragment.setHomePriceTitle();
            }
        });


        checkNumTV = (TextView) findViewById(R.id.checkNum);
        userTV=(TextView) findViewById(R.id.user);
        clientTV=(TextView) findViewById(R.id.user);
        tableTV=(TextView)findViewById(R.id.table);

        userTV.setText(userName);
        tableTV.setText(table);





        //priceListAdapter = new PriceListAdapter(CheckDetailsActivity.this, R.layout.row_price,goodsList);
        //rv.setAdapter(priceListAdapter);
        Log.d("my","check id = "+checkId);
        if(checkId<=0) {


            Thread createCheck = new Thread() {
                @Override
                public void run() {
                    RequestCreateCheck requestCreateCheck = new RequestCreateCheck();
                    JSONObject jsonObject = requestCreateCheck.createChek(tableId,userId);
                    Log.d("my", " created check = " + jsonObject.toString());

                    try {
                        String id = jsonObject.getString("ID");
                        String num = jsonObject.getString("NUM");


                        int iid = Integer.parseInt(id);

                        MainApplication.currentCheck = new Check(iid, num);
                        createCheckH.post(new Runnable() {
                            @Override
                            public void run() {
                                if (MainApplication.currentCheck.getNum() != null) {
                                    String chn = checkNumTV.getText().toString();

                                    String newstr = chn + " " + MainApplication.currentCheck.getNum();

                                    checkNumTV.setText(newstr);
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            createCheck.start();
        }else{//getCheck

            UpdateCheckFromServer updateCheckFromServer = new UpdateCheckFromServer();
            updateCheckFromServer.start();

        }
    }

    Handler updateCheckH = new Handler();


    class UpdateCheckFromServer extends Thread
    {
        @Override
        public void run() {
            String updateCheckQuery = " select jc.id, jc.hd_id, (select dg.name from dic_goods dg where dg.id = jc.goods_id) as NAME, "+
            " jc.cnt , jc.orig_price , jc.sum_  from  jor_checks_dt  jc  where  jc.hd_id = "+checkId+
            " and (select jh.doc_state from jor_checks jh where jh.id = jc.hd_id) = 0 ";

            Request request = new Request();
             JSONObject jsonObject = request.getJSON(updateCheckQuery);
            Log.d("my"," we updated check = "+jsonObject.toString());
        }
    }



    static RVAdapter adapter;

    Handler createCheckH = new Handler();


  /*  public void collapseToolbar(){
        android.support.design.widget.AppBarLayout.LayoutParams param1 = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        CoordinatorLayout.Behavior behavior =  params.getBehavior();
        if(behavior!=null) {
            CoordinatorLayout coordinator =(CoordinatorLayout)findViewById(R.id.coordinator_layout);
            behavior.onNestedFling(coordinator, collapsingToolbarLayout, null, 0, 10000, true);
        }
    }


    public void expandToolbar(){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();

       CoordinatorLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if(behavior!=null) {
            CoordinatorLayout coordinator =(CoordinatorLayout)findViewById(R.id.coordinator_layout);
            behavior.onNestedFling(coordinator, collapsingToolbarLayout, null, 0, -10000, false);
        }
    }*/



    private static ArrayList<CheckDetail> checkDetails = new ArrayList<>();
    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private void initializeData(){

       // checkDetails.add(new Person("Emma Wilson", "23 years old", R.drawable.go_next));
     //   checkDetails.add(new Person("Lavery Maiss", "25 years old", R.drawable.go_next));

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (data == null) {return;}
        Log.d("my"," RESULT CODE = "+resultCode+" request code = "+requestCode);

        checkDetails.clear();
        if(MainApplication.currentCheck!=null) {
            ArrayList<CheckDetail> chlist = MainApplication.currentCheck.getCheckDetails();
            Log.d("my", " we have = " + chlist.toString());
            checkDetails.addAll(chlist);
            adapter.notifyDataSetChanged();
            AddPositionsToServer addPositionsToServer = new AddPositionsToServer();
            addPositionsToServer.start();
        }
    }

    @Override
    public void clickOnPriceGroup(DicTable goodGRP) {

    }

    @Override
    public void clickOnPricePosition(Good good) {
        Log.d("my","click chick");
        if(MainApplication.currentCheck!=null) {
            checkDetails.clear();
            ArrayList<CheckDetail> chlist = MainApplication.currentCheck.getCheckDetails();
            Log.d("my", " we have = " + chlist.toString());
            checkDetails.addAll(chlist);
            adapter.notifyDataSetChanged();
            AddPositionsToServer addPositionsToServer = new AddPositionsToServer();
            addPositionsToServer.start();
        }
    }

    class AddPositionsToServer extends Thread{

        @Override
        public void run() {

            RequestAddDetails requestAddDetails = new RequestAddDetails();
            Log.d("my","okno det size = "+checkDetails);
            requestAddDetails.addDetailsList(MainApplication.currentCheck.getId(), checkDetails);
        }
    }


    PriceListFragment priceListFragment = null;

    public class ZakazPagerAdapter extends FragmentPagerAdapter {

        public ZakazPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position==0) return new CheckDetailsFragment();
            else{
                if(priceListFragment==null){
                    priceListFragment  =  new PriceListFragment();
                    priceListFragment.setPriceListInterface(CheckDetailsActivity.this);
                }
                return  priceListFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";

            }
            return null;
        }
    }


    public static class CheckDetailsFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public CheckDetailsFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CheckDetailsFragment newInstance(int sectionNumber) {
            CheckDetailsFragment fragment = new CheckDetailsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.content_check_details, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText("");
            rv = (RecyclerView)rootView.findViewById(R.id.rv);
             LinearLayoutManager llm = new LinearLayoutManager(CheckDetailsFragment.this.getContext());
              rv.setLayoutManager(llm);

            // initializeData();
            Log.d("my","List = "+ checkDetails.toString());
            adapter = new RVAdapter(checkDetails);
       rv.setAdapter(adapter);

            return rootView;
        }
    }


}
