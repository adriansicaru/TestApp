package adriansicaru.testapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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
    int currentPage = 1;
    final int result = 100;
    final int lazyLoadPos = 20;
    final String seed = "abc";
    RandomUsers randomUsers;
    ListView userListView;
    UserListAdapter userListAdapter;
    View footerView;
    Call<JsonElement> call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        mContext = this;

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorTextView = (TextView) findViewById(R.id.error_text);
        userListView = (ListView) findViewById(R.id.userList);
        errorTextView.setVisibility(View.GONE);
        footerView = View.inflate(mContext, R.layout.footer_view, null);
        currentPage = initalPage;
        apiService = ServerClient.getClient(serverName).create(ServerInterface.class);

        makeApiCall(currentPage);

    }

    private void setErrorMessage(String errorMessage) {
        errorTextView.setText(errorMessage);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void handleRequestResponse(Response<JsonElement> response) {
        Gson gson = new Gson();
        if(currentPage==0) {
            randomUsers = gson.fromJson(response.body(), RandomUsers.class);
            userListAdapter = new UserListAdapter(mContext, R.layout.user_list_item_layout, randomUsers.getResults());
            userListView.setAdapter(userListAdapter);
            progressBar.setVisibility(View.GONE);
        } else {
            userListView.removeFooterView(footerView);
            userListAdapter.addAll(gson.fromJson(response.body(), RandomUsers.class).getResults());
            userListAdapter.notifyDataSetChanged();
        }
        if(currentPage<2) {
            currentPage++;
            userListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                     int totalItemCount) {
                    int position = firstVisibleItem + visibleItemCount;
                    int limit = totalItemCount - lazyLoadPos;

                    // Check if bottom has been reached
                    if (position >= limit && totalItemCount > 0) {
                        userListView.addFooterView(footerView);
//                              newPageReqest.tryRequest(practitionerID, patientsPerPage, initialPage, startDate, endDate, search, wardId);
                        userListView.setOnScrollListener(null);
                        Log.e("PAGER", "TRIGGERED SCROLL FOR POS " + limit + " AND THEN CANCELLED AND PAGE " + currentPage);
                        makeApiCall(currentPage);
                    }
                }
            });
        }
    }

    private void makeApiCall(Integer page) {
        call = apiService.randomUsers(page, result, seed, "application/json", "application/json");

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {
                    handleRequestResponse(response);
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
}
