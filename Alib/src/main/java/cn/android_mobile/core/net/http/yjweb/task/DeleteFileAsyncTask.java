package cn.android_mobile.core.net.http.yjweb.task;

import cn.android_mobile.core.net.http.yjweb.BaseDao;
import cn.android_mobile.core.net.http.yjweb.IServiceListener;

public class DeleteFileAsyncTask extends BaseServiceAsyncTask {
	private IServiceListener listener;
	private String fileUrl;
	public DeleteFileAsyncTask(String fileUrl){
		this.fileUrl = fileUrl;
		dao=new BaseDao(null);
	}
	public void setListener(IServiceListener listener){
		this.listener=listener;
	}
	@Override
	protected Object doInBackground(Void... arg0) {
		boolean b = dao.deleteFile(fileUrl);
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
