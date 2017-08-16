package MyFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import along.mifeng.us.mylongshangcheng.MyZhuYe;
import along.mifeng.us.mylongshangcheng.R;

/**
 * Created by 21903 on 2017/4/21.
 */

public class GouWuFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.gouwuchefragment, null);
        Button guang = (Button) v.findViewById(R.id.guang);
        guang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("us.mifeng");
                getActivity().sendBroadcast(intent);
            }
        });
        return v;
    }
}
