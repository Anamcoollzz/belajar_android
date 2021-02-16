package anamapp.pro.belajar.services;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

public class ApiCall {
    /**
     * Get notification
     */
    public static ANRequest getWhatsapp(String waEndpoint) {
        return AndroidNetworking.get(waEndpoint)
//                .addPathParameter("pageNumber", "0")
//                .addQueryParameter("limit", "3")
//                .addHeaders("token", "1234")
                .addHeaders("Accept", "application/json")
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build();

    }
}
