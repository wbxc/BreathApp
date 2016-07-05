package com.hhd.breath.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhd.breath.app.R;
import com.hhd.breath.app.model.RecordUnitData;

import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public class TrainSecondAdapter extends BaseAdapter {

    private List<RecordUnitData> mRecordUnitDatas ;
    private Context mContext ;

    public TrainSecondAdapter() {
    }
    public TrainSecondAdapter(List<RecordUnitData> mRecordUnitDatas, Context mContext) {
        this.mRecordUnitDatas = mRecordUnitDatas;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        if (mRecordUnitDatas!=null && !mRecordUnitDatas.isEmpty())
            mRecordUnitDatas.size() ;
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mRecordUnitDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewSecond mViewSecond ;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_second_expand, null) ;
            mViewSecond = new ViewSecond() ;
            mViewSecond.recordName = (TextView)convertView.findViewById(R.id.record_name) ;
            mViewSecond.starandRate = (TextView)convertView.findViewById(R.id.strandRate) ;
            mViewSecond.recordTime = (TextView)convertView.findViewById(R.id.record_time) ;
            mViewSecond.second_bootom_view = (View)convertView.findViewById(R.id.second_bootom_view) ;
            mViewSecond.second_bootom_view1 = (View)convertView.findViewById(R.id.second_bootom_view1) ;
            convertView.setTag(mViewSecond);
        }else{
            mViewSecond = (ViewSecond)convertView.getTag() ;
        }

        if (position<mRecordUnitDatas.size()){
            mViewSecond.recordTime.setText(mRecordUnitDatas.get(position).getTrainTime());
            mViewSecond.starandRate.setText(mRecordUnitDatas.get(position).getStandardRate());
            mViewSecond.recordName.setText(mRecordUnitDatas.get(position).getTrainName());
            mViewSecond.recordTime.setText(mRecordUnitDatas.get(position).getTrainTime());
            mViewSecond.starandRate.setText(mRecordUnitDatas.get(position).getStandardRate());
        }

        if (position==mRecordUnitDatas.size()-1){
            mViewSecond.second_bootom_view.setVisibility(View.GONE);
            mViewSecond.second_bootom_view1.setVisibility(View.VISIBLE);
        }else{
            mViewSecond.second_bootom_view1.setVisibility(View.GONE);
            mViewSecond.second_bootom_view.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    class ViewSecond{
        TextView recordTime ;
        TextView recordName ;
        TextView starandRate ;
        View second_bootom_view ;
        View second_bootom_view1 ;
    }

}
