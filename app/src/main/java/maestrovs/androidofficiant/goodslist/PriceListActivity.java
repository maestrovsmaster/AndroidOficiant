package maestrovs.androidofficiant.goodslist;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.LinkedList;

import maestrovs.androidofficiant.R;
import maestrovs.androidofficiant.application.MainApplication;
import maestrovs.androidofficiant.essences.CheckDetail;
import maestrovs.androidofficiant.essences.DicTable;
import maestrovs.androidofficiant.essences.Good;
import maestrovs.androidofficiant.essences.GoodGRP;
import maestrovs.androidofficiant.requests.RequestGoodsGRP;


public class PriceListActivity extends AppCompatActivity  implements PriceListInterface//ListActivity
{

    private LinkedList<String> mListItems;
    ListView listView;

    private ActionBar mainActionBar;

    TextView title;

    ImageView offlineIcon;

    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapterDoc;
    static PriceGRPListAdapter<DicTable> adapterGRPList;

    ViewGroup container;


    LayoutInflater inflater;
    View myRootView;


    private String titletxt = "";

    int docType = 0;

   // Dialog dialog;
    EditText idEditText;
    EditText nameEditText;

    ImageButton closeButton;
    TextView abarTitle;
    ImageButton homeBt;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_docs_list);
        Log.d("my", "hello dock list");

      //  Object actionBar = getSupportActionBar();
       // Log.d("my","action bar class = "+actionBar.getClass().toString());

        //android.support.v7.internal.app.WindowDecorActionBar bar = (android.support.v7.internal.app.WindowDecorActionBar) actionBar;

      /*  mainActionBar = getActionBar(); //main bar
        mainActionBar.setDisplayShowCustomEnabled(true);
        mainActionBar.setDisplayShowHomeEnabled(false);
        mainActionBar.setCustomView(R.layout.abar_price_list_act);*/

        closeButton = (ImageButton) findViewById(R.id.closeButton);
        abarTitle = (TextView) findViewById(R.id.abarTitle);
        homeBt=(ImageButton)findViewById(R.id.homeBt);


        Intent iin = getIntent();

        Bundle bundle = iin.getExtras();

        try {
            docType = (Integer) bundle.getInt("type");


        } catch (Exception e) {
            Log.d("my", " no dockType =((( ");
            docType=0; ///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }


        Log.d("my", "my shelf 2  onCreateView ");


        title = (TextView) findViewById(R.id.title);
      /*  View v = findViewById(R.id.closeBt);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        homeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomePriceTitle();
                loadDocList(-1,-1);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.putExtra("name", etName.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                finish();
            }
        });




        listView = (ListView) findViewById(R.id.list_view);
        // Set a listener to be invoked when the list should be refreshed.
        // listView.setOnRefreshListener

        //  mListItems = new LinkedList<String>();
        //  mListItems.addAll(Arrays.asList(mStrings));

        setHomePriceTitle();
        loadDocList(-1,-1);
    }



    private void setHomePriceTitle()
    {
        abarTitle.setText("Прайс");
    }

    private void setPriceTitle(String price)
    {
        abarTitle.setText(price);
    }




    TextView countDocksDB;

    ArrayList<DicTable> priceGRPList = new ArrayList<>();

    /////////////inventory

    public void loadDocList(int id, int grp_id) {

        LoadDocumentsThread loadDocumentsThread = new LoadDocumentsThread(id, grp_id);
        loadDocumentsThread.start();
    }




    class LoadDocumentsThread extends Thread {
        int id=-1;
        int grp_id=-1;
        public LoadDocumentsThread(int id, int grp_id){
            this.id=id;
            this.grp_id=grp_id;
        }

        @Override
        public void run() {
            priceGRPList.clear();

            RequestGoodsGRP requestGoodsGRP = new RequestGoodsGRP();

            Log.d("my"," thread request. id="+id+" grp="+grp_id);
            ArrayList<DicTable> grpList = requestGoodsGRP.getGoodsGRPList(id,grp_id);
            Log.d("my","--------------------");
            GoodGRP emptyGood = new GoodGRP(-1, id, "...");

            Log.d("my"," empty grp . id="+id);


            priceGRPList.add(emptyGood);
            if(grpList!=null) priceGRPList.addAll(grpList);
            Log.d("my", "!!!!thread: invlist sz = " + priceGRPList.size());




                h.post(readyLoadDocks);


        }
    }







    Handler h = new Handler();

    Runnable readyLoadDocks = new Runnable() {
        @Override
        public void run() {


            if(priceGRPList.size()==0){

            }

            adapterGRPList = new PriceGRPListAdapter<>(PriceListActivity.this, PriceListActivity.this,R.layout.row_price_grp, priceGRPList);
            listView.setAdapter(adapterGRPList);
        }
    };
    String ans="";

    int gruppa_praisa=1;



    @Override
    public void clickOnPriceGroup(DicTable goodGRP) {



        if(goodGRP instanceof GoodGRP) {

            GoodGRP grp = (GoodGRP)goodGRP;
            int id = grp.getId();

            int grp_id = grp.getParentGrpId();
            String name = grp.getName();
            setPriceTitle(name);
            gruppa_praisa=grp.getId();

            Log.d("my", "--------------click price grp + id = " + id + " grp=" + grp_id);

            loadDocList(id, grp_id);
        }

    }

    @Override
    public void clickOnPricePosition(Good good) {

        Log.d("my","we take good: "+good.toString());

        String gname = good.getName();

        if(MainApplication.currentCheck!=null){
        ArrayList<CheckDetail> details = MainApplication.currentCheck.getCheckDetails();
        if(details!=null) {
            /*for (CheckDetail det : details) {
                if (det.getName().equals(gname)) MainApplication.currentCheck.removeDetails(det);
            }*/

            CheckDetail checkDetail = new CheckDetail( good, good.getFcnt(),gruppa_praisa);
            MainApplication.currentCheck.addCheckDetail(checkDetail);
        }}
    }

}
