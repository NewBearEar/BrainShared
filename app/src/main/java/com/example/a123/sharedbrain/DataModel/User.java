package com.example.a123.sharedbrain.DataModel;



import java.io.Serializable;

public class User implements IJsonModel,Serializable {

    /**
     * 用户名
     */
    public String username;
    /**
     * 用户密码
     */
    public String password;
    /**
     * 用户头像地址
     */
   // @SerializedName("avatar")
    public String icon;
    /**
     * 生日
     */
    public String birthday;
    /**
     * 昵称
     */
    public String nickname;
    /**
     * 用户类型  1 学生  2 管理员 3 游客
     */
    public int type;
    /**
     * 专业id
     */
    public int Majorid;
    /**
     * 家庭地址
     */
    public String address;


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", icon='" + icon + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nickname='" + nickname + '\'' +
                ", type=" + type +
                ", Majorid=" + Majorid +
                ", address='" + address + '\'' +
                '}';
    }
}
