package cn.android_mobile.core.database.vo;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "indexMessage")
public class IndexMessage
{
    /** 手机号码,此字段为Id字段 */
    @DatabaseField(id = true)
    private String phone;
    /** 驾驶评分 */
    @DatabaseField
    private String drivingRatings;
    /** 电压 */
    @DatabaseField
    private String voltage;
    /** 更新时间 */
    @DatabaseField
    private String updateTime;
    /** 地址*/
    @DatabaseField
    private String address;
    /** 纬度 */
    @DatabaseField
    private String lng;
    /** 经度 */
    @DatabaseField
    private String lat = "";
    /** 行驶里程*/
    @DatabaseField
    private String kilometer;
    /** 油量*/
    @DatabaseField
    private String oil = "";

    public IndexMessage()
    {
        //ormlite中必须要有一个无参的构造函数
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getDrivingRatings()
    {
        return drivingRatings;
    }

    public void setDrivingRatings(String drivingRatings)
    {
        this.drivingRatings = drivingRatings;
    }

    public String getVoltage()
    {
        return voltage;
    }

    public void setVoltage(String voltage)
    {
        this.voltage = voltage;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getLng()
    {
        return lng;
    }

    public void setLng(String lng)
    {
        this.lng = lng;
    }

    public String getKilometer()
    {
        return kilometer;
    }

    public void setKilometer(String kilometer)
    {
        this.kilometer = kilometer;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getOil()
    {
        return oil;
    }

    public void setOil(String oil)
    {
        this.oil = oil;
    }

}
