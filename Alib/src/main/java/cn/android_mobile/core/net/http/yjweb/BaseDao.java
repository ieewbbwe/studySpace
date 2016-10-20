package cn.android_mobile.core.net.http.yjweb;

import java.util.ArrayList;
import java.util.Stack;

import cn.android_mobile.core.net.http.Http;
import cn.android_mobile.toolkit.Escape;
import cn.android_mobile.toolkit.Lg;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * // Gson g=new Gson(); // DBDao dao=new DBDao(user.class); // QueryCondition
 * qc=new QueryCondition(); // PageList pl = dao.query(qc); // List list =
 * pl.getList(); // for (Object object : list) { // user u=(user) object; //
 * dao.delete(u.uuid); //
 * System.out.println(StringTookit.JSONStringFormat(g.toJson(u))); // } Gson g =
 * new Gson(); user user = new user(); user.username="123"; user.password="123";
 * user.address="123"; user.imei="123"; user.is_online="true";
 * user.uuid=UUID.randomUUID().toString().toUpperCase(); ADao d=new
 * ADao(user.class); // d.insert(user);
 * 
 * @SuppressWarnings("unchecked") PageList<user> pl = (PageList<user>)
 *                                d.query(new QueryCondition(),new
 *                                TypeToken<PageList<user>>(){}); for (Object o
 *                                :pl.getList()) { user
 *                                u=(cn.database.create.table.user) o;
 *                                Lg.println
 *                                (StringTookit.JSONStringFormat(g.toJson(u)));
 *                                }
 * @author Administrator
 * 
 */
public class BaseDao {
	private Gson g = new Gson();
	public static String ip = "aoxingsygs.vicp.net";
	public static int port=9090;
	public static String SERVER_BASE = "http://" + ip + ":"+port+"/yjweb";
	public static String SERVER_INTERFACE = "http://" + ip + ":"+port+"/yjweb/service";
	public static String SERVER_UPLOAD = "http://" + ip + ":"+port+"/yjweb/upload";
	public Class<?> c = null;
	private String tbn = "";
	private Stack<Http> https = new Stack<Http>();

	public BaseDao(Class<?> a) {
		if (a != null) {
			c = a;
			this.tbn = a.getSimpleName().toLowerCase();
		}
	}

	public void cancel() {
		for (Http h : https) {
			h.cancel();
		}
		https.clear();
	}

	public PageList<?> query(QueryCondition qc, TypeToken<?> type) {
		Http http = new Http(SERVER_INTERFACE);
		https.add(http);
		http.addParam("name", "query__" + tbn);
		http.addParam("body", Escape.escape(g.toJson(qc)));
		String result = http.execute();
		http.cancel();
		Lg.print(result);
		PageList<?> pl = g.fromJson(result, type.getType());
		return pl;
	}

	public boolean insert(Object obj) {
		Http http = new Http(SERVER_INTERFACE);
		https.add(http);
		http.addParam("name", "insert__" + tbn);
		http.addParam("body", Escape.escape(g.toJson(obj)));
		String result = http.execute();
		http.cancel();
		boolean b = false;
		if (result.indexOf("true") >= 0) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}

	public boolean update(Object obj) {
		Http http = new Http(SERVER_INTERFACE);
		https.add(http);
		http.addParam("name", "update__" + tbn);
		http.addParam("body", Escape.escape(g.toJson(obj)));
		String result = http.execute();
		http.cancel();
		boolean b = false;
		if (result.indexOf("true") >= 0) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}

	public boolean deleteFile(String fileUrl){
		Http http = new Http(SERVER_BASE+"/delete");
		https.add(http);
		http.addParam("name", fileUrl);
		String result = http.execute();
		http.cancel();
		boolean b = false;
		if (result.indexOf("true") >= 0) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}
	public boolean delete(String id) {
		Http http = new Http(SERVER_INTERFACE);
		https.add(http);
		http.addParam("name", "delete__" + tbn);
		http.addParam("body", id);
		String result = http.execute();
		http.cancel();
		boolean b = false;
		if (result.indexOf("true") >= 0) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}

	public boolean deletes(String[] ids) {
		Http http = new Http(SERVER_INTERFACE);
		https.add(http);
		http.addParam("name", "deletes__" + tbn);
		http.addParam("body", Escape.escape(g.toJson(ids)));
		String result = http.execute();
		http.cancel();
		boolean b = false;
		if (result.indexOf("true") >= 0) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}

	// boolean b=d.deletes((String[])ids.toArray(new String[1]));
	public boolean deletes(ArrayList<String> ids) {
		Http http = new Http(SERVER_INTERFACE);
		https.add(http);
		http.addParam("name", "deletes__" + tbn);
		http.addParam("body", Escape.escape(g.toJson(ids)));
		String result = http.execute();
		http.cancel();
		boolean b = false;
		if (result.indexOf("true") >= 0) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}
	public String upload(String filePath){
		Http h=new Http(SERVER_UPLOAD,Http.POST);
		https.add(h);
		h.addFile(filePath);
		String execute = h.execute();
		Lg.print(execute);
		h.cancel();
		return execute;
	}
}
