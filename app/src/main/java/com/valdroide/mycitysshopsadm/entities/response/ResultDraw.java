package com.valdroide.mycitysshopsadm.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public class ResultDraw {
    @SerializedName("responseWS")
    @Expose
    ResponseWS responseWS;
    @SerializedName("draw")
    @Expose
    Draw draw;

    public ResponseWS getResponseWS() {
        return responseWS;
    }

    public void setResponseWS(ResponseWS responseWS) {
        this.responseWS = responseWS;
    }

    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }
}
