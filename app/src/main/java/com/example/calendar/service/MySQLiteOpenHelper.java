package com.example.calendar.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.calendar.appContext.MyContext;
import com.example.calendar.bean.Tip;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private  static SQLiteDatabase writableDatabase;
    private static MySQLiteOpenHelper instance;


    private static final String DB_NAME = "notebook.db";
    private  final String CREATE_TABLE_SQL =
            "create table tip (id integer primary key autoincrement," +
                    "dayId text," +
                    "startTime text," +
                    "endTime text," +
                    "title text," +
                    "content text," +
                    "picture text," +
                    "state integer);";


    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        writableDatabase= getWritableDatabase();

    }

    public static synchronized MySQLiteOpenHelper getInstance() {
        if (writableDatabase == null) {
            instance = new MySQLiteOpenHelper(MyContext.getContext());
        }
        return instance;
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public long insertData(Object data,String table) {


        ContentValues contentValues = this.convertToContentValues(data);

        return writableDatabase.insert(table, null, contentValues);
    }

    public int deleteItemById(String id,String table){
        return writableDatabase.delete(table, "id=?", new String[]{id});
    }

    public int updateItemById(String id,String table,Object object){
        ContentValues contentValues = convertToContentValues(object);
        return writableDatabase.update(table,contentValues,"id=?",new String[]{id});
    }

    public int updateItemById(String id,String table,ContentValues contentValues){

        return writableDatabase.update(table,contentValues,"id=?",new String[]{id});
    }



    public <T> List<T> queryAllItems(String table,Class<T> myClass){
        String sql = "select * from "+table;
        Cursor cursor = writableDatabase.rawQuery(sql, null);

        return getObjectsWithCursor(myClass, cursor);

    }

    public <T> List<T> queryItemsByArgs(String table,Class<T> myClass,String selection,String[] selectionArgs){
        Cursor cursor = writableDatabase.query(table, null, selection, selectionArgs, null, null, null);

        return getObjectsWithCursor(myClass, cursor);

    }

    public <T> T queryItemById(String table,Class<T> myClass,String selection,String[] selectionArgs){
        Cursor cursor = writableDatabase.query(table, null, selection, selectionArgs, null, null, null);

        return getObjectWithCursor(myClass, cursor);

    }

    public Long queryCount(String table,String selection,String[] selectionArgs,String groupBy){
        if (selection==null){
            selection="";
        }
        else {
            selection = " where "+selection;
        }

        if (groupBy==null){
            groupBy="";
        }
        else {
            groupBy=" group by "+groupBy;
        }
//          select count(* )  count,  state from tip group by state
//        "select * from person where name like ?and age=?", new String[]{"%iteedu%", "4"}
        Cursor cursor = writableDatabase.rawQuery("select count(*) from "+table+selection+groupBy ,selectionArgs);

        cursor.moveToFirst();
        long aLong = cursor.getLong(0);
        cursor.close();
        return aLong;
    }

    public<T> List<T> queryGroupCount(String table,String columns, String selection,String[] selectionArgs,String groupBy,Class<T> myClass){
        if (selection==null){
            selection="";
        }
        else {
            selection = " where "+selection;
        }

        if (groupBy==null){
            groupBy="";
        }
        else {
            groupBy=" group by "+groupBy;
        }
//          select count(* )  count,  state from tip group by state
//        "select * from person where name like ?and age=?", new String[]{"%iteedu%", "4"}
        Cursor cursor = writableDatabase.rawQuery("select "+columns+" from "+table+selection+groupBy ,selectionArgs);

        List<T> objectsWithCursor = getObjectsWithCursor(myClass, cursor);
        return objectsWithCursor;
    }






    private <T> T getObjectWithCursor(Class<T> myClass, Cursor cursor) {

        if (cursor.isLast()){
            return null;
        }
        cursor.moveToNext();
        Field[] fields = myClass.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
        }
        T t = bindObjectWithCursor(myClass, cursor, fields);
        cursor.close();
        return t;


    }

    private <T> T bindObjectWithCursor(Class<T> myClass, Cursor cursor, Field[] fields) {
        T o=null;
        try {
            o= myClass.newInstance();
            for (Field field : fields) {  //遍历每个属性
                String name = field.getName();
                int columnIndex1 = cursor.getColumnIndex(name);
                String string = null;
                if (columnIndex1 > -1) {
                    string = cursor.getString(columnIndex1); //得到值
                }

                Type genericType = field.getGenericType();
                //Class类有个isAssignableFrom(Class<?> cls) 的方法，其作用是：
                //判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口
                Log.i("dd", genericType.toString());

                if (genericType.toString().equals("class java.lang.Integer")) {
                    assert string != null;
                    field.set(o, Integer.parseInt(string)); //对每个属性赋值
                } else {
                    field.set(o, string);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return o;
    }

    private <T> List<T> getObjectsWithCursor(Class<T> myClass, Cursor cursor) {


        List<T> dataList = new ArrayList<>();
        Field[] fields = myClass.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
        }
        while (cursor.moveToNext()){
            T o = bindObjectWithCursor(myClass, cursor,fields);
            dataList.add(o);

        }

        cursor.close();
        return dataList;
    }



    private ContentValues convertToContentValues(Object object) {
        Class<?> aClass = object.getClass();

        Field[] fields = aClass.getDeclaredFields();
        ContentValues contentValues = new ContentValues();

        try {
            for (Field field : fields) {
                field.setAccessible(true);

                if (field.get(object) != null) {

                    //当isAccessible()的结果是false时，
                    // 如果该字段是private修饰的不允许通过反射访问该字段 ，
                    // 必须要改成true才可以访问
                    // 所以  setAccessible(true) 的作用就是让我们在反射时访问私有变量
//                    Log.i(field.getName(), field.get(object).toString());
                    contentValues.put(field.getName(), Objects.requireNonNull(field.get(object)).toString());
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return contentValues;
    }
}


//assert string != null;
//        String fieldName = field.getName();
//        String firstLetter = fieldName.substring(0, 1).toUpperCase();
//        String methodName = "set"+firstLetter+fieldName.substring(1);
//        Method method = myClass.getMethod(methodName, type);
//        //从数据库获得的数据给对象赋值
//        if (type.toString().equals("class java.lang.Integer")) {
//
//        //12：00-》2324353
//        method.invoke(o, Integer.parseInt(string)); //对每个属性赋值
//        } else {
//        method.invoke(o, string);
//        }