package com.zj.mqtt.bean.toapp;

import com.zj.mqtt.bean.device.DeviceBean;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/29 下午2:04.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmdNodeListResult extends CmdResult {

    private List<DeviceBean> device;

}
