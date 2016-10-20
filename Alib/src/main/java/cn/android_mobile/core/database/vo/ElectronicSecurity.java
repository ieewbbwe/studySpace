package cn.android_mobile.core.database.vo;


import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/** 
 * 电子围栏对象类
 * @ClassName:ElectronicSecurity 
 * @Description: 这个类用于安防模块，包含了电子围栏的必要数据。 
 * @date: 2014-1-19
 *  
 */
@DatabaseTable(tableName = "security")
public class ElectronicSecurity
{
    /** key*/
    @DatabaseField(id = true)
    private String key;
    /** 电子围栏距离，单位KM */
    @DatabaseField
    private String range;
    /** 电子围栏开关*/
    @DatabaseField
    private String fenceIsOpen;
    @DatabaseField
    private String longitude;
    @DatabaseField
    private String latitude;

    public ElectronicSecurity()
    {

    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getRange()
    {
        return range;
    }

    public void setRange(String range)
    {
        this.range = range;
    }

    public String getFenceIsOpen()
    {
        return fenceIsOpen;
    }

    public void setFenceIsOpen(String fenceIsOpen)
    {
        this.fenceIsOpen = fenceIsOpen;
    }

    /**
     * JSON数据处理
     * <p>
     * Description: 将JSON数据转化成一个ElectronicSecurity对象
     * <p>
     * @date 2014-1-9 
     * @param jsonObject
     * @return
     */
    public ElectronicSecurity fromJSONTOAccount(JSONObject jsonObject)
    {
        this.range = jsonObject.optString("range");
        this.longitude = jsonObject.optString("longitude");
        this.latitude = jsonObject.optString("latitude");
        this.fenceIsOpen = jsonObject.optString("fenceIsOpen");
        this.key = Constant.PRIMARY_KEY;
        return this;
    }

}
