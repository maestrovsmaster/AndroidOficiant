package maestrovs.androidofficiant.goodslist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import maestrovs.androidofficiant.R;
import maestrovs.androidofficiant.essences.DicTable;
import maestrovs.androidofficiant.essences.Good;
import maestrovs.androidofficiant.essences.GoodGRP;
import maestrovs.androidofficiant.spinnerwheel.AbstractWheel;
import maestrovs.androidofficiant.spinnerwheel.adapters.NumericWheelAdapter;


public class PriceGRPListAdapter<T> extends ArrayAdapter<DicTable> implements WheelInterface{


    private ArrayList<DicTable> groups;

    Context context;
    PriceListInterface priceListInterface;
    AbstractWheel wheelcnt;

    public PriceGRPListAdapter(Context context, PriceListInterface priceListInterface,int textViewResourceId, ArrayList<DicTable> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.groups = objects;
        this.priceListInterface=priceListInterface;
    }


    TextView titleTextView;

    EditText  cntBt;



    View v;

    @SuppressLint("NewApi")
    public View getView(int position, final View convertView, ViewGroup parent) {

        //Log.d("my", "*--");
        v = convertView;

        final DicTable tablepos = groups.get(position);
        int id = tablepos.getId();
        if(id==-1){

            String title = tablepos.getName();

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_price_grp_back, null);

            titleTextView = (TextView) v.findViewById(R.id.titleTextView);


            v.setTag(tablepos);
            titleTextView.setText(title);


            v.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("my", "pik");
                    Object tag = v.getTag();
                    if (tag instanceof GoodGRP) {
                        GoodGRP inv = (GoodGRP) tag;
                        priceListInterface.clickOnPriceGroup(inv);
                    }
                }
            });

        }
        else {
            if (tablepos instanceof GoodGRP) {
                final GoodGRP goodGRP = (GoodGRP) tablepos;

                String title = goodGRP.getName();

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.row_price_grp, null);

                titleTextView = (TextView) v.findViewById(R.id.titleTextView);


                v.setTag(goodGRP);
                titleTextView.setText(title);


                v.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Object tag = v.getTag();
                        if (tag instanceof GoodGRP) {
                            Log.d("my", "pik grp");
                            GoodGRP inv = (GoodGRP) tag;

                            priceListInterface.clickOnPriceGroup(goodGRP);

                        }

                    }
                });
            }

            if (tablepos instanceof Good) {
                final Good good = (Good) tablepos;

                String title = good.getName();
                String unit = good.getUnit();
                String summ= title+","+unit;
                double price = good.getOut_price();
                //String strprc = Double.toString(price);

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.row_price, null);

                TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
                //TextView descriptionTextView= (TextView) v.findViewById(R.id.descriptionTextView);
                TextView priceTextView =(TextView) v.findViewById(R.id.priceTextView);
               // EditText editCnt = (EditText) v.findViewById(R.id.editCnt);

                /*  wheelcnt = (AbstractWheel) v.findViewById(R.id.hour_horizontal);
                NumericWheelAdapter hourAdapter = new NumericWheelAdapter(PriceGRPListAdapter.this,v.getContext(), 0, 10, "%2d");
                hourAdapter.setItemResource(R.layout.wheel_text_centered);
                hourAdapter.setItemTextResource(R.id.text);
                wheelcnt.setViewAdapter(hourAdapter);*/

                cntBt = (EditText )v.findViewById(R.id.cntButton);
                cntBt.setTag(good);
                cntBt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(cntBt.getContext());
                        builder.setTitle("Title");


                    }
                });

                cntBt.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {

                            InputMethodManager in = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                            // NOTE: In the author's example, he uses an identifier
                            // called searchBar. If setting this code on your EditText
                            // then use v.getWindowToken() as a reference to your
                            // EditText is passed into this callback as a TextView

                            in.hideSoftInputFromWindow(cntBt
                                            .getApplicationWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);


                            // Perform action on key press
                            //Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
                            Log.d("my","ENTER");
                            changeCnt( v);
                            return true;
                        }
                        return false;
                    }
                });


                v.setTag(good);
                titleTextView.setText(summ);
                //descriptionTextView.setText(unit);
                String prc= String.format( "%.2f", price );

                priceTextView.setText(prc);


                v.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        changeCnt(  v);
                    }
                });
            }

        }

        // the view must be returned to our activity
        return v;

    }



    private void changeCnt(View v)
    {
        Object tag = v.getTag();
        if (tag instanceof Good) {
            Log.d("my", "pik price ");
            EditText  cntBt = (EditText )v.findViewById(R.id.cntButton);

        String scnt = cntBt.getText().toString();
        Good good = (Good) tag;

        double currentCnt = 0;

            Log.d("my","current scnt = "+scnt);
        if(scnt!=null)
            if(scnt.length()>0) {
                try {
                    NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                    Number number = format.parse(scnt);
                    currentCnt =number.doubleValue();

                } catch (Exception e) {
                    try{
                        currentCnt =Double.parseDouble(scnt);
                    }catch (Exception e2){}
                }
            }
            Log.d("my","current cnt = "+currentCnt);


        if(! (v instanceof EditText)) currentCnt++;
        String newcnt= String.format( "%.2f", currentCnt );

        cntBt.setText(newcnt);


        good.setFcnt(currentCnt);

        priceListInterface.clickOnPricePosition(good);
        }
    }


    @Override
    public void changePosition() {
      /*  int currentItem = wheelcnt.getCurrentItem();
        Log.d("my","currentItem="+currentItem);*/
    }
}
