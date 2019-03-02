package com.service.community.model;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO<json数据源>
 *
 * @author: 小嵩
 * @date: 2017/3/16 15:36
 */

public class JsonBean implements IPickerViewData {


    private String Cn;
    private String Id;
    private String description;
    private String others;
    public ArrayList<JsonBean> country;
    private List<CityBean> Provinces;
    public JsonBean(String cn, String id, String description, String others) {
        Cn = cn;
        this.description = description;
        this.others = others;
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }
    public String getName() {
        return Cn;
    }

    public void setName(String name) {
        this.Cn = name;
    }

    public List<CityBean> getCityList() {
        return Provinces;
    }

    public void setCityList(List<CityBean> city) {
        this.Provinces = city;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.Cn;
    }

    public static class CityBean {
        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */

        private String Cn;
        private List<IdBean> Cities;

        public String getName() {
            return Cn;
        }

        public void setName(String name) {
            this.Cn = name;
        }

        public List<IdBean> getArea() {
            return Cities;
        }

        public void setArea(List<IdBean> area) {
            this.Cities = area;
        }

    }

    public static class IdBean {

        private String Cn;
        private String Id;

        public String getName() {
            return Cn;
        }
        public void setName(String name) {
            this.Cn = name;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            this.Id = id;
        }

    }
}
