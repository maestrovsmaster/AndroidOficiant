package maestrovs.androidofficiant.essences;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by userd088 on 28.04.2016.
 */
public class JorHeadTable extends JorTable {

    Subdivision subdivision=null;

    public static final int INVENTORY=0;
    public static final int BILL_IN=1;

    int docState=0;
    private String num="";
    String datetime="";
    java.util.Date date=null;

    public JorHeadTable(int id, int grpId, String num, String dateTime, Subdivision subdivision, int docState) {
        super(id, grpId);
        this.subdivision=subdivision;
        this.num=num;
        this.datetime=dateTime;
        this.docState=docState;

        java.util.Date d = null;
        try {
            d = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(datetime);
            Log.d("my", " d = " + d.toString());
            date= new java.util.Date(d.getTime());
            Log.d("my", " date = " + date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("my", " date err= " + e.toString());

            try {
                d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(datetime);
                Log.d("my", " d = " + d.toString());
                date= new java.util.Date(d.getTime());
                Log.d("my", " date = " + date.toString());
            } catch (ParseException e2) {
                e.printStackTrace();
                Log.d("my", " date err2= " + e2.toString());
            }
        }


    }

    public Subdivision getSubdivision() {
        return subdivision;
    }

    private ArrayList<JorDtTableInterface> dtList=new ArrayList<>();

    public ArrayList<JorDtTableInterface> getDtList()
    {
        return dtList;
    }

    public void addDt(JorDtTableInterface dt)
    {
        dtList.add(dt);
    }

    public void clearDtList()
    {
        dtList.clear();
    }


    public String getNum() {
        return num;
    }

    public String getDatetime() {

        //if (date==null) return "";
        //else return date.toString();
        return datetime;
    }

    public int getDocState() {
        return docState;
    }

    public java.util.Date getDate() {
        return date;
    }
}
