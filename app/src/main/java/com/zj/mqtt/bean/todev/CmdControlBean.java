package com.zj.mqtt.bean.todev;

import com.zj.mqtt.bean.toapp.NodeBean;
import com.zj.mqtt.bean.toapp.NodedataBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/30 上午9:42.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmdControlBean {

    private String cmd;
    private int seq;

    /**
     * 调光控制
     */
    private LevelBean mv_to_level;

    private HuaSetBean movetohueandsat;

    private NodeBean node;

    private OtaBean ota_info;

    private NodedataBean nodedata;

    private ReportParaBean para;

    private CmdNetworkParaBean network_para;

    public CmdControlBean(String cmd) {
        this.cmd = cmd;
    }


}
