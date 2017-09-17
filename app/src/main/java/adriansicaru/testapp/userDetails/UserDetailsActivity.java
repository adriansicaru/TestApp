package adriansicaru.testapp.userDetails;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import adriansicaru.testapp.JSONObjects.randomUsers.RandomUsers;
import adriansicaru.testapp.JSONObjects.randomUsers.Result;
import adriansicaru.testapp.R;
import adriansicaru.testapp.networking.ServerInterface;
import adriansicaru.testapp.userList.CircleTransformation;
import adriansicaru.testapp.userList.UserListAdapter;
import retrofit2.Call;

/**
 * Created by adriansicaru on 9/17/17.
 */

public class UserDetailsActivity extends AppCompatActivity {

    Context mContext;
    Result result;
    Toolbar mToolbar;
    TextView nameTextView;
    TextView idText, addressText, emailText, phoneText;
    ImageView headerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_activity_layout);
        mContext = this;

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            result = bundle.getParcelable("bundleResult");
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        nameTextView = (TextView) findViewById(R.id.name_text);
        headerIcon = (ImageView) findViewById(R.id.header_image);
        idText = (TextView) findViewById(R.id.id_text);
        addressText = (TextView) findViewById(R.id.address_content);
        emailText = (TextView) findViewById(R.id.email_content);
        phoneText = (TextView) findViewById(R.id.phone_content);

        addressText.setText(result.getLocation().getCity()+ " " + result.getLocation().getPostcode()
                + " " + result.getLocation().getState()+ result.getLocation().getStreet());
        emailText.setText(result.getEmail());
        phoneText.setText(result.getPhone());

        nameTextView.setText(result.getName().getFirst()+" "+result.getName().getLast());
        idText.setText("ID: "+result.getId().getName()+" "+result.getId().getValue());
        Picasso.with(mContext).load(result.getPicture().getMedium())
                .transform(new CircleTransformation()).placeholder(R.drawable.user_placeholder).into(headerIcon);


    }

}
