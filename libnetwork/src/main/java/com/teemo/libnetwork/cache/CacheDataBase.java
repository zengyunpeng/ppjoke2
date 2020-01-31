package com.teemo.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.teemo.libcommon.AppGloble;

//entities指定数据库中油那些表
//version版本号
//exportSchema 默认是true 会导出一个json 文件,这个文件包含了数据库在创建和升级的时候执行的sql,所包含的表和表的字段,这要在build.gradle中配置文件要生成的目录
@Database(entities = {Cache.class}, version = 1, exportSchema = true)
public abstract class CacheDataBase extends RoomDatabase {
    private static final CacheDataBase cacheDataBase;

    static {
        //创建一个内存数据库
        //但是这种数据库的数据只存在于内存中,进程被杀后,数据随之丢失
//        Room.inMemoryDatabaseBuilder()
        cacheDataBase = Room.databaseBuilder(AppGloble.getApplication(), CacheDataBase.class, "ppjoke2_cache")
                //是否允许在主线程进行查询
                .allowMainThreadQueries()
                //数据库创建和打开后的回调
                .addCallback(new Callback() {
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    }

                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    }
                })
                //设置查询的线程池
//        .setQueryExecutor()
//        .openHelperFactory()
                //Room的数据模式
//        .setJournalMode(JournalMode.AUTOMATIC)
                //当数据库版本升级时候发生了异常后回滚数据库版本
                .fallbackToDestructiveMigration()
                //数据库版本升级时候发生了异常后指定版本进行回滚
                .fallbackToDestructiveMigrationFrom()//数据库版本升级操作,数据库升级的时候会执行migration中的代码
//                .addMigrations(CacheDataBase.sMigration_1_3)
                .build();
    }

    public static CacheDataBase getDataBase() {
        return cacheDataBase;
    }


    static Migration sMigration_1_3 = new Migration(1, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table teacher rename to student");
            database.execSQL("alter table teacher add column teacher_age INTEGER not null default 0");
        }
    };
}
