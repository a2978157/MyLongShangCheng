package along.mifeng.us.mylongshangcheng;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import MyFragment.Jsonben;

/**
 * Created by 21903 on 2017/5/11.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    ArrayList<Jsonben> list=new ArrayList<Jsonben>();
    Context context;
    private onItemClickListerner listener;//自己写的回调接口
    public MyAdapter(Context ctx,ArrayList<Jsonben> list) {
        this.context=ctx;
        this.list=list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.listitme, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position){
        holder.tv.setText(list.get(position).getGoods_name());
        holder.jiage.setText(list.get(position).getShop_price());
        Glide.with(context).load(list.get(position).getOriginal_img()).placeholder(R.mipmap.tupian).thumbnail(0.01f).into(holder.iv);
        //设置回调
        if(listener!=null){
            //itme点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos=holder.getAdapterPosition();
                    //回调
                    listener.onItemClick(holder.itemView, pos);
                }
            });
            //删除选中条目的长按点击事件
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    int pos=holder.getAdapterPosition();
                    listener.onLongItemClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView iv;
        TextView jiage;

        public MyViewHolder(View view){
            super(view);
            tv= (TextView) view.findViewById(R.id.tv);
            jiage= (TextView) view.findViewById(R.id.jiage);
            iv= (ImageView) view.findViewById(R.id.iv);
        }
    }
    //自己定义的回调接口
    public interface onItemClickListerner{
        void onItemClick(View view,int position);
        void onLongItemClick(View view,int position);
    }
    //设置回调接口的方法
    public void setOnItemClickListener(onItemClickListerner listener){
        this.listener=listener;
    }
    //删除选中条目的方法
    public void RemoveView(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }
}
