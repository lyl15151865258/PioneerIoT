package com.pioneeriot.pioneeriot.bean;

import java.util.List;

/**
 * Created by LiYuliang on 2017/10/7.
 * 水表报警信息结果的实体类
 *
 * @author LiYuliang
 * @version 2017/11/20
 */

public class WaterMeterWarning {
    private String result;

    private int totalCount;

    private List<Data> data;

    private String msg;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return this.data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public class Data {
        private double total;

        private String createTime;

        private String building;

        private double t1Inp;

        private float pressure;

        private String status;

        private double electric;

        private String imei;

        private String valveStatus;

        private int buildingArea;

        private String entrance;

        private String productType;

        private String vol;

        private String meterId;

        private String timeInP;

        private String village;

        private String exchangStation;

        private int meterDN;

        private String userName;

        private String meterSize;

        private float remainTotal;

        private String doorPlate;

        private float flowRate;

        private String supplier;

        public void setTotal(double total) {
            this.total = total;
        }

        public double getTotal() {
            return this.total;
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

        public void setT1Inp(double t1Inp) {
            this.t1Inp = t1Inp;
        }

        public double getT1Inp() {
            return this.t1Inp;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

        public float getPressure() {
            return this.pressure;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setElectric(double electric) {
            this.electric = electric;
        }

        public double getElectric() {
            return this.electric;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getImei() {
            return this.imei;
        }

        public void setValveStatus(String valveStatus) {
            this.valveStatus = valveStatus;
        }

        public String getValveStatus() {
            return this.valveStatus;
        }

        public void setBuildingArea(int buildingArea) {
            this.buildingArea = buildingArea;
        }

        public int getBuildingArea() {
            return this.buildingArea;
        }

        public void setEntrance(String entrance) {
            this.entrance = entrance;
        }

        public String getEntrance() {
            return this.entrance;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProductType() {
            return this.productType;
        }

        public void setVol(String vol) {
            this.vol = vol;
        }

        public String getVol() {
            return this.vol;
        }

        public void setMeterId(String meterId) {
            this.meterId = meterId;
        }

        public String getMeterId() {
            return this.meterId;
        }

        public void setTimeInP(String timeInP) {
            this.timeInP = timeInP;
        }

        public String getTimeInP() {
            return this.timeInP;
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

        public void setMeterDN(int meterDN) {
            this.meterDN = meterDN;
        }

        public int getMeterDN() {
            return this.meterDN;
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

        public void setRemainTotal(float remainTotal) {
            this.remainTotal = remainTotal;
        }

        public float getRemainTotal() {
            return this.remainTotal;
        }

        public void setDoorPlate(String doorPlate) {
            this.doorPlate = doorPlate;
        }

        public String getDoorPlate() {
            return this.doorPlate;
        }

        public void setFlowRate(float flowRate) {
            this.flowRate = flowRate;
        }

        public float getFlowRate() {
            return this.flowRate;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }

        public String getSupplier() {
            return this.supplier;
        }

    }
}
