package com.rao.controller;

import com.rao.annotation.SimpleValid;
import com.rao.pojo.dto.PasswordLoginDTO;
import com.rao.pojo.dto.SmsCodeLoginDTO;
import com.rao.pojo.dto.WxLoginDTO;
import com.rao.pojo.vo.LoginSuccessVO;
import com.rao.service.LoginService;
import com.rao.util.result.ResultMessage;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 登录管理
 * @author raojing
 * @date 2019/12/2 14:14
 */
@RestController
public class UserLoginController {

    @Resource
    private LoginService loginService;
    @Resource
    private TokenStore tokenStore;

    /**
     * 后台用户账号密码登录
     * @param passwordLoginDTO
     * @return
     */
    @PostMapping(value = "/login/system_user")
    public ResultMessage<LoginSuccessVO> loginSystemUser(@RequestBody PasswordLoginDTO passwordLoginDTO) {
        LoginSuccessVO loginSuccessVO = loginService.loginAdmin(passwordLoginDTO);
        return ResultMessage.success(loginSuccessVO).message("登录成功");
    }

    /**
     * 后台用户短信验证码登录
     * @param smsCodeLoginDTO
     * @return
     */
    @PostMapping(value = "/login/sms_code/system_user")
    public ResultMessage<LoginSuccessVO> smsCodeLoginSystemUser(@RequestBody SmsCodeLoginDTO smsCodeLoginDTO){
        LoginSuccessVO loginSuccessVO = loginService.smsCodeLoginSystemUser(smsCodeLoginDTO);
        return ResultMessage.success(loginSuccessVO).message("登录成功");
    }

    /**
     * C端用户账号密码登录
     * @return
     */
    @PostMapping(value = "/login/c_user")
    public ResultMessage<LoginSuccessVO> loginCUser(){
        return ResultMessage.fail().message("暂未实现");
    }

    /**
     * C端用户登录微信第三方登录
     * @param wxLoginDTO
     * @return
     */
    @PostMapping(value = "/login/wx/c_user")
    public ResultMessage wxLoginCUser(@RequestBody WxLoginDTO wxLoginDTO){
        LoginSuccessVO loginSuccessVO = loginService.wxLoginCUser(wxLoginDTO);
        return ResultMessage.success(loginSuccessVO).message("登录成功");
    }

    /**
     * 刷新token
     * @param accountType
     * @param refreshToken
     * @return
     */
    @PostMapping(value = "/refresh_token")
    public ResultMessage<LoginSuccessVO> refreshToken(@SimpleValid @NotNull(message = "用户类型不能为空") @Range(min = 1, max = 2, message = "用户类型非法") Integer accountType,
                                                      @SimpleValid @NotBlank(message = "refreshToken 不能为空") String refreshToken){
        LoginSuccessVO loginSuccessVO = loginService.refreshToken(accountType, refreshToken);
        return ResultMessage.success(loginSuccessVO).message("令牌刷新成功");
    }

    /**
     * 注销
     * @param request
     * @return
     */
    @PostMapping(value = "/user/logout")
    public ResultMessage logout(HttpServletRequest request){
        String token = request.getParameter("access_token");
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        tokenStore.removeAccessToken(oAuth2AccessToken);
        return ResultMessage.success().message("用户注销成功");
    }
    
}
