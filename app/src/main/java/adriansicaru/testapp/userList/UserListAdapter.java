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

import java.util.List;

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
        row.setTag(holder);

        Picasso.with(context).load(result.getPicture().getThumbnail()).transform(new CircleTransformation()).into(holder.userIcon);

        return row;
    }

    static class Holder {

        ImageView userIcon;
        TextView name;
        TextView age;
        ImageView flagIcon;

    }

}
