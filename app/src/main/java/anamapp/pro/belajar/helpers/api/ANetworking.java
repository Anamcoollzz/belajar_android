package anamapp.pro.belajar.helpers.api;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;

public class ANetworking {

    public static void initialize(Context context) {
        AndroidNetworking.initialize(context);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
    }

    public static ANRequest get(String url) {
        return AndroidNetworking.get(url)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build();
    }

}
