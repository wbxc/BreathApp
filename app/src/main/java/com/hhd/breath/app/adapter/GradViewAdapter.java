package com.hhd.breath.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.R;
import com.hhd.breath.app.model.MedicalHis;

import java.util.List;

public class GradViewAdapter extends BaseAdapter {

        private Context mContext ;
        private List<MedicalHis> exportTimeTypes ;

        public GradViewAdapter(Context mContext ,List<MedicalHis> exportTimeTypes) {
            // TODO Auto-generated constructor stub
            this.exportTimeTypes = exportTimeTypes ;
            this.mContext = mContext ;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (exportTimeTypes!=null) {
                return exportTimeTypes.size() ;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return exportTimeTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public void refresh(List<MedicalHis> medicalHises){

            this.exportTimeTypes = medicalHises ;
            notifyDataSetChanged();

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            GradViewHolder mGradView ;
            if (convertView==null) {
                mGradView = new GradViewHolder() ;
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_gradview_item, null) ;
                mGradView.textMedicalName = (TextView)convertView.findViewById(R.id.medical_name) ;
                mGradView.content_bg = (RelativeLayout)convertView.findViewById(R.id.content_bg) ;
                convertView.setTag(mGradView);
            }else {
                mGradView = (GradViewHolder) convertView.getTag() ;
            }
            if (position>=0) {
                if (exportTimeTypes.get(position).getType()>0) {
                    mGradView.textMedicalName.setTextColor(mContext.getResources().getColor(R.color.common_top_color));
                    mGradView.content_bg.setBackgroundResource(R.drawable.select_blue_circle);
                }else {
                    mGradView.textMedicalName.setTextColor(mContext.getResources().getColor(R.color.common_color_9A9A9A));
                    mGradView.content_bg.setBackgroundResource(R.drawable.select_gray_circle);
                }
                mGradView.textMedicalName.setText(exportTimeTypes.get(position).getName());
            }

            return convertView;
        }
        class GradViewHolder{
            TextView textMedicalName ;

            RelativeLayout content_bg ;
        }
}