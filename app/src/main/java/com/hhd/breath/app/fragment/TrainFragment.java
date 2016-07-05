package com.hhd.breath.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hhd.breath.app.R;
import com.hhd.breath.app.main.ui.BreathTrainActivity;


public class TrainFragment extends Fragment {


    private RelativeLayout breath_chuji ;
    private RelativeLayout breath_zhongji ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mTrainView = inflater.inflate(R.layout.fragment_train, container, false) ;
        breath_chuji = (RelativeLayout)mTrainView.findViewById(R.id.breath_chuji) ;
        breath_zhongji = (RelativeLayout)mTrainView.findViewById(R.id.breath_zhongji) ;
        initEvent();
        return mTrainView;
    }

    private void initEvent(){
        breath_chuji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   BreathTrainActivity.actionStart(getActivity(), "缩唇呼吸训练(初级)", String.valueOf(2), String.valueOf(101),true);
            }
        });

        breath_zhongji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  BreathTrainActivity.actionStart(getActivity(),"缩唇呼吸训练(中级)",String.valueOf(3),String.valueOf(102),true);
            }
        });
    }
}
