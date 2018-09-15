package com.zj.mqtt.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;
import com.zj.mqtt.ui.device.DeviceFragment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeviceFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> mPlaceList = new ArrayList<>();
    private FragmentManager mManager;

    public DeviceFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mManager = fm;
        //mPlaceList = list;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mPlaceList == null) {
            return "";
        }
        return mPlaceList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        //实例化Fragment
        DeviceFragment allFragment = DeviceFragment.newInstance(mPlaceList.get(position));
        return allFragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }


    ////必须重写此方法，添加tag一一做记录
    //@Override
    //public Object instantiateItem(ViewGroup container, int position) {
    //    tags.add(makeFragmentName(container.getId(), getItemId(position)));
    //    Fragment fragment = (Fragment) super.instantiateItem(container, position);
    //    this.fragmentManager.beginTransaction().show(fragment).commit();
    //    return fragment;
    //}

    @Override
    public int getCount() {
        if (mPlaceList == null) {
            return 0;
        }
        return mPlaceList.size();
    }

    public void setData(List<String> data) {
        mPlaceList = data;
    }

    public void removeAll() {
        if (this.mPlaceList != null) {
            FragmentTransaction fragmentTransaction = mManager.beginTransaction();
            List<Fragment> list = mManager.getFragments();
            for (Fragment fr : list) {
                fragmentTransaction.remove(fr);
            }
            fragmentTransaction.commit();
            //mManager.executePendingTransactions();
        }
        notifyDataSetChanged();
    }

        public void udpateList(List<String> newList) {
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        Iterator<String> it = mPlaceList.iterator();
        while (it.hasNext()) {
            String place = it.next();

            int newListSize = newList.size();
            for (int i = 0; i < newListSize; i++) {
                if (newList.get(i).equals(place)) {
                    break;
                }
                if (i == newListSize - 1) {
                    it.remove();
                    Fragment  fragment = mManager.findFragmentByTag(place);
                    if (fragment != null) {
                        fragmentTransaction.remove(fragment);
                    }
                }
            }
        }

        int newSize = newList.size();
        for (int j = 0; j < newSize; j++) {
            String newPlace = newList.get(j);

            int oldPlaceSize = mPlaceList.size();
            if (oldPlaceSize ==0) {
                mPlaceList.add(newPlace);
                continue;
            }
            for (int i = 0; i < oldPlaceSize; i++) {
                if (mPlaceList.get(i).equals(newPlace)) {
                    break;
                }
                if (i == oldPlaceSize - 1) {
                    mPlaceList.add(newPlace);
                }
            }
        }
        fragmentTransaction.commit();
        notifyDataSetChanged();
    }
}