package org.techtown.writeyourdreambyleeyongjun;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.ViewDebug;

public class AppHelper {
    private static final String TAG = "AppHelper";

    public static void println(String string){
        Log.d(TAG,string);
    }

    public static SQLiteDatabase database;

    private  static String createTableDreamInfoSql = "create table if not exists dreaminfo"+
            "(" +
            "    _id integer PRIMARY KEY autoincrement, " +
            "    title text, "+
            "    startYY text, "+
            "    startMM text, "+
            "    startDD text, "+
            "    finishYY text, "+
            "    finishMM text, "+
            "    finishDD text, "+
            "    image text, "+
            "    alarm_hour integer, "+
            "    alarm_minute integer, "+
            "    feedbackCount integer "+
            ")";

    public static void createTable(String tableName){
        println("createTable 호출됨 : "+tableName);

            database.execSQL(createTableDreamInfoSql);
            //println("테이블 생성됨");

    }

    public static void insertTitleData(String title){
        String sql = "insert into dreamInfo(title) values(?) ";
        Object[] params = {title};

        database.execSQL(sql,params);
        println("목표 데이터 추가함.");
    }

    public static void updateTitleData(String title,int keyId){
        String sql = "update dreamInfo set " +
                "title = ? " +
                "where _id = ?";
        Object[] params = {title, keyId};

        database.execSQL(sql,params);
        println("목표 데이터 변경함.");
    }

    public static void insertCalendarData(String startYY, String startMM, String startDD, String finishYY, String finishMM, String finishDD, int keyId){
        String sql = "update dreamInfo set " +
                "startYY = ?, " +
                "startMM = ?, " +
                "startDD = ?, " +
                "finishYY = ?, " +
                "finishMM = ?, " +
                "finishDD = ? " +
                "where _id= ?";
        Object[] params = {startYY, startMM, startDD, finishYY, finishMM, finishDD, keyId};

        database.execSQL(sql,params);
        println("날짜 데이터 추가함.");
    }

    public static void insertImageData(String image,int keyId){
        String sql = "update dreamInfo set " +
                "image = ? " +
                "where _id = ?";
        Object[] params = {image, keyId};

        database.execSQL(sql,params);
        println("이미지 데이터 추가함.");
    }

    public static void insertAlarmData(int alarmHour, int alarmMinute, int keyId){
        String sql = "update dreamInfo set " +
                "alarm_hour = ?, " +
                "alarm_minute = ? " +
                "where _id = ?";
        Object[] params = { alarmHour, alarmMinute, keyId};

        database.execSQL(sql,params);
        println("알림 데이터 추가함.");
    }

    public static void insertFeedbackData(String feedback, int keyId, int feedbackCount){
        String sql = "update dreamInfo set " +
                "feedback_"+(feedbackCount+"")+" = ? where _id= ?";
        Object[] params = { feedback,keyId};
        database.execSQL(sql,params);

        String sql2 = "update dreamInfo set " +
                "feedbackCount = ? where _id= ?";
        Object[] params2 = {feedbackCount+1, keyId};
        database.execSQL(sql2,params2);

        int k = feedbackCount+1;
        println("증가된 카운트는"+ k+"");
        println("피드백 데이터 추가함.");
        println("피드백 데이터 추가함.");
    }

    public static void updateFeedbackCount(int keyId, int count){
        String sql2 = "update dreamInfo set " +
                "feedbackCount = ? where _id= ?";
        Object[] params2 = {count, keyId};
        database.execSQL(sql2,params2);
        println("최종 카운트는"+ count+"");
    }
    public static void deleteTableData(int id){
        String sql3 = "select _id from dreaminfo";
        Cursor cursor = AppHelper.database.rawQuery(sql3, null);

        for(int i=0; i<id+1;i++){
            cursor.moveToNext();
        }
        String sql = "delete from dreamInfo "+
                "where _id = ?";
        Object[] params = {cursor.getInt(0)};

        database.execSQL(sql,params);
        println("데이터 삭제함.");
    }

    public static void deleteFeedbackData(int keyId, int feedbackIndex ){


    }

    public static void createColumn(int i){
        String columnName = "feedback_" +(i+"") ;
        String sql = "alter table dreaminfo add column "+ columnName +" text";

        database.execSQL(sql);

    }


    public static void reIndex(){
        String sql = "update sqlite_sequence set seq = 1 where name = 'dreaminfo'";
        database.execSQL(sql);
    }


    public static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            println("onCreate() 호출됨.");

            String sql = "create table if not exists dreaminfo"+
                    "(" +
                    "    _id integer PRIMARY KEY autoincrement, " +
                    "    title text, "+
                    "    startYY text, "+
                    "    startMM text, "+
                    "    startDD text, "+
                    "    finishYY text, "+
                    "    finishMM text, "+
                    "    finishDD text, "+
                    "    image text, "+
                    "    alarm_hour integer, "+
                    "    alarm_minute integer, "+
                    "    feedbackCount integer default 0 "+
                    ")";
            db.execSQL(sql);

            database = db;

            println("테이블 생성됨.");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("onUpgrade 호출됨 : " + oldVersion+ ", "+newVersion);

            //수정 필수!!

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            println("onOpen() 호출됨.");

        }
    }

}


