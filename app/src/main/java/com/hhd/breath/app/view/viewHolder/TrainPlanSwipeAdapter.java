package com.hhd.breath.app.view.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhd.breath.app.R;
import com.hhd.breath.app.model.TrainPlan;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30.
 */
public class TrainPlanSwipeAdapter extends RecyclerView.Adapter<TrainPlanSwipeAdapter.ViewHolder>{
    private Context context ;
    private List<TrainPlan> trainPlans ;
    private LayoutInflater layoutInflater = null;
    private OnRecyclerItemClickListener onRecyclerItemClickListener ;
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener){
        this.onRecyclerItemClickListener = onRecyclerItemClickListener ;
    }
    public TrainPlanSwipeAdapter(Context context ,List<TrainPlan> trainPlans) {
        this.context = context ;
        this.trainPlans = trainPlans ;
        layoutInflater  = LayoutInflater.from(context) ;
    }

    @Override
    public int getItemViewType(int position) {
        if (trainPlans!=null && !trainPlans.isEmpty()){
            if (position==(trainPlans.size()-1)){
                return 1 ;
            }else if (position == 0){
                return 2 ;
            }
        }
        return super.getItemViewType(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder  = null;
        View view = null  ;

        if (viewType==1){
            view = layoutInflater.inflate(R.layout.layout_add_train_plan,null) ;
            viewHolder = new ViewHolder(view,viewType) ;
        }else{
            view = layoutInflater.inflate(R.layout.layout_train_listview,null) ;
            viewHolder = new ViewHolder(view,viewType) ;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {



        holder.itemView.setTag(position);
        if (holder.getViewType()==0){
            TrainPlan trainPlan = trainPlans.get(position) ;
            holder.tvTrainName.setText("训练名称:" + trainPlan.getName());
            holder.tvCreateTime.setText("呼吸强度:"+trainPlan.getStrength()) ;
            holder.tvCumulativeTime.setText("呼吸持久:"+trainPlan.getPersistent());
            holder.imgFlag.setImageResource(R.mipmap.icon_custom_plan);
        }else if (holder.getViewType()==2){
            TrainPlan trainPlan = trainPlans.get(position) ;
            holder.tvTrainName.setText("训练名称:" + trainPlan.getName());
            holder.tvCreateTime.setText("呼吸强度:"+trainPlan.getStrength()) ;
            holder.tvCumulativeTime.setText("呼吸持久:"+trainPlan.getPersistent());
            holder.imgFlag.setImageResource(R.mipmap.icon_init_plan);
        }

        holder.itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_swipe_click));
        //holder.itemView.setBackground();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerItemClickListener.onItemClick(holder.itemView, (int) holder.itemView.getTag());
            }
        });



    }

    @Override
    public int getItemCount() {
        if (trainPlans!=null && !trainPlans.isEmpty()){
            return  trainPlans.size() ;
        }
        return 0;
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCreateTime ;
        private TextView tvCumulativeTime ;
        private TextView tvTrainName ;
        private ImageView imgFlag ;

        private int viewType ;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            this.viewType = viewType ;
            if (viewType==0 || viewType ==2){
                tvCumulativeTime = (TextView)itemView.findViewById(R.id.tvCumulativeTime) ;
                tvCreateTime = (TextView)itemView.findViewById(R.id.tvCreateTime) ;
                tvTrainName = (TextView)itemView.findViewById(R.id.tvTrainName) ;
                imgFlag = (ImageView)itemView.findViewById(R.id.imgFlag) ;
            }
        }

        public int getViewType(){
            return  this.viewType ;
        }
        @Override
        public String toString() {
            return super.toString();
        }
    }

    public interface OnRecyclerItemClickListener{
       public void onItemClick(View view,int position) ;
    }
}
