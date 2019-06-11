package com.yuki.sere.servlets;

import com.yuki.sere.pojo.MailPo;
import com.yuki.sere.services.FileDetector;
import com.yuki.sere.services.MailSender;
import com.yuki.sere.utils.StringUtils;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * Created by fan on 2019/6/11.
 * usage:
         [htmlEmailTempPath]: 邮件模板路径 xxxxxxxxx/serenity-summary.html, 必选项
         [mailHost]: smtp服务器地址 smtp.qq.com, 必选项
         [authpass]: 验证密钥 xxxx, 必选项
         [fromuser]: 发件人, 必选项
         [tousers]: 收件人, 必选项, 多个收件人使用逗号分隔
         [ccusers]: 抄送人, 可选项, 多个收件人使用逗号分隔
         [bccusers]: 密抄人, 可选项, 多个收件人使用逗号分隔
         [interval]: 定时时间，默认1000毫秒(1秒)执行一次
 *
 *
 */
@WebServlet(name = "ThreadHandler", urlPatterns = "/sendMail", loadOnStartup = 1,
        initParams = {@WebInitParam(name = "htmlEmailTempPath", value = "xxxxxxxxx/serenity-summary.html")
                , @WebInitParam(name = "mailHost", value = "smtp.qq.com")
                , @WebInitParam(name = "fromuser", value = "xxxxx@qq.com")
                , @WebInitParam(name = "authpass", value = "xxxx")
                , @WebInitParam(name = "tousers", value = "31773648@qq.com, 296178718@qq.com, 784492791@qq.com")
                , @WebInitParam(name = "ccusers", value = "411525805@qq.com, 398752242@qq.com, 564590079@qq.com")
                , @WebInitParam(name = "bccusers", value = "54327852@qq.com, 20001117@qq.com, 312788260@qq.com")
                , @WebInitParam(name = "interval", value = "1000")})
public class ThreadHandler extends HttpServlet {

    private FileDetector fileDetector;

    private MailSender mailSender;

    private String path;

    private String mailHost;

    private String authPass;

    private String fromuser;

    private String tousers;

    private String ccusers;

    private String bccusers;

    private String interval;

    @Override
    public void init() throws ServletException {
        printUsage();

        parameterCheck();

        MailPo mailPo = new MailPo()
                .setSubject("自动化测试报告")
                .setFromuser(fromuser)
                .setTouser(tousers)
                .setCcuser(ccusers)
                .setBccuser(bccusers)
                .setHost(mailHost)
                .setAuthPass(authPass);

        mailSender = new MailSender(mailPo);

        fileDetector = new FileDetector(path);


        new Thread(() -> {
            while (true) {
                try {
                    // 定时任务1秒钟执行一次
                    Thread.sleep(interval == null ? 1000 : Integer.valueOf(interval));

                    System.out.println("开始执行检查任务");
                    boolean isChange;
                    synchronized (this) {
                        isChange = fileDetector.isChange();
                    }
                    if (isChange) {
                        System.out.println("文件变更, 发送邮件");
                        mailPo.setContent(fileDetector.getHtmlEmailTemp());
                        mailSender.sendHtml();
                    }
                } catch (InterruptedException | MessagingException | IOException e) {
                    System.out.println(e);
                }
            }
        }).start();
    }

    private void printUsage() {
        System.out.println("usage: ");
        System.out.println("      [htmlEmailTempPath]: 邮件模板路径, 必选项");
        System.out.println("      [mailHost]: smtp服务器地址, 必选项");
        System.out.println("      [authpass]: 验证密钥, 必选项");
        System.out.println("      [fromuser]: 发件人, 必选项");
        System.out.println("      [tousers]: 收件人, 必选项, 多个收件人使用逗号分隔");
        System.out.println("      [ccusers]: 抄送人, 可选项, 多个收件人使用逗号分隔");
        System.out.println("      [bccusers]: 密抄人, 可选项, 多个收件人使用逗号分隔");
    }

    private void parameterCheck() {
        path = getInitParameter("htmlEmailTempPath");
        mailHost = getInitParameter("mailHost");
        authPass = getInitParameter("authpass");
        fromuser = getInitParameter("fromuser");
        tousers = getInitParameter("tousers");
        ccusers = getInitParameter("ccusers");
        bccusers = getInitParameter("bccusers");
        interval = getInitParameter("interval");
        if (StringUtils.areNull(path, mailHost, fromuser, tousers, authPass)) {
            throw new IllegalArgumentException("参数有误, 请指定htmlEmailTempPath, mailHost, fromuser, tousers, authPass等参数");
        }
    }
}
