package maestrovs.androidofficiant.requests;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import maestrovs.androidofficiant.essences.DicTable;
import maestrovs.androidofficiant.essences.Good;
import maestrovs.androidofficiant.essences.GoodGRP;
import maestrovs.androidofficiant.application.MainApplication;


public class RequestGoodsGRP {

    //private static  String mainURL = "http://"+StaticParameterNames.SERVER_IP+":"+StaticParameterNames.SERVER_PORT+"/BarcodeServer3";

    EditText inputValue = null;
    Integer doubledValue = 0;
    Button doubleMe;


    ArrayList<DicTable> goodsGRPList = new ArrayList<DicTable>();

    int id = -1;
    int grp_id = -1;

    public ArrayList<DicTable> getGoodsGRPList(int id, int grp_id) {
        goodsGRPList.clear();

        this.id = id;
        this.grp_id = grp_id;

        request();

        return goodsGRPList;
    }


    public void request() {

        String returnString = "";

        try {
            URL url = new URL(MainApplication.mainURL + "/GoodsGRPListServlet");
            URLConnection connection = url.openConnection();


            JSONObject jsonObject = new JSONObject();


            jsonObject.put("ID", id);
            jsonObject.put("GRP_ID", grp_id);
            //inputString = URLEncoder.encode(inputString, "UTF-8");

            String inputString = jsonObject.toString();


            Log.d("my", inputString);

            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(inputString);
            out.close();


            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));


            doubledValue = 0;

            returnString = in.readLine();

            JSONObject jsonObj = new JSONObject(returnString);


            Log.d("my", "keep ans===" + jsonObj.toString());

            try {
                JSONArray data = jsonObj.getJSONArray("data");
                if(data.length()>0) {
                    String tableName = jsonObj.getString("name");

                    if(tableName.equals("DIC_PRICE_LIST_GRP")){

                    for (int i = 0; i<data.length();i++) {




                        JSONObject obj = data.getJSONObject(i);

                        int id = obj.getInt("ID");
                        int grp_id = obj.getInt("PARENT_ID");
                        String name = obj.getString("NAME");

                        GoodGRP goodGRP = new GoodGRP(id, grp_id, name);
                        goodsGRPList.add(goodGRP);
                    }
                    }

                    if(tableName.equals("DIC_GOODS")){

                        for (int i = 0; i<data.length();i++) {




                            JSONObject obj = data.getJSONObject(i);

                            int id = obj.getInt("ID");
                            Double cena =  obj.getDouble("CENA");
                            //Log.d("my"," type of cena = "+cena.getClass().toString());
                            String name = obj.getString("NAME");
                            String UNIT = obj.getString("UNIT");
                            String ARTICLE = obj.getString("ARTICLE");

                            Good good = new Good(id, grp_id, name, UNIT, ARTICLE);
                            good.setOut_price(cena);
                            goodsGRPList.add(good);
                        }
                    }

                }

            } catch (Exception e) {

            }


            in.close();
        } catch (Exception e) {
            Log.d("my", "err=" + e.toString());
        }


    }


}