package cn.android_mobile.core.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

/**
 *  <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 */
public class ImageLoaderTask extends AsyncTask<String, Float, ImageLoaderTask.ImageData> {
	public static final String TAG="ImageLoaderTask";
	public static Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	public static ImageFileCache fileCache = null;
	private Bitmap bitmap;
	private BitmapFactory.Options sDefaultOptions;
	private ImageLoaderListener listener;

	private ImageView imageView;
	private HttpURLConnection conn;
	public interface ImageLoaderListener{
		public void callback(ImageLoaderTask.ImageData bitmap);
	}
	public ImageLoaderTask(Context context,ImageLoaderTask.ImageLoaderListener listener) {
		this.listener = listener;
	}
	public ImageLoaderTask(Context context,ImageView imageView) {
		this.imageView=imageView;
	}

	@Override
	protected void onCancelled() {
		if (bitmap != null) {
			bitmap.recycle();
		}
		if(conn!=null){
			conn.disconnect();
		}
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(ImageLoaderTask.ImageData result) {
		super.onPostExecute(result);
		if(listener!=null){
			listener.callback(result);
		}
		if(imageView!=null){
			imageView.setImageBitmap(result.bitmap);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (fileCache == null) {
			fileCache = new ImageFileCache();
		}
	}

	@Override
	protected void onProgressUpdate(Float... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected ImageLoaderTask.ImageData doInBackground(String... params) {
		return getBitmap(params[0].toString());
	}

	public ImageData getBitmap(String url) {
		Bitmap result = getBitmapFromMemoryCache(url);
		if (result == null) {
			result = fileCache.getImage(url);
			if (result == null) {
				result = downloadBitmap(url);
				if (result != null) {
					fileCache.saveBitmap(result, url);
					addBitmapToMemoryCache(url, result);
				}
			} else {
				addBitmapToMemoryCache(url, result);
			}
		}
		ImageData data=new ImageData();
		data.bitmap=result;
		data.url=url;
		data.path=fileCache.getImagePath(url);
		return data;
	}

	private Bitmap downloadBitmap(String url) {
		if (sDefaultOptions == null) {
			sDefaultOptions = new BitmapFactory.Options();
			sDefaultOptions.inPreferredConfig = Bitmap.Config.ARGB_4444; 
			sDefaultOptions.inPurgeable = true;
			sDefaultOptions.inInputShareable = true;
			sDefaultOptions.inDither = true;
			sDefaultOptions.inScaled = true;
			sDefaultOptions.inDensity = DisplayMetrics.DENSITY_MEDIUM;
			int be = sDefaultOptions.outHeight / 20;
			if (be % 10 != 0)
				be += 10;
			be = be / 10;
			if (be <= 0)
				be = 1;
			sDefaultOptions.inSampleSize = be;
		}
		Bitmap bmp = null;
		conn = null;
		ByteArrayOutputStream outStream = null;
		try {
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			conn.setDoOutput(false);
			conn.setDoInput(true);
			// conn.setRequestMethod("GET");
			// conn.setConnectTimeout(5 * 1000);
			// conn.setReadTimeout(5 * 1000);
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inStream = conn.getInputStream();
				float length = conn.getContentLength();
				float count = 0;
				float newPersent = 0;
				outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inStream.read(buffer)) != -1) {
					/*if (flag == false) {
						publishProgress(0f);
						return null;
					}*/
					outStream.write(buffer, 0, len);
					count += len;
					newPersent = (float) (count / length);
					// System.out.println(newPersent);
					/*publishProgress(newPersent);*/
					publishProgress(newPersent);
					// uiHandler.sendMessage(Message.obtain(uiHandler,
					// ON_PROGRESS, newPersent));
				}
				inStream.close();
				outStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
			conn = null;
		}
		if(outStream!=null){
		bmp = BitmapFactory.decodeByteArray(outStream.toByteArray(), 0,
				outStream.toByteArray().length, sDefaultOptions);
		}
		return bmp;
	}
	
	private Bitmap getBitmapFromMemoryCache(String url) {
		Bitmap b = null;
		try {
			if (imageCache.containsKey(url)) { 
				SoftReference<Bitmap> softReference =imageCache
						.get(url);
				if (softReference.get() != null) {
					b = softReference.get();
				} else {
					imageCache.remove(url);
					b = null;
				}
			} else {
				b = null;
			}

		} catch (Exception e) {
			b = null;
		}
		return b;
	}

	private void addBitmapToMemoryCache(String url, Bitmap b) {
		try {
			if (imageCache.containsKey(url)) { 
				SoftReference<Bitmap> softReference = imageCache
						.get(url); 
				if (softReference.get() == null) {
					imageCache.put(url, new SoftReference<Bitmap>(b)); 
				}
			} else {
				imageCache.put(url, new SoftReference<Bitmap>(b));
			}

		} catch (Exception e) {
			Log.v(TAG, e.toString());
		}
	}
	public class ImageFileCache {
		private static final String CACHDIR = "ImgCach";
		private static final String WHOLESALE_CONV = "";//.cach

		private static final int MB = 1024 * 1024;
		private static final int CACHE_SIZE = 10;
		private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

		public ImageFileCache() {
			removeCache(getDirectory());
		}

		public Bitmap getImage(final String url) {
			final String path = getDirectory() + "/"
					+ convertUrlToFileName(url);
			File file = new File(path);
			if (file.exists()) {
				Bitmap bmp = BitmapFactory.decodeFile(path, sDefaultOptions);
				if (bmp == null) {
					file.delete();
				} else {
					updateFileTime(path);
					return bmp;
				}
			}
			return null;
		}

		public String getImagePath(final String url) {
			final String path = getDirectory() + "/"
					+ convertUrlToFileName(url);
			return path;
		}
		public void saveBitmap(Bitmap bm, String url) {
			if (bm == null) {
				return;
			}
			if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
				return;
			}
			String filename = convertUrlToFileName(url);
			String dir = getDirectory();
			File dirFile = new File(dir);
			
			if (!dirFile.exists())
				dirFile.mkdirs();
			File file = new File(dir + "/" + filename);
			System.out.println(file.toString());
			try {
				file.createNewFile();
				OutputStream outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				outStream.flush();
				outStream.close();
			} catch (FileNotFoundException e) {
				Log.w("ImageFileCache", "FileNotFoundException");
			} catch (IOException e) {
				Log.w("ImageFileCache", "IOException");
			}
		}

		private boolean removeCache(String dirPath) {
			File dir = new File(dirPath);
			File[] files = dir.listFiles();
			if (files == null) {
				return true;
			}
			if (!android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				return false;
			}

			int dirSize = 0;
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().contains(WHOLESALE_CONV)) {
					dirSize += files[i].length();
				}
			}

			if (dirSize > CACHE_SIZE * MB
					|| FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
				int removeFactor = (int) ((0.4 * files.length) + 1);
				Arrays.sort(files, new FileLastModifSort());
				for (int i = 0; i < removeFactor; i++) {
					if (files[i].getName().contains(WHOLESALE_CONV)) {
						files[i].delete();
					}
				}
			}

			if (freeSpaceOnSd() <= CACHE_SIZE) {
				return false;
			}

			return true;
		}

		public void updateFileTime(String path) {
			File file = new File(path);
			long newModifiedTime = System.currentTimeMillis();
			file.setLastModified(newModifiedTime);
		}

		private int freeSpaceOnSd() {
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
					.getPath());
			double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
					.getBlockSize()) / MB;
			return (int) sdFreeMB;
		}

		private String convertUrlToFileName(String url) {
			String[] strs = url.split("/");
			return strs[strs.length - 1] + WHOLESALE_CONV;
		}

		private String getDirectory() {
			String dir = getSDPath() + "/" + CACHDIR;
			return dir;
		}

		private String getSDPath() {
			File sdDir = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED); // ��??�?sd��??������
			if (sdCardExist) {
				sdDir = Environment.getExternalStorageDirectory(); // �峰��???�褰�?
				Log.v(TAG,sdDir.toString()	);
			}else{
				Log.v(TAG,"sd card not found");
			}
			if (sdDir != null) {
				return sdDir.toString();
			} else {
				return "";
			}
		}
		private class FileLastModifSort implements Comparator<File> {
			public int compare(File arg0, File arg1) {
				if (arg0.lastModified() > arg1.lastModified()) {
					return 1;
				} else if (arg0.lastModified() == arg1.lastModified()) {
					return 0;
				} else {
					return -1;
				}
			}
		}
	}
	public class ImageData{
		public String url;
		public String path;
		public Bitmap bitmap;
	}
}
