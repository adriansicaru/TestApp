package adriansicaru.testapp.JSONObjects.randomUsers;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Picture implements Parcelable
{

    @SerializedName("large")
    @Expose
    private String large;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    public final static Parcelable.Creator<Picture> CREATOR = new Creator<Picture>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Picture createFromParcel(Parcel in) {
            Picture instance = new Picture();
            instance.large = ((String) in.readValue((String.class.getClassLoader())));
            instance.medium = ((String) in.readValue((String.class.getClassLoader())));
            instance.thumbnail = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Picture[] newArray(int size) {
            return (new Picture[size]);
        }

    }
            ;

    /**
     * No args constructor for use in serialization
     *
     */
    public Picture() {
    }

    /**
     *
     * @param thumbnail
     * @param medium
     * @param large
     */
    public Picture(String large, String medium, String thumbnail) {
        super();
        this.large = large;
        this.medium = medium;
        this.thumbnail = thumbnail;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(large);
        dest.writeValue(medium);
        dest.writeValue(thumbnail);
    }

    public int describeContents() {
        return 0;
    }

}