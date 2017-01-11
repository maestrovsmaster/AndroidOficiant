package maestrovs.androidofficiant.requests;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import maestrovs.androidofficiant.application.MainApplication;


public class RequestGoodsCount {


    Integer doubledValue =0;


    int goodsCount=0;

    /**
     * @return goodsCount
     */
    public int getGoodsCount()
    {
        request();
        return goodsCount;
    }


	public void request() {
			String  returnString="";
    		String urlStr= MainApplication.mainURL+"/GoodsCountServlet";
    		

    		
    		 try{
                 URL url = new URL(urlStr);
                 URLConnection connection = url.openConnection();

                 JSONObject jsonObject0 = new JSONObject();

                 jsonObject0.put("device_id", MainApplication.device_id);
                 jsonObject0.put("device_name", MainApplication.device_name);

                 String inputString =jsonObject0.toString();
                 connection.setDoOutput(true);
                 OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                 out.write(inputString);
                 out.close();

                 BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
              
               
                 doubledValue =0;
                 
                 returnString = in.readLine();
                 
                 JSONObject jsonObject = new JSONObject(returnString);
                 
               
                 Log.d("my", "===" + jsonObject.toString());

                 int cnt =0;
                 try {
                     cnt = jsonObject.getInt("cnt");
                     goodsCount=cnt;
                }catch (Exception e)
                {

                }


                 in.close();


                 }catch(Exception e)
                 {
                     Log.d("my","err="+e.toString());
                 }
    		
    	}
 
    }