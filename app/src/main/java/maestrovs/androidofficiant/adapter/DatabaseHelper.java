package maestrovs.androidofficiant.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import maestrovs.androidofficiant.application.MainApplication;
import maestrovs.androidofficiant.essences.Good;
import maestrovs.androidofficiant.essences.GoodGRP;
import maestrovs.androidofficiant.essences.Inventory;
import maestrovs.androidofficiant.essences.Subdivision;


public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

	public static final String DATABASE_NAME = "mydatabase.db";

	public static final String DATABASE_SETTINGS_TABLE = "DATABASE_SETTINGS_TABLE";
	public static final String PARAMETER = "PARAMETER";
	public static final String VALUE = "VALUE";

	private static final String DATABASE_CREATE_SCRIPT1 = "CREATE TABLE "
			+ DATABASE_SETTINGS_TABLE + "(" + BaseColumns._ID
			+ " integer primary key autoincrement, " + PARAMETER
			+ " text not null," + VALUE + "      text  )";

	public static final String DIC_GOODS_GRP = "DIC_GOODS_GRP";
	private static final String DATABASE_CREATE_DIC_GOODS_GRP = "CREATE TABLE "
			+ DIC_GOODS_GRP + "( ID integer primary key not null, PARENT_ID integer, GRP_NAME text not null )";


	public static final String DIC_GOODS = "DIC_GOODS";
	private static final String DATABASE_CREATE_DIC_GOODS = "CREATE TABLE "
			+ DIC_GOODS + "( GID integer primary key not null, GRP_ID integer, GNAME text not null, ARTICLE text not null, UNIT text not null, BARCODE text, OUT_PRICE  real)";

	public static final String JOR_INVENTORY = "JOR_INVENTORY";
	private static final String DATABASE_CREATE_JOR_INVENTORY = "CREATE TABLE "
			+ JOR_INVENTORY + "( ID integer primary key not null, NUM varchar not null , DATETIME string not null , SUBDIV  string not null, DOC_TYPE int )";

	public static final String AC_GOODS_CNT = "AC_GOODS_CNT";
	private static final String DATABASE_CREATE_AC_GOODS_CNT = "CREATE TABLE "
			+ AC_GOODS_CNT + "( ID integer primary key autoincrement, DOCK_ID integer not null,  GOOD_ID integer not null , COUNT real not null )";
	public static final String AC_GOODS_INDEX = "AC_GOODS_INDEX ";
	private static final String DATABASE_CREATE_ACC_UNIQ =  "CREATE UNIQUE INDEX "+AC_GOODS_INDEX +" ON "+AC_GOODS_CNT+"(GOOD_ID);";






	public static final String DATABASE_USER_ACCOUNT_TABLE = "USER_ACCOUNT";
	public static final String UNIQ_UID_COLUMN = "UNIQ_UID";
	public static final String LOGIN_COLUMN = "LOGIN";
	public static final String PASSWORD_COLUMN = "PASSWORD";

	private static final String DATABASE_CREATE_SCRIPT2 = "CREATE TABLE "
			+ DATABASE_USER_ACCOUNT_TABLE + "(" +  UNIQ_UID_COLUMN
			+ " text not null," + LOGIN_COLUMN + " text not null,"
			+ PASSWORD_COLUMN + " text  NOT null)";


	public static final String OPTIONS_TABLE = "OPTIONS_TABLE";
	public static final String OPTION_PARAM = "OPTION_PARAM";
	public static final String OPTION_VALUE = "OPTION_VALUE";

	private static final String DATABASE_CREATE_OPTIONS_TABLE_SCRIPT = "CREATE TABLE " + OPTIONS_TABLE + "(" +
			BaseColumns._ID
			+ " integer primary key autoincrement, " +
			OPTION_PARAM + " text unique  not null," +
			OPTION_VALUE + "      text  NOT NULL)";




	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("my", "execute scripts");
		db.execSQL(DATABASE_CREATE_SCRIPT1);

		db.execSQL(DATABASE_CREATE_SCRIPT2);

		db.execSQL(DATABASE_CREATE_DIC_GOODS);

		db.execSQL(DATABASE_CREATE_JOR_INVENTORY);

		db.execSQL(DATABASE_CREATE_DIC_GOODS_GRP);

		db.execSQL(DATABASE_CREATE_AC_GOODS_CNT);
		db.execSQL(DATABASE_CREATE_ACC_UNIQ);

		db.execSQL(DATABASE_CREATE_OPTIONS_TABLE_SCRIPT);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		Log.w("SQLite", "DB old version: " + oldVersion + " , new version "
				+ newVersion);


		db.execSQL("DROP TABLE IF IT EXIST " + DATABASE_SETTINGS_TABLE);

		db.execSQL("DROP TABLE IF IT EXIST " + DATABASE_USER_ACCOUNT_TABLE);

		db.execSQL("DROP TABLE IF IT EXIST " + DIC_GOODS );

		db.execSQL("DROP TABLE IF IT EXIST " + JOR_INVENTORY );

		db.execSQL("DROP TABLE IF IT EXIST " + DIC_GOODS_GRP );
		db.execSQL("DROP TABLE IF IT EXIST " + AC_GOODS_CNT);

		db.execSQL("DROP TABLE IF IT EXIST " + OPTIONS_TABLE);




		onCreate(db);
	}

	public String getServerIp() {
		ready = false;
		executeQueryThread = new Thread(new Runnable() {

			@Override
			public void run() {
				//Log.d("my","run");
				String selectQuery = "SELECT DST.VALUE FROM "
				+DATABASE_SETTINGS_TABLE+" DST WHERE DST.PARAMETER = '"
						+ MainApplication.serverIP + "'";
				
				
				
			//	Log.d("my","qery="+selectQuery);
				SQLiteDatabase db = MainApplication.sdb;
				try {
					Cursor cursor = db.rawQuery(selectQuery, null);
				//	Log.d("my","cursor="+cursor.toString());
					if (cursor.moveToFirst()) {
						do {
							Log.d("my","cursor=ok");
							
							serverIP = cursor.getString(0);
							//Log.d("my","cursor2="+cursor.getString(2));
							Log.d("my","cursor0="+cursor.getString(0));

						} while (cursor.moveToNext());
					}
				} catch (Exception e) {
					Log.d("my","Get Ip err="+e.toString());
				}
				
				ready = true;
			}
		});
		executeQueryThread.start();

		while (!ready) {
			//Log.d("my","***");
		}
		Log.d("my","return");
		return serverIP;

	}

	private String serverIP = null;
	private String serverPort = null;
	private boolean ready = false;
	Thread executeQueryThread = null;

	public String getServerPort() {
		ready = false;
		executeQueryThread = new Thread(new Runnable() {

			@Override
			public void run() {
				String selectQuery = "SELECT DST.VALUE FROM DATABASE_SETTINGS_TABLE DST WHERE DST.PARAMETER = '"
						+ MainApplication.serverPort + "'";
			//	Log.d("my","select port = ="+selectQuery);
				SQLiteDatabase db = MainApplication.sdb;
				
				try {
					Cursor cursor = db.rawQuery(selectQuery, null);

					if (cursor.moveToFirst()) {
						do {
						//	Log.d("my","cursor0 port="+cursor.getString(0));
							serverPort = cursor.getString(0);

						} while (cursor.moveToNext());
					}
					
				} catch (Exception e) {
					Log.d("my","Get Port err="+e.toString());
				}
				
				ready = true;
			}
		});

		executeQueryThread.start();

		while (!ready) {
		}

		return serverPort;

	}

	public void setServerIp(String ip) {
		serverIP = ip;
		executeQueryThread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					String insertQuery = "INSERT INTO  "
							+ DatabaseHelper.DATABASE_SETTINGS_TABLE + " ("
							+ DatabaseHelper.PARAMETER + ","
							+ DatabaseHelper.VALUE + ") VALUES ('"
							+ MainApplication.serverIP + "'" + ",'"
							+ serverIP + "')";
					
					// Log.d("my", "INS = "+insertQuery);
					MainApplication.sdb.execSQL(insertQuery);
				} catch (SQLException e) {
					 Log.d("my", "INS EROOR "+e.toString());
				}
			}
		});
		executeQueryThread.start();
	}

	public void setServerPort(String port) {
		//Log.d("my","serv ins");
		serverPort = port;
		executeQueryThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String insertQuery = "INSERT INTO  "
							+ DatabaseHelper.DATABASE_SETTINGS_TABLE + " ("
							+ DatabaseHelper.PARAMETER + ","
							+ DatabaseHelper.VALUE + ") VALUES ('"
							+ MainApplication.serverPort + "'" + ",'"
							+ serverPort + "')";
				//	Log.d("my", "INS PORT= "+insertQuery);
					MainApplication.sdb.execSQL(insertQuery);
				} catch (SQLException e) {
					 Log.d("my", "INS EROOR "+e.toString());
				}

			}
		});
		executeQueryThread.start();

	}


	//DIC_GOODS_GRP + "( ID integer primary key not null, PARENT_ID integer, GRP_NAME text not null )";
	public void writeGoodGRP(int id, int parent_id, String gname) {

		try {
			String insertQuery = " INSERT INTO  "
					+ DatabaseHelper.DIC_GOODS_GRP + " (ID, PARENT_ID, GRP_NAME) VALUES ("+id+","+parent_id+ ",'"
					+ gname + "')";
			Log.d("my", "insertGoodGRP= "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
		}

	}

	public void clearDic_Goods_GRP() {

		try {
			String insertQuery = " delete from  "+ DIC_GOODS_GRP+";";
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
		}
	}

	public int countOfGoodsGRP() {

		int cnt=0;
		String selectQuery = " select count(dg.GID) from  "+ DIC_GOODS_GRP+" dg";
		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					//	Log.d("my","cursor0 port="+cursor.getString(0));
					cnt = cursor.getInt(0);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}

		return cnt;
	}

	public ArrayList<GoodGRP> getGoodsGRPList()
	{
		ArrayList<GoodGRP> goodList = new ArrayList<>();

		String selectQuery = " select * from  "+ DIC_GOODS_GRP+" dg";



		selectQuery+=" order by dg.GRP_NAME ";

		Log.d("my","select goods = "+selectQuery);

		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					//GID integer primary key not null, GRP_ID integer, GNAME text not null, ARTICLE text not null, UNIT text not null, BARCODE text, OUT_PRICE  real
					int id = cursor.getInt(0);
					int parent_id = cursor.getInt(1);
					String gname = cursor.getString(2);


					GoodGRP goodGRP = new GoodGRP(id,parent_id,gname);
					goodList.add(goodGRP);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}

		Log.d("my","return good List="+goodList.size());
		return  goodList;
	}



//"( GID integer primary key not null, GRP_ID integer, GNAME text not null, ARTICLE text not null, UNIT text not null, BARCODE text, OUT_PRICE  real)";
	/**
	 *
	 * @param gid
	 * @param grp_id
	 * @param article
	 * @param unit
	 * @param barcode
	 * @param out_price
	 */
	public void writeGood(int gid, int grp_id, String gname, String article, String unit, String barcode, double out_price) {

				try {
					String insertQuery = " INSERT INTO  "
							+ DatabaseHelper.DIC_GOODS + " (GID, GRP_ID, GNAME, ARTICLE, UNIT, BARCODE, OUT_PRICE) VALUES ("+gid+","+grp_id+ ",'"
							+ gname + "','"
							+ article + "','"
							+ unit + "','"
							+ barcode + "',"
							+ out_price+")";
						Log.d("my", "insertGood= "+insertQuery);
					MainApplication.sdb.execSQL(insertQuery);
				} catch (SQLException e) {
					Log.d("my", "INS EROOR "+e.toString());
				}

	}


	public void clearDic_Goods() {

		try {
			String insertQuery = " delete from  "+ DIC_GOODS+";";
			//Log.d("my", "insertGood= "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
		}

	}

	public int countOfGoods() {

		int cnt=0;
			String selectQuery = " select count(dg.GID) from  "+ DIC_GOODS+" dg";
			SQLiteDatabase db = MainApplication.sdb;

			try {
				Cursor cursor = db.rawQuery(selectQuery, null);

				if (cursor.moveToFirst()) {
					do {
						//	Log.d("my","cursor0 port="+cursor.getString(0));
						cnt = cursor.getInt(0);

					} while (cursor.moveToNext());
				}

			} catch (Exception e) {
				Log.d("my","Get Port err="+e.toString());
			}

		return cnt;
	}

	//SELECT *
	//WHERE id=MAX(id)
	//FROM history;

	//select dg.GID from  "+ DIC_GOODS+" dg ORDER BY id DESC LIMIT 1
	public int getMaxGoodId() {

		int maxId=0;
		String selectQuery = " select MAX(dg.GID) from  "+ DIC_GOODS+" dg";
		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					//	Log.d("my","cursor0 port="+cursor.getString(0));
					maxId = cursor.getInt(0);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}

		return maxId;
	}

	public int getMaxInventoryId() {

		int maxId=0;
		String selectQuery = " select MAX(dg.ID) from  "+ JOR_INVENTORY+" dg";
		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					//	Log.d("my","cursor0 port="+cursor.getString(0));
					maxId = cursor.getInt(0);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}

		return maxId;
	}






	//(GID, GRP_ID, GNAME, ARTICLE, UNIT, BARCODE, OUT_PRICE

	/**
	 *
	 * @param name
	 * @param article
	 * @param barcode
	 * @return
	 */
	public ArrayList<Good> getGoodList(String name, String article, String barcode, int invId)
	{
		ArrayList<Good> goodList = new ArrayList<>();

		String selectQuery = " select dg.GID , dg.GRP_ID , dg.GNAME , dg.ARTICLE , dg.UNIT , dg.BARCODE , " +
				"("+
				" select gg.COUNT from  "+ AC_GOODS_CNT+" gg " +
				" where gg.DOCK_ID = "+invId+
				" and gg.GOOD_ID = dg.GID"+
				") as FCNT from  "+ DIC_GOODS+" dg";

		if(name.length()>0||article.length()>0||barcode.length()>0 )
		{

			selectQuery+=" where ";
			if(name.length()>0)
			{
				selectQuery+="LOWER(dg.GNAME) LIKE  LOWER('%"+name+"%')";
				if(article.length()>0||barcode.length()>0 ) selectQuery+=" or ";
			}
			if(article.length()>0)
			{
				selectQuery+="dg.ARTICLE LIKE  '%"+article+"%'";
				if(barcode.length()>0 ) selectQuery+=" or ";
			}
			if(barcode.length()>0)
			{
				selectQuery+="dg.BARCODE LIKE  '%"+barcode+"%'";
			}

		}

		selectQuery+=" order by dg.GNAME ";

		Log.d("my","select goods = "+selectQuery);

		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					//GID integer primary key not null, GRP_ID integer, GNAME text not null, ARTICLE text not null, UNIT text not null, BARCODE text, OUT_PRICE  real
					int id = cursor.getInt(0);
					int grp_id = cursor.getInt(1);
					String gname = cursor.getString(2);
					String garticle = cursor.getString(3);
					String gunit = cursor.getString(4);
					String gbarcode= cursor.getString(5);
					double fcnt= cursor.getDouble(6);

					Good good = new Good(id,grp_id,gname,gunit,garticle);
					ArrayList<String> barc = new ArrayList<>();
					barc.add(gbarcode);
					good.setBarcodes(barc);
					good.setFcnt(fcnt);
					goodList.add(good);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}

		Log.d("my","return good List="+goodList.size());
		return  goodList;
	}


	public ArrayList<Good> getGoodListByGrpId(int grp_id, int invId)
	{
		ArrayList<Good> goodList = new ArrayList<>();

		String selectQuery = " select dg.GID , dg.GRP_ID , dg.GNAME , dg.ARTICLE , dg.UNIT , dg.BARCODE , " +
				"("+
				" select gg.COUNT from  "+ AC_GOODS_CNT+" gg " +
				" where gg.DOCK_ID = "+invId+
				" and gg.GOOD_ID = dg.GID"+
				") as FCNT from  "+ DIC_GOODS+" dg ";



			selectQuery+=" where dg.grp_id = "+grp_id;




		selectQuery+=" order by dg.GNAME ";

		Log.d("my","select goods = "+selectQuery);

		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					//GID integer primary key not null, GRP_ID integer, GNAME text not null, ARTICLE text not null, UNIT text not null, BARCODE text, OUT_PRICE  real
					int id = cursor.getInt(0);
					int ggrp_id = cursor.getInt(1);
					String gname = cursor.getString(2);
					String garticle = cursor.getString(3);
					String gunit = cursor.getString(4);
					String gbarcode= cursor.getString(5);
					double fcnt= cursor.getDouble(6);

					Good good = new Good(id,ggrp_id,gname,gunit,garticle);
					ArrayList<String> alb = new ArrayList<>();
					alb.add(gbarcode);
					good.setBarcodes(alb);
					good.setFcnt(fcnt);
					Log.d("my","out price");

					goodList.add(good);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}

		Log.d("my","return good List="+goodList.size());
		return  goodList;
	}


	public ArrayList<Good> getGoodList()
	{
		ArrayList<Good> goodList = new ArrayList<>();

		String selectQuery = " select dg.GID , dg.GRP_ID , dg.GNAME , dg.ARTICLE , dg.UNIT , dg.BARCODE  " +

				" from  "+ DIC_GOODS+" dg ";

		selectQuery+=" order by dg.GNAME ";

		Log.d("my","select goods = "+selectQuery);

		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					//GID integer primary key not null, GRP_ID integer, GNAME text not null, ARTICLE text not null, UNIT text not null, BARCODE text, OUT_PRICE  real
					int id = cursor.getInt(0);
					int ggrp_id = cursor.getInt(1);
					String gname = cursor.getString(2);
					String garticle = cursor.getString(3);
					String gunit = cursor.getString(4);
					String gbarcode= cursor.getString(5);
					double fcnt=0;// cursor.getDouble(6);

					Good good = new Good(id,ggrp_id,gname,gunit,garticle);
					ArrayList<String> alb = new ArrayList<>();
					alb.add(gbarcode);
					good.setBarcodes(alb);
					good.setFcnt(fcnt);
					Log.d("my","out price");

					goodList.add(good);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}

		Log.d("my","return good List="+goodList.size());
		return  goodList;
	}




	//JOR_INVENTORY + "( ID integer primary key not null, NUM integer not null , DATETIME string not null , SUBDIV  string not null, DOC_TYPE int)";

	public String insertInventoryDoc(int id, String num, String datetime, String subdiv, int doc_type )
	{

		try {
			String insertQuery = " INSERT INTO  "
					+ DatabaseHelper.JOR_INVENTORY + " (ID, NUM, DATETIME, SUBDIV, DOC_TYPE ) VALUES ("+id+ ",'"
					+ num + "','"
					+ datetime + "','"
					+ subdiv + "'," +doc_type
					+")";
			Log.d("my", "insert inv hd= "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
			return "ins inv err"+e.toString();
		}

		return "ins inventory ok!";
	}


	public ArrayList<Inventory> getInventoryList() {

		ArrayList<Inventory> invList = new ArrayList<>();
		String selectQuery = " select * from  "+ JOR_INVENTORY;
		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {

					int id = cursor.getInt(0);
					String num = cursor.getString(1);
					String datetime = cursor.getString(2);
					String subdiv = cursor.getString(3);
					int doc_type = cursor.getInt(4);

					Log.d("my","cursor get sql lite inv num = "+num);

					Subdivision subdiv_ = new Subdivision(0,0,subdiv);
					Inventory inventory = new Inventory(id, 0 ,num, datetime, subdiv_ , 0,0);
					invList.add(inventory);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
			Subdivision subdiv_ = new Subdivision(-1,-1,""+e.toString());
			Inventory inventory = new Inventory(-1, -1 ,"err", "err", subdiv_ , 0,0);
			invList.add(inventory);
		}
		Log.d("my","return inv list size="+invList.size());
		return invList;
	}

	public void clear_INVENTORIES() {
		Log.d("my","      ---------===========================--------------");
		Log.d("my","-----=============CLEAR INVENTORIES!!!==============--------------");
		Log.d("my","      ---------===========================--------------");
		try {
			String insertQuery = " delete from  "+ JOR_INVENTORY+";";
			//Log.d("my", "insertGood= "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
		}

	}

	public void clear_INVENTORY(int id) {

		try {
			String insertQuery = " delete from  "+ JOR_INVENTORY+"  where ID = "+id+ " or NUM= "+id;
			//Log.d("my", "insertGood= "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
		}

	}



	//AC_GOODS_CNT + "( ID integer primary key autoincrement, DOCK_ID integer not null,  GOOD_ID integer not null , COUNT real not null )

	public void insertGoodsAcCnt(int invId, int goodId, double cnt)
	{
		try {
			String insertQuery = "insert or replace INTO  "
					+ DatabaseHelper.AC_GOODS_CNT + " ( DOCK_ID, GOOD_ID, COUNT ) VALUES ( "
					+ invId + ","
					+ goodId + ","
					+ cnt + ")";
			Log.d("my", "insert good inv acc = "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
		}
	}

	public double  getGoodCountAcc(int invId, int goodId) {

		double count=-1;
		String selectQuery = " select gg.COUNT from  "+ AC_GOODS_CNT+" gg " +
				" where gg.DOCK_ID = "+invId+
				" and gg.GOOD_ID = "+goodId;
		Log.d("my","query="+selectQuery);
		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					count = cursor.getDouble(0);
					//Log.d("my","cursor get sql lite inv num = "+num);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}
		Log.d("my","acc good count ="+count);
		return count;
	}


	/**
	 * Пока что почти пустой товар. Только ид и кол-во!
	 * @param invId
	 * @return
	 */
	public ArrayList<Good>  getGoodsCountListAcc(int invId) {

		ArrayList<Good> goodsList = new ArrayList<>();
		String selectQuery = " select gg.GOOD_ID, gg.COUNT , (select dgg.GNAME from DIC_GOODS dgg where dgg.GID = gg.GOOD_ID) as GNAME   from    "+ AC_GOODS_CNT+" gg " +
				" where gg.DOCK_ID = "+invId;
		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					int goodId = cursor.getInt(0);
					double count = cursor.getDouble(1);
					String name = cursor.getString(2);
					Good good = new Good(goodId,-1,name,"","");
					good.setFcnt(count);
					goodsList.add(good);
					//Log.d("my","cursor get sql lite inv num = "+num);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}
		Log.d("my","acc good count ="+goodsList.size());
		return goodsList;
	}


	//select ag.DOCK_ID , (select dg.GNAME from DIC_GOODS dg where dg.id = ag.GOOD_ID) as GNAME, (select dg.ARTICLE from DIC_GOODS dg where dg.id = ag.GOOD_ID) as ARTICLE, (select dg.BARCODE from DIC_GOODS dg where dg.id = ag.GOOD_ID) as BARCODE, (select dg.UNIT from DIC_GOODS dg where dg.id = ag.GOOD_ID) as UNIT, ag.COUNT	from AC_GOODS_CNT ag
	/**
	 * Get all good's params
	 * @param invId
	 * @return
	 */
	public ArrayList<Good>  getALLGoodsCountListAcc(int invId) {

		ArrayList<Good> goodsList = new ArrayList<>();
		String selectQuery = " select  gg.GOOD_ID, gg.COUNT , (select dgg.GNAME from DIC_GOODS dgg where dgg.GID = gg.GOOD_ID) as GNAME" +
				" , (select dgg.ARTICLE from DIC_GOODS dgg where dgg.GID = gg.GOOD_ID) as ARTICLE" +
				" , (select dgg.BARCODE from DIC_GOODS dgg where dgg.GID = gg.GOOD_ID) as BARCODE  " +
				" , (select dgg.UNIT from DIC_GOODS dgg where dgg.GID = gg.GOOD_ID) as UNIT   from    "+ AC_GOODS_CNT+" gg " +
				" where gg.DOCK_ID = "+invId;
		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					int goodId = cursor.getInt(0);
					double count = cursor.getDouble(1);
					String name = cursor.getString(2);
					String article = cursor.getString(3);
					String barcode = cursor.getString(4);
					String unit = cursor.getString(5);
					//int dockId = cursor.getInt(0);
					Good good = new Good(goodId,-1,name,unit,article);
					ArrayList<String> barclist = new ArrayList<>();
					barclist.add(barcode);
					good.setBarcodes(barclist);
					good.setFcnt(count);
					goodsList.add(good);
					//Log.d("my","cursor get sql lite inv num = "+num);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my","Get Port err="+e.toString());
		}
		Log.d("my","acc good count ="+goodsList.size());
		return goodsList;
	}


	public void clear_AC_GOODS_CNT() {

		try {
			String insertQuery = " delete from  "+ AC_GOODS_CNT+";";
			//Log.d("my", "insertGood= "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
		}

	}

	public void clearAllForDocID_AC_GOODS_CNT(int docId) {

		try {
			String insertQuery = " delete from  "+ AC_GOODS_CNT+" where DOCK_ID ="+docId+";";
			//Log.d("my", "insertGood= "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
		}

	}

	public void clearGoodForDocID_AC_GOODS_CNT(int docId, int goodId) {

		try {
			String insertQuery = " delete from  "+ AC_GOODS_CNT+"  where  DOCK_ID ="+docId+" and GOOD_ID ="+goodId+";";
			//Log.d("my", "insertGood= "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR "+e.toString());
		}

	}


	/////-----------------------------------------------OPTIONS----------------------------------------

	//public static final String OPTIONS_TABLE = "OPTIONS_TABLE";
	//public static final String OPTION_PARAM = "OPTION_PARAM";
	//public static final String OPTION_VALUE = "OPTION_VALUE";

	public void insertOrReplaceOption(String param, String value) {


		//insert or replace into Book (ID, Name, TypeID, Level, Seen) values
		//((select ID from Book where Name = "SearchName"), "SearchName", ...);

		try {
			String insertQuery = " insert or replace into  "
					+ DatabaseHelper.OPTIONS_TABLE + " (OPTION_PARAM, OPTION_VALUE ) VALUES ('" + param + "','"
					+ value + "')";
			//Log.d("my", "insertGood= "+insertQuery);
			MainApplication.sdb.execSQL(insertQuery);
		} catch (SQLException e) {
			Log.d("my", "INS EROOR " + e.toString());
		}

	}



	//( BOOK_ID integer primary key not null, TITLE text not null, SRC text not null,  OFFSET  real not null, SIZE  real not null )
	public String getOption(String param) {
		ArrayList<String> values = new ArrayList<>();

		String selectQuery = " select  dg.OPTION_VALUE " +
				" from  " + OPTIONS_TABLE + " dg where dg.OPTION_PARAM = '" + param+"'";

		Log.d("my", "select  = " + selectQuery);

		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {

					//int bk_id = cursor.getInt(0);
					String component = cursor.getString(0);
					values.add(component);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my", "Get Port err=" + e.toString());
		}

		Log.d("my", "return bookComponent List=" + values.size());
		if (values.size() > 0) return values.get(0);
		else return null;
	}


	public  String getOrInsertParamFromDB(String param, String defValue)
	{
		String str = getOption(param);
		if (str == null) insertOrReplaceOption(param, defValue);
		else {
			if (str.length() > 0) return str;
			else insertOrReplaceOption(param, defValue);
		}
		return defValue;
	}


	//selectQuery+="LOWER(dg.GNAME) LIKE  LOWER('%"+name+"%')";


	public  String getOrInsertUnitFromDB( String unit)
	{
		String param="unit_"+unit;

		String value = getOrInsertParamFromDB(param, unit);
		return value;
	}

	public ArrayList<String> getUnits() {
		String param="unit_";

		ArrayList<String> values = new ArrayList<>();

		String selectQuery = " select  dg.OPTION_VALUE " +
				" from  " + OPTIONS_TABLE + " dg where dg.OPTION_PARAM LIKE LOWER('%" + param+"%')";

		Log.d("my", "select  = " + selectQuery);

		SQLiteDatabase db = MainApplication.sdb;

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {

					//int bk_id = cursor.getInt(0);
					String component = cursor.getString(0);
					values.add(component);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("my", "Get Port err=" + e.toString());
		}

		Log.d("my", "return bookComponent List=" + values.size());
		 return values;
	}





}