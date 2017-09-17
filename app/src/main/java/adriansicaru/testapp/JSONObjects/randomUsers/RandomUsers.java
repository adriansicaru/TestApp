package adriansicaru.testapp.JSONObjects.randomUsers;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RandomUsers implements Parcelable
{

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("info")
    @Expose
    private Info info;
    public final static Parcelable.Creator<RandomUsers> CREATOR = new Creator<RandomUsers>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RandomUsers createFromParcel(Parcel in) {
            RandomUsers instance = new RandomUsers();
            in.readList(instance.results, (adriansicaru.testapp.JSONObjects.randomUsers.Result.class.getClassLoader()));
            instance.info = ((Info) in.readValue((Info.class.getClassLoader())));
            return instance;
        }

        public RandomUsers[] newArray(int size) {
            return (new RandomUsers[size]);
        }

    }
            ;

    /**
     * No args constructor for use in serialization
     *
     */
    public RandomUsers() {
    }

    /**
     *
     * @param results
     * @param info
     */
    public RandomUsers(List<Result> results, Info info) {
        super();
        this.results = results;
        this.info = info;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(results);
        dest.writeValue(info);
    }

    public int describeContents() {
        return 0;
    }

}
