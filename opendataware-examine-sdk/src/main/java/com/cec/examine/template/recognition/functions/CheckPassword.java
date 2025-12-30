package com.cec.examine.template.recognition.functions;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName CheckCollege.java
 * @Description 密码检查
 * @createTime 2022/12/19
 */
public class CheckPassword {
    public static boolean isPassword(String str){
        String regex = "登录密码|密码|口令|dlmn|login_password|password";
        String[] regexs = regex.split("\\|");
        for(String rege:regexs){
            if(str.contains(rege)){
                return true;
            }
        }
        return regex.contains(str.toLowerCase());

    }

    public static void main(String[] args) {
        String str = "民族代码";
        String str2 = "登录密码";
        System.out.println(isPassword(str2));
        System.out.println(isPassword(str));
    }

    public static boolean checkPassword(String colName, String comment) {
        return isPassword(colName) || isPassword(comment);
    }
}
