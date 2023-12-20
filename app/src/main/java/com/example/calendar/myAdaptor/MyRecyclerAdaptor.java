package com.example.calendar.myAdaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendar.EditActivity;
import com.example.calendar.R;
import com.example.calendar.tool.MyTool;
import com.example.calendar.bean.Tip;
import com.example.calendar.constant.TipState;
import com.example.calendar.service.TipService;

import java.util.List;

public class MyRecyclerAdaptor extends RecyclerView.Adapter<MyRecyclerAdaptor.ViewHolder> {

    Context context;
    List<Tip> tips;

    public MyRecyclerAdaptor(Context context, List<Tip> tips) {
        this.context = context;
        this.tips = tips;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip2, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("dd","12345");
        holder.setData(tips.get(position));
        holder.setEvent(position);

    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TipService tipService;
        CheckBox checkBox;
        TextView tipTitle;
        TextView tipDate;
        TextView tipTime;
        ImageButton dlnBtn;
        RelativeLayout relativeLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkBox);
            tipTitle=itemView.findViewById(R.id.tipTitle);
            tipDate=itemView.findViewById(R.id.tipDate);
            tipTime=itemView.findViewById(R.id.tipTime);
            dlnBtn=itemView.findViewById(R.id.dltBtn);
            tipService=new TipService();
            relativeLayout=itemView.findViewById(R.id.container);


        }

        void setData(Tip tip){
//            checkBox.setText(ti);
            tipTitle.setText(tip.getTitle());
            tipDate.setText(tip.getDayId());
            tipTime.setText(tip.getStartTime());
            if (tip.getState()==TipState.isOk){
                checkBox.setChecked(true);
            }else {
                checkBox.setChecked(false);
            }

            String currentCalendar = MyTool.getCurrentCalendar();
            if (Integer.parseInt(currentCalendar)>Integer.parseInt(tip.getDayId())
                    && tip.getState().equals(TipState.isNo)){
                relativeLayout.setBackgroundColor(Color.RED);
                Log.i("dd","1234");
            }
        }

        void setEvent(int position){
            initTipStateEvent(position);
            initDltEvent(position);
            initTurnToDetailEvent(position);
        }

        private void initTurnToDetailEvent(int position) {
            tipTitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("tipId", tips.get(position).getId() + "");
                    context.startActivity(intent);
                    return false;
                }
            });


        }

        private void initDltEvent(int position) {
            dlnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyTool.getDialog(view.getContext(),"您确认要删除"+tips.get(position).getTitle()+"吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int i1 = tipService.deleteItemById(tips.get(position).getId()+"");
                            if (i1!=-1){
                                tips.remove(position);
                            }
                            notifyDataSetChanged();
                            MyTool.showToast(view.getContext(), i1!=-1,"删除成功","删除失败");

                        }
                    }).show();
                }
            });
        }

        private void initTipStateEvent(int position) {
            checkBox.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        int i = tipService.updateStateById(tips.get(position).getId() + "", TipState.isOk);

                        MyTool.showToast(context,i!=-1,"已完成","修改失败",()->{
                            relativeLayout.setBackgroundColor(Color.WHITE);
                        },null);
                    }
                    else {
                        int i = tipService.updateStateById(tips.get(position).getId() + "", TipState.isNo);
                        MyTool.showToast(context,i!=-1,"未完成","修改失败",()->{
                            relativeLayout.setBackgroundColor(Color.RED);
                        },null);

                    }
                }
            });
        }
    }


}
