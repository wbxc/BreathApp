package com.hhd.breath.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhd.breath.app.R;
import com.hhd.breath.app.model.RecordDayData;
import com.hhd.breath.app.utils.Utility;
import com.hhd.breath.app.widget.MyListView;

import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public class TrainListAdapter extends BaseAdapter {
    private Context mContext ;
    private List<RecordDayData> mRecordDayDatas ;

    public TrainListAdapter() {

    }
    public TrainListAdapter(Context mContext,List<RecordDayData> mTrainRecordDatas) {
        this.mContext = mContext ;
        this.mRecordDayDatas = mTrainRecordDatas ;
    }


    @Override
    public int getCount() {
        if (mRecordDayDatas!=null && !mRecordDayDatas.isEmpty())
            return  mRecordDayDatas.size() ;
        return 0;
    }


    public void  refresh(List<RecordDayData> mRecordDayDatas) {
        this.mRecordDayDatas = mRecordDayDatas ;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewTrainHolder mViewTrainHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_train_his,null) ;
            mViewTrainHolder = new ViewTrainHolder() ;
            mViewTrainHolder.mTopTextView = (TextView)convertView.findViewById(R.id.today_time) ;
            mViewTrainHolder.mSecondListView = (MyListView)convertView.findViewById(R.id.listView_second) ;
            convertView.setTag(mViewTrainHolder);
        }else{
            mViewTrainHolder = (ViewTrainHolder)convertView.getTag() ;
        }

        if (position<mRecordDayDatas.size()){
            mViewTrainHolder.mTopTextView.setText(mRecordDayDatas.get(position).getRecordTime());

            TrainSecondAdapter mTrainSecondAdapter = new TrainSecondAdapter(mRecordDayDatas.get(position).getmRecordUnitDatas(),mContext) ;
            mViewTrainHolder.mSecondListView.setAdapter(mTrainSecondAdapter);
            Utility.setListViewHeightBasedOnChildren(mViewTrainHolder.mSecondListView);
            mViewTrainHolder.mSecondListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mRecordDayDatas.get(position);
    }

    class ViewTrainHolder{

        TextView mTopTextView ;
        MyListView mSecondListView ;
    }
}
