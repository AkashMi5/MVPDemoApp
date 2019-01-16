package com.demoapp.Model;

import com.google.gson.annotations.SerializedName;

public class VenueList {
    @SerializedName("meta")
    private Meta meta;
    @SerializedName("response")
    private Response response;

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return meta;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
