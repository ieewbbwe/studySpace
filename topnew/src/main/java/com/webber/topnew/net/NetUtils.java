package com.webber.topnew.net;

import android.util.Log;

import com.google.gson.Gson;
import com.webber.topnew.net.request.BaseRequest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxh on 2016/11/24.
 * Describe：
 */

public class NetUtils {

    public static Map<String, String> getParams(BaseRequest req) {
        Map<String, String> params = new HashMap<>();
        Class<? extends BaseRequest> c = req.getClass();
        Class supreClazz = c.getSuperclass();
        Field[] fields = c.getDeclaredFields();
        Field[] parentFields = supreClazz.getDeclaredFields();

        Field[] result = new Field[fields.length + parentFields.length];
        System.arraycopy(fields, 0, result, 0, fields.length);
        System.arraycopy(parentFields, 0, result, fields.length,
                parentFields.length);

        for (Field f : result) {
            f.setAccessible(true);
            try {
                String s = null;
                if (f.get(req) instanceof Integer) {
                    s = String.valueOf((Integer) f.get(req));
                }
                if (f.get(req) instanceof String) {
                    s = (String) f.get(req);
                }
                if (f.get(req) instanceof Double) {
                    s = String.valueOf((Double) f.get(req));
                }
                if (f.get(req) instanceof Float) {
                    s = String.valueOf((Float) f.get(req));
                }
                if (f.get(req) instanceof Boolean) {
                    s = String.valueOf((Boolean) f.get(req));
                }
                if (f.get(req) instanceof Long) {
                    s = String.valueOf((Long) f.get(req));
                }
                if (s != null) {
                    params.put(f.getName(), s);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        Log.d("network", "params:" + new Gson().toJson(params));
        return params;
    }
}
