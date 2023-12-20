package com.example.calendar.myAdaptor;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.calendar.R;
import com.example.calendar.bean.Tip;
import com.example.calendar.constant.TipState;
import com.example.calendar.tool.MyTool;

import java.util.List;

public class MyListAdaptor extends BaseAdapter {

    private Context context;
    private List<Tip> tips;

    public MyListAdaptor(Context context, List<Tip> tips) {
        this.context = context;
        this.tips = tips;
    }

    @Override
    public int getCount() {
        return tips.size();
    }

    @Override
    public Object getItem(int position) {
        return tips.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tip,parent,false);
            viewHolder.tipTitle = convertView.findViewById(R.id.tipTitle);
            viewHolder.tipTime=convertView.findViewById(R.id.tipTime);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.tipTitle.setText(tips.get(position).getTitle());
        viewHolder.tipTime.setText(tips.get(position).getStartTime()+"-"+tips.get(position).getEndTime());


        return convertView;
    }

    private  class ViewHolder{
        TextView tipTitle;
        TextView tipTime;
    }
}
