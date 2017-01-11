package maestrovs.androidofficiant.photocube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Vasily on 08.11.2016.
 */

public class GetPixelColor {

    int bitmapId=0;

    private Bitmap bitmap;

    private int bitmap_w=0,bitmap_h=0;

    ArrayList<ArrayList<MyPixel>> pixelsList = new ArrayList<>();



    public GetPixelColor(Context context, int bitmapId) {
        this.bitmapId = bitmapId;

        bitmap = BitmapFactory.decodeStream(
                context.getResources().openRawResource(bitmapId));


        bitmap_w=bitmap.getWidth();
        bitmap_h=bitmap.getHeight();
        Log.d("my","bitmap size w= "+bitmap_w+" h="+bitmap_h);

        bitmap = scaleBitmap(bitmap,128,128);

        bitmap_w=bitmap.getWidth();
        bitmap_h=bitmap.getHeight();
        Log.d("my","NEW ---bitmap size w= "+bitmap_w+" h="+bitmap_h);

        for(int y=0;y<bitmap_h;y++) {

            ArrayList<MyPixel> xList = new ArrayList<>();

            for (int x = 0; x < bitmap_w; x++) {


                int colour = bitmap.getPixel(x, y);

                int r= Color.red(colour);
                int g = Color.green(colour);
                int b = Color.blue(colour);


                int a = Color.alpha(colour);



                MyPixel myPixel = new MyPixel(x,y,r,g,b,a);
                xList.add(myPixel);
            }
            pixelsList.add(xList);
        }
        Log.d("my","myscaleBitmap size="+pixelsList.size());
    }

    /**
     *
     * @param koef
     * @return
     */
    public  ArrayList<ArrayList<MyPixel>> getApproximatesList(float koef)
    {
        ArrayList<ArrayList<MyPixel>> approximatesList = new ArrayList<>();

        int stepX = (int)koef*bitmap_w;
        int stepY = (int)koef*bitmap_h;

        for(int y=0;y<bitmap_h;y+=stepY) {

            ArrayList<MyPixel> xList = new ArrayList<>();

            for (int x=0;x<bitmap_w;x+=stepX) {

                int dx=x+stepX;
                int dy=y+stepY;

                int cnt=0;
                int sr=0,sg=0,sb=0,sa=0;
                int rr=0,gg=0,bb=0,aa=0;

              /*  for(int q=dy;q<dy;q++)
                {
                    for(int f=x;f<dx;f++)
                    {
                        int colour = bitmap.getPixel(f, q);

                        int r= Color.red(colour);
                        int g = Color.green(colour);
                        int b = Color.blue(colour);

                        int a = Color.alpha(colour);

                        sr+=r;
                        sg+=g;
                        sb+=b;
                        cnt++;

                    }
                }*/

                if(cnt==0) cnt=1;
                rr=sr/cnt;
                gg=sg/cnt;
                bb=sb/cnt;
                aa=sa/cnt;

                MyPixel myPixel = new MyPixel(0,0,rr,gg,bb,aa);

                xList.add(myPixel);

            }
            approximatesList.add(xList);
        }

      //  Log.d("my","appr list size = "+approximatesList.size());
      //  Log.d("my","appr list    = "+approximatesList.toString());

        return  approximatesList;
    }


    private Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {

        if(bitmapToScale == null)
            return null;
        //get the original width and height
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(newWidth / width, newHeight / height);

        // recreate the new Bitmap and set it back
        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);

    }




}
