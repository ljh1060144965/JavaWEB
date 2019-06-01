package cn.mrdear.excel;

import cn.mrdear.excel.util.FieldName;

import java.math.BigDecimal;

/**
 * @author Niu Li
 * @since 2017/2/23
 */
public class Demo {

    public Demo(String username, String password) {
        this.userName = username;
        this.passWord = password;
        this.amount = new BigDecimal(0.654523);
    }
    public Demo() {
    }
    //fildName字段需要和header对应
    @FieldName(value = "username")
    private String userName;
    private String passWord;
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "Demo{" +
            "userName='" + userName + '\'' +
            ", passWord='" + passWord + '\'' +
            '}';
    }
}
