package gson;

import java.util.List;

/**
 * Created by Administrator on 2018/7/26.
 */

public class Sun_time {
    private String status;

    private List<SunBean> sunrise_sunset;

    public void setSun(List<SunBean>  s) {
        this.sunrise_sunset = s;
    }

    public  List<SunBean> getSun() {
        return sunrise_sunset;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public static class SunBean
    {
        private String date;
        private String sr;
        private String ss;
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSr() {
            return sr;
        }

        public void setSr(String sr) {
            this.sr = sr;
        }

        public String getSs() {
            return ss;
        }

        public void setSs(String ss) {
            this.ss = ss;
        }


    }


}
