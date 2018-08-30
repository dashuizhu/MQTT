package com.zj.mqtt.bean.toapp;

import com.zj.mqtt.bean.device.DeviceBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/29 下午2:05.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmdNodeResult extends CmdResult {

    private DeviceBean device;

}
