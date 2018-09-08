package com.zj.mqtt.database;

import android.util.Log;
import com.zj.mqtt.bean.device.DeviceBean;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuj 2018/8/28 上午9:37.
 */
public class DeviceDao extends RealmObject {

    private final static String TAG = DeviceDao.class.getSimpleName();

    private final static String COLUMN_SEQ = "seq";
    private final static String COLUMN_MAC = "mac";

    @PrimaryKey private String mac;
    private String name;
    private int seq;
    private String place;

    public static void saveOrUpdate(final DeviceBean bean) {
        Log.w(TAG, "save " + bean.getDeviceMac() + " " + bean.getName());
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (bean.getSeq() == 0) {
                    Number number = realm.where(DeviceDao.class).max(COLUMN_SEQ);
                    int seq = 0;
                    if (number != null) {
                        seq = number.intValue();
                        seq++;
                    }
                    bean.setSeq(seq);
                }
                realm.copyToRealmOrUpdate(castDao(bean));
            }
        });
    }

    public static void saveOrUpdate(final List<DeviceBean> list) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //先清空旧的，再保存新的
                realm.where(DeviceDao.class).findAll().deleteAllFromRealm();
                int size = list.size();
                DeviceBean bean;
                for (int i = 0; i < size; i++) {
                    bean = list.get(i);
                    bean.setSeq(i + 1);
                    realm.copyToRealmOrUpdate(castDao(bean));
                }
            }
        });
    }

    public static List<DeviceBean> queryList() {
        return queryList(99999);
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

    public static DeviceBean getDeviceByMac(String mac) {
        Realm realm = Realm.getDefaultInstance();
        DeviceDao results;
        realm.beginTransaction();
        results = realm.where(DeviceDao.class).equalTo(COLUMN_MAC, mac).findFirst();
        realm.commitTransaction();

        if (results!=null) {
            return results.castBean();
        } else {
            return null;
        }
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
        dao.name = bean.getName();
        dao.seq = bean.getSeq();
        dao.mac = bean.getDeviceMac();
        dao.place = bean.getPlace();

        return dao;
    }

    private DeviceBean castBean() {
        DeviceBean bean = new DeviceBean();
        bean.setDeviceMac(mac);
        bean.setName(this.name);
        bean.setSeq(seq);
        bean.setPlace(place);

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
