package com.example.calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.calendar.tool.MyTool;
import com.example.calendar.bean.Tip;
import com.example.calendar.constant.CameraConstant;
import com.example.calendar.service.TipService;

import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity {


    public static final int REQUEST_CODE_TAKE = 1;
    public static final int REQUEST_CODE_CHOOSE = 0;
    TextView dateInput;
    TextView startTimeInput;
    TextView endTimeInput;
    TextView tipTitleInput;
    TextView tipContentInput;
    ImageView imageInput;
    Button cclBtn;
    Button addBtn;
    TipService tipService;
    Calendar calendar = Calendar.getInstance();
    Integer id=-1;
    Uri imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initProperty();

        initDateInputEvent();

        initStartTimeInputEvent();

        initEndTimeInputEvent();

        initAddOrUpdateTipEvent();

        renderPageByInitialData();

        initCancelAddTipEvent();

        initCameraEvent();


    }

    private void initCameraEvent() {

        imageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果有这个权限，就进行拍照
                if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    //如果没有，去申请权限
                    ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        });

    }

    @Override//接受申请的权限
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            //判断申请的结果是否通过
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                takePicture();
            }else {
                Toast.makeText(this,"没有摄像头权限",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takePicture() {
        imageUrl = MyTool.getDefaultPictureUri(this,String.valueOf(new Date().getTime()));
        Intent intent = new Intent();
        intent.setAction(CameraConstant.MY_IMAGE_CAPTURE);
        //拍完照之后，照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUrl);
        //需要返回的结果
        startActivityForResult(intent, REQUEST_CODE_TAKE);

    }
//
    @Override//接受数据
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果是拍照的话
        if (requestCode==REQUEST_CODE_TAKE){
            if (resultCode == RESULT_OK){
                //获取拍摄的图片
                Bitmap bitmap = MyTool.getBitmapForUri(this,imageUrl);
                imageInput.setImageBitmap(bitmap);
            }
        }
    }

    private void initCancelAddTipEvent() {
        cclBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void renderPageByInitialData() {
        Intent intent = getIntent();
        String tipId = intent.getStringExtra("tipId");
        if (tipId==null){ //空的，需增加
            String date = intent.getStringExtra("date");
            dateInput.setText(date);
            addBtn.setText("添加");
        }
        else { //更新
            Tip tip = tipService.queryItemById(tipId);
            id=tip.getId();
            dateInput.setText(tip.getDayId());
            startTimeInput.setText(tip.getStartTime());
            endTimeInput.setText(tip.getEndTime());
            tipTitleInput.setText(tip.getTitle());
            tipContentInput.setText(tip.getContent());
            imageUrl = Uri.parse(tip.getPicture());
            imageInput.setImageBitmap(MyTool.getBitmapForUri(this,imageUrl));
            addBtn.setText("更新");
//
        }

    }



    private void initEndTimeInputEvent() {
        endTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTool.showTimePickerDialog(EditActivity.this,endTimeInput,calendar.get(Calendar.HOUR_OF_DAY)+8,calendar.get(Calendar.MINUTE));
            }
        });
    }

    private void initAddOrUpdateTipEvent() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateInput.getText().toString();
                String startTime = startTimeInput.getText().toString();
                String endTime = endTimeInput.getText().toString();
                String tipTitle = tipTitleInput.getText().toString();
                String tipContent = tipContentInput.getText().toString();
//                String image = imageInput.getText().toString();
                Tip tip = new Tip(date, startTime, endTime, tipTitle, tipContent, imageUrl.toString());
                if (id==-1){//插入
                    long l = tipService.insertData(tip);
                    MyTool.showToast(EditActivity.this,l!=-1,"添加成功","添加失败");

                }else {//更新
                    tip.setId(id);
                    int i = tipService.updateItemById(tip);
                    MyTool.showToast(EditActivity.this,i!=-1,"更新成功","更新失败");

                }
                cclBtn.setText("返回");


            }
        });
    }



    private void initStartTimeInputEvent() {
        startTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTool.showTimePickerDialog(EditActivity.this,startTimeInput,calendar.get(Calendar.HOUR_OF_DAY)+8,calendar.get(Calendar.MINUTE));
            }
        });

    }

    private void initDateInputEvent() {
        dateInput.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                MyTool.showDatePickerDialog(EditActivity.this,dateInput,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }
        });
    }



    private void initProperty() {
        dateInput = findViewById(R.id.dateInput);
        startTimeInput = findViewById(R.id.startTimeInput);
        endTimeInput = findViewById(R.id.endTimeInput);
        tipTitleInput=findViewById(R.id.tipTitleInput);
        tipContentInput=findViewById(R.id.tipContentInput);
        imageInput=findViewById(R.id.imageInput);
        cclBtn=findViewById(R.id.cclBtn);
        addBtn=findViewById(R.id.addBtn);
        tipService=new TipService();
    }
}