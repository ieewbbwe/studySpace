package cn.android_mobile.core.net.http.yjweb.task;

import cn.android_mobile.core.net.http.yjweb.BaseDao;
import cn.android_mobile.core.net.http.yjweb.IServiceListener;

public class UploadAsyncTask extends BaseServiceAsyncTask {
	private IServiceListener listener;
	private String filePath;
	public UploadAsyncTask(String filePath){
		this.filePath = filePath;
		dao=new BaseDao(null);
	}
	public void setListener(IServiceListener listener){
		this.listener=listener;
	}
	@Override
	protected Object doInBackground(Void... arg0) {
		String b = dao.upload(filePath);
		return b;
	}
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if(listener!=null){
			listener.onServiceResult(result);
		}
	}
}
