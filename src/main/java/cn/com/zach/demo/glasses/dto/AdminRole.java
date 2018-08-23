package cn.com.zach.demo.glasses.dto;

import java.io.Serializable;
import java.util.Date;

public class AdminRole implements Serializable {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * 角色名称
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 角色介绍
     *
     * @mbg.generated
     */
    private String intro;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date ctime;

    /**
     * 是否有效(虚拟删除)  0:ENABLE:有效  1:DISABLE:无效
     *
     * @mbg.generated
     */
    private Integer enable;

    /**
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return id 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return name 角色名称
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 角色名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return intro 角色介绍
     */
    public String getIntro() {
        return intro;
    }

    /**
     * @param intro 角色介绍
     */
    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
    }

    /**
     * @return ctime 创建时间
     */
    public Date getCtime() {
        return ctime;
    }

    /**
     * @param ctime 创建时间
     */
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    /**
     * @return enable 是否有效(虚拟删除)  0:ENABLE:有效  1:DISABLE:无效
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * @param enable 是否有效(虚拟删除)  0:ENABLE:有效  1:DISABLE:无效
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", intro=").append(intro);
        sb.append(", ctime=").append(ctime);
        sb.append(", enable=").append(enable);
        sb.append("]");
        return sb.toString();
    }
}