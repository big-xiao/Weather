package gson;
import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("location")
    public String cityName;

    @SerializedName("cid")
    public String weatherId;

    public  Update update;

    public class Update{
        public String loc;

    }


}