package gson;

import java.util.List;

/**
 * Created by Administrator on 2018/7/27.
 */

public class City {

        /**
         * basic : [{"cid":"CN101010100","location":"北京","parent_city":"北京","admin_area":"北京","cnty":"中国","lat":"39.90498734","lon":"116.4052887","tz":"+8.00","type":"city"},{"cid":"CN101210410","location":"北仑","parent_city":"宁波","admin_area":"浙江","cnty":"中国","lat":"29.90943909","lon":"121.83130646","tz":"+8.00","type":"city"},{"cid":"CN101301301","location":"北海","parent_city":"北海","admin_area":"广西","cnty":"中国","lat":"21.4733429","lon":"109.11925507","tz":"+8.00","type":"city"},{"cid":"CN101040800","location":"北碚","parent_city":"重庆","admin_area":"重庆","cnty":"中国","lat":"29.82542992","lon":"106.43786621","tz":"+8.00","type":"city"},{"cid":"CN101070706","location":"北镇","parent_city":"锦州","admin_area":"辽宁","cnty":"中国","lat":"41.59876251","lon":"121.79595947","tz":"+8.00","type":"city"}]
         * status : ok
         */

        private String status;
        private List<BasicBean> basic;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<BasicBean> getBasic() {
            return basic;
        }

        public void setBasic(List<BasicBean> basic) {
            this.basic = basic;
        }

        public static class BasicBean {
            /**
             * cid : CN101010100
             * location : 北京
             * parent_city : 北京
             * admin_area : 北京
             * cnty : 中国
             * lat : 39.90498734
             * lon : 116.4052887
             * tz : +8.00
             * type : city
             */

            private String cid;
            private String location;
            private String parent_city;
            private String admin_area;
            private String cnty;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getParent_city() {
                return parent_city;
            }

            public void setParent_city(String parent_city) {
                this.parent_city = parent_city;
            }

            public String getAdmin_area() {
                return admin_area;
            }

            public void setAdmin_area(String admin_area) {
                this.admin_area = admin_area;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }
        }
    }

