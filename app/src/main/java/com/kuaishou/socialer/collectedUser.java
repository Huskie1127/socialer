package com.kuaishou.socialer;
/*
    收集到的好友信息 这里没写后端接口 先用adapter暂存
 */
public class collectedUser {
    private String name;
    private  String phoneNumber;
    private String QQ;
    private  String WeChat;
    private  String weiBo;
    collectedUser(String name,String phoneNumber,String QQ,String weChat,String weiBo)
    {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.QQ = QQ;
        this.weiBo = weiBo;
        this.WeChat = weChat;
    }
    collectedUser()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getWeChat() {
        return WeChat;
    }

    public void setWeChat(String weChat) {
        WeChat = weChat;
    }

    public String getWeiBo() {
        return weiBo;
    }

    public void setWeiBo(String weiBo) {
        this.weiBo = weiBo;
    }
}
