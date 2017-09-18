package adriansicaru.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import adriansicaru.testapp.JSONObjects.randomUsers.RandomUsers;
import adriansicaru.testapp.JSONObjects.randomUsers.Result;
import adriansicaru.testapp.networking.ServerClient;
import adriansicaru.testapp.networking.ServerInterface;
import adriansicaru.testapp.userDetails.UserDetailsActivity;
import adriansicaru.testapp.userList.UserListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

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
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mToolbar!=null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Users");
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + "Users" + "</font>"));
        }

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorTextView = (TextView) findViewById(R.id.error_text);
        userListView = (ListView) findViewById(R.id.userList);
        errorTextView.setVisibility(View.GONE);
        footerView = View.inflate(mContext, R.layout.footer_view, null);
        currentPage = initalPage;
        apiService = ServerClient.getClient(serverName).create(ServerInterface.class);

        makeApiCall(currentPage);

    }

    //in case something goes wrong with the download
    private void setErrorMessage(String errorMessage) {
        errorTextView.setText(errorMessage);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void handleRequestResponse(Response<JsonElement> response) {
        Gson gson = new Gson();
        //if this is the first time the list is populated, we set an adapter, if not, we only update it
        //if we reach a certain position in our list, we can start loading more data (pages)
        if(currentPage==0) {
            randomUsers = gson.fromJson(response.body(), RandomUsers.class);
            userListAdapter = new UserListAdapter(mContext, R.layout.user_list_item_layout, randomUsers.getResults());
            userListView.setAdapter(userListAdapter);
            progressBar.setVisibility(View.GONE);
            userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Result bundleResult = randomUsers.getResults().get(i);
                    Intent intent = new Intent(mContext, UserDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bundleResult", bundleResult);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
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
                    //lazyLoadPos stores how many positions before we should start loading the next page
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

    //send request to server with our page
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
