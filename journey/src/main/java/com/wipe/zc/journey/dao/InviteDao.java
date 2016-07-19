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
 * 用于本地使用，保存好友请求信息
 */
public class InviteDao {

    private static IMSQliteOpenHelper imSQLite = new IMSQliteOpenHelper(MyApplication.getContext());

    /**
     * 添加邀请信息
     *
     * @param invite 好友请求
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
     * @param inviter   好友请求
     * @param reason    请求内容
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
     * @param invite    好友请求
     */
    public static void deleteInvite(Invite invite) {
        SQLiteDatabase db = imSQLite.getReadableDatabase();
        db.delete("invite", "inviter=?", new String[]{invite.getInviter()});
    }

    /**
     * 移除邀请消息
     *
     * @param inviter   邀请者昵称
     */
    public static void deleteInvite(String inviter) {
        SQLiteDatabase db = imSQLite.getReadableDatabase();
        db.delete("invite", "inviter=?", new String[]{inviter});
    }


    /**
     * 查询邀请
     * @return  邀请者昵称
     */
    public static boolean checkInvite(String inviter){
        SQLiteDatabase db = imSQLite.getReadableDatabase();
        Cursor cursor = db.query("invite",null,"inviter=?",new String[]{inviter},null,null,null,null);
        if(cursor.moveToNext()){
            cursor.close();
            return true;
        }else{
            cursor.close();
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
        cursor.close();
        return list;
    }
}
