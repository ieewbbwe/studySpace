package cn.android_mobile.core.database.vo;


import java.io.Serializable;

import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/** 
 * 个人车辆对象
 * @ClassName:Car 
 * @Description: 这个对象包含了个人车辆的所有的信息 
 * @author: 
 * @date: 2014-1-19
 *  
 */
@DatabaseTable(tableName = "car")
public class Car implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 手机号码,此字段为Id字段 */
    @DatabaseField(id = true)
    private String phone;
    @DatabaseField
    private String plateNum;
    @DatabaseField
    private String carId;
    @DatabaseField
    private String engineNo;
    @DatabaseField
    private String longitude;
    @DatabaseField
    private String latitude;
    @DatabaseField
    private String cName;
    @DatabaseField
    private String vinCode;
    @DatabaseField
    private String carPic;

    public Car()
    {
    }

    public String getCarId()
    {
        return carId;
    }

    public void setCarId(String carId)
    {
        this.carId = carId;
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

    public String getcName()
    {
        return cName;
    }

    public void setcName(String cName)
    {
        this.cName = cName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEngineNo()
    {
        return engineNo;
    }

    public void setEngineNo(String engineNo)
    {
        this.engineNo = engineNo;
    }

    public String getCarPic()
    {
        return carPic;
    }

    public void setCarPic(String carPic)
    {
        this.carPic = carPic;
    }

    public String getPlateNum()
    {
        return plateNum;
    }

    public void setPlateNum(String plateNum)
    {
        this.plateNum = plateNum;
    }

    public String getVinCode()
    {
        return vinCode;
    }

    public void setVinCode(String vinCode)
    {
        this.vinCode = vinCode;
    }

    /**
     * JSON数据处理
     * <p>
     * Description: 将JSON数据转化成一个Car对象
     * <p>
     * @date 2014-1-19 
     * @param jsonObject
     * @return
     */
    public Car fromJSONTOAccount(JSONObject jsonObject)
    {
        this.plateNum = jsonObject.optString("plateNum");
        this.engineNo = jsonObject.optString("engineNo");
        this.longitude = jsonObject.optString("longitude");
        this.latitude = jsonObject.optString("latitude");
        this.vinCode = jsonObject.optString("vinCode");
        this.cName = jsonObject.optString("cName");
        this.carPic = jsonObject.optString("carPic");
        this.carId = jsonObject.optString("carId");
        return this;
    }

}
