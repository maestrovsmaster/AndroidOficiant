package maestrovs.androidofficiant.goodslist;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import maestrovs.androidofficiant.R;
import maestrovs.androidofficiant.essences.CheckDetail;
import maestrovs.androidofficiant.essences.DicTable;
import maestrovs.androidofficiant.essences.Good;
import maestrovs.androidofficiant.essences.GoodGRP;
import maestrovs.androidofficiant.spinnerwheel.AbstractWheel;

/**
 * Created by userd088 on 28.11.2016.
 */

public class RVPriceAdapter extends RecyclerView.Adapter<RVPriceAdapter.PersonViewHolder>{
    public static class PersonViewHolder extends RecyclerView.ViewHolder {



        CardView cv;
        TextView personName;
        TextView priceTW;
        EditText cntBt;
        TextView unitTW;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            priceTW = (TextView)itemView.findViewById(R.id.person_age);
            cntBt = (EditText)itemView.findViewById(R.id.cntButton);
            unitTW=(TextView)itemView.findViewById(R.id.unitTW);

        }
    }

    private ArrayList<DicTable> groups;

    Context context;
    PriceListInterface priceListInterface;
    AbstractWheel wheelcnt;

    public RVPriceAdapter (Context context, PriceListInterface priceListInterface,int textViewResourceId, ArrayList<DicTable> objects) {

        this.context = context;
        this.groups = objects;
        this.priceListInterface=priceListInterface;
    }


    TextView titleTextView;

    EditText  cntBt;



    View v;

   /* List<CheckDetail> checkDetailList;
    RVPriceAdapter(List<CheckDetail> persons){
        this.checkDetailList = persons;
    }*/

    @Override
    public int getItemCount() {
        return groups.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if(i==0){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_price_grp_back, viewGroup, false);
            PersonViewHolder pvh = new PersonViewHolder(v);
            return pvh;
        }else {
            if (groups.get(i) instanceof GoodGRP) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_price_grp, viewGroup, false);
                PersonViewHolder pvh = new PersonViewHolder(v);
                return pvh;
            }
            if (groups.get(i) instanceof Good) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_price, viewGroup, false);
                PersonViewHolder pvh = new PersonViewHolder(v);
                return pvh;
            }
        }
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_price_grp, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;


    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        /*personViewHolder.personName.setText(checkDetailList.get(i).getGood().getName());
        String newprc= String.format( "%.2f", checkDetailList.get(i).getGood().getOut_price());
        personViewHolder.priceTW.setText(newprc+" грн");

        personViewHolder.unitTW.setText(checkDetailList.get(i).getGood().getUnit());
        String newcnt= String.format( "%.2f", checkDetailList.get(i).getQuantity());
        personViewHolder.cntBt.setText(newcnt);
        personViewHolder.cntBt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    InputMethodManager in = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    // NOTE: In the author's example, he uses an identifier
                    // called searchBar. If setting this code on your EditText
                    // then use v.getWindowToken() as a reference to your
                    // EditText is passed into this callback as a TextView




                    // Perform action on key press
                    //Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
                    Log.d("my","ENTER");
                    //changeCnt( v);
                    return true;
                }
                return false;
            }
        });*/



    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}