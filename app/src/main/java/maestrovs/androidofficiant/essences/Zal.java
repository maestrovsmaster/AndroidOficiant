package maestrovs.androidofficiant.essences;

import java.util.ArrayList;

/**
 * Created by userd088 on 08.12.2016.
 */

public class Zal {

    int id;

    String name;

    ArrayList<Stolik> stolikList = new ArrayList<>();

    public Zal(int id,  String name) {
        this.id = id;

        this.name = name;
    }

    public int getId() {
        return id;
    }



    public String getName() {
        return name;
    }

    public ArrayList<Stolik> getStolikList() {
        return stolikList;
    }

    public void setStolikList(ArrayList<Stolik> stolikList) {
        this.stolikList.clear();
        this.stolikList.addAll(stolikList);
    }

    public void addStol(Stolik stolik)
    {
        stolikList.add(stolik);
    }

    public int getMaxTableX()
    {
        int x=0;
        int currentW=0;
        for(Stolik stol:stolikList){
            int currentX=stol.getX();
            if(currentX>x){
                x=currentX;
                currentW=stol.getW();
            }
        }
        return  x+currentW;
    }

    public int getMinTableX()
    {
        int x=getMaxTableX();
        for(Stolik stol:stolikList){
            int currentX=stol.getX();
            if(currentX<x) x=currentX;
        }
        return  x;
    }



    public int getMaxTableY()
    {
        int y=0;
        int currentH=0;
        for(Stolik stol:stolikList){
            int currentY=stol.getY();
            if(currentY>y){
                y=currentY;
                currentH=stol.getH();
            }
        }
        return  y+currentH;
    }

    public int getMinTableY()
    {
        int y=getMaxTableY();
        for(Stolik stol:stolikList){
            int currentY=stol.getY();
            if(currentY<y) y=currentY;
        }
        return  y;
    }


}
