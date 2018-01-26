package com.pioneeriot.pioneeriot.bean;

import java.util.List;

/**
 * 水表、DMA平台登录返回值
 * Created by LiYuliang on 2017/2/21 0021.
 *
 * @author LiYuliang
 * @version 2017/10/30
 */

public class WaterMeterLoginResult {

    private String result;

    private String privilege;

    private List<Data> data;

    private String userName;

    private String msg;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getPrivilege() {
        return this.privilege;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return this.data;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public class Data {

        private int exchangStationId;

        private int entranceId;

        private String deptNo;

        private String deptName;

        private String createTime;

        private int villageId;

        private int no;

        private String userName;

        private int lv;

        private String login;

        private String password;

        private int buildingId;

        private int supplierId;

        private String supplier;

        public int getExchangStationId() {
            return exchangStationId;
        }

        public void setExchangStationId(int exchangStationId) {
            this.exchangStationId = exchangStationId;
        }

        public int getEntranceId() {
            return entranceId;
        }

        public void setEntranceId(int entranceId) {
            this.entranceId = entranceId;
        }

        public String getDeptNo() {
            return deptNo;
        }

        public void setDeptNo(String deptNo) {
            this.deptNo = deptNo;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getVillageId() {
            return villageId;
        }

        public void setVillageId(int villageId) {
            this.villageId = villageId;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getLv() {
            return lv;
        }

        public void setLv(int lv) {
            this.lv = lv;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public int getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(int supplierId) {
            this.supplierId = supplierId;
        }

        public String getSupplier() {
            return supplier;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }
    }
}
