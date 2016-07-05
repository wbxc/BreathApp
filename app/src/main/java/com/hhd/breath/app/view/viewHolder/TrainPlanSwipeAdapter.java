package com.hhd.breath.app.view.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        }else {
            view = layoutInflater.inflate(R.layout.adaper_train_listview,null) ;
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
            holder.tvStrength.setText("呼吸强度:"+trainPlan.getStrength()) ;
            holder.tvPresenter.setText("呼吸持久:"+trainPlan.getPersistent());
            holder.tvController.setText("呼吸控制:"+trainPlan.getControl());
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

        private TextView tvStrength ;
        private TextView tvPresenter ;
        private TextView tvController ;
        private TextView tvStartTime ;
        private TextView tvTrainName ;

        private int viewType ;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            this.viewType = viewType ;
            if (viewType==0){

                tvController = (TextView)itemView.findViewById(R.id.tvControl) ;
                tvPresenter = (TextView)itemView.findViewById(R.id.tvPersistent) ;
                tvStartTime = (TextView)itemView.findViewById(R.id.tvTrainStartTime) ;
                tvStrength = (TextView)itemView.findViewById(R.id.tvStrength) ;
                tvTrainName = (TextView)itemView.findViewById(R.id.tvTrainName) ;
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
