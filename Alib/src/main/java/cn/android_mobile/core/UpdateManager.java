package cn.android_mobile.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import cn.android_mobile.toolkit.Lg;

public class UpdateManager {
	private Context mContext;
	private String apkUrl = "";
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	private static final String savePath = "/sdcard/";
	private static final String saveFileName = savePath + "dingkesen.apk";
	private ProgressBar mProgress;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress;
	private Thread downLoadThread;
	private boolean interceptFlag = false;

	public interface IUpdateManager {
		public void updateListener();
	}

	private IUpdateManager listener;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	public void startUpdateInfo(String url,String date,String msg, boolean flag,
			UpdateManager.IUpdateManager listener) {
		apkUrl = url;
		this.listener = listener;
		showNoticeDialog(date,msg,flag);
	}

	@SuppressLint("NewApi")
	private void showNoticeDialog(String date,String msg,final boolean flag) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("发现新版本("+date+")");
		builder.setMessage(msg);
		builder.setPositiveButton("升级", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		if (flag == false) {
			builder.setNegativeButton("忽略", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Lg.print(flag);
					dialog.dismiss();
				}
			});
		}
		builder.setCancelable(!flag);
		builder.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				if (listener != null) {
					listener.updateListener();
				}
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("下载");
		// final LayoutInflater inflater = LayoutInflater.from(mContext);
		// View v = inflater.inflate(R.layout.progress, null);
		// mProgress = (ProgressBar)v.findViewById(R.id.progressBar);
		mProgress = new ProgressBar(mContext, null,
				android.R.attr.progressBarStyleHorizontal);
		mProgress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		builder.setView(mProgress);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
