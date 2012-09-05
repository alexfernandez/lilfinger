package com.moveinblue.lilfinger;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class LilFinger extends Activity
{
	@SuppressWarnings("unused")
	private static final String LOG_TAG = LilFinger.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paint);
		PaintingView surface = (PaintingView) findViewById(R.id.painting);
		surface.setColor(getResources().getColor(R.color.red));
		ViewGroup group = (ViewGroup) findViewById(R.id.controls);
		for (int i = 0; i < group.getChildCount(); i++)
		{
			View view = group.getChildAt(i);
			switch (view.getId())
			{
			case R.id.cancel:
				view.setOnClickListener(canceller);
				break;
			default:
				view.setOnClickListener(colorChanger);
				break;
			}
		}
	}

	private OnClickListener canceller = new OnClickListener()
	{
		public void onClick(View v)
		{
			PaintingView painting = (PaintingView) findViewById(R.id.painting);
			painting.clear();
		}
	};
	private OnClickListener colorChanger = new OnClickListener()
	{
		public void onClick(View view)
		{
			ColorDrawable background = (ColorDrawable) view.getBackground();
			PaintingView painting = (PaintingView) findViewById(R.id.painting);
			painting.setColor(background.getColor());
			View marker = findViewById(R.id.marker);
			LayoutParams params = (LayoutParams) marker.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_LEFT, view.getId());
			marker.setLayoutParams(params);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.lilfinger_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_save:
			return true;
		case R.id.menu_share:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed()
	{
		PaintingView painting = (PaintingView) findViewById(R.id.painting);
		if (!painting.empty)
		{
			painting.clear();
			return;
		}
		finish();
	}
}
