package maestrovs.androidofficiant.essences;



public class GoodGRP extends DicTable{
	
	private int id;
	private int grpId;
	private String name;

	private boolean current = false;
	private boolean hasChild = false;
	private boolean parent = false;


	//ArrayList<String> barcodes = new ArrayList<String>();

	public GoodGRP(int id, int parentGrpId, String name) {
		super(id, parentGrpId,name);

		this.id = id;
		this.grpId = parentGrpId;
		this.name = name;
		
	}

	

	public int getId() {
		return id;
	}

	public int getParentGrpId() {
		return grpId;
	}

	public String getName() {
		return name;
	}


	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public boolean isParent() {
		return parent;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}
}
