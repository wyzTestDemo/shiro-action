package im.zhaojun.controller;

import cn.hutool.core.util.IdUtil;
import im.zhaojun.annotation.OperationLog;
import im.zhaojun.config.ShiroConfig;
import im.zhaojun.exception.CaptchaIncorrectException;
import im.zhaojun.exception.DuplicateNameException;
import im.zhaojun.model.User;
import im.zhaojun.service.MailService;
import im.zhaojun.service.UserService;
import im.zhaojun.util.CaptchaUtil;
import im.zhaojun.util.ResultBean;
import im.zhaojun.util.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;

    @Resource
    private TemplateEngine templateEngine;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginVerify", ShiroUtil.getLoginVerify());
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/login")
    @ResponseBody
    public ResultBean login(User user, @RequestParam(value = "captcha", required = false) String captcha) {
        Subject subject = SecurityUtils.getSubject();

        // 如果开启了登录校验
        if (ShiroUtil.getLoginVerify()) {
            String realCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");
            // session 中的验证码过期了
            if (realCaptcha == null || !realCaptcha.equals(captcha.toLowerCase())) {
                throw new CaptchaIncorrectException();
            }
        }

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        userService.updateLastLoginTimeByUsername(user.getUsername());
        Map<String,String> result = new HashMap<>();
        if(subject.hasRole("admin")){
            result.put("role","admin");
        }else {
            result.put("role","user");
        }
        return ResultBean.success(result);
    }

    @OperationLog("注销")
    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:userIndex";
    }

    @GetMapping("/checkUser")
    @ResponseBody
    public ResultBean checkUser(String username) {
        return ResultBean.success(userService.checkUserNameExist(username));
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultBean register(User user) {
        if (userService.checkUserNameExist(user.getUsername())) {
            throw new DuplicateNameException();
        }
        String activeCode = IdUtil.fastSimpleUUID();
        user.setActiveCode(activeCode);
        user.setStatus("0");

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getScheme() + "://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + "/active/"
                + activeCode;
        Context context = new Context();
        context.setVariable("url", url);
        String mailContent = templateEngine.process("/mail/registerTemplate", context);
        new Thread(() ->
                mailService.sendHTMLMail(user.getEmail(), "Shiro-Action 激活邮件", mailContent))
                .start();

        // 注册后默认的角色, 根据自己数据库的角色表 ID 设置
        Integer[] initRoleIds = {2};
        return ResultBean.success(userService.add(user, initRoleIds));
    }

    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CaptchaUtil.Captcha captcha = CaptchaUtil.createCaptcha(140, 38, 4, 10, 30);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("captcha", captcha.getCode());

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(captcha.getImage(), "png", os);
    }

    @OperationLog("激活注册账号")
    @GetMapping("/active/{token}")
    public String active(@PathVariable("token") String token, Model model) {
        User user = userService.selectByActiveCode(token);
        String msg;
        if (user == null) {
            msg = "请求异常, 激活地址不存在!";
        } else if ("1".equals(user.getStatus())) {
            msg = "用户已激活, 请勿重复激活!";
        } else {
            msg = "激活成功!";
            user.setStatus("1");
            userService.update(user);
        }
        model.addAttribute("msg", msg);
        return "active";
    }

}
