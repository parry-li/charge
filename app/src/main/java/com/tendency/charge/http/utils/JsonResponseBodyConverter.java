package com.tendency.charge.http.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.parry.utils.code.LogUtils;
import com.tendency.charge.http.ApiException;
import com.tendency.charge.utils.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import rx.android.BuildConfig;

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    public final static String JSON_ERROR_STR = "数据结构异常!";


    JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        if (BuildConfig.DEBUG) {
            LogUtil.i("NetData", response);
        }
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response);
        JsonObject root = element.getAsJsonObject();
        int resultCode = root.get("code").getAsInt();

        if (resultCode != 0) {
            String message = root.get("msg").getAsString();
            throw new ApiException(message);
        }

        try {
            InputStream inputStream = new ByteArrayInputStream(response.getBytes());
            Reader reader = new InputStreamReader(inputStream);
            JsonReader jsonReader = gson.newJsonReader(reader);
            T t = adapter.read(jsonReader);
            return t;
        } catch (Exception e) {
            LogUtils.i("数据结构异常","Cause"+e.getCause(),"Message"+e.getMessage(),e.toString());
            e.printStackTrace();
            throw new RuntimeException(JSON_ERROR_STR);
        } finally {
            value.close();
        }


    }

}
