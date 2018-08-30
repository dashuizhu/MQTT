package com.zj.mqtt.bean.todev;

import com.zj.mqtt.constant.AppConstants;
import lombok.Data;

/**
 * @author zhuj 2018/8/30 下午1:50.
 */
@Data
public class OtaBean {

    private String url = AppConstants.OTA_URL;
}
