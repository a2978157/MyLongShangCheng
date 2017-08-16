package MyFragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import along.mifeng.us.mylongshangcheng.FenLei;
import along.mifeng.us.mylongshangcheng.MD5Util;
import along.mifeng.us.mylongshangcheng.R;
import along.mifeng.us.mylongshangcheng.TimeUtil;

/**
 * Created by 21903 on 2017/4/21.
 */

public class FenLeiFragment extends Fragment {
    ArrayList<String> list=new ArrayList<>();
    ArrayList<String> list2=new ArrayList<>();
    private ListView lv;
    private StringBuffer sb;
    private ListView lv2;
    private MyAdapter ma;
    private MyAdapter ma1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fenleifragment, null);
        initWang();
        initView(v);
        return v;
    }

    private void initWang() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s1 = MD5Util.md5(TimeUtil.getTime() + "shop.sdlinwang.com");
                    String s ="&time=" + URLEncoder.encode(String.valueOf(TimeUtil.getTime()), "UTF-8") + "&sign=" + URLEncoder.encode(s1, "UTF-8");
                    System.out.println(s);
                    byte[] bytes = s.getBytes();
                    URL url = new URL("http://shop.sdlinwang.com/index.php?m=Api&c=Goods&a=goodsCategoryList");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setDoInput(true);
                    http.setDoOutput(true);
                    OutputStream os = http.getOutputStream();
                    os.write(bytes);
                    os.flush();
                    os.close();
                    InputStream is = http.getInputStream();
                    byte[] by = new byte[1024];
                    int ii = 0;
                    sb = new StringBuffer();
                    while ((ii = is.read(by)) != -1) {
                        sb.append(new String(by, 0, ii));
                    }
                    is.close();
                    Log.e("e",sb.toString());
                 /*   URL url = new URL("http://shop.sdlinwang.com/index.php?m=Api&c=Goods&a=goodsCategoryList");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    InputStream inputStream = conn.getInputStream();
                    byte[] by = new byte[1024];
                    int len = 0;
                    sb = new StringBuffer();
                    while ((len = inputStream.read(by)) != -1) {
                        sb.append(new String(by, 0, len));
                    }
                    inputStream.close();*/
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i=0;i<result.length();i++){
                            JSONObject jsonObject1 = result.getJSONObject(i);
                            String name = jsonObject1.getString("name");
                            list.add(name);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list2.clear();
                            JSONObject jsonObject = null;
                            try {
                                Log.e("e",sb.toString());
                                jsonObject = new JSONObject(sb.toString());
                                JSONArray result = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = result.getJSONObject(0);
                                JSONArray twoCategory = jsonObject1.getJSONArray("twoCategory");
                                for (int u=0;u<twoCategory.length();u++){
                                    JSONObject jsonObject2 = twoCategory.getJSONObject(u);
                                    String name = jsonObject2.getString("name");
                                    list2.add(name);
                                }
                                lv2.setAdapter(new MyAdapter2(list2));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ma1 = new MyAdapter(list);
                            lv.setAdapter(ma1);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private class MyAdapter extends BaseAdapter {
        ArrayList<String> list3=new ArrayList<>();
        private int position = 0;
        public MyAdapter(ArrayList<String> list3){
            this.list3=list3;
        }

        @Override
        public int getCount() {
            return list3.size();
        }

        @Override
        public Object getItem(int position) {
            return list3.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public void setSelectedPosition(int position) {
            this.position = position;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHander vh=null;
            if (view==null){
                view=View.inflate(getActivity(),R.layout.fenlei,null);
                vh=new ViewHander();
                vh.tv= (TextView) view.findViewById(R.id.tv);
                view.setTag(vh);
            }else {
                vh= (ViewHander) view.getTag();
            }
            vh.tv.setText(list3.get(i));
            if (i==position){
                vh.tv.setBackgroundColor(Color.WHITE);
            }else {
                vh.tv.setBackgroundColor(0xEEEEEE);
            }
            return view;
        }
        class ViewHander{
            TextView tv;
        }
    }
    private class MyAdapter2 extends BaseAdapter {
        ArrayList<String> list3=new ArrayList<>();
        public MyAdapter2(ArrayList<String> list3){
            this.list3=list3;
        }

        @Override
        public int getCount() {
            return list3.size();
        }

        @Override
        public Object getItem(int position) {
            return list3.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHander vh=null;
            if (view==null){
                view=View.inflate(getActivity(),R.layout.fenlei,null);
                vh=new ViewHander();
                vh.tv= (TextView) view.findViewById(R.id.tv);
                view.setTag(vh);
            }else {
                vh= (ViewHander) view.getTag();
            }
            vh.tv.setText(list3.get(i));
            return view;
        }
        class ViewHander{
            TextView tv;
        }
    }
    private void initView(View v) {
        lv = (ListView) v.findViewById(R.id.lv);
        lv2 = (ListView) v.findViewById(R.id.lv2);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),FenLei.class);
                intent.putExtra("name",list2.get(i));
                startActivity(intent);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list2.clear();
                ma1.setSelectedPosition(i);
                ma1.notifyDataSetInvalidated();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(sb.toString());
                    JSONArray result = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = result.getJSONObject(i);
                    JSONArray twoCategory = jsonObject1.getJSONArray("twoCategory");
                    for (int u=0;u<twoCategory.length();u++){
                        JSONObject jsonObject2 = twoCategory.getJSONObject(u);
                        String name = jsonObject2.getString("name");
                        list2.add(name);
                    }
                    lv2.setAdapter(new MyAdapter2(list2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
