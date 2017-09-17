package adriansicaru.testapp.networking;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by adriansicaru on 9/17/17.
 */

public interface ServerInterface {

    @GET("/api/")
    Call<JsonElement> randomUsers(@Query("page") Integer page,
                                  @Query("result") Integer result,
                                  @Query("seed") String seed,
                                  @Header("accept") String accept,
                                  @Header("content-type") String contentType);


}
