package maestrovs.androidofficiant.essences;

public class Inventory  extends JorHeadTable{
	
	int id=-1;
	String num="";
	String datetime="";

	private int docType=0;



	
	public Inventory(int id, int grpId, String num, String datetime, Subdivision subdivision, int docState, int docType) {
		super(id, grpId,  num,datetime, subdivision,  docState);
		this.id = id;
		this.num = num;
		this.datetime = datetime;

		this.docType=docType;
	}

	public int getId() {
		return id;
	}

	public String getNum() {
		return num;
	}

	public String getDatetime() {
		return datetime;
	}




	public int getDocType() {
		return docType;
	}
}
