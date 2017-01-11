package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 28.04.2016.
 */
public class BillIn extends JorHeadTable {

    public static final int NORMAL=0;
    public static final int RETURN=1;

    //public static final int IS_TAX_IN=1;

    private Employee employee=null;
    private int isReturn=0;
    private int isTaxIn=0;
    private String supplierNum="";
    private Organization organization=null;

    public BillIn(int id, int grpId,String num, String dateTime, String supplierNum, Subdivision subdivision, int docState, Employee employee, Organization organization, int isTaxIn, int isReturn) {
        super(id, grpId,  num,dateTime, subdivision, docState);
        this.employee = employee;
        this.isReturn = isReturn;
        this.supplierNum=supplierNum;
        this.organization=organization;
        this.isTaxIn=isTaxIn;
    }

    public Employee getEmployee() {
        return employee;
    }

    public int getIsReturn() {
        return isReturn;
    }

    public String getSupplierNum() {
        return supplierNum;
    }

    public Organization getOrganization() {
        return organization;
    }








}
