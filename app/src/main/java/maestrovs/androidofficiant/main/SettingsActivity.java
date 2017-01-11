package maestrovs.androidofficiant.main;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import maestrovs.androidofficiant.R;
import maestrovs.androidofficiant.application.MainApplication;


public class SettingsActivity extends Activity  {
	
	

ImageButton backButton = null;

FragmentManager fragmentManager = getFragmentManager();


EditText editIp ;
EditText editPort;

Button settingsProfileBt;
Button settingsEmailBt;
Button settingsNotificationsBt;
Button settingsSubscriptionBt;
Button settingsHelpBt;
Button settingsAboutAppBt;
Button settingsWriteRewBt;
Button settingsLogoutBt;

	Button saveButton;

ToggleButton loadOnWiFiBt;

	IPAdddressText idAdrFilter;

private boolean isLogined = true;

	Handler exitH = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.d("my", "settings1");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		setContentView(R.layout.activity_settings1);
		Log.d("my", "settings12");
		backButton =(ImageButton) findViewById(R.id.settingsBackBt);


		 saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveButton.setEnabled(false);

				String tempIp =editIp.getText().toString();
				String tempPort =editPort.getText().toString();
				ip=tempIp;
				new Thread(){
					public void run() {

						MainApplication.dbHelper.insertOrReplaceOption("IP",ip);
						MainApplication.dbHelper.insertOrReplaceOption("PORT",port);
						//MainActivity.dbHelper.setServerIp(ip);
						//MainActivity.dbHelper.setServerPort(port);
						MainApplication.serverIP=ip;
						MainApplication.serverPort=port;

						exitH.post(new Runnable() {
							@Override
							public void run() {

								saveButton.setEnabled(true);
							}
						});
					};
				}.start();
			}
		});



		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				Log.d("my","finish");
				finish();

			}
		});
		
		editIp = (EditText)findViewById(R.id.editIP);
		//editIp.setOnFocusChangeListener(editIPFocusChangeListener);


		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start,
									   int end, Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart) +
							source.subSequence(start, end) +
							destTxt.substring(dend);
					if (!resultingTxt.matches ("^\\d{1,3}(\\." +
							"(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
						return "";
					} else {
						String[] splits = resultingTxt.split("\\.");
						for (int i=0; i<splits.length; i++) {
							if (Integer.valueOf(splits[i]) > 255) {
								return "";
							}
						}
					}
				}
				return null;
			}
		};
		editIp.setFilters(filters);

		//idAdrFilter = (main.IPAdddressText)findViewById(R.id.ipishka);

		
		editPort = (EditText)findViewById(R.id.editPort);
		//editPort.setOnFocusChangeListener(editPortFocusChangeListener);



		Log.d("my","settings-----------13");
		LoadIpPort loadIpPort = new LoadIpPort();
		loadIpPort.start();
		Log.d("my","settings13");
		}
	
	Handler readyLoadIpPortHandler=new Handler();
	String ip = "";
	String port="";
	
	
	class LoadIpPort extends Thread
	{
		@Override
		public void run() {
			
			 ip = MainApplication.dbHelper.getOption("IP");//getServerIp();
			 port = MainApplication.dbHelper.getOption("PORT");//getServerPort();
			 
			Log.d("my","IPPORT = "+ip+port);
			readyLoadIpPortHandler.post(new Runnable() {
				@Override
				public void run() {
					editIp.setText(ip);
					editPort.setText(port);
				}
			});
		}
	}


	

	OnFocusChangeListener editIPFocusChangeListener =new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			Log.d("my","has focus = "+hasFocus);
			if(!hasFocus){
			EditText ed = (EditText) v;
			String tempIp =ed.getText().toString();
			if(!ip.equals(tempIp))
			{
				Log.d("my","write ip");
				ip=tempIp;
				new Thread(){
					public void run() {
						MainApplication.dbHelper.setServerIp(ip);
					};
				}.start();
			}
				
			}
			
		}
	};
	
OnFocusChangeListener editPortFocusChangeListener =new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			Log.d("my","has focus = "+hasFocus);
			if(!hasFocus){
				EditText ed = (EditText) v;
				String tempPort =ed.getText().toString();
				if(!port.equals(tempPort))
				{
					
					port=tempPort;
					new Thread(){
						public void run() {
							MainApplication.dbHelper.setServerPort(port);
						};
					}.start();
					
				}
					
				}
			
		}
	};
	
	





}
