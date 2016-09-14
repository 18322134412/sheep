package com.xpple.sheep.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseListAdapter;
import com.xpple.sheep.bean.OverageHistory;

import java.util.List;

public class OverageDetailsAdapter extends BaseListAdapter<OverageHistory> {

    public OverageDetailsAdapter(Context context, List<OverageHistory> items) {
        super(context, items);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_item, null);
        }
        OverageHistory contract = getList().get(position);
//        TextView tv_appointment_time = ViewHolder.get(convertView, R.id.tv_time);
//        TextView tv_order_state = ViewHolder.get(convertView, R.id.tv_order_state);
//        TextView tv_project_name = ViewHolder.get(convertView, R.id.tv_project_name);
//        TextView tv_beautician = ViewHolder.get(convertView, R.id.tv_location);
////        TextView tv_server_place = ViewHolder.get(convertView, R.id.tv_server_place);
//        TextView tv_original_price = ViewHolder.get(convertView, R.id.tv_original_count);
//        TextView tv_real_pay = ViewHolder.get(convertView, R.id.tv_real_pay);
//        TextView tv_server_time = ViewHolder.get(convertView, R.id.tv_server_t);
//
//        ImageView iv_order =  ViewHolder.get(convertView,R.id.iv_order_item);

//
//        tv_project_name.setText(contract.getProject_name().trim());
//        tv_order_state.setText(contract.getOrder_state().toString().trim());
        return convertView;
    }


}
