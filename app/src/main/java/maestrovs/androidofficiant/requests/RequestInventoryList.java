package maestrovs.androidofficiant.requests;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import maestrovs.androidofficiant.essences.Inventory;
import maestrovs.androidofficiant.essences.Subdivision;
import maestrovs.androidofficiant.application.MainApplication;


public class RequestInventoryList {
	
	//private static  String mainURL = "http://"+StaticParameterNames.SERVER_IP+":"+StaticParameterNames.SERVER_PORT+"/BarcodeServer3";
 

    
    ArrayList<Inventory> inventoryList = new ArrayList<>();
    
    
    
    public ArrayList<Inventory> getInventoryList()
    {
    	inventoryList.clear();
        request();
    	
		return inventoryList;
    }
   

    public void request() {

            String  returnString="";
    		
    		 try{
                 URL url = new URL(MainApplication.mainURL+"/InventorysListServlet");
                 URLConnection connection = url.openConnection();

                 JSONObject jsonObject = new JSONObject();

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
                 
                 JSONArray jsonArray = new JSONArray(returnString);
                 
               
                 Log.d("my","==="+jsonArray.length());
                 
                for(int i=0;i<jsonArray.length();i++)
                {
                	JSONObject obj = jsonArray.getJSONObject(i);
                	
                	int id = obj.getInt("id");
                	String num = obj.getString("num");
                	String datetime = obj.getString("datetime");
                	String subdivision = obj.getString("subdivision");

                    Subdivision subdivision1 = new Subdivision(0,0,subdivision);
                	Inventory inventory = new Inventory(id,0, num, datetime, subdivision1,0,Inventory.INVENTORY);
                	inventoryList.add(inventory);
                }
                
                 in.close();
                 


                 }catch(Exception e)
                 {
                     Log.d("my","err="+e.toString());
                 }
    		

    		
    	}

    
    
 
    }