package cn.android_mobile.core.database;


import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import cn.android_mobile.core.database.vo.About;
import cn.android_mobile.core.database.vo.AccountUser;
import cn.android_mobile.core.database.vo.Car;
import cn.android_mobile.core.database.vo.ElectronicSecurity;
import cn.android_mobile.core.database.vo.IndexMessage;
import cn.android_mobile.core.database.vo.PushRemind;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


/** 
 * 数据库管理类
 * @ClassName:PateoDataBaseHelper 
 * @Description: 用于创建数据库和表的控制
 * @author: jason
 * @date: 2014-1-3
 *  
 */
public class DBHelper extends OrmLiteSqliteOpenHelper
{

    /** 数据库名�?*/
    private static final String DATABASE_NAME = "pateo.db";
    /** 数据库版本，升级使用 */
    private static final int DATABASE_VERSION = 1;
    /** dao层数�?*/
    private Dao<AccountUser, String> accountDao = null;
    private Dao<IndexMessage, String> indexMessageDao = null;
    private Dao<About, String> aboutDao = null;
    private Dao<Car, String> carDao = null;
    private Dao<PushRemind, String> pushRemindDao = null;
    private Dao<ElectronicSecurity, String> electronicSecurityDao = null;

    public DBHelper(Context context, String databaseName,
            CursorFactory factory, int databaseVersion)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, AccountUser.class);
            TableUtils.createTable(connectionSource, Car.class);
            TableUtils.createTable(connectionSource, About.class);
            TableUtils.createTable(connectionSource, PushRemind.class);
            TableUtils.createTable(connectionSource, IndexMessage.class);
            TableUtils.createTable(connectionSource, ElectronicSecurity.class);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int arg2,
            int arg3)
    {
        try
        {
            TableUtils.dropTable(connectionSource, AccountUser.class, true);
            TableUtils.dropTable(connectionSource, Car.class, true);
            TableUtils.dropTable(connectionSource, About.class, true);
            TableUtils.dropTable(connectionSource, PushRemind.class, true);
            TableUtils.dropTable(connectionSource, IndexMessage.class, true);
            TableUtils.dropTable(connectionSource, ElectronicSecurity.class, true);
            onCreate(db, connectionSource);
        }
        catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void close()
    {
        super.close();
        accountDao = null;
        carDao = null;
        aboutDao = null;
        pushRemindDao = null;
    }

    public Dao<AccountUser, String> getUserDao() throws SQLException
    {
        if (accountDao == null)
        {
            accountDao = getDao(AccountUser.class);
        }
        return accountDao;
    }

    public Dao<Car, String> getCarDao() throws SQLException
    {
        if (null == carDao)
        {
            carDao = getDao(Car.class);
        }

        return carDao;
    }

    public Dao<About, String> getAboutDao() throws SQLException
    {
        if (null == aboutDao)
        {
            aboutDao = getDao(About.class);
        }

        return aboutDao;
    }

    public Dao<PushRemind, String> getPushRemindDao() throws SQLException
    {
        if (null == pushRemindDao)
        {
            pushRemindDao = getDao(PushRemind.class);
        }

        return pushRemindDao;
    }

    public Dao<IndexMessage, String> getIndexMessage() throws SQLException
    {
        if (null == indexMessageDao)
        {
            indexMessageDao = getDao(IndexMessage.class);
        }

        return indexMessageDao;
    }

    public Dao<ElectronicSecurity, String> getElectronicSecurity() throws SQLException
    {
        if (null == electronicSecurityDao)
        {
            electronicSecurityDao = getDao(ElectronicSecurity.class);
        }

        return electronicSecurityDao;
    }
}
