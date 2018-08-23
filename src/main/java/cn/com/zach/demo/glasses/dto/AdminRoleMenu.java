package cn.com.zach.demo.glasses.dto;

import java.io.Serializable;

public class AdminRoleMenu implements Serializable {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * 角色Id
     *
     * @mbg.generated
     */
    private Long roleid;

    /**
     * 菜单Id
     *
     * @mbg.generated
     */
    private Long menuid;

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

    /**
     * @return menuId 菜单Id
     */
    public Long getMenuid() {
        return menuid;
    }

    /**
     * @param menuid 菜单Id
     */
    public void setMenuid(Long menuid) {
        this.menuid = menuid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleid=").append(roleid);
        sb.append(", menuid=").append(menuid);
        sb.append("]");
        return sb.toString();
    }
}