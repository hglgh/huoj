package com.hgl.huojbackend.controller.user;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgl.huojbackend.common.BaseResponse;
import com.hgl.huojbackend.common.DeleteRequest;
import com.hgl.huojbackend.common.ResultUtils;
import com.hgl.huojbackend.common.constant.UserRoleConstant;
import com.hgl.huojbackend.exception.ErrorCode;
import com.hgl.huojbackend.exception.ThrowUtils;
import com.hgl.huojbackend.model.entity.User;
import com.hgl.huojbackend.model.enums.UserRoleEnum;
import com.hgl.huojbackend.model.qo.user.*;
import com.hgl.huojbackend.model.vo.CaptchaVO;
import com.hgl.huojbackend.model.vo.LoginUserVO;
import com.hgl.huojbackend.model.vo.UserVO;
import com.hgl.huojbackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName UserController
 * @Author 请别把我整破防
 * @Description //TODO
 * @Date 2025/7/8 15:17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 获取图形验证码
     *
     * @return 验证码
     */
    @GetMapping("/getCaptcha")
    public BaseResponse<CaptchaVO> getCaptcha() {
        return ResultUtils.success(userService.getCaptcha());
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        return ResultUtils.success(userService.userRegister(userRegisterRequest));
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录信息
     * @return 登录结果(脱敏后的用户信息)
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        return ResultUtils.success(userService.userLogin(userLoginRequest));
    }

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户(脱敏后的用户信息)
     */
    @GetMapping("get/login")
    public BaseResponse<LoginUserVO> getLoginUser() {
        return ResultUtils.success(userService.getLoginUser());
    }

    /**
     * 用户登出
     *
     * @return 登出结果
     */
    @GetMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        return ResultUtils.success(userService.userLogout());
    }

    /**
     * 添加用户(仅管理员可操作)
     *
     * @param userAddRequest 添加信息
     * @return 添加结果
     */
    @PostMapping("/add")
    @SaCheckRole(UserRoleConstant.USER_ROLE_ADMIN)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        return ResultUtils.success(userService.addUser(userAddRequest));
    }

    /**
     * 更新用户信息(仅管理员可操作)
     *
     * @param userRegisterRequest 更新信息
     * @return 更新结果
     */
    @PostMapping("/update")
    @SaCheckRole(UserRoleConstant.USER_ROLE_ADMIN)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userRegisterRequest) {
        return ResultUtils.success(userService.updateUser(userRegisterRequest));
    }

    /**
     * 删除用户(仅管理员可操作)
     *
     * @param deleteRequest 删除信息
     * @return 删除结果
     */
    @PostMapping("/delete")
    @SaCheckRole(UserRoleConstant.USER_ROLE_ADMIN)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.removeById(deleteRequest.getId()));
    }

    /**
     * 根据id获取用户(仅管理员可操作)
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/get")
    @SaCheckRole(UserRoleConstant.USER_ROLE_ADMIN)
    public BaseResponse<User> getUserById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据id获取用户VO
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/get/vo")
    @SaCheckLogin
    public BaseResponse<UserVO> getUserVoById(Long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表(仅管理员可操作)
     *
     * @param userQueryRequest 查询信息
     * @return 用户列表
     */
    @PostMapping("/list/page")
    @SaCheckRole(UserRoleConstant.USER_ROLE_ADMIN)
    public BaseResponse<Page<UserVO>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        int current = userQueryRequest.getCurrent();
        int pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize), userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVoPage = new Page<>(current, pageSize, userPage.getTotal());
        userVoPage.setRecords(userService.getUserVOList(userPage.getRecords()));
        return ResultUtils.success(userVoPage);
    }
}
