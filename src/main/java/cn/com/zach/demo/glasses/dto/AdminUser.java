package cn.com.zach.demo.glasses.dto;

import java.io.Serializable;
import java.util.Date;

public class AdminUser implements Serializable {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * 用户名
     *
     * @mbg.generated
     */
    private String username;

    /**
     * 密码
     *
     * @mbg.generated
     */
    private String password;

    /**
     * 昵称
     *
     * @mbg.generated
     */
    private String nickname;

    /**
     * 头像
     *
     * @mbg.generated
     */
    private String avatar;

    /**
     * 手机号
     *
     * @mbg.generated
     */
    private String phone;

    /**
     * 邮箱
     *
     * @mbg.generated
     */
    private String email;

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
     * @return username 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * @return password 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * @return nickname 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * @return avatar 头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar 头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    /**
     * @return phone 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return email 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", nickname=").append(nickname);
        sb.append(", avatar=").append(avatar);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", ctime=").append(ctime);
        sb.append(", enable=").append(enable);
        sb.append("]");
        return sb.toString();
    }
}