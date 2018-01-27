package com.example.lunbo;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private int[] imageResIds;
    private String[] contentDescs;
    private LinearLayout ll_point_container;
    private TextView tv_desc;
    private List<ImageView> imgList;
    private int previousSelectedPosition=0;

    private boolean isRunning=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //初始化视图 View
        initView();

        //Model数据
        initData();

        //control 控制器
        initAdapter();

        //开启轮询
        new Thread(){

            @Override
            public void run() {

                while(isRunning){

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //往下跳

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });

                }

            }
        }.start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning=false;
    }

    private void initData() {
//初始化要显示的数据， 图片资源
        imageResIds = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};

        // 文本描述
        contentDescs = new String[]{
                "巩俐不低俗，我就不能低俗",
                "扑树又回来啦！再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级",
                "乐视网TV版大派送",
                "热血屌丝的反杀"
        };

        // 初始化要展示的5个ImageView
        ImageView img;
        LayoutParams layoutParams;

        imgList = new ArrayList<>();

        for (int i = 0; i < imageResIds.length; i++) {

            img = new ImageView(this);
            img.setBackgroundResource(imageResIds[i]);
            imgList.add(img);



        // 加小白点, 指示器

           View pointView;

            pointView=new View(this);
            pointView.setBackgroundResource(R.drawable.selector_bg_point);

           layoutParams=new LayoutParams(15,15);

        if(i != 0)
            layoutParams.leftMargin = 10;

            // 设置默认所有都不可用
            pointView.setEnabled(false);

            ll_point_container.addView(pointView, layoutParams);

        }
    }


    private void initView() {
        viewPager = findViewById(R.id.viewpager);

        viewPager.setOnPageChangeListener(this);
        ll_point_container =  findViewById(R.id.ll_point_container);

        tv_desc =  findViewById(R.id.tv_desc);

    }

    private void initAdapter() {

        ll_point_container.getChildAt(0).setEnabled(true);
        tv_desc.setText(contentDescs[0]);

        viewPager.setAdapter(new MyAdapter());

        //设置到中间某位置  而且是0 号位
      viewPager.setCurrentItem(50000);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        // 滚动时调用

      int newPosition = position % imgList.size();

        //设置文本
        tv_desc.setText(contentDescs[newPosition]);


        // 把之前的禁用, 把最新的启用, 更新指示器

        ll_point_container.getChildAt(previousSelectedPosition).setEnabled(false);
        ll_point_container.getChildAt(newPosition).setEnabled(true);

        // 记录之前的位置
        previousSelectedPosition = newPosition;

    }

    @Override
    public void onPageSelected(int position) {

        // 新的条目被选中时调用
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        // 滚动状态变化时调用
    }






    class MyAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }


        // 3. 指定复用的判断逻辑, 固定写法
        @Override
        public boolean isViewFromObject(View view, Object object) {
            // 当划到新的条目, 又返回来, view是否可以被复用.
            // 返回判断规则
            return view == object;

        }


// 1. 返回要显示的条目内容, 创建条目

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // container: 容器: ViewPager
            // position: 当前要显示条目的位置 0 -> 4

            int newPosition=position%imgList.size();

            ImageView imageView = imgList.get(newPosition);


            // a. 把View对象添加到container中
            container.addView(imageView);
            // b. 把View对象返回给框架, 适配器
            return imageView; // 必须重写, 否则报异常

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            // object 要销毁的对象
           container.removeView((View) object);
        }
    }

}
