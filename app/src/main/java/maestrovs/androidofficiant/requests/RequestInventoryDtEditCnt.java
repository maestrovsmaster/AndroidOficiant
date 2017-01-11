package maestrovs.androidofficiant.requests;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import maestrovs.androidofficiant.application.MainApplication;


public class RequestInventoryDtEditCnt {
	
	//private static  String mainURL = "http://"+StaticParameterNames.SERVER_IP+":"+StaticParameterNames.SERVER_PORT+"/BarcodeServer3";
 

    
   // ArrayList<Inventory> jsonObject = new ArrayList<>();
    double factCnt=0;
    
    int invId=0;
    int goodId=0;
    
    JSONObject ansJSON=null;

    Context context=null;

    public RequestInventoryDtEditCnt(Context context)
    {
        this.context=context;
    }
    
    
    /**
     *
     * @return
     */
    public JSONObject sendEditInventoryDt (int invId, int goodId, double cnt)
    {
    	this.invId=invId;
    	this.goodId=goodId;
    	this.factCnt=cnt;


        request();
    	
		return ansJSON;
    }
   
    


    public void request() {

            String  returnString="";
    		
    		 try{
                 URL url = new URL(MainApplication.mainURL+"/InventoryEditCntServlet");
                 URLConnection connection = url.openConnection();
                 
                 JSONObject  jsonObject = new JSONObject();
                 jsonObject.put("invId", invId);
                 jsonObject.put("goodId", goodId);
                 jsonObject.put("cnt", factCnt);


                 jsonObject.put("device_id", MainApplication.device_id);
                 jsonObject.put("device_name", MainApplication.device_name);

                 String inputString = jsonObject.toString();
                 //inputString = URLEncoder.encode(inputString, "UTF-8");

                 Log.d("my", inputString);

                 connection.setDoOutput(true);
                 OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                 out.write(inputString);
                 out.close();
                 
               
               
                 BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
              
               
                
                 
                 returnString = in.readLine();
                 
                 JSONObject jsonObj = new JSONObject(returnString);
                 
               
                 Log.d("my", "===input inv edit ==" + jsonObj.toString());
                 try {
                    String  status = jsonObj.getString("status");
                     if(status.equals("full")){
                         JSONArray devListArray = jsonObj.getJSONArray("dev_list");


                         Intent intent = new Intent(context, MainApplication.class);
                         Bundle arg = new Bundle();

                         arg.putString("list", devListArray.toString());


                         intent.putExtras(arg);
                         context.startActivity(intent);


                     }
                     if(status.equals("wrong_date")){

                         Intent intent = new Intent(context, MainApplication.class);
                         Bundle arg = new Bundle();
                         arg.putString("list", "wrong_date");
                         intent.putExtras(arg);
                         context.startActivity(intent);

                     }
                 }catch (Exception e){  }

                ansJSON=jsonObj;
                	
                	//int id = obj.getInt("id");
                	//String num = obj.getString("num");
                	//String datetime = obj.getString("datetime");
                	//String subdivision = obj.getString("subdivision");
                	
                	//Inventory inventory = new Inventory(id, num, datetime, subdivision);
                	//jsonObject.add(inventory);

                 in.close();
                 


                 }catch(Exception e)
                 {
                     Log.d("my", "err=" + e.toString());
                     String msg = ""+e.toString();
                     try {
                         ansJSON=new JSONObject();
                         ansJSON.putOpt("error",msg);
                     } catch (JSONException e1) {
                         e1.printStackTrace();
                     }
                 }
    		

    		
    	}

    
    
 
    }