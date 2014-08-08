package com.image;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MyBaseAdapter extends BaseAdapter {
	private Context mContext;
	private List<Bitmap> mBitmaps;

	public MyBaseAdapter(Context context, List<Bitmap> bitmaps) {
		this.mContext = context;
		this.mBitmaps = bitmaps;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBitmaps.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageView = null;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setLayoutParams(new GridView.LayoutParams(48, 48));
			imageView.setBackgroundResource(R.drawable.backgrounda);
			convertView = imageView;
		} else {
			imageView = (ImageView) convertView;
		}
		int count = mBitmaps.size();
		if (position < count) {
			Bitmap bitmap = mBitmaps.get(position);
			if (bitmap!= null) {
				imageView.setImageBitmap(bitmap);
			}
		} else {
			imageView.setImageResource(R.drawable.edit);
		}
		return convertView;
	}
}
