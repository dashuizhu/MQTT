package com.zj.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.bean.todev.OtaBean;
import com.zj.mqtt.protocol.CmdPackage;
import org.junit.Test;

/**
 * @author zhuj 2018/8/29 上午11:49.
 */
public class CmdParseTest {
    @Test
    public void onTest() {

        //String cmd = "{\n"
        //        + "\"cmd\":\"read-node\",\n"
        //        + "\"seq\":1,\n"
        //        + "\"node\":{\n"
        //        + "\"mac\":\"string\",\n"
        //        + "\"endpoint\":1,\n"
        //        + "\"clusterId\":1,\n"
        //        + "\"attributeId\":1\n"
        //        + "}\n"
        //        + "}\n";
        //
        //
        //CmdParse.parseMsg(cmd);

        System.out.print(JSONObject.toJSONString(new OtaBean()));

        CmdControlBean cb = CmdPackage.setUpdate();
        System.out.print(JSONObject.toJSONString(cb));
    }
}
