package maestrovs.androidofficiant.essences;

import java.util.ArrayList;

/**
 * Created by Vasily on 30.11.2016.
 */

public class Check {

    int id;
    String num;
    ArrayList<CheckDetail> checkDetails = new ArrayList<>();

    double totalSum=0;
    int clientId;
    int tableId;
    int preprintCnt;
    int employeeId;

    public Check(int id, String num) {
        this.id = id;
        this.num = num;
        checkDetails.clear();
    }

    public int getId() {
        return id;
    }

    public String getNum() {
        return num;
    }

    public ArrayList<CheckDetail> getCheckDetails()
    {
        return checkDetails;
    }

    public void addCheckDetail(CheckDetail checkDetail)
    {
        checkDetails.add(checkDetail);
    }

    public void refreshCheckDetailsList(ArrayList<CheckDetail> checkDetails)
    {
        checkDetails.clear();
        checkDetails.addAll(checkDetails);
    }

    public void removeDetails(CheckDetail detail)
    {
        if(checkDetails.contains(detail)) checkDetails.remove(detail);
    }


    public double getTotalSum() {
        return totalSum;
    }

    public int getClientId() {
        return clientId;
    }

    public int getTableId() {
        return tableId;
    }

    public int getPreprintCnt() {
        return preprintCnt;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public void setPreprintCnt(int preprintCnt) {
        this.preprintCnt = preprintCnt;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
