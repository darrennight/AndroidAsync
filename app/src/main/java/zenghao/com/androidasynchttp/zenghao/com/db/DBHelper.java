package zenghao.com.androidasynchttp.zenghao.com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zenghao on 15/11/26.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final static int DATABASE_VERSION =1;// 数据库版本号
    private static String DATABASE_NAME = "tvcloud.disk.db";// 数据库名
    private static DBHelper dbHelperInstance = null;
    public static final String TABLE_VIDEO = "disk_video";// 数据表名
    public static final String TABLE_ACCOUNT = "table_account";// 账号登陆的数据表名
    private static SQLiteDatabase db = null;
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (dbHelperInstance == null) {
            dbHelperInstance = new DBHelper(context);
        }
        return dbHelperInstance;
    }

    public static synchronized SQLiteDatabase getReadSQLiteDatabase(
            Context context) {
        if (db == null) {
            db = getInstance(context).getReadableDatabase();
        }
        return db;
    }

    public static synchronized SQLiteDatabase getWriteSQLiteDatabase(
            Context context) {
        if (db == null) {
            db = getInstance(context).getWritableDatabase();
        }
        return db;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

//        createTableVideo(db);
//        createTableAccount(db);
//        DebugLog.logd("onCreate+++++++++++++++++++++++");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if(oldVersion < LEDISK_VERSION_1002) {
//            db.execSQL("Alter table " + TABLE_FILE_JOB + " add column pid varchar not null default '0'");
//        }
//        if(oldVersion < LEDISK_VERSION_1003) {
//            db.execSQL("Alter table " + TABLE_FILE + " add column isHide varchar not null default '0'");
//        }
//        if(oldVersion < LEDISK_VERSION_1004) {
//            db.execSQL("Alter table " + TABLE_FILE_JOB + " add column mediatype varchar not null default '0'");
//        }
    }

}
