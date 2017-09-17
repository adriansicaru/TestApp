package adriansicaru.testapp.userList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import adriansicaru.testapp.JSONObjects.randomUsers.Result;
import adriansicaru.testapp.R;

/**
 * Created by adriansicaru on 9/17/17.
 */

public class UserListAdapter extends ArrayAdapter<Result> {

    Context context;
    int layoutResourceId;
    List<Result> results = null;
    Holder holder = null;
    Result result;

    public UserListAdapter(Context context, int layoutResourceId, List<Result> list) {
        super(context, layoutResourceId, list);
        this.results = list;
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        holder = null;
        result = results.get(position);

        if(row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        holder = new Holder();

        holder.userIcon = (ImageView) row.findViewById(R.id.user_icon);
        holder.name = (TextView) row.findViewById(R.id.name_text);
        holder.age = (TextView) row.findViewById(R.id.age_text);
        holder.flagIcon = (ImageView) row.findViewById(R.id.flag_image);
        row.setTag(holder);

        holder.name.setText(result.getName().getFirst()+" "+result.getName().getLast());
        holder.age.setText(getAge(result.getDob())+" years");

        Picasso.with(context).load("https://flagpedia.net/data/flags/mini/"+result.getNat()
                .toLowerCase(Locale.US)+".png").placeholder(R.drawable.flag_placeholder).into(holder.flagIcon);
        Picasso.with(context).load(result.getPicture().getThumbnail())
                .transform(new CircleTransformation()).placeholder(R.drawable.user_placeholder).into(holder.userIcon);

        return row;
    }

    static class Holder {

        ImageView userIcon;
        TextView name;
        TextView age;
        ImageView flagIcon;

    }


    private String getAge(String dob){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(dob));
        } catch (ParseException e) {

        }

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

}
