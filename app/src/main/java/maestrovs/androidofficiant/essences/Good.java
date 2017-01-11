package maestrovs.androidofficiant.essences;

import java.util.ArrayList;


public class Good extends DicTable {
	
	private int id;
	private int grpId;
	private String name;
	private String unit;
	private String article;
	
	private double fcnt=0;
	private double out_price=0;
	
	private boolean changed=false;
	
	ArrayList<String> barcodes = new ArrayList<String>();

	public Good(int id, int grpId, String name, String unit, String article) {
		super(id, grpId);
		this.id = id;
		this.grpId = grpId;
		this.name = name;
		this.unit = unit;
		this.article = article;
	}


	private Unit unit_=null;

	public Good(int id, int grpId, String name, Unit unit, String article) {
		super(id, grpId);
		this.id = id;
		this.grpId = grpId;
		this.name = name;
		this.unit_ = unit;
		this.article = article;
	}

	public ArrayList<String> getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(ArrayList<String> barcodes) {
		this.barcodes.clear();
		this.barcodes.addAll(barcodes);
	}

	public int getId() {
		return id;
	}

	public int getGrpId() {
		return grpId;
	}

	public String getName() {
		return name;
	}

	public String getUnit() {
		return unit;
	}

	public String getArticle() {
		return article;
	}

	public double getFcnt() {
		return fcnt;
	}

	public void setFcnt(double fcnt) {
		this.fcnt = fcnt;
	}

	public double getOut_price() {
		return out_price;
	}

	public void setOut_price(double out_price) {
		this.out_price = out_price;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}


	public Unit getUnit_() {
		return unit_;
	}

	@Override
	public String toString() {
		return "{id:"+id+" , grp:"+grpId+" , name:"+name+" , art:"+article +" , unit:"+unit+" }";
	}
}
