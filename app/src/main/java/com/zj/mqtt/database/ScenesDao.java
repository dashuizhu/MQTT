package com.zj.mqtt.database;

import android.util.Log;
import com.zj.mqtt.bean.ActionBean;
import com.zj.mqtt.bean.ScenesBean;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuj 2018/8/28 上午9:37.
 */
public class ScenesDao extends RealmObject {

    private final static String TAG = ScenesDao.class.getSimpleName();

    private final static String COLUMN_SEQ = "seq";

    @PrimaryKey private String id;
    private String name;
    private int seq;
    private RealmList<ActionDao> actionList;

    public static void saveOrUpdate(final ScenesBean bean) {
        Log.w(TAG, "save " + bean.getId() + " " + bean.getName());
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(castDao(bean));
            }
        });
    }

    public static void saveOrUpdate(final List<ScenesBean> list) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //先清空旧的，再保存新的
                realm.where(ScenesDao.class).findAll().deleteAllFromRealm();
                int size = list.size();
                ScenesBean bean;
                for (int i = 0; i < size; i++) {
                    bean = list.get(i);
                    bean.setSeq(i);
                    realm.copyToRealmOrUpdate(castDao(bean));
                }
            }
        });
    }

    public static List<ScenesBean> queryList() {
        Realm realm = Realm.getDefaultInstance();
        List<ScenesDao> results;
        realm.beginTransaction();
        results = realm.where(ScenesDao.class).findAllSorted(COLUMN_SEQ);
        realm.commitTransaction();

        List<ScenesBean> beanList = new ArrayList<>();
        for (ScenesDao dao : results) {
            beanList.add(dao.castBean());
        }

        return beanList;
    }

    /**
     * 数据库个数
     * @return
     */
    public static int queryMaxSeq() {
        Realm realm = Realm.getDefaultInstance();
        List<ScenesDao> results;
        realm.beginTransaction();
        int maxSeq = realm.where(ScenesDao.class).max(COLUMN_SEQ).intValue();
        realm.commitTransaction();
        return maxSeq;
    }

    private static ScenesDao castDao(ScenesBean bean) {
        ScenesDao dao = new ScenesDao();
        dao.id = bean.getId();
        dao.name = bean.getName();
        dao.seq = bean.getSeq();

        RealmList<ActionDao> actionList = new RealmList<>();
        if (bean.getActionList() != null) {
            for (ActionBean actBean : bean.getActionList()) {
                ActionDao actDao = new ActionDao();
                actDao.setDeviceMac(actBean.getDeviceMac());
                actDao.setCmdJson(actBean.getCmdJson());
                actDao.setDeviceName(actBean.getDeviceName());
                actionList.add(actDao);
            }
        }
        dao.actionList = actionList;

        return dao;
    }

    private ScenesBean castBean() {
        ScenesBean bean = new ScenesBean();
        bean.setId(id);
        bean.setName(this.name);
        bean.setSeq(seq);

        List<ActionBean> list = new ArrayList<>();
        if (actionList != null) {
            for (ActionDao actDao : actionList) {
                ActionBean actBean = new ActionBean();
                actBean.setCmdJson(actDao.getCmdJson());
                actBean.setDeviceMac(actDao.getDeviceMac());
                actBean.setDeviceName(actDao.getDeviceName());

                list.add(actBean);
            }
        }
        bean.setActionList(list);
        return bean;
    }
}
