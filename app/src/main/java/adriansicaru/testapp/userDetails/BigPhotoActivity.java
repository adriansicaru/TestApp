package adriansicaru.testapp.userDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import adriansicaru.testapp.R;
import adriansicaru.testapp.userList.CircleTransformation;

/**
 * Created by adriansicaru on 9/17/17.
 */

public class BigPhotoActivity extends AppCompatActivity {

    Context mContext;
    String photoUrl;
    ImageView bigPhoto;
    int touchState;
    final int IDLE = 0;
    final int TOUCH = 1;
    final int PINCH = 2;
    float dist0, distCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_photo_activity_layout);
        mContext = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            photoUrl = bundle.getString("photoUrl");
        }

        bigPhoto = (ImageView) findViewById(R.id.big_photo);
        Picasso.with(mContext).load(photoUrl)
                .transform(new CircleTransformation()).placeholder(R.drawable.user_placeholder).into(bigPhoto);

        resetZoom();

        bigPhoto.setOnTouchListener(new View.OnTouchListener() {
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
                        if (curScale < 1) {
                            onBackPressed();
                        }
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
