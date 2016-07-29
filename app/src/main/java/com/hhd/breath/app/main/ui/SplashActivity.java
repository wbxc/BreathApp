package com.hhd.breath.app.main.ui;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.R;
import com.hhd.breath.app.adapter.GradViewAdapter;
import com.hhd.breath.app.utils.ShareUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.viewPageContent)
    ViewPager viewPageContent ;
    private List<View> viewPagers ;
    private GuideViewPager gradViewAdapter ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        viewPagers = new ArrayList<View>() ;
        createView();
        gradViewAdapter = new GuideViewPager(viewPagers) ;
        viewPageContent.setAdapter(gradViewAdapter);
    }

    private void createView(){

        for (int i=0 ; i<5 ;i++){

            View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.layout_viewpager_sp,null) ;
            ImageView imageView = (ImageView)view.findViewById(R.id.imgSp) ;
            Button btnStartApp = (Button)view.findViewById(R.id.btnStartApp) ;
            btnStartApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.setIsLaunchFirst(SplashActivity.this,true);
                    Intent intent = new Intent() ;
                    intent.setClass(SplashActivity.this,LoginBreathActivity.class) ;
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            });
            btnStartApp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        v.setBackgroundResource(R.mipmap.icon_sp_start_un_button);
                    }else if(event.getAction()==MotionEvent.ACTION_UP){
                        v.setBackgroundResource(R.mipmap.icon_sp_start_button);
                    }
                    return false;
                }
            });
            switch (i){
                case 0:
                    imageView.setBackgroundResource(R.mipmap.icon_sp1);
                    btnStartApp.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    imageView.setBackgroundResource(R.mipmap.icon_sp2);
                    btnStartApp.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    imageView.setBackgroundResource(R.mipmap.icon_sp3);
                    btnStartApp.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    imageView.setBackgroundResource(R.mipmap.icon_sp4);
                    btnStartApp.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    imageView.setBackgroundResource(R.mipmap.icon_sp5);
                    btnStartApp.setVisibility(View.VISIBLE);
                    break;
            }
            viewPagers.add(view);
        }
    }


    class GuideViewPager extends PagerAdapter{

        private List<View> viewPagers ;

        public GuideViewPager(List<View> viewPagers) {

            this.viewPagers = viewPagers ;
        }

        @Override
        public int getCount() {
            return viewPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(viewPagers.get(position));
            return viewPagers.get(position);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            ((ViewPager)container).removeView(viewPagers.get(position));
        }
    }
}
