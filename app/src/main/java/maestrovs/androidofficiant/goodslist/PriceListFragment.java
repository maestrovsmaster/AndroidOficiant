package maestrovs.androidofficiant.goodslist;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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


public class PriceListFragment extends Fragment implements PriceListInterface//ListActivity
{

    private LinkedList<String> mListItems;
    ListView listView;

    private ActionBar mainActionBar;

    TextView title;

    ImageView offlineIcon;

    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapterDoc;
    static PriceGRPListAdapter<DicTable> adapterGRPList;


    static RecyclerView rv;



    private String titletxt = "";

    int docType = 0;

   // Dialog dialog;
    EditText idEditText;
    EditText nameEditText;

   /* ImageButton closeButton;
    TextView abarTitle;
    ImageButton homeBt;*/


    LayoutInflater inflater;
    ViewGroup container;
    View myRootView;

    PriceListInterface priceListInterface;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        this.inflater = inflater;
        this.container = container;

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme_NoActionBar);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);


        myRootView = localInflater.inflate(R.layout.activity_docs_list, null);

       // setContentView(R.layout.activity_docs_list);
        Log.d("my", "hello dock list");

      //  Object actionBar = getSupportActionBar();
       // Log.d("my","action bar class = "+actionBar.getClass().toString());

        //android.support.v7.internal.app.WindowDecorActionBar bar = (android.support.v7.internal.app.WindowDecorActionBar) actionBar;

      /*  mainActionBar = getActionBar(); //main bar
        mainActionBar.setDisplayShowCustomEnabled(true);
        mainActionBar.setDisplayShowHomeEnabled(false);
        mainActionBar.setCustomView(R.layout.abar_price_list_act);*/

      //  closeButton = (ImageButton) myRootView.findViewById(R.id.closeButton);
       // abarTitle = (TextView) myRootView.findViewById(R.id.abarTitle);
       // homeBt=(ImageButton)myRootView.findViewById(R.id.homeBt);


      /*  Intent iin = getIntent();

        Bundle bundle = iin.getExtras();

        try {
            docType = (Integer) bundle.getInt("type");


        } catch (Exception e) {
            Log.d("my", " no dockType =((( ");
            docType=0; ///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }*/


        Log.d("my", "my shelf 2  onCreateView ");


        title = (TextView) myRootView.findViewById(R.id.title);
      /*  View v = findViewById(R.id.closeBt);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

       /* homeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomePriceTitle();
                loadDocList(-1,-1);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/




        listView = (ListView) myRootView.findViewById(R.id.list_view);

        /*rv = (RecyclerView)myRootView.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(PriceListFragment.this.getContext());
        rv.setLayoutManager(llm);*/

        // initializeData();


        // Set a listener to be invoked when the list should be refreshed.
        // listView.setOnRefreshListener

        //  mListItems = new LinkedList<String>();
        //  mListItems.addAll(Arrays.asList(mStrings));

       /* setHomePriceTitle();
        loadDocList(-1,-1);*/

        return  myRootView;
    }





    public void setHomePriceTitle()
    {
        loadDocList(-1,-1);
        //abarTitle.setText("Прайс");
    }

    private void setPriceTitle(String price)
    {

        //abarTitle.setText(price);
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

            if(PriceListFragment.this!=null) {
                adapterGRPList = new PriceGRPListAdapter<>(getActivity().getApplicationContext(), PriceListFragment.this, R.layout.row_price_grp, priceGRPList);
                listView.setAdapter(adapterGRPList);
            }
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
            Log.d("my","clic interface:");
            if(priceListInterface!=null){
                Log.d("my","clic ok:");
                priceListInterface.clickOnPricePosition(good);
            }else{
                Log.d("my","clic null");
            }
        }}else{
            Log.d("my","main activity current check =null");
        }
    }

    public PriceListInterface getPriceListInterface() {
        return priceListInterface;
    }

    public void setPriceListInterface(PriceListInterface priceListInterface) {
        this.priceListInterface = priceListInterface;
    }
}
