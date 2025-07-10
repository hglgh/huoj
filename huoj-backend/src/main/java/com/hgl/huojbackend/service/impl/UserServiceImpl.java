package com.hgl.huojbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgl.huojbackend.common.constant.CacheKeyConstant;
import com.hgl.huojbackend.common.constant.CommonConstant;
import com.hgl.huojbackend.common.constant.UserConstant;
import com.hgl.huojbackend.common.redis.RedisCache;
import com.hgl.huojbackend.exception.ErrorCode;
import com.hgl.huojbackend.exception.ThrowUtils;
import com.hgl.huojbackend.model.entity.User;
import com.hgl.huojbackend.model.enums.UserRoleEnum;
import com.hgl.huojbackend.model.qo.user.*;
import com.hgl.huojbackend.model.vo.CaptchaVO;
import com.hgl.huojbackend.model.vo.LoginUserVO;
import com.hgl.huojbackend.model.vo.UserVO;
import com.hgl.huojbackend.service.UserService;
import com.hgl.huojbackend.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 请别把我整破防
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-07-08 15:23:02
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String SALT = "hgl";

    @Resource
    private RedisCache redisCache;

    @Override
    public CaptchaVO getCaptcha() {
        //创建指定宽高的验证码
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100);
        //设置随机字符(只允许数字),4位
        captcha.setGenerator(new RandomGenerator("0123456789", 4));
        //获取验证码
        String captchaValue = captcha.getCode();
        // 获取 Base64 编码的图片数据（Data URI 格式）
        String captchaImageBase64 = captcha.getImageBase64();
        // 将图片数据转换为 Data URI,加上 MIME 前缀
        captchaImageBase64 = String.format("data:image/png;base64,%s", captchaImageBase64);
        String captchaKey = IdUtil.randomUUID();
        redisCache.set(String.format(CacheKeyConstant.CAPTCHA_KEY_PREFIX, captchaKey), captchaValue, 5, TimeUnit.MINUTES);
        return new CaptchaVO(captchaKey, captchaImageBase64);
    }

    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        //校验传入参数
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword, checkPassword), ErrorCode.PARAMS_ERROR, "请确认参数");
        ThrowUtils.throwIf(userAccount.length() < 4, ErrorCode.PARAMS_ERROR, "用户账号过短");
        ThrowUtils.throwIf(userPassword.length() < 8 || checkPassword.length() < 8, ErrorCode.PARAMS_ERROR, "用户密码过短");
        ThrowUtils.throwIf(userAccount.contains(" ") || userPassword.contains(" ") || checkPassword.contains(" "), ErrorCode.PARAMS_ERROR, "参数包含空格");
        ThrowUtils.throwIf(!userPassword.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        long count = this.count(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount));
        ThrowUtils.throwIf(count > 0, ErrorCode.OPERATION_ERROR, "账号重复");
        //加密
        String encryptPassword = getEncryptPassword(userPassword);

        //填充属性
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(String.format("用户%s", RandomUtil.randomString(8)));
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean save = this.save(user);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "注册失败");
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        String captchaKey = userLoginRequest.getCaptchaKey();
        String captcha = userLoginRequest.getCaptcha();
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword, captchaKey, captcha), ErrorCode.PARAMS_ERROR, "参数为空");
        ThrowUtils.throwIf(userAccount.length() < 4, ErrorCode.PARAMS_ERROR, "用户账号过短");
        ThrowUtils.throwIf(userPassword.length() < 8, ErrorCode.PARAMS_ERROR, "用户密码过短");
        ThrowUtils.throwIf(userAccount.contains(" ") || userPassword.contains(" "), ErrorCode.PARAMS_ERROR, "参数包含空格");

        String cacheKey = String.format(CacheKeyConstant.CAPTCHA_KEY_PREFIX, captchaKey);
        String redisCaptcha = redisCache.get(cacheKey);
        redisCache.delete(cacheKey);

        ThrowUtils.throwIf(redisCaptcha == null, ErrorCode.OPERATION_ERROR, "验证码已过期");
        ThrowUtils.throwIf(!redisCaptcha.equals(captcha), ErrorCode.OPERATION_ERROR, "验证码错误");
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount));
        ThrowUtils.throwIf(user == null, ErrorCode.OPERATION_ERROR, "用户不存在");
        ThrowUtils.throwIf(!user.getUserPassword().equals(getEncryptPassword(userPassword)), ErrorCode.OPERATION_ERROR, "密码错误");

        StpUtil.login(user.getId());
        StpUtil.getSession().set(UserConstant.USER_LOGIN_STATE + user.getId(), user);
        return this.getLoginUserVO(user);
    }

    @Override
    public LoginUserVO getLoginUser() {
        ThrowUtils.throwIf(!StpUtil.isLogin(), ErrorCode.OPERATION_ERROR, "未登录");
        long userId = StpUtil.getLoginIdAsLong();
        User user = BeanUtil.toBean(StpUtil.getSession().get(UserConstant.USER_LOGIN_STATE + userId), User.class);
        if (user == null) {
            user = this.getById(userId);
            ThrowUtils.throwIf(user == null, ErrorCode.OPERATION_ERROR, "用户不存在");
        }
        return this.getLoginUserVO(user);
    }

    @Override
    public Boolean userLogout() {
        ThrowUtils.throwIf(!StpUtil.isLogin(), ErrorCode.OPERATION_ERROR, "未登录");
        long id = StpUtil.getLoginIdAsLong();
        log.info("用户【{}】退出登录", id);
        StpUtil.logout();
        return true;
    }

    @Override
    public Boolean updateUser(UserUpdateRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null || userRegisterRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        User user = BeanUtil.copyProperties(userRegisterRequest, User.class);
        boolean update = this.updateById(user);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "更新失败");
        return true;
    }

    @Override
    public Long addUser(UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = BeanUtil.copyProperties(userAddRequest, User.class);
        user.setUserRole(UserRoleEnum.USER.getValue());
        user.setUserPassword(getEncryptPassword("12345678"));
        boolean save = this.save(user);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "添加失败");
        return user.getId();
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Override
    public Wrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR, "请求参数为空");
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(id != null, "id", id);
        userQueryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        userQueryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        userQueryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        userQueryWrapper.orderBy(StrUtil.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return userQueryWrapper;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> records) {
        return records.stream().map(this::getUserVO).toList();
    }

    /**
     * 获取登录用户信息
     *
     * @param user 用户信息
     * @return 登录用户信息
     */
    private LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        return BeanUtil.copyProperties(user, LoginUserVO.class);
    }

    /**
     * 获取加密后的密码
     *
     * @param userPassword 原始密码
     * @return 加密后的密码
     */
    private String getEncryptPassword(String userPassword) {
        return DigestUtil.md5Hex(SALT + userPassword);
    }
}




