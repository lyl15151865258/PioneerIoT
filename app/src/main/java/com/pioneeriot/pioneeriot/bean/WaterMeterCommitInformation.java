package com.pioneeriot.pioneeriot.bean;

import java.util.List;

/**
 * Created by LiYuliang on 2017/4/15 0015.
 * 水表上传信息的实体类
 */

public class WaterMeterCommitInformation {

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

        private String entrance;
        private String doorPlate;
        private String productType;
        private String imei;
        private String meterId;
        private String valveStatus;
        private double total;
        private double flowRate;
        private double t1Inp;
        private String status;
        private String createTime;
        private String timeInP;
        private double pressure;
        private double meterDN;
        private String meterSize;
        private double remainTotal;
        private double electric;
        private String vol;
        private String userName;
        private String supplier;
        private String exchangStation;
        private String village;
        private String building;
        private String userAddress;
        private int chargingModelId;
        private String chargingModel;

        public String getEntrance() {
            return entrance;
        }

        public void setEntrance(String entrance) {
            this.entrance = entrance;
        }

        public String getDoorPlate() {
            return doorPlate;
        }

        public void setDoorPlate(String doorPlate) {
            this.doorPlate = doorPlate;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getMeterId() {
            return meterId;
        }

        public void setMeterId(String meterId) {
            this.meterId = meterId;
        }

        public String getValveStatus() {
            return valveStatus;
        }

        public void setValveStatus(String valveStatus) {
            this.valveStatus = valveStatus;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getFlowRate() {
            return flowRate;
        }

        public void setFlowRate(double flowRate) {
            this.flowRate = flowRate;
        }

        public double getT1Inp() {
            return t1Inp;
        }

        public void setT1Inp(double t1Inp) {
            this.t1Inp = t1Inp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTimeInP() {
            return timeInP;
        }

        public void setTimeInP(String timeInP) {
            this.timeInP = timeInP;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public double getMeterDN() {
            return meterDN;
        }

        public void setMeterDN(double meterDN) {
            this.meterDN = meterDN;
        }

        public String getMeterSize() {
            return meterSize;
        }

        public void setMeterSize(String meterSize) {
            this.meterSize = meterSize;
        }

        public double getRemainTotal() {
            return remainTotal;
        }

        public void setRemainTotal(double remainTotal) {
            this.remainTotal = remainTotal;
        }

        public double getElectric() {
            return electric;
        }

        public void setElectric(double electric) {
            this.electric = electric;
        }

        public String getVol() {
            return vol;
        }

        public void setVol(String vol) {
            this.vol = vol;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getSupplier() {
            return supplier;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }

        public String getExchangStation() {
            return exchangStation;
        }

        public void setExchangStation(String exchangStation) {
            this.exchangStation = exchangStation;
        }

        public String getVillage() {
            return village;
        }

        public void setVillage(String village) {
            this.village = village;
        }

        public String getBuilding() {
            return building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public int getChargingModelId() {
            return chargingModelId;
        }

        public void setChargingModelId(int chargingModelId) {
            this.chargingModelId = chargingModelId;
        }

        public String getChargingModel() {
            return chargingModel;
        }

        public void setChargingModel(String chargingModel) {
            this.chargingModel = chargingModel;
        }
    }
}
