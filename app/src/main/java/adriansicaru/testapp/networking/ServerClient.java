package adriansicaru.testapp.networking;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adriansicaru on 9/17/17.
 */

public class ServerClient {

    public static String BASE_URL = "";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
                .build();
        return retrofit;
    }

}