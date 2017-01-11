package maestrovs.androidofficiant.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;



import java.util.ArrayList;

import maestrovs.androidofficiant.R;
import maestrovs.androidofficiant.essences.GoodGRP;


public class GoodsGrpListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<GoodGRP> goodGRPs;


	OnClickListener onClickListener=null;
	
	
	public GoodsGrpListAdapter(Context context, ArrayList<GoodGRP> navDrawerItems, OnClickListener onClickListener){
		this.context = context;
		this.goodGRPs = navDrawerItems;
		this.onClickListener=onClickListener;
		Log.d("my","goods drawer adapter");


	}

	@Override
	public int getCount() {
		return goodGRPs.size();
	}

	@Override
	
	public Object getItem(int position) {		
		return goodGRPs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		


            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
           int layoutId= R.layout.row_grp_item;
         convertView = mInflater.inflate(layoutId, null);
         String title ="";
         if(position==0) title+="/";
         int id=0;
		Button tw = new Button(context);
         if(position< goodGRPs.size()){


         GoodGRP goodGRP = goodGRPs.get(position);
 		 title += goodGRP.getName();
 		 
 		 id=goodGRP.getId();

         
         //Button tw = (Button) convertView.findViewById(R.id.grpName);
			/* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
			 params.weight = 1.0f;
			 params.gravity = Gravity.RIGHT;

			 tw.setLayoutParams(params);*/

         tw.setText(title);
         tw.setTag(goodGRP);
		if(goodGRP.isCurrent()){ tw.setTextColor(context.getResources().getColor(R.color.white));
			tw.setBackgroundColor(context.getResources().getColor(R.color.transparent_90));}
			 else{ tw.setTextColor(context.getResources().getColor(R.color.very_ligth_gray));
			tw.setBackgroundColor(context.getResources().getColor(R.color.dark_gray_a30));}
         
         if(position==0) {
			 tw.setTextColor(context.getResources().getColor(R.color.indigo_100));
			 tw.setBackgroundColor(context.getResources().getColor(R.color.indigo_500_a90));
		 }

			Drawable img = context.getResources().getDrawable(R.drawable.ic_arrow_back_white_36dp);
			tw.setCompoundDrawables(img,null,null,null);
         
         tw.setOnClickListener(onClickListener);
         
         
        }


		
		//return convertView;
		return  tw;
		
	}
	



}
