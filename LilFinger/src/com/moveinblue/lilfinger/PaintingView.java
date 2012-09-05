package com.moveinblue.lilfinger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;

/**
 * A surface to paint upon.
 * @author chenno
 */
public class PaintingView extends View
{
	private static final String LOG_TAG = LilFinger.class.getSimpleName();
	private Paint paint = new Paint();
	private Bitmap bitmap;
	private PointerCoords previous;
	public boolean empty = true;

	public PaintingView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.init();
	}

	public PaintingView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.init();
	}

	public PaintingView(Context context)
	{
		super(context);
		this.init();
	}
	public void init()
	{
		paint.setStrokeWidth(10);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeJoin(Paint.Join.ROUND);
	    paint.setStrokeCap(Paint.Cap.ROUND);
	}

	public void clear()
	{
		this.bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
		this.bitmap.eraseColor(getResources().getColor(R.color.white));
		this.empty = true;
		this.invalidate();
	}

	public void setColor(int color)
	{
		paint.setColor(color);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (this.bitmap == null)
		{
			this.clear();
		}
		Log.d(LOG_TAG, "Painting (" + event.getRawX() + ", " + event.getRawY() + ")");
		this.empty = false;
		Canvas canvas = new Canvas(this.bitmap);
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			this.startTouch(event, canvas);
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			this.continueTouch(event, canvas);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			this.endTouch(event, canvas);
		}
		this.invalidate();
		return true;
	}

	/**
	 * Start a touch gesture.
	 * @param event specifies the coordinates.
	 * @param canvas to write on.
	 */
	private void startTouch(MotionEvent event, Canvas canvas)
	{
		canvas.drawPoint(event.getX(), event.getY(), paint);
		this.previous = new PointerCoords();
		this.previous.x = event.getX();
		this.previous.y = event.getY();
	}

	/**
	 * Draw a line from previous point to here.
	 * @param event specifies the coordinates.
	 * @param canvas to write on.
	 */
	private void continueTouch(MotionEvent event, Canvas canvas)
	{
		if (this.previous == null)
		{
			Log.e(LOG_TAG, "Could not continue touch");
			return;
		}
		canvas.drawLine(this.previous.x, this.previous.y, event.getX(), event.getY(), paint);
		this.previous.x = event.getX();
		this.previous.y = event.getY();
	}

	/**
	 * Stop the current line.
	 * @param event specifies the coordinates.
	 * @param canvas to write on.
	 */

	private void endTouch(MotionEvent event, Canvas canvas)
	{
		this.previous = null;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (bitmap == null)
		{
			return;
		}
		canvas.drawBitmap(bitmap, 0, 0, null);
	}
}
