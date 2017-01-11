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

import maestrovs.androidofficiant.essences.Good;
import maestrovs.androidofficiant.application.MainApplication;


public class RequestGoodsList {
	

 
    EditText inputValue=null;
    Integer doubledValue =0;
    Button doubleMe;
 
    
    private int type =0;
    

    
    ArrayList<Good> goodsList = new ArrayList<Good>();
    
    int grp_id=-1;
    int invId=-1;

    int offset=-1;
    int limit=-1;

    /**
     *
     * @param grp_id
     * @param invId
     * @param offset
     * @param limit
     * @return
     */
    public ArrayList<Good> getGoodsList(int grp_id, int invId,int offset, int limit)
    {
        type =0;
        goodsList.clear();

        this.grp_id=grp_id;
        this.invId=invId;
        this.offset=offset;
        this.limit=limit;

        request();

        return goodsList;
    }
    
    /**
     * 
     * @param grp_id
     * @param invId
     * @return
     */
    public ArrayList<Good> getGoodsList(int grp_id, int invId)
    {
    	type =0;
    	goodsList.clear();

    	this.grp_id=grp_id;
    	this.invId=invId;

		request();
    	
		return goodsList;
    }
    
    
    String barcode="";
    String article="";
    String name="";
    /**
     * 
     * @param invId
     * @param barcode
     * @param article
     * @return
     */
    public ArrayList<Good> getGoodsListByBarcode(int invId,String barcode, String article,String name)
    {
    	type =1;
    	goodsList.clear();

    	this.invId=invId;
    	this.barcode=barcode;
    	this.article=article;
    	this.name=name;

		request();

		return goodsList;
    }
   



	public void request() {
			String  returnString="";
    		String urlStr="";
    		 if(type==0)urlStr= MainApplication.mainURL+"/GoodsListServlet";
    		 if(type==1)urlStr= MainApplication.mainURL+"/GoodsByBarcodeServlet";
    		
    		
    		
    		
    		 try{
                 URL url = new URL(urlStr);
                 URLConnection connection = url.openConnection();

                 String inputString ="";// Integer.toString(grp_id);
                 //inputString = URLEncoder.encode(inputString, "UTF-8");
                 JSONObject jsonObject = new JSONObject();

                 jsonObject.put("device_id", MainApplication.device_id);
                 jsonObject.put("device_name", MainApplication.device_name);
                
                 if(type==0){
                 jsonObject.put("invId", invId);
                 jsonObject.put("grp_id", grp_id);

                     if(offset>=0)jsonObject.put("offset", offset);
                     if(limit>=0)jsonObject.put("limit", limit);
                 }
                 if(type==1){
                     jsonObject.put("invId", invId);
                     jsonObject.put("barc", barcode);
                     jsonObject.put("article", article);
                     jsonObject.put("name", name);
                 }
                 
                 inputString= jsonObject.toString();

               //  Log.d("my", inputString);

                 connection.setDoOutput(true);
                 OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                 
                 
                 out.write(inputString);
                 out.close();
                 
               
               
                 BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
              
               
                 doubledValue =0;
                 
                 returnString = in.readLine();
                 
                 JSONArray jsonArray = new JSONArray(returnString);
                 
               
                 Log.d("my","==="+jsonArray.length());
                 
                for(int i=0;i<jsonArray.length();i++)
                {
                	JSONObject obj = jsonArray.getJSONObject(i);
                	
                	int id = obj.getInt("id");
                	int grp_id = obj.getInt("grp_id");
                	String name = obj.getString("name");
                	String unit = obj.getString("unit");
                	String article = obj.getString("article");
                	
                	double fcnt=0;
                	Object obcnt = obj.get("fcnt");
                	if(obcnt instanceof Double ) fcnt= (double) obcnt;
                	if(obcnt instanceof Integer) {
                		int icnt = (int) obcnt;
                		fcnt=icnt;
                	}
                	
                	double out_price=0;
                	 Object obprc = obj.get("out_price");
                	if(obprc instanceof Double ) out_price= (double) obprc;
                	if(obprc instanceof Integer) {
                		int iprc = (int) obprc;
                		out_price=iprc;
                	}

                    String barcode = obj.getString("barcode");
                	
                	Good good = new Good(id, grp_id, name, unit, article);
                	good.setFcnt(fcnt);
                	good.setOut_price(out_price);
                    ArrayList<String> barclist = new ArrayList<>();
                    barclist.add(barcode);
                    good.setBarcodes(barclist);
                	
                	goodsList.add(good);
                }
                
                 in.close();
                 


                 }catch(Exception e)
                 {
                     Log.d("my","err="+e.toString());
                 }
    		
    	}

    
    
 
    }