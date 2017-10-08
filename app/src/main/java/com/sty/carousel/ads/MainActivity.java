package com.sty.carousel.ads;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sty.carousel.ads.adapter.MyAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private ViewPager viewPager;
    private LinearLayout llPointContainer;
    private TextView tvDesc;
    private int[] imageResIds;
    private String[] contentDescs;
    private ArrayList<ImageView> imageViewList;
    private int previousSelectPosition;

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化布局View视图
        initViews();

        //Model数据
        initData();

        //Controller控制器
        initAdapter();

        //开启轮询
        new Thread(){
            public void run(){
                isRunning = true;
                while (isRunning){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //往下跳一位
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            Log.i(TAG, "设置当前位置: " + viewPager.getCurrentItem());
                        }
                    });
                }
            }
        }.start();
    }

    private void initViews(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1); //左右各保留几个view作为备用
        viewPager.setOnPageChangeListener(this); //设置页面更新监听
        llPointContainer = (LinearLayout) findViewById(R.id.ll_point_content);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
    }

    //初始化要显示的数据
    private void initData(){
        //图片资源id数组
        imageResIds = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};

        //文本描述
        contentDescs = new String[]{
                "巩俐不低俗，我就不能低俗",
                "扑树又回来啦！再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级",
                "乐视网TV版大派送",
                "热血屌丝的反杀"
        };

        //初始化要显示的5个ImageView
        imageViewList = new ArrayList<>();
        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for(int i = 0; i < imageResIds.length; i++){
            imageView = new ImageView(this);
            imageView.setBackgroundResource(imageResIds[i]);
            imageViewList.add(imageView);

            //加上小白点，指示器
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.selector_bg_point);

            layoutParams = new LinearLayout.LayoutParams(15, 15);
            if(i != 0) {
                layoutParams.leftMargin = 30;
            }
            pointView.setEnabled(false);
            llPointContainer.addView(pointView, layoutParams);
        }
    }

    private void initAdapter(){
        llPointContainer.getChildAt(0).setEnabled(true);
        tvDesc.setText(contentDescs[0]);
        previousSelectPosition = 0;

        //设置适配器
        viewPager.setAdapter(new MyAdapter(imageViewList));

        //默认设置到中间的某个位置--------向左无限循环
        //int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        // 2147483647 / 2 - 1073741823 - (1073741823 % 5)
        //viewPager.setCurrentItem(pos);

        //默认设置到中间的某个位置--------向左无限循环
        viewPager.setCurrentItem(5000000); //当设置过大时ViewPager处理有校问题，所有设置小一些

    }

    //滚动时调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //新的条目被选中时调用
    @Override
    public void onPageSelected(int position) {
        Log.i(TAG, "onPageSelected: " + position);

        int newPosition = position % imageViewList.size(); //处理无限循环

        //设置文本
        tvDesc.setText(contentDescs[newPosition]);
        //更新指示器(两种方法择一即可)
//        for(int i = 0; i < llPointContainer.getChildCount(); i++){
//            View childAt = llPointContainer.getChildAt(i);
//            childAt.setEnabled(newPosition == i);
//        }
        //把之前的禁用，把最新的启用，更新指示器
        llPointContainer.getChildAt(previousSelectPosition).setEnabled(false);
        llPointContainer.getChildAt(newPosition).setEnabled(true);
        //记录之前的位置
        previousSelectPosition = newPosition;
    }

    //滚动状态变化时调用
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }
}
