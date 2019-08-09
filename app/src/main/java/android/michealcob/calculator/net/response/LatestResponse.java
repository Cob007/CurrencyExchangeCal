package android.michealcob.calculator.net.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

public class LatestResponse {

    @SerializedName("success")
    public Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @SerializedName("base")
    public String base;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    @SerializedName("rates")
    public JsonObject ratesJs;

    public JsonObject getRatesJs() {
        return ratesJs;
    }

    public void setRatesJs(JsonObject ratesJs) {
        this.ratesJs = ratesJs;
    }


    public class Rates extends JSONObject{
    }
}

