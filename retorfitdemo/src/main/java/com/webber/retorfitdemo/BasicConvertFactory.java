package com.webber.retorfitdemo;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by mxh on 2016/11/17.
 * Describe：自定义的对象转换类
 */

public class BasicConvertFactory extends Converter.Factory {

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new BasicResponseConvert(type);
    }

    public class BasicResponseConvert<T> implements Converter<ResponseBody, T> {

        private Type type;
        Gson gson = new Gson();

        public BasicResponseConvert(Type type) {
            this.type = type;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            return gson.fromJson(value.string(), type);
        }
    }
}
