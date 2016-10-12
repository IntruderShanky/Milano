package com.intrusoft.milano;

import org.json.JSONObject;

/**
 * Created by apple on 10/11/16.
 */

public class Connection {

    RequestCreator requestCreator;

    Connection(RequestCreator requestCreator) {
        this.requestCreator = requestCreator;
    }

    enum RequestType {
        GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");
        public String value;
        RequestType(String value) {
            this.value = value;
        }
    }

    public RequestCreator doGet() {
        requestCreator.setRequestType(RequestType.GET);
        return requestCreator;
    }

    public RequestCreator doPost(String request) {
        requestCreator.setRequestType(RequestType.POST);
        requestCreator.setRequest(request);
        return requestCreator;
    }

    public RequestCreator doDelete(String request) {
        requestCreator.setRequestType(RequestType.DELETE);
        requestCreator.setRequest(request);
        return requestCreator;
    }

    public RequestCreator doPut(String request) {
        requestCreator.setRequestType(RequestType.PUT);
        requestCreator.setRequest(request);
        return requestCreator;
    }


}
