package maestrovs.androidofficiant.requests;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import maestrovs.androidofficiant.application.MainApplication;


public class Request {
	
	//private static  String mainURL = "http://"+StaticParameterNames.SERVER_IP+":"+StaticParameterNames.SERVER_PORT+"/BarcodeServer3";
 

    
    JSONObject jsonObject = new JSONObject();
    
    String query="";
    
    public JSONObject getJSON( String query)
    {
    	this.query=query;
        request();
    	
		return jsonObject;
    }
   

    public void request() {

            String  returnString="";
    		
    		 try{
                 URL url = new URL(MainApplication.mainURL+"/Request");
                 URLConnection connection = url.openConnection();

                 JSONObject jsonObject0 = new JSONObject();

                 jsonObject0.put("device_id", MainApplication.device_id);
                 jsonObject0.put("device_name", MainApplication.device_name);
                 jsonObject0.put("query", query);
                 String inputString = jsonObject0.toString();





                 //inputString = URLEncoder.encode(inputString, "UTF-8");

                 Log.d("my", inputString);

                 connection.setDoOutput(true);
                 OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                 out.write(inputString);
                 out.close();
                 
               
               
                 BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
              
               
                
                 
                 returnString = in.readLine();

                 
                 jsonObject = new JSONObject(returnString);

                 Log.d("my","json obj = "+jsonObject.toString());
                 Log.d("my", "===" + jsonObject.length());
                 
               /* for(int i=0;i<jsonObject1.length();i++)
                {
                	JSONObject obj = jsonObject1.getJSONObject(i);
                	
                	int id = obj.getInt("id");
                	String num = obj.getString("num");
                	String datetime = obj.getString("datetime");
                	String subdivision = obj.getString("subdivision");
                	
                	Inventory inventory = new Inventory(id, num, datetime, subdivision,Inventory.INVENTORY);
                	jsonObject.add(inventory);
                }*/
                
                 in.close();
                 


                 }catch(Exception e)
                 {
                     Log.d("my","err="+e.toString());
                 }
    		

    		
    	}

    
    
 
    }