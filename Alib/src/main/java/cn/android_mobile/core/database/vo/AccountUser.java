package cn.android_mobile.core.database.vo;


import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/** 
 * 账户类
 * @ClassName:Account 
 * @Description: 账户数据模型
 * @author: jason
 * @date: 2014-1-3
 *  
 */
@DatabaseTable(tableName = "account")
public class AccountUser
{

    /** 手机号码,此字段为Id字段 */
    @DatabaseField(id = true)
    private String phone;
    /** 区号 */
    @DatabaseField
    private String areaCode;
    /** 绑定超时时间*/
    @DatabaseField
    private String timeoutActivatDate;
    /** 激活日期 */
    @DatabaseField
    private String activatDate;
    /** obdId */
    @DatabaseField
    private String obdId;
    /** 头像图片地址 */
    @DatabaseField
    private String headPic;
    /** 紧急联系人电话*/
    @DatabaseField
    private String emergencyContactPhone;
    /** 紧急联系人名称*/
    @DatabaseField
    private String emergencyContact;
    /** 等级*/
    @DatabaseField
    private String level;
    /** 绑定状态*/
    @DatabaseField
    private String bindStatus;
    /** 昵称 */
    @DatabaseField
    private String nickName;
    /** 用户ID，推送的时候用 */
    @DatabaseField
    private String userId;
    /** 车牌号*/
    @DatabaseField
    private String driverNum;
    /** 用户状态*/
    @DatabaseField
    private String userStatus;
    /** 驾驶有效期*/
    @DatabaseField
    private String driverEffectiveDate;
    /** 驾驶有效年限*/
    @DatabaseField
    private String driverEffectiveYear;

    public AccountUser()
    {
        //ormlite中必须要有一个无参的构造函数
    }

    public String getBindStatus()
    {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus)
    {
        this.bindStatus = bindStatus;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getAreaCode()
    {
        return areaCode;
    }

    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode;
    }

    public String getEmergencyContactPhone()
    {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone)
    {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getEmergencyContact()
    {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact)
    {
        this.emergencyContact = emergencyContact;
    }

    public String getDriverNum()
    {
        return driverNum;
    }

    public void setDriverNum(String driverNum)
    {
        this.driverNum = driverNum;
    }

    public String getDriverEffectiveDate()
    {
        return driverEffectiveDate;
    }

    public void setDriverEffectiveDate(String driverEffectiveDate)
    {
        this.driverEffectiveDate = driverEffectiveDate;
    }

    public String getDriverEffectiveYear()
    {
        return driverEffectiveYear;
    }

    public void setDriverEffectiveYear(String driverEffectiveYear)
    {
        this.driverEffectiveYear = driverEffectiveYear;
    }

    public String getUserStatus()
    {
        return userStatus;
    }

    public void setUserStatus(String userStatus)
    {
        this.userStatus = userStatus;
    }

    public String getTimeoutActivatDate()
    {
        return timeoutActivatDate;
    }

    public void setTimeoutActivatDate(String timeoutActivatDate)
    {
        this.timeoutActivatDate = timeoutActivatDate;
    }

    public String getActivatDate()
    {
        return activatDate;
    }

    public void setActivatDate(String activatDate)
    {
        this.activatDate = activatDate;
    }

    public String getObdId()
    {
        return obdId;
    }

    public void setObdId(String obdId)
    {
        this.obdId = obdId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getHeadPic()
    {
        return headPic;
    }

    public void setHeadPic(String headPic)
    {
        this.headPic = headPic;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * JSON数据处理
     * <p>
     * Description: 将JSON数据转化成一个Account对象
     * <p>
     * @date 2014-1-9 
     * @param jsonObject
     * @return
     */
    public AccountUser fromJSONTOAccount(JSONObject jsonObject)
    {
        this.phone = jsonObject.optString("phone");
        this.nickName = jsonObject.optString("nickName");
        this.headPic = jsonObject.optString("headPic");
        this.userId = jsonObject.optString("userId");
        this.activatDate = jsonObject.optString("activatDate");
        this.obdId = jsonObject.optString("obdId");
        this.timeoutActivatDate = jsonObject.optString("timeoutActivatDate");
        this.level = jsonObject.optString("level");
        this.userStatus = jsonObject.optString("userStatus");
        this.bindStatus = jsonObject.optString("bindStatus");
        this.areaCode = jsonObject.optString("areaCode");
        this.emergencyContactPhone = jsonObject.optString("emergencyContactPhone");
        this.emergencyContact = jsonObject.optString("emergencyContact");
        this.driverNum = jsonObject.optString("driverNum");
        this.driverEffectiveDate = jsonObject.optString("driverEffectiveDate");
        this.driverEffectiveYear = jsonObject.optString("driverEffectiveYear");
        return this;
    }

    public boolean equals(Object o)
    {
        if (o instanceof AccountUser)
        {
            return this.phone.equals(((AccountUser) o).phone);
        }
        return false;
    }

    public int hashCode()
    {
        return this.phone.hashCode();
    }

    public String toString()
    {
        return this.phone + "---" + this.userId + "---" + this.obdId + "---"
            + this.nickName + "---" + this.userStatus;
    }

}
