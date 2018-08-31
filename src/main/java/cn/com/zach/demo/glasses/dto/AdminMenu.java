package cn.com.zach.demo.glasses.dto;

import java.io.Serializable;
import java.util.Date;

public class AdminMenu implements Serializable {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * 父菜单id
     *
     * @mbg.generated
     */
    private Long pid;

    /**
     * 菜单名称
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 菜单url
     *
     * @mbg.generated
     */
    private String url;

    /**
     * 排序
     *
     * @mbg.generated
     */
    private Integer sorter;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date ctime;

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
     * @return pid 父菜单id
     */
    public Long getPid() {
        return pid;
    }

    /**
     * @param pid 父菜单id
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * @return name 菜单名称
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 菜单名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return url 菜单url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url 菜单url
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * @return sorter 排序
     */
    public Integer getSorter() {
        return sorter;
    }

    /**
     * @param sorter 排序
     */
    public void setSorter(Integer sorter) {
        this.sorter = sorter;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pid=").append(pid);
        sb.append(", name=").append(name);
        sb.append(", url=").append(url);
        sb.append(", sorter=").append(sorter);
        sb.append(", ctime=").append(ctime);
        sb.append("]");
        return sb.toString();
    }
}