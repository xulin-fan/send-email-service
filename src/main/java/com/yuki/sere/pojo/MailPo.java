package com.yuki.sere.pojo;

/**
 * Created by fan on 2019/6/11.
 */
public class MailPo {

    private String subject;
    private String host;
    private String fromuser;
    private String authPass;
    private String touser;
    private String ccuser;
    private String bccuser;
    private String content;

    public String getCcuser() {
        return ccuser;
    }

    public MailPo setCcuser(String ccuser) {
        this.ccuser = ccuser;
        return this;
    }

    public String getBccuser() {
        return bccuser;
    }

    public MailPo setBccuser(String bccuser) {
        this.bccuser = bccuser;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public MailPo setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getAuthPass() {
        return authPass;
    }

    public MailPo setAuthPass(String authPass) {
        this.authPass = authPass;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MailPo setContent(String content) {
        this.content = content;
        return this;
    }


    public String getHost() {
        return host;
    }

    public MailPo setHost(String host) {
        this.host = host;
        return this;
    }

    public String getFromuser() {
        return fromuser;
    }

    public MailPo setFromuser(String fromuser) {
        this.fromuser = fromuser;
        return this;
    }

    public String getTouser() {
        return touser;
    }

    public MailPo setTouser(String touser) {
        this.touser = touser;
        return this;
    }
}
