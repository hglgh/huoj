package com.hgl.huojbackend.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.hgl.huojbackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgl.huojbackend.model.qo.user.*;
import com.hgl.huojbackend.model.vo.CaptchaVO;
import com.hgl.huojbackend.model.vo.LoginUserVO;
import com.hgl.huojbackend.model.vo.UserVO;

import java.util.List;

/**
 * @author 请别把我整破防
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-07-08 15:23:02
 */
public interface UserService extends IService<User> {

    /**
     * 获取图形验证码
     *
     * @return CaptchaVO
     */
    CaptchaVO getCaptcha();

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册信息
     * @return 用户ID
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录信息
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest);

    /**
     * 获取当前登录用户
     *
     * @return 脱敏后的用户信息
     */
    LoginUserVO getLoginUser();

    /**
     * 用户注销
     *
     * @return 是否注销成功
     */
    Boolean userLogout();

    /**
     * 更新用户信息
     *
     * @param userRegisterRequest 用户信息
     * @return 是否更新成功
     */
    Boolean updateUser(UserUpdateRequest userRegisterRequest);

    /**
     * 添加用户
     *
     * @param userAddRequest 用户信息
     * @return 用户ID
     */
    Long addUser(UserAddRequest userAddRequest);

    UserVO getUserVO(User user);

    Wrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    List<UserVO> getUserVOList(List<User> records);
}
