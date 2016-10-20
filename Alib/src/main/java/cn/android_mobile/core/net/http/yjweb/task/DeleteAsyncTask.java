package cn.android_mobile.core.net.http.yjweb.task;

import cn.android_mobile.core.net.http.yjweb.BaseDao;
import cn.android_mobile.core.net.http.yjweb.IServiceListener;

public class DeleteAsyncTask extends BaseServiceAsyncTask {
	private String uuid;
	private IServiceListener listener;
	public DeleteAsyncTask(Class<?> c ,String uuid){
		this.uuid = uuid;
		dao=new BaseDao(c);
	}
	public void setListener(IServiceListener listener){
		this.listener=listener;
	}
	@Override
	protected Object doInBackground(Void... arg0) {
		boolean b = dao.delete(uuid);
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
