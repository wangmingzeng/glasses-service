package cn.com.zach.demo.glasses.dto;

import java.io.Serializable;
import java.math.BigDecimal;
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
    private String names;

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
     * 积分
     *
     * @mbg.generated
     */
    private BigDecimal integral;

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
     * @return names 菜单名称
     */
    public String getNames() {
        return names;
    }

    /**
     * @param names 菜单名称
     */
    public void setNames(String names) {
        this.names = names == null ? null : names.trim();
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

    /**
     * @return integral 积分
     */
    public BigDecimal getIntegral() {
        return integral;
    }

    /**
     * @param integral 积分
     */
    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pid=").append(pid);
        sb.append(", names=").append(names);
        sb.append(", url=").append(url);
        sb.append(", sorter=").append(sorter);
        sb.append(", ctime=").append(ctime);
        sb.append(", integral=").append(integral);
        sb.append("]");
        return sb.toString();
    }
}