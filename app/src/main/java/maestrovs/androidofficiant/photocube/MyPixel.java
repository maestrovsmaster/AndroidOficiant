package maestrovs.androidofficiant.photocube;

/**
 * Created by Vasily on 08.11.2016.
 */

public  class MyPixel{

    int x,y;
    int r,g,b,a;

    public MyPixel(int x, int y, int r, int g, int b, int a) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }
}
