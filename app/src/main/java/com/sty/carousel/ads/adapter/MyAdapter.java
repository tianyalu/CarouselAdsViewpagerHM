package com.sty.carousel.ads.adapter;

import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Shi Tianyi on 2017/10/6/0006.
 */

public class MyAdapter extends PagerAdapter {
    private static final String TAG = MyAdapter.class.getSimpleName();

    private ArrayList<ImageView> imageViewList;

    public MyAdapter(ArrayList<ImageView> imageViewList){
        this.imageViewList = imageViewList;
    }

    @Override
    public int getCount() {
//        if(imageViewList != null && imageViewList.size() > 0){
//            return imageViewList.size();
//        }
//        return 0;
        return Integer.MAX_VALUE;
    }

    /**
     * 1、返回要显示的条目内容,创建条目
     * @param container 容器,ViewPager
     * @param position 当前要显示的条目的位置
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i(TAG, "instantiateItem初始化： " + position);

        int newPosition = position % imageViewList.size(); //无限循环

        ImageView imageView = imageViewList.get(newPosition);
        //1、把View对象添加到container中
        container.addView(imageView);
        //2、把View对象返回给框架，适配器
        return imageView; //必须重写，否则报异常
    }

    /**
     * 2、销毁条目
     * @param container
     * @param position
     * @param object 要销毁的对象
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i(TAG, "destroyItem销毁： " + position);

        container.removeView((View) object);
    }

    /**
     * 3、指定复用的判断逻辑，固定写法
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        //当滑到新的条目时，又返回来，使view可以被复用
        //返回判断规则
        return view == object;
    }
}
