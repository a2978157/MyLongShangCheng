package MyFragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import along.mifeng.us.mylongshangcheng.R;

/**
 * Created by 21903 on 2017/4/21.
 */

public class MyListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Jsonben> list=new ArrayList<>();
    public  MyListAdapter(Context context,ArrayList<Jsonben> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHerdle viewHerdle=null;
        if (view==null){
            view=View.inflate(context, R.layout.listitme,null);
            viewHerdle =  new ViewHerdle();
            viewHerdle.tv= (TextView) view.findViewById(R.id.tv);
            viewHerdle.jiage= (TextView) view.findViewById(R.id.jiage);
            viewHerdle.iv= (ImageView) view.findViewById(R.id.iv);
            view.setTag(viewHerdle);
        }else {
            viewHerdle= (ViewHerdle) view.getTag();
        }
        viewHerdle.tv.setText(list.get(i).getGoods_name());
        viewHerdle.jiage.setText(list.get(i).getShop_price());
        Glide.with(context).load(list.get(i).getOriginal_img()).placeholder(R.mipmap.tupian).thumbnail(0.01f).into(viewHerdle.iv);

        return view;
    }
    class ViewHerdle{
        TextView tv;
        ImageView iv;
        TextView jiage;

    }

}
