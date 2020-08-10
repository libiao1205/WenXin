package com.sundeinfo.kd.zs.apis.restful.auth.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sundeinfo.core.authentication.dao.DaoAuthenticationProvider;
import com.sundeinfo.core.permission.strategy.token.AuthenticationResponse;
import com.sundeinfo.foundation.mvc.controller.AbstractController;
import com.sundeinfo.foundation.request.RequestState;
import com.sundeinfo.foundation.request.ResponseCallback;
import com.sundeinfo.foundation.request.result.Result;
import com.sundeinfo.kd.zs.define.enums.WeChatMpType;
import com.sundeinfo.kd.zs.dto.auth.wx.WxLoginDTO;
import com.sundeinfo.kd.zs.dto.auth.wx.WxRegisterPhoneDTO;
import com.sundeinfo.kd.zs.dto.auth.wx.WxSessionInfoDTO;
import com.sundeinfo.kd.zs.dto.sys.UserDTO;
import com.sundeinfo.kd.zs.service.auth.UserAuthService;
import com.sundeinfo.kd.zs.service.auth.wx.WxAuthService;
import com.sundeinfo.kd.zs.service.user.UserService;
import com.sundeinfo.kd.zs.utility.StringUtils;
import com.sundeinfo.kd.zs.utility.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("kdzs/api/auth/wx/")
public class WxAuthRestController extends AbstractController<WxAuthRestController> {

    @Autowired
    private WxAuthService wxAuthService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping(value = "/login")
    public ResponseEntity<Result<AuthenticationResponse>> login(@RequestBody WxLoginDTO wxLoginDTO) throws AuthenticationException {
        return requestHandler.doPost(
                new ResponseCallback<AuthenticationResponse>() {
                    String token = "";
                    @Override
                    public RequestState doCheck() {
                        return RequestState.SUCCESS;
                    }

                    @Override
                    public RequestState invoke() throws Exception {
                        WxSessionInfoDTO wxSessionInfoDto = wxAuthService.jscode2session(wxLoginDTO.getMpType(), wxLoginDTO.getWxCode());
                        if (wxSessionInfoDto == null) {
                            return RequestState.WX_AUTH_ERROR;
                        }
                        UserDTO userDTO = null;
                        if (wxSessionInfoDto.getSession_key() != null) {
                            if (wxSessionInfoDto.getUnionid() != null) {
                                userDTO = userService.findByUnionId(wxSessionInfoDto.getUnionid());
                            } else {
                                if (wxSessionInfoDto.getOpenid() != null) {
                                    userDTO = userService.findByOpenId(wxSessionInfoDto.getOpenid());
                                }
                            }
                            if (userDTO != null) {
                                token = userAuthService.login(userDTO.getUsername(), DaoAuthenticationProvider.getNoEncoderPassword(userDTO.getLoginPassword()));
                            } else {
                                return RequestState.WX_AUTH_NO_USER_ERROR;
                            }
                        } else {
                            return RequestState.WX_AUTH_ERROR;
                        }
                        return RequestState.SUCCESS;
                    }

                    @Override
                    public AuthenticationResponse response() throws Exception {
                        return UserHelper.createJwtAuthenticationResponse(token);
                    }
                }
        );
    }



}
