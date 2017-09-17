package adriansicaru.testapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import adriansicaru.testapp.JSONObjects.randomUsers.RandomUsers;
import adriansicaru.testapp.networking.ServerClient;
import adriansicaru.testapp.networking.ServerInterface;
import adriansicaru.testapp.userList.UserListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    ProgressBar progressBar;
    TextView errorTextView;
    ServerInterface apiService;
    Context mContext;
    final String serverName = "https://randomuser.me";
    final int initalPage = 0;
    final int result = 100;
    final String seed = "abc";
    RandomUsers randomUsers;
    ListView userListView;
    UserListAdapter userListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        mContext = this;

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorTextView = (TextView) findViewById(R.id.error_text);
        userListView = (ListView) findViewById(R.id.userList);
        errorTextView.setVisibility(View.GONE);

        apiService = ServerClient.getClient(serverName).create(ServerInterface.class);

        Call<JsonElement> call;
        call = apiService.randomUsers(initalPage, result, seed, "application/json", "application/json");

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    randomUsers = gson.fromJson(response.body(), RandomUsers.class);
                    userListAdapter = new UserListAdapter(mContext, R.layout.user_list_item_layout, randomUsers.getResults());
                    userListView.setAdapter(userListAdapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                   setErrorMessage(response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                setErrorMessage(t.toString());
            }
        });

    }

    private void setErrorMessage(String errorMessage) {
        errorTextView.setText(errorMessage);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
    }
}
