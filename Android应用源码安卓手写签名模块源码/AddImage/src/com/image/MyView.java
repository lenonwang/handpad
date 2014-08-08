package com.image;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

	private Paint mPaint;

	private static final float MINP = 0.25f;
	private static final float MAXP = 0.75f;

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;

	private long mStartTime;
	private long mEndTime;

	private List<Bitmap> mBitmaps;

	private boolean isStart = false;

	private Handler mHandler;
	private Context mContext;

	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				mBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
						mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
				mCanvas.setBitmap(mBitmap);
				invalidate();
				AddImageActivity activity = (AddImageActivity) mContext;
				activity.test();
			}
		};
	}

	public MyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setPaint(Paint paint) {
		this.mPaint = paint;
	}

	public void setBitmapMap(List<Bitmap> bitmaps) {
		mBitmaps = bitmaps;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0x00000000);

		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

		canvas.drawPath(mPath, mPaint);
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mEndTime = System.currentTimeMillis();
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			mEndTime = System.currentTimeMillis();
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			mEndTime = System.currentTimeMillis();
			touch_up();
			invalidate();
			if (!isStart) {
				isStart = true;
				new Thread() {
					public void run() {
						while (isStart) {
							long time = System.currentTimeMillis();
							if (time >= mEndTime + 1000) {
								if (mBitmaps != null) {
									int width = mBitmap.getWidth();
									int height = mBitmap.getHeight();
									Matrix matrix = new Matrix();
									float scaleWidht = ((float) 60 / width);
									float scaleHeight = ((float) 80 / height);
									matrix.postScale(scaleWidht, scaleHeight);
									Bitmap newbmp = Bitmap.createBitmap(mBitmap, 0, 0, width, height,
											matrix, true);
									mBitmaps.add(newbmp);
								}
								mHandler.sendEmptyMessage(0);
								isStart = false;
							}
						}
					};
				}.start();
			}
			break;
		}
		return true;
	}

}
