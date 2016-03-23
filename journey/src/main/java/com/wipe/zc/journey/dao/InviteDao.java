package com.wipe.zc.journey.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wipe.zc.journey.db.IMSQliteOpenHelper;
import com.wipe.zc.journey.domain.Invite;
import com.wipe.zc.journey.global.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/3/22.
 */
public class InviteDao {

    private static IMSQliteOpenHelper imSQLite = new IMSQliteOpenHelper(MyApplication.getContext());

    /**
     * 添加邀请信息
     *
     * @param invite
     */
    public static void addInvite(Invite invite) {
        SQLiteDatabase db = imSQLite.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("inviter", invite.getInviter());
        values.put("reason", invite.getReason());
        db.insert("invite", null, values);
    }

    /**
     * 添加邀请信息
     *
     * @param inviter
     * @param reason
     */
    public static void addInvite(String inviter, String reason) {
        SQLiteDatabase db = imSQLite.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("inviter", inviter);
        values.put("reason", reason);
        db.insert("invite", null, values);
    }

    /**
     * 移除邀请消息
     *
     * @param invite
     */
    public static void deleteInvite(Invite invite) {
        SQLiteDatabase db = imSQLite.getReadableDatabase();
        db.delete("invite", "inviter=?", new String[]{invite.getInviter()});
    }

    /**
     * 移除邀请消息
     *
     * @param inviter
     */
    public static void deleteInvite(String inviter) {
        SQLiteDatabase db = imSQLite.getReadableDatabase();
        db.delete("invite", "inviter=?", new String[]{inviter});
    }


    /**
     * 查询邀请
     * @return
     */
    public static boolean checkInvite(String inviter){
        SQLiteDatabase db = imSQLite.getReadableDatabase();
        Cursor cursor = db.query("invite",null,"inviter=?",new String[]{inviter},null,null,null,null);
        if(cursor.moveToNext()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取所有邀请信息
     */
    public static List<Invite> queryAllInvite(){
        SQLiteDatabase db = imSQLite.getReadableDatabase();
        Cursor cursor = db.query("invite", null, null, null, null, null, null, null);
        List<Invite> list = new ArrayList<>();
        while (cursor.moveToNext()){
            Invite invite = new Invite();
            invite.setInviter(cursor.getString(cursor.getColumnIndex("inviter")));
            invite.setReason(cursor.getString(cursor.getColumnIndex("reason")));
            list.add(invite);
        }
        return list;
    }
}
