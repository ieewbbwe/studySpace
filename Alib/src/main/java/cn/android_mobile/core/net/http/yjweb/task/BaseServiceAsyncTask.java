package cn.android_mobile.core.net.http.yjweb.task;

import cn.android_mobile.core.net.http.yjweb.BaseDao;
import android.os.AsyncTask;

public abstract class BaseServiceAsyncTask extends AsyncTask<Void, Void, Object>{
	protected BaseDao dao=null;
	
	@Override
	protected void onCancelled(Object result) {
		dao.cancel();
	}
	@Override
	protected void onCancelled() {
		dao.cancel();
		super.onCancelled();
	}
	
}
