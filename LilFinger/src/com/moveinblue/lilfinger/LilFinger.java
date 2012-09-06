package com.moveinblue.lilfinger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ShareActionProvider.OnShareTargetSelectedListener;
import android.widget.Toast;

/**
 * I paint with my lil' finger.
 * @author Alex.
 */
public class LilFinger extends Activity
{
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
		ShareActionProvider provider = (ShareActionProvider) menu.findItem(R.id.menu_share).getActionProvider();
		// Set the default share intent
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		Uri uri = Uri.fromFile(ImageWriter.shared);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		Log.d(LOG_TAG, "Sharing URI: " + uri.toString());
		provider.setShareIntent(shareIntent);
		provider.setOnShareTargetSelectedListener(new OnShareTargetSelectedListener()
		{
			public boolean onShareTargetSelected(ShareActionProvider source, Intent intent)
			{
				PaintingView painting = (PaintingView) findViewById(R.id.painting);
				if (!painting.prepareShare(intent))
				{
					return true;
				}
				return false;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_save:
			PaintingView painting = (PaintingView) findViewById(R.id.painting);
			if (painting.save())
			{
				Toast.makeText(this, "Image saved!", Toast.LENGTH_SHORT).show();
				return true;
			}
			return false;
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
