package com.zj.mqtt.bean.toapp;

public class NodedataBean {
        /**
         * mac : string
         * endpoint : 1
         * clusterId : 1
         * attributeId : 1
         * dataType : 1
         * data : 1
         */

        private String mac;
        private int endpoint;
        private int clusterId;
        private int attributeId;
        private int dataType;
        private int data;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(int endpoint) {
            this.endpoint = endpoint;
        }

        public int getClusterId() {
            return clusterId;
        }

        public void setClusterId(int clusterId) {
            this.clusterId = clusterId;
        }

        public int getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(int attributeId) {
            this.attributeId = attributeId;
        }

        public int getDataType() {
            return dataType;
        }

        public void setDataType(int dataType) {
            this.dataType = dataType;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }
    }