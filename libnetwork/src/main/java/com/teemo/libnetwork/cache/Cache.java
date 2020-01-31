package com.teemo.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//tableName指定表名
//foreignKeys外简约束 entity约束的表名 parentColumns childColumns childColumns改变 parentColumns也会改变
@Entity(tableName = "cache"
//        foreignKeys = {@ForeignKey(entity =User.class ,parentColumns ="id" ,childColumns ="key" ,onDelete = ForeignKey.RESTRICT,onUpdate = ForeignKey.RESTRICT)}
        , indices = {@Index(value = {"key", "id"})}//indices 复合主键//    @Index() //加快查询操作,减慢增删改操作
)
public class Cache implements Serializable {
    //主键约束
    @PrimaryKey
    @NonNull
//    @Ignore//这个字段不会成为表的列名

    public String key;
    //ColumnInfo重命名列名
//    @ColumnInfo(name = "_data")
    public byte[] data;

    //@Embedded :嵌套对象 对象中的所有字段会被映射到数据表中
//    @Embedded
//    public User user;


    /**
     * 常用注解
     *
     *
     */
}
