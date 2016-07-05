package com.hhd.breath.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhd.breath.app.R;
import com.hhd.breath.app.model.HisRecordData;

import java.util.List;

/**
 * Created by familylove on 2015/12/4.
 */
public class TrainRecordAdapter extends BaseAdapter {

    private Context mContext ;
    private List<HisRecordData> mHisRecordDatas ;


    private LayoutInflater mLayoutInflater ;

    public TrainRecordAdapter() {

    }

    public  TrainRecordAdapter(Context mContext,List<HisRecordData> mHisRecordDatas ){
        this.mContext = mContext ;
        this.mHisRecordDatas = mHisRecordDatas ;
        mLayoutInflater = LayoutInflater.from(mContext) ;
    }


    @Override
    public int getCount() {
        if (mHisRecordDatas!=null){
            return mHisRecordDatas.size() ;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder ;

        if (convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.adapter_train_record,null) ;
            mViewHolder = new ViewHolder() ;
            mViewHolder.mRecordValue = (TextView)convertView.findViewById(R.id.train_value) ;
            mViewHolder.mTimeRecord = (TextView)convertView.findViewById(R.id.train_time) ;
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder)convertView.getTag() ;
        }

        mViewHolder.mRecordValue.setText(mHisRecordDatas.get(position).getResultValue());
        mViewHolder.mTimeRecord.setText(mHisRecordDatas.get(position).getRecordTime());
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return mHisRecordDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{

        TextView mTimeRecord ;
        TextView mRecordValue ;

    }
}
