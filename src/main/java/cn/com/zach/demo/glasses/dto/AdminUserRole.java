package cn.com.zach.demo.glasses.dto;

import java.io.Serializable;

public class AdminUserRole implements Serializable {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * 用户Id
     *
     * @mbg.generated
     */
    private Long userid;

    /**
     * 角色Id
     *
     * @mbg.generated
     */
    private Long roleid;

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
     * @return userId 用户Id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * @param userid 用户Id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * @return roleId 角色Id
     */
    public Long getRoleid() {
        return roleid;
    }

    /**
     * @param roleid 角色Id
     */
    public void setRoleid(Long roleid) {
        this.roleid = roleid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", roleid=").append(roleid);
        sb.append("]");
        return sb.toString();
    }
}