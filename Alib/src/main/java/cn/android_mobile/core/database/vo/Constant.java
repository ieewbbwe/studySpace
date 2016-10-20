package cn.android_mobile.core.database.vo;


import android.os.Environment;


/**
 * @description TODO
 */
public final class Constant
{

    public static final String APP_ID = "wx95d35afbab45ea33";
    public static final int BUFFER_SIZE = 1024;
    /**
     * SharedPreferences key Strings
     */
    public static final String VIEWISLOAD = "viewisload";
    public static final String LOGIN_PASSWORD = "password";
    public static final String LOGIN_NAME = "userName";
    public static final String LOGIN_NICK = "userNick";
    public static final String LOGIN_USERID = "userId";
    public static boolean LOGIN_STATE = false;
    public static int CURRENT_PAGE = 0;

    public static String END_YEAR = "endYear";
    public static String START_YEAR = "startYear";
    public static String LAST_LOGIN = "lastLogin";//24H = 86400000ms
    public static final String FINISH_ACTION = "finish";
    /**
     * userd for favurite articles
     *
     */
    public static final String abstract_userId = "1001";

    public final static String server_url = "https://msp.alipay.com/x.htm";
    //高德刷新频率时间
    public static final long MAX_AMAP_REFRESH_TIME = 20000; //20s
    public static final long MIN_AMAP_REFRESH_TIME = 3000; //3s
    public static final int RADIUSINMETERS = 500; //标记搜索半径

    //注册存储数据的key
    public static final String PHONENUM = "phoneNum";
    public static final String PASSWORD = "password";
    public static final String NICKNAME = "nickName";
    public static final String HEAD_PIC = "headPic";
    public static final String CAR_PIC = "carPic";
    public static final String UPDATE_APK = "update";
    public static final String CAR_MODEL = "cardModel";

    //应用存储文件的默认地址
    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory()
        + "/pateo/";
    //应用存储临时文件的默认地址
    public static final String TEMP_SDCARD_DIR = Environment
            .getExternalStorageDirectory() + "/pateo/temp/";

    public static final String CRASH_SDCARD_DIR = Environment
            .getExternalStorageDirectory() + "/pateo/crash/";
    public static final String CRASH_CONDITION_DIR = Environment
            .getExternalStorageDirectory() + "/pateo/condition/";

    //应用存储地点（标记）图片的默认地址
    public static final String MARKS_SDCARD_DIR = Environment
            .getExternalStorageDirectory() + "/pateo/mark/";
    //头像类型
    public static final String PHOtO_TYPE = "photo_type";

    //获取图片的标志位
    public static final int GET_IMAGE_VIA_CAMERA = 5;
    public static final int GET_IMAGE_VIA_LOCAL = 7;

    public static String MSFILEPATH_PERSON = Constant.TEMP_SDCARD_DIR + "personPic.jpg";
    public static String MSFILEPATH_VECHILE = Constant.TEMP_SDCARD_DIR + "vechilePic.jpg";
    public static String MSFILEPATH_DEFAULT = Constant.TEMP_SDCARD_DIR + "default.jpg";

    /** 主键，用于一些只有一条数据且没有ID的表数据 */
    public static final String PRIMARY_KEY = "100001";

    /** 头像的最大显示大小 */
    public static final int HEADPIC_MAXNUM_PIX = 150 * 150;
    /** 表示图片处理是否成功 */
    //    public static boolean processImageIsOk = false;

    /**激活OBD的扫描页面跳转来源,1、源于注册，2、源于主页面，3、源于设置页面 */
    public static String WHERE_TO_ACTIVE = "where";
    public static final int REGISTER_TO_ACTIVE = 10000;
    public static final int HOMEPAGE_TO_ACTIVE = 11000;
    public static final int SETTINGS_TO_ACTIVE = 12000;

    public static final String CAPTURESKIP = "captureskip";

    public static final String RESETPASNUM = "phone";
    public static final String RESETPASMSG = "vcode";

    /** 检查激活状态 ，结果值 */
    public static final String NOVERIFIED = "0"; // 未验证
    public static final String NOTACTIVED = "1"; // 已验证未激活
    public static final String ACTIVED = "2"; // 已激活
    public static final String SUSPEND = "3"; // 停用
    public static final String ISACTIVING = "4"; // 激活中
    /** 检查激活状态 ，结果值 */

    /** 账户相关的文件存放位置 */
    public static final String ACCOUNT_SHAREDPREFERENCES = "account";
}
