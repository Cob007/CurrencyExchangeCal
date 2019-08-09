package android.michealcob.calculator.net;

import android.michealcob.calculator.net.response.LatestResponse;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("latest")
    Call<LatestResponse> getLatest(
            @Query("access_key") String key
    );
}
