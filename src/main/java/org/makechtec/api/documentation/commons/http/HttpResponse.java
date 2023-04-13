package org.makechtec.api.documentation.commons.http;

import com.google.gson.Gson;

public record HttpResponse<T>(int code, String message, T data) {

    public String toJson(){
        return (new Gson()).toJson(this);
    }
}
