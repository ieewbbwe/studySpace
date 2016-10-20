package cn.android_mobile.core.net.http.yjweb.task;

import cn.android_mobile.core.net.http.yjweb.BaseDao;
import cn.android_mobile.core.net.http.yjweb.IServiceListener;
import cn.android_mobile.core.net.http.yjweb.PageList;
import cn.android_mobile.core.net.http.yjweb.QueryCondition;

import com.google.gson.reflect.TypeToken;

public class QueryAsyncTask extends BaseServiceAsyncTask {
	private QueryCondition qc;
	private TypeToken type;
	private IServiceListener listener;
	public QueryAsyncTask(Class<?> c ,QueryCondition qc,TypeToken type){
		this.qc = qc;
		dao=new BaseDao(c);
		this.type=type;
	}
	public void setListener(IServiceListener listener){
		this.listener=listener;
	}
	@Override
	protected Object doInBackground(Void... arg0) {
		PageList<?> query = dao.query(qc,type);
		return query;
	}
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if(listener!=null){
			listener.onServiceResult(result);
		}
	}
}
