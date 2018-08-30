package com.zj.mqtt.bean.toapp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/29 下午2:06.
 */
@Data

@EqualsAndHashCode(callSuper = false)
public class CmdNodeLeftResult extends CmdResult {

    /**
     * cmd : deviceleft
     * seq : 1
     * deviceleft : {"mac":"string"}
     */

    private String cmd;
    private int seq;
    private DeviceLeftBean deviceleft;


    public static class DeviceLeftBean {
        /**
         * mac : string
         */

        private String mac;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }
    }
}
