package cn.android_mobile.core.database.vo;


import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/** 
 * 关于信息对象
 * @ClassName:About 
 * @Description: 用于设置-关于页面的对象数据
 * @author: jason
 * @date: 2014-1-3
 *  
 */
@DatabaseTable(tableName = "about")
public class About
{
    /** key*/
    @DatabaseField(id = true)
    private String key;
    @DatabaseField
    String about = "";
    @DatabaseField
    String version = "";
    @DatabaseField
    String updateUrl = "";
    @DatabaseField
    String versionExplain = "";

    public About()
    {

    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getVersionExplain()
    {
        return versionExplain;
    }

    public void setVersionExplain(String versionExplain)
    {
        this.versionExplain = versionExplain;
    }

    public String getAbout()
    {
        return about;
    }

    public void setAbout(String about)
    {
        this.about = about;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getUpdateUrl()
    {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl)
    {
        this.updateUrl = updateUrl;
    }

    /**
     * JSON数据处理
     * <p>
     * Description: 将JSON数据转化成一个About对象
     * <p>
     * @date 2014-1-19 
     * @param jsonObject
     * @return
     */
    public About fromJSONTOAbout(JSONObject jsonObject)
    {
        this.about = jsonObject.optString("about");
        this.version = jsonObject.optString("version");
        this.updateUrl = jsonObject.optString("updateUrl");
        this.versionExplain = jsonObject.optString("versionExplain");
        this.key = Constant.PRIMARY_KEY;
        return this;
    }
}
