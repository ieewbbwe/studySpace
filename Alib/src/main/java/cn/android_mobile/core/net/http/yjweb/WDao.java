package cn.android_mobile.core.net.http.yjweb;

import java.util.Stack;

import cn.android_mobile.core.net.http.Http;
import cn.android_mobile.core.net.http.yjweb.task.BaseServiceAsyncTask;
import cn.android_mobile.core.net.http.yjweb.task.DeleteAsyncTask;
import cn.android_mobile.core.net.http.yjweb.task.DeleteFileAsyncTask;
import cn.android_mobile.core.net.http.yjweb.task.InsertAsyncTask;
import cn.android_mobile.core.net.http.yjweb.task.QueryAsyncTask;
import cn.android_mobile.core.net.http.yjweb.task.UpdateAsyncTask;
import cn.android_mobile.core.net.http.yjweb.task.UploadAsyncTask;

import com.google.gson.reflect.TypeToken;

public class WDao {
	private Stack<BaseServiceAsyncTask> tasks=new Stack<BaseServiceAsyncTask>();
	public WDao(){
		
	}
	public void query(Class<?> c,QueryCondition qc,TypeToken<?> type,IServiceListener listener){
		QueryAsyncTask task=new QueryAsyncTask(c, qc, type);
		tasks.add(task);
		task.setListener(listener);
		task.execute();
	}
	public void insert(Class<?> c,Object obj,IServiceListener listener){
		InsertAsyncTask task=new InsertAsyncTask(c, obj);
		tasks.add(task);
		task.setListener(listener);
		task.execute();
	}
	public void update(Class<?> c,Object obj,IServiceListener listener){
		UpdateAsyncTask task=new UpdateAsyncTask(c, obj);
		tasks.add(task);
		task.setListener(listener);
		task.execute();
	}
	public void delete(Class<?> c,String uuid,IServiceListener listener){
		DeleteAsyncTask task=new DeleteAsyncTask(c, uuid);
		tasks.add(task);
		task.setListener(listener);
		task.execute();
	}
	public void deleteFile(String fileUrl,IServiceListener listener){
		DeleteFileAsyncTask task=new DeleteFileAsyncTask(fileUrl);
		tasks.add(task);
		task.setListener(listener);
		task.execute();
	}
	public void upload(String filePath,IServiceListener listener){
		UploadAsyncTask task=new UploadAsyncTask(filePath);
		tasks.add(task);
		task.setListener(listener);
		task.execute();
	}
	public void cancel(){
		for (BaseServiceAsyncTask task : tasks) {
			task.cancel(true);
		}
		tasks.clear();
	}
}
