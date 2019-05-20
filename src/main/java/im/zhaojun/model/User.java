package im.zhaojun.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import im.zhaojun.validate.constraints.UserNameNotDuplicate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = -3200103254689137288L;

    private Integer userId;

    @NotBlank(message = "用户名不能为空")
    @UserNameNotDuplicate
    private String username;

    @JsonIgnore
    @NotBlank(message = "密码不能为空")
    private String password;

    @JsonIgnore
    private String salt;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String status;

    private Date lastLoginTime;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date modifyTime;

    @JsonIgnore
    private String activeCode;
    private String image;
    private String sex;
    private Date birth;
    private Date registerTime;
    private String phone;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", activeCode='" + activeCode + '\'' +
                ", image='" + image + '\'' +
                ", sex='" + sex + '\'' +
                ", birth=" + birth +
                ", registerTime=" + registerTime +
                ", phone='" + phone + '\'' +
                '}';
    }
}