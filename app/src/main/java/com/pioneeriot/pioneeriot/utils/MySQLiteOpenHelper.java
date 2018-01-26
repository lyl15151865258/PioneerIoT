package com.pioneeriot.pioneeriot.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LiYuliang on 2017/6/5.
 * 数据库操作类
 *
 * @author LiYuliang
 * @version 2017/12/22
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "pioneeriot.db";
    private static final int DB_VISION = 20180116;
    private Context mContext;
    private MySQLiteOpenHelper mMySQLiteOpenHelper;
    private SQLiteDatabase db;

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VISION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //保存水表平台管理员选择水司层级记录的表
        String sql = "create table if not exists hierarchy_record(id integer primary key autoincrement,name varchar(50),fieldName varchar(20),fieldValue varchar(20))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS hierarchy_record");
        onCreate(db);
    }

    /**
     * 打开数据库,如果已经打开就使用，否则创建
     *
     * @return MySQLiteOpenHelper对象
     */
    public MySQLiteOpenHelper open() {
        if (null == mMySQLiteOpenHelper) {
            mMySQLiteOpenHelper = new MySQLiteOpenHelper(mContext);
        }
        db = mMySQLiteOpenHelper.getWritableDatabase();
        return this;
    }

    /**
     * 关闭数据库
     */
    @Override
    public void close() {
        db.close();
    }

    /**
     * 插入数据
     * delete()方法接收三个参数，第一个参数同样是表名，第二和第三个参数用于指定删除哪些行，对应了SQL语句中的where部分
     */
    public long insert(String tableName, ContentValues values) {
        return db.insert(tableName, null, values);
    }

    /**
     * 删除数据
     * delete()方法接收三个参数，第一个参数同样是表名，第二和第三个参数用于指定删除哪些行，对应了SQL语句中的where部分
     */
    public long delete(String tableName, String whereClause, String[] whereArgs) {
        return db.delete(tableName, whereClause, whereArgs);
    }

    /**
     * 查询数据
     */
    public Cursor findList(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy,
                           String having, String orderBy) {
        return db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    /**
     * 修改数据
     * update weiboTb set title='heihiehiehieh' where id=2;
     */
    public int update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(tableName, values, whereClause, whereArgs);
    }

    /**
     * 添加字段
     * 增加一列 - ALTER TABLE 表名 ADD COLUMN 列名 数据类型 限定符
     * db.execSQL("alter table comment add column publishdate integer");
     *
     * @param tableName  表名
     * @param columnName 列名
     * @param columnType 列类型
     */
    public void deleteTable(String tableName, String columnName, String columnType) {
        db.execSQL("alter table " + tableName + " add column " + columnName + columnType);
    }

    /**
     * 修改表名
     * 增加一列 - ALTER TABLE 表名 ADD COLUMN 列名 数据类型 限定符
     * db.execSQL("alter table comment add column publishdate integer");
     *
     * @param tableName    表名
     * @param newTableName 新表名
     */
    public void rename(String tableName, String newTableName) {
        db.execSQL("alter table " + tableName + "rename to" + newTableName);
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     */
    public void deleteTable(String tableName) {
        db.delete(tableName, null, null);
    }

}