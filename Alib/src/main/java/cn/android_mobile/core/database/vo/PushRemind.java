package cn.android_mobile.core.database.vo;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;


/** 
 * 推送消息类
 * @ClassName:PushRemind 
 * @Description: 推送消息类
 * @date: 2014-5-16
 *  
 */
public class PushRemind
{

    @DatabaseField(id = true)
    private String messageId;
    @DatabaseField
    private String pushTime;

    @DatabaseField
    private String obdid;

    @DatabaseField
    private String vincode;

    @DatabaseField
    private String messageType;

    @DatabaseField
    private String messageLevel;

    @DatabaseField
    private String title;

    @DatabaseField
    private String message;

    @DatabaseField
    private String value;

    @DatabaseField
    private String alert;

    @DatabaseField
    private String id;

    @DatabaseField
    private String userId;

    @DatabaseField
    private boolean isRead;

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }

    public String getPushTime()
    {
        return pushTime;
    }

    public void setPushTime(String pushTime)
    {
        this.pushTime = pushTime;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public String getMessageLevel()
    {
        return messageLevel;
    }

    public void setMessageLevel(String messageLevel)
    {
        this.messageLevel = messageLevel;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getObdid()
    {
        return obdid;
    }

    public void setObdid(String obdid)
    {
        this.obdid = obdid;
    }

    public String getVincode()
    {
        return vincode;
    }

    public void setVincode(String vincode)
    {
        this.vincode = vincode;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getAlert()
    {
        return alert;
    }

    public void setAlert(String alert)
    {
        this.alert = alert;
    }

    public boolean isRead()
    {
        return isRead;
    }

    public void setRead(boolean isRead)
    {
        this.isRead = isRead;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public PushRemind fromJSONTOAccount(JSONObject jsonObject)
    {
        this.messageId = jsonObject.optString("messageId");
        this.obdid = jsonObject.optString("obdid");
        this.vincode = jsonObject.optString("vincode");
        this.messageType = jsonObject.optString("messageType");
        this.messageLevel = jsonObject.optString("messageLevel");
        this.title = jsonObject.optString("title");
        this.message = jsonObject.optString("message");
        this.value = jsonObject.optString("value");
        this.alert = jsonObject.optString("alert");
        this.pushTime = jsonObject.optString("pushTime");
        this.isRead = false;
        this.userId = jsonObject.optString("userId");
        return this;
    }

    public ArrayList<PushRemind> fromJSONArrayTOAccount(JSONArray jsonArray)
    {
        ArrayList<PushRemind> list = new ArrayList<PushRemind>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            if (null != jsonArray.optJSONObject(i))
            {
                list.add(new PushRemind().fromJSONTOAccount(jsonArray.optJSONObject(i)));
            }
        }
        return list;
    }
}
