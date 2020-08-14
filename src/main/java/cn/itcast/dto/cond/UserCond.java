package cn.itcast.dto.cond;

/**
 * 用户查找条件
 * Created by Donghua.Chen on 2018/4/30.
 */
public class UserCond {
    private String username;
    private String password;
    private String premise;

    public String getPremise() {
        return premise;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
