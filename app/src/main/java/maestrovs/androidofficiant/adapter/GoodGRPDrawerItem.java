package maestrovs.androidofficiant.adapter;


import maestrovs.androidofficiant.essences.GoodGRP;

public class GoodGRPDrawerItem {
	
	private String title="";
	private int icon=-1;
	private String count = "0";
	
	private int id=0;
	
	private GoodGRP goodGRP=null;
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	
	//public GoodGRPDrawerItem(){}
	
	public GoodGRPDrawerItem(GoodGRP goodGRP){
		this.goodGRP = goodGRP;
		
	}

	public GoodGRPDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}
	
	public GoodGRPDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public GoodGRPDrawerItem(String title) {
		this.title = title;
	}

	public String getTitle(){
		if(goodGRP!=null) return goodGRP.getName();
		return this.title;
	}
	
	public int getId()
	{
		if(goodGRP!=null) return goodGRP.getId();
		return id;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}

	public GoodGRP getGoodGRP() {
		return goodGRP;
	}
	
	
}
