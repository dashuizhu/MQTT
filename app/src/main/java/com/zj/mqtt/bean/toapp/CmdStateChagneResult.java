package com.zj.mqtt.bean.toapp;

/**
 * @author zhuj 2018/8/29 下午2:26.
 */
public class CmdStateChagneResult extends CmdResult {

    /**
     * statechange : {"mac":"string","deviceState":1}
     */

    private StatechangeBean statechange;

    public StatechangeBean getStatechange() {
        return statechange;
    }

    public void setStatechange(StatechangeBean statechange) {
        this.statechange = statechange;
    }

    public static class StatechangeBean {
        /**
         * mac : string
         * deviceState : 1
         */

        private String mac;
        private int deviceState;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getDeviceState() {
            return deviceState;
        }

        public void setDeviceState(int deviceState) {
            this.deviceState = deviceState;
        }
    }
}
