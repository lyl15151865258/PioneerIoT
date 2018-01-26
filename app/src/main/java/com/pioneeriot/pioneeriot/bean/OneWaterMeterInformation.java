package com.pioneeriot.pioneeriot.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiYuliang on 2017/9/6 0006.
 * 单只水表信息
 */

public class OneWaterMeterInformation implements Serializable {

    private String result;

    private List<Data> data;

    private List<Pic> pic;

    private String msg;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return this.data;
    }

    public void setPic(List<Pic> pic) {
        this.pic = pic;
    }

    public List<Pic> getPic() {
        return this.pic;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public class Pic implements Serializable {

        private String pic_Name5;

        private String pic_Name4;

        private int group_Id;

        private long pic_Size1;

        private int supplier_Id;

        private String pic_Name1;

        private long pic_Size;

        private long pic_Size2;

        private long pic_Size3;

        private String pic_Name3;

        private long pic_Size4;

        private String pic_Name2;

        private long pic_Size5;

        private String lng;

        private double initial_Water;

        private String create_Time;

        private String meter_Id;

        private int pic_Id;

        private String lat;

        private String operator_Name;

        private double last_Water;

        private String pic_Desc;

        private int sign;

        private String pic_Url2;

        private String pic_Url;

        private String pic_Url3;

        private String address;

        private String pic_Url4;

        private String pic_Url5;

        private String pic_Url1;

        private String pic_Name;

        private String user_Name;

        private String pic_Path;

        public void setPic_Name5(String pic_Name5) {
            this.pic_Name5 = pic_Name5;
        }

        public String getPic_Name5() {
            return this.pic_Name5;
        }

        public void setPic_Name4(String pic_Name4) {
            this.pic_Name4 = pic_Name4;
        }

        public String getPic_Name4() {
            return this.pic_Name4;
        }

        public void setGroup_Id(int group_Id) {
            this.group_Id = group_Id;
        }

        public int getGroup_Id() {
            return this.group_Id;
        }

        public void setPic_Size1(long pic_Size1) {
            this.pic_Size1 = pic_Size1;
        }

        public long getPic_Size1() {
            return this.pic_Size1;
        }

        public void setSupplier_Id(int supplier_Id) {
            this.supplier_Id = supplier_Id;
        }

        public int getSupplier_Id() {
            return this.supplier_Id;
        }

        public void setPic_Name1(String pic_Name1) {
            this.pic_Name1 = pic_Name1;
        }

        public String getPic_Name1() {
            return this.pic_Name1;
        }

        public void setPic_Size(long pic_Size) {
            this.pic_Size = pic_Size;
        }

        public long getPic_Size() {
            return this.pic_Size;
        }

        public void setPic_Size2(long pic_Size2) {
            this.pic_Size2 = pic_Size2;
        }

        public long getPic_Size2() {
            return this.pic_Size2;
        }

        public void setPic_Size3(long pic_Size3) {
            this.pic_Size3 = pic_Size3;
        }

        public long getPic_Size3() {
            return this.pic_Size3;
        }

        public void setPic_Name3(String pic_Name3) {
            this.pic_Name3 = pic_Name3;
        }

        public String getPic_Name3() {
            return this.pic_Name3;
        }

        public void setPic_Size4(long pic_Size4) {
            this.pic_Size4 = pic_Size4;
        }

        public long getPic_Size4() {
            return this.pic_Size4;
        }

        public void setPic_Name2(String pic_Name2) {
            this.pic_Name2 = pic_Name2;
        }

        public String getPic_Name2() {
            return this.pic_Name2;
        }

        public void setPic_Size5(long pic_Size5) {
            this.pic_Size5 = pic_Size5;
        }

        public long getPic_Size5() {
            return this.pic_Size5;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLng() {
            return this.lng;
        }

        public void setInitial_Water(double initial_Water) {
            this.initial_Water = initial_Water;
        }

        public double getInitial_Water() {
            return this.initial_Water;
        }

        public void setCreate_Time(String create_Time) {
            this.create_Time = create_Time;
        }

        public String getCreate_Time() {
            return this.create_Time;
        }

        public void setMeter_Id(String meter_Id) {
            this.meter_Id = meter_Id;
        }

        public String getMeter_Id() {
            return this.meter_Id;
        }

        public void setPic_Id(int pic_Id) {
            this.pic_Id = pic_Id;
        }

        public int getPic_Id() {
            return this.pic_Id;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLat() {
            return this.lat;
        }

        public String getOperator_Name() {
            return operator_Name;
        }

        public void setOperator_Name(String operator_Name) {
            this.operator_Name = operator_Name;
        }

        public void setLast_Water(double last_Water) {
            this.last_Water = last_Water;
        }

        public double getLast_Water() {
            return this.last_Water;
        }

        public void setPic_Desc(String pic_Desc) {
            this.pic_Desc = pic_Desc;
        }

        public String getPic_Desc() {
            return this.pic_Desc;
        }

        public void setSign(int sign) {
            this.sign = sign;
        }

        public int getSign() {
            return this.sign;
        }

        public void setPic_Url2(String pic_Url2) {
            this.pic_Url2 = pic_Url2;
        }

        public String getPic_Url2() {
            return this.pic_Url2;
        }

        public void setPic_Url(String pic_Url) {
            this.pic_Url = pic_Url;
        }

        public String getPic_Url() {
            return this.pic_Url;
        }

        public void setPic_Url3(String pic_Url3) {
            this.pic_Url3 = pic_Url3;
        }

        public String getPic_Url3() {
            return this.pic_Url3;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return this.address;
        }

        public void setPic_Url4(String pic_Url4) {
            this.pic_Url4 = pic_Url4;
        }

        public String getPic_Url4() {
            return this.pic_Url4;
        }

        public void setPic_Url5(String pic_Url5) {
            this.pic_Url5 = pic_Url5;
        }

        public String getPic_Url5() {
            return this.pic_Url5;
        }

        public void setPic_Url1(String pic_Url1) {
            this.pic_Url1 = pic_Url1;
        }

        public String getPic_Url1() {
            return this.pic_Url1;
        }

        public void setPic_Name(String pic_Name) {
            this.pic_Name = pic_Name;
        }

        public String getPic_Name() {
            return this.pic_Name;
        }

        public void setUser_Name(String user_Name) {
            this.user_Name = user_Name;
        }

        public String getUser_Name() {
            return this.user_Name;
        }

        public void setPic_Path(String pic_Path) {
            this.pic_Path = pic_Path;
        }

        public String getPic_Path() {
            return this.pic_Path;
        }

    }

    public class Data {
        private int entranceId;

        private String createTime;

        private String building;

        private int index;

        private int supplierId;

        private String imei;

        private String identityCard;

        private String remark1;

        private String entrance;

        private String remark2;

        private int exchangStationId;

        private int villageId;

        private String meterId;

        private String customerId;

        private String village;

        private String exchangStation;

        private int userId;

        private String userName;

        private String meterSize;

        private String doorPlate;

        private String telephone;

        private String supplier;

        private int buildingId;

        private String lat;

        private String lng;

        public void setEntranceId(int entranceId) {
            this.entranceId = entranceId;
        }

        public int getEntranceId() {
            return this.entranceId;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTime() {
            return this.createTime;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getBuilding() {
            return this.building;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        public void setSupplierId(int supplierId) {
            this.supplierId = supplierId;
        }

        public int getSupplierId() {
            return this.supplierId;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getImei() {
            return this.imei;
        }

        public void setIdentityCard(String identityCard) {
            this.identityCard = identityCard;
        }

        public String getIdentityCard() {
            return this.identityCard;
        }

        public void setRemark1(String remark1) {
            this.remark1 = remark1;
        }

        public String getRemark1() {
            return this.remark1;
        }

        public void setEntrance(String entrance) {
            this.entrance = entrance;
        }

        public String getEntrance() {
            return this.entrance;
        }

        public void setRemark2(String remark2) {
            this.remark2 = remark2;
        }

        public String getRemark2() {
            return this.remark2;
        }

        public void setExchangStationId(int exchangStationId) {
            this.exchangStationId = exchangStationId;
        }

        public int getExchangStationId() {
            return this.exchangStationId;
        }

        public void setVillageId(int villageId) {
            this.villageId = villageId;
        }

        public int getVillageId() {
            return this.villageId;
        }

        public void setMeterId(String meterId) {
            this.meterId = meterId;
        }

        public String getMeterId() {
            return this.meterId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerId() {
            return this.customerId;
        }

        public void setVillage(String village) {
            this.village = village;
        }

        public String getVillage() {
            return this.village;
        }

        public void setExchangStation(String exchangStation) {
            this.exchangStation = exchangStation;
        }

        public String getExchangStation() {
            return this.exchangStation;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserId() {
            return this.userId;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setMeterSize(String meterSize) {
            this.meterSize = meterSize;
        }

        public String getMeterSize() {
            return this.meterSize;
        }

        public void setDoorPlate(String doorPlate) {
            this.doorPlate = doorPlate;
        }

        public String getDoorPlate() {
            return this.doorPlate;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getTelephone() {
            return this.telephone;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }

        public String getSupplier() {
            return this.supplier;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public int getBuildingId() {
            return this.buildingId;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
    }
}
