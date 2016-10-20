package cn.android_mobile.core.database.vo;


/** 
 * 位置对象
 * @ClassName:PositionPoint
 * @Description: 这个对象包含了车辆的坐标
 * @date: 2014-1-10
 *  
 */
public class PositionPoint
{
    private Double latitude;
    private Double longitude;

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public boolean isEmpty()
    {
        return this.latitude < 0.00001 || this.longitude < 0.00001;
    }
}
