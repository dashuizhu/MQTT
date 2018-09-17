package com.zj.mqtt.bean.toapp;

import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.device.DeviceEndpointBean;
import com.zj.mqtt.bean.device.NodeDevicesBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/29 下午2:04.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmdNodeListResult extends CmdResult {

    private List<NodeDevicesBean> devices;

    public List<DeviceBean> castDeviceList() {
        if (devices == null || devices.size() == 0) {
            return new ArrayList<>();
        }
        Map<String, DeviceBean> deviceMap = new HashMap<String, DeviceBean>();
        DeviceBean deviceBean;
        for (NodeDevicesBean node : devices) {

            //忽略官方默认的
            if (isFilter(node)) {
                continue;
            }

            String mac = node.getDeviceEndpoint().getMac();
            if (deviceMap.containsKey(mac)) {
                deviceBean = deviceMap.get(mac);
                deviceBean.getEndpointList().add(node.getDeviceEndpoint());
            } else {
                deviceBean = new DeviceBean();
                deviceBean.setDeviceType(node.getDeviceType());
                deviceBean.setNodeId(node.getNodeId());
                deviceBean.setDeviceMac(node.getDeviceEndpoint().getMac());
                deviceBean.setDeviceState(node.getDeviceState());

                List<DeviceEndpointBean> list = new ArrayList<>();
                list.add(node.getDeviceEndpoint());

                deviceBean.setEndpointList(list);
                deviceMap.put(mac, deviceBean);
            }
        }
        return new ArrayList<DeviceBean>(deviceMap.values());
    }

    public boolean isFilter(NodeDevicesBean nodeDevicesBean) {
        if (nodeDevicesBean == null) {
            return true;
        }
        if ("0x0000".equals(nodeDevicesBean.getDeviceType())) {
            return true;
        }
        if ("0x0810".equals(nodeDevicesBean.getDeviceType())) {
            return true;
        }
        return false;
    }

}
