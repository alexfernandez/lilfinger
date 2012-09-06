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
	public static File shared = getFile("shared.png");

	/**
	 * Write a bitmap to the SD card. Create the folder if necessary.
	 * @param bitmap the image to store.
	 * @return true if the file was saved.
	 */
	public static boolean writeToSD(Bitmap bitmap)
	{
		String filename = "lilfinger-" + fileFormat.format(new Date()) + ".png";
		File destination = getFile(filename);
		return writeToSD(bitmap, destination);
	}

	/**
	 * Write a bitmap to the SD card. Create the folder if necessary.
	 * @param bitmap the image to store.
	 * @return true if the file was saved.
	 */
	public static boolean shareToSD(Bitmap bitmap)
	{
		return writeToSD(bitmap, shared);
	}

	/**
	 * Write a bitmap to the SD card. Create the folder if necessary.
	 * @param bitmap the image to store.
	 * @param destination the file to write into.
	 * @return the file into which it was written, or null if not saved.
	 */
	public static boolean writeToSD(Bitmap bitmap, File destination)
	{
		if (!canWriteOnSD())
		{
			return false;
		}
		try
		{
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
	 * Get the file that corresponds to a URL.
	 * @param filename for the file.
	 * @return a file.
	 */
	public static File getFile(String filename)
	{
		File directory = new File(Environment.getExternalStorageDirectory(), "LilFinger/");
		if (!directory.exists())
		{
			directory.mkdir();
		}
		return new File(directory, filename);
	}

	/**
	 * Checks whether the app can write on SD card or not
	 * @return true if the app can write on SD.
	 */
	private static boolean canWriteOnSD()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
}
