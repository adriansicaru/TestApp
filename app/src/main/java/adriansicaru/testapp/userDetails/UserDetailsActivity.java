package adriansicaru.testapp.userDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    CardView addressCard, phoneCard, emailCard;
    RelativeLayout headerContainer;
    int touchState;
    final int IDLE = 0;
    final int TOUCH = 1;
    final int PINCH = 2;
    float dist0, distCurrent;

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

        addressCard = (CardView) findViewById(R.id.address_card);
        phoneCard = (CardView) findViewById(R.id.phone_card);
        emailCard = (CardView) findViewById(R.id.email_card);
        headerContainer = (RelativeLayout) findViewById(R.id.header_container);

        addressText.setText(result.getLocation().getCity()+ " " + result.getLocation().getPostcode()
                + " " + result.getLocation().getState()+ result.getLocation().getStreet());
        emailText.setText(result.getEmail());
        phoneText.setText(result.getPhone());

        nameTextView.setText(result.getName().getFirst()+" "+result.getName().getLast());
        idText.setText("ID: "+result.getId().getName()+" "+result.getId().getValue());
        Picasso.with(mContext).load(result.getPicture().getMedium())
                .transform(new CircleTransformation()).placeholder(R.drawable.user_placeholder).into(headerIcon);


        addressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" +Uri.encode(result.getLocation().getState()+"+"
                        +result.getLocation().getCity()+"+"+result.getLocation().getStreet()+"+"
                        +result.getLocation().getPostcode()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(mContext, "No navigation app available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        phoneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + result.getPhone() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        emailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uriText =
                        "mailto:" + result.getEmail();

                Uri uri = Uri.parse(uriText);

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                startActivity(sendIntent);
            }
        });



        headerContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // TODO Auto-generated method stub
                float distx, disty;
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        touchState = TOUCH;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchState = PINCH;
                        distx = motionEvent.getX(0) - motionEvent.getX(1);
                        disty = motionEvent.getY(0) - motionEvent.getY(1);
                        dist0 = (float) Math.sqrt(distx * distx + disty * disty);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (touchState == PINCH) {
                            distx = motionEvent.getX(0) - motionEvent.getX(1);
                            disty = motionEvent.getY(0) - motionEvent.getY(1);
                            distCurrent = (float) Math.sqrt(distx * distx + disty * disty);
                            logZoom();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        touchState = IDLE;
                        float curScale = distCurrent / dist0;
                        if (curScale > 1) {
                            Intent intent = new Intent(mContext, BigPhotoActivity.class);
                            startActivity(intent);
                        };
                        resetZoom();
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        touchState = TOUCH;
                        break;
                }
                return true;
            }
        });

    }

    private void logZoom() {
        float curScale = distCurrent / dist0;
        Log.e("ZOOM",curScale+"");
    }
    private void resetZoom() {
        distCurrent = 1;
        dist0 = 1;
        logZoom();
        touchState = IDLE;
    }

}
