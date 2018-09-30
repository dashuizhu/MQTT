package com.zj.mqtt.database;

import android.text.TextUtils;
import android.util.Log;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.device.DeviceEndpointBean;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhuj 2018/8/28 上午9:37.
 */
public class DeviceDao extends RealmObject {

    private final static String TAG = DeviceDao.class.getSimpleName();

    private final static String COLUMN_ID = "id";
    private final static String COLUMN_SEQ = "seq";
    private final static String COLUMN_MAC = "mac";
    private final static String COLUMN_NODEID = "nodeId";
    private final static String COLUMN_PLACE = "place";

    @PrimaryKey private String id;
    private String mac;
    private int nodeId;
    //private int endPoint;
    private String deviceType;
    private String name;
    private int seq;
    private String place;
    private RealmList<EndpointDao> endpointList;

    public static void saveOrUpdate(final DeviceBean bean) {
        Log.w(TAG, "save " + bean.getDeviceMac() + " " + bean.getName());
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //如果没有排序号， 就查找本地最大的 +1
                if (bean.getSeq() == 0) {
                    Number number = realm.where(DeviceDao.class).max(COLUMN_SEQ);
                    int seq = 0;
                    if (number != null) {
                        seq = number.intValue();
                        seq++;
                    }
                    bean.setSeq(seq);
                }
                //查询本地是否有记录
                DeviceDao dao =
                        realm.where(DeviceDao.class).equalTo(COLUMN_ID, bean.getId()).findFirst();
                //没有记录
                if (dao == null) {
                    realm.copyToRealmOrUpdate(castDao(bean));
                } else {
                    //有记录
                    dao.place = bean.getPlace();
                    dao.name = bean.getName();
                    dao.seq = bean.getSeq();
                }
            }
        });
    }

    public static void saveOrUpdate(final List<DeviceBean> list) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //先清空旧的，再保存新的
                Number number = realm.where(DeviceDao.class).max(COLUMN_SEQ);
                int seq = 0;
                if (number != null) {
                    seq = number.intValue();
                    seq++;
                }

                int size = list.size();
                DeviceBean bean;
                for (int i = 0; i < size; i++) {
                    bean = list.get(i);

                    DeviceDao dao = realm.where(DeviceDao.class)
                            .equalTo(COLUMN_ID, bean.getDeviceMac())
                            .findFirst();
                    if (dao == null ) {
                        bean.setSeq(seq);
                        seq++;
                        realm.copyToRealmOrUpdate(castDao(bean));
                    } else {
                        //更新endpoint
                        RealmList<EndpointDao> actionList = new RealmList<>();
                        if (bean.getEndpointList() != null) {
                            for (DeviceEndpointBean actBean : bean.getEndpointList()) {
                                EndpointDao actDao = new EndpointDao();
                                actDao.setEndPoint(actBean.getEndpoint());
                                actDao = realm.copyToRealm(actDao);
                                actionList.add(actDao);
                            }
                        }
                        dao.deviceType = bean.getDeviceType();
                        Log.w("test", dao.isValid()+"acitonLIst " + actionList.isValid() + actionList.size());
                        dao.endpointList = actionList;
                    }
                }
            }
        });
    }

    public static List<DeviceBean> queryList() {
        return queryList(9999);
    }

    public static List<DeviceBean> queryList(int querySize) {
        Realm realm = Realm.getDefaultInstance();
        List<DeviceDao> results;
        realm.beginTransaction();
        results = realm.where(DeviceDao.class).findAllSorted(COLUMN_SEQ);
        realm.commitTransaction();

        List<DeviceBean> beanList = new ArrayList<>();
        int count = 0;
        for (DeviceDao dao : results) {
            beanList.add(dao.castBean());
            count++;
            if (count >= querySize) {
                break;
            }
        }
        return beanList;
    }

    public static List<DeviceBean> queryListByPlace(String place) {
        Realm realm = Realm.getDefaultInstance();
        List<DeviceDao> results;
        realm.beginTransaction();
        results =
                realm.where(DeviceDao.class).equalTo(COLUMN_PLACE, place).findAllSorted(COLUMN_SEQ);
        realm.commitTransaction();

        List<DeviceBean> beanList = new ArrayList<>();
        int count = 0;
        for (DeviceDao dao : results) {
            beanList.add(dao.castBean());
        }
        return beanList;
    }

    public static DeviceBean getDeviceByMac(String mac) {
        Realm realm = Realm.getDefaultInstance();
        DeviceDao results;
        realm.beginTransaction();
        results = realm.where(DeviceDao.class).equalTo(COLUMN_MAC, mac).findFirst();
        realm.commitTransaction();

        if (results != null) {
            return results.castBean();
        } else {
            return null;
        }
    }

    public static Set<String> getPlaceSet() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DeviceDao> results;
        realm.beginTransaction();
        results = realm.where(DeviceDao.class).findAll();
        realm.commitTransaction();

        Set<String> set = new HashSet<>();

        for (DeviceDao dao : results) {
            //空地名 直接跳过
            if (TextUtils.isEmpty(dao.place)) {
                continue;
            }
            if (!set.contains(dao.place)) {
                set.add(dao.place);
            }
        }
        return set;
    }

    /**
     * 数据库个数
     */
    public static int queryMaxSeq() {
        Realm realm = Realm.getDefaultInstance();
        List<DeviceDao> results;
        realm.beginTransaction();
        int maxSeq = realm.where(DeviceDao.class).max(COLUMN_SEQ).intValue();
        realm.commitTransaction();
        return maxSeq;
    }

    private static DeviceDao castDao(DeviceBean bean) {
        DeviceDao dao = new DeviceDao();
        dao.id = bean.getDeviceMac();
        dao.nodeId = bean.getNodeId();
        dao.name = bean.getName();
        dao.deviceType = bean.getDeviceType();
        dao.seq = bean.getSeq();
        dao.mac = bean.getDeviceMac();
        dao.place = bean.getPlace();

        RealmList<EndpointDao> actionList = new RealmList<>();
        if (bean.getEndpointList() != null) {
            for (DeviceEndpointBean actBean : bean.getEndpointList()) {
                EndpointDao actDao = new EndpointDao();
                actDao.setEndPoint(actBean.getEndpoint());
                actionList.add(actDao);
            }
        }
        dao.endpointList = actionList;

        return dao;
    }

    private DeviceBean castBean() {
        DeviceBean bean = new DeviceBean();
        bean.setId(id);
        bean.setDeviceMac(mac);
        bean.setDeviceType(deviceType);
        bean.setNodeId(nodeId);
        bean.setName(this.name);
        bean.setSeq(seq);
        bean.setPlace(place);

        List<DeviceEndpointBean> list = new ArrayList<>();
        if (endpointList != null) {
            for (EndpointDao actDao : endpointList) {
                DeviceEndpointBean actBean = new DeviceEndpointBean();
                actBean.setEndpoint(actDao.getEndPoint());

                list.add(actBean);
            }
        }
        bean.setEndpointList(list);

        return bean;
    }

    public static void delete(final DeviceBean deviceBean) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DeviceDao dao = realm.where(DeviceDao.class)
                        .equalTo(COLUMN_MAC, deviceBean.getDeviceMac())
                        .findFirst();
                if (dao != null) {
                    dao.deleteFromRealm();
                }
            }
        });
    }
}
