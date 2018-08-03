package com.cohajj.app.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cohajj.app.AppController;
import com.cohajj.app.models.PostReport;
import com.google.gson.Gson;

public class ServiceConnector {

    public static final String HOST = "http://10.0.8.212:9595/";
    public static final String BASE_API = HOST + "hajj_roads_backend/";
    public static final String GET_ROUTES = BASE_API + "roads_status";
    public static final String POST_STATUS = BASE_API + "report_status";

    public static void getRoutes(Response.Listener listener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.GET, GET_ROUTES, listener, errorListener);
        AppController.getInstance().addToRequestQueue(request);
    }


    public static void postReport(final PostReport postReport, Response.Listener listener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.POST, POST_STATUS, listener, errorListener)
        {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return new Gson().toJson(postReport).getBytes();
            }
        };
        AppController.getInstance().addToRequestQueue(request);


    }


}
