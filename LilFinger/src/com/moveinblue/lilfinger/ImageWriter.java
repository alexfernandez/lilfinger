package com.moveinblue.lilfinger;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

/**
 * A class to store images in external storage (SD card).
 * @author Alex.
 */
public class ImageWriter
{
	private static final String LOG_TAG = ImageWriter.class.getSimpleName();
	private static SimpleDateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");

	/**
	 * Write a bitmap to the SD card. Create the folder if necessary.
	 * @param bitmap the image to store.
	 * @return true if written, false otherwise.
	 */
	public static boolean writeToSD(Bitmap bitmap)
	{
		if (!canWriteOnSD())
		{
			return false;
		}
		try
		{
			File destination = getFile();
			Log.d(LOG_TAG, "Storing " + destination.getName());
			if (!destination.exists())
			{
				destination.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(destination);
			bitmap.compress(CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			return true;
		}
		catch (Exception e)
		{
			Log.e(LOG_TAG, "Cannot write to SD", e);
		}
		return false;
	}

	/**
	 * Checks whether the app can write on SD card or not
	 * @return true if the app can write on SD.
	 */
	private static boolean canWriteOnSD()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * Get the file that corresponds to a URL.
	 * @param url the URL to use as filename.
	 * @return a file.
	 */
	private static File getFile()
	{
		String relativeFile = "lilfinger-" + fileFormat.format(new Date()) + ".png";
		File directory = new File(Environment.getExternalStorageDirectory(), "LilFinger/");
		if (!directory.exists())
		{
			directory.mkdir();
		}
		return new File(directory, relativeFile);
	}
}
