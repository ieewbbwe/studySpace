package cn.android_mobile.core.net.http.yjweb.task;

import cn.android_mobile.core.net.http.yjweb.BaseDao;
import cn.android_mobile.core.net.http.yjweb.IServiceListener;

public class InsertAsyncTask extends BaseServiceAsyncTask {
	private Object obj;
	private IServiceListener listener;
	public InsertAsyncTask(Class<?> c ,Object obj){
		this.obj = obj;
		dao=new BaseDao(c);
	}
	public void setListener(IServiceListener listener){
		this.listener=listener;
	}
	@Override
	protected Object doInBackground(Void... arg0) {
		boolean b = dao.insert(obj);
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
