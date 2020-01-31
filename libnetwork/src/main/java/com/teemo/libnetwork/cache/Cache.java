package com.teemo.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.SkipQueryVerification;
import androidx.room.Transaction;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

//tableName指定表名
//foreignKeys外简约束 entity约束的表名 parentColumns childColumns childColumns改变 parentColumns也会改变
@Entity(tableName = "cache"
//        foreignKeys = {@ForeignKey(entity =User.class ,parentColumns ="id" ,childColumns ="key" ,onDelete = ForeignKey.RESTRICT,onUpdate = ForeignKey.RESTRICT)}
//        , indices = {@Index(value = {"key", "id"})}//indices 复合主键//    @Index() //加快查询操作,减慢增删改操作
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


    //关联查询projection表示要查询出哪些字段
//    @Relation(entity = User.class, parentColumn = "id", entityColumn = "id", projection = {})
//    public User mUser;

    //跳过sql验证,不检验sql的正确性,不常用
//    @SkipQueryVerification

//    @Transaction 事务处理,不常用

//    类型转换用
//    @TypeConverter
//    @TypeConverters()

    //使用DataTypeCovert中的方法进行类型转换
//    @TypeConverters(value = {DataTypeCovert.class})
//    public Date date;

}
