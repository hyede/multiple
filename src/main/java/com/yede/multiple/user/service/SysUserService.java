package com.yede.multiple.user.service;

import com.google.common.base.Preconditions;
import com.yede.multiple.exception.ApplicationException;
import com.yede.multiple.model.PageVO;
import com.yede.multiple.model.QueryBean;
import com.yede.multiple.model.UserDetailsBean;
import com.yede.multiple.user.dao.SysUserMapper;
import com.yede.multiple.user.entity.SysUser;
import com.yede.multiple.user.entity.bean.UserParam;
import com.yede.multiple.utils.AppSec;
import com.yede.multiple.utils.BeanValidator;
import com.yede.multiple.utils.DatabaseContextHolder;
import com.yede.multiple.utils.IpUtil;
import com.yede.multiple.utils.MD5Util;
import com.yede.multiple.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;




    public UserDetailsBean findUserDetails(String organizationCode, String userName) {
        SysUser user;
        try {
            DatabaseContextHolder.setDataSourceKey(organizationCode);
            user = sysUserMapper.findByName(userName);
            if (user == null) {
                return null;
            }
            return  UserDetailsBean.builder().userName(user.getUsername()).fullName(user.getUsername()).userId(Long.valueOf(user.getId())).organizationCode(organizationCode).build();
        } finally {
            DatabaseContextHolder.setDataSourceKey(organizationCode);
        }
    }


    public void save(UserParam param) throws ApplicationException{
        BeanValidator.check(param);
        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ApplicationException("电话已被占用");
        }
        if(checkEmailExist(param.getMail(), param.getId())) {
            throw new ApplicationException("邮箱已被占用");
        }
        String password = PasswordUtil.randomPassword();
        //TODO:
        password = "12345678";
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser user = SysUser.builder().username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
                .password(encryptedPassword).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        user.setOperator(AppSec.getLoginUser().getFullName());
        user.setOperateIp(IpUtil.getRemoteIp(AppSec.getCurrentRequest()));
        user.setOperateTime(Calendar.getInstance().getTime());
        // TODO: sendEmail
        sysUserMapper.insertSelective(user);
    }

    public void update(UserParam param) throws ApplicationException{
        BeanValidator.check(param);
        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ApplicationException("电话已被占用");
        }
        if(checkEmailExist(param.getMail(), param.getId())) {
            throw new ApplicationException("邮箱已被占用");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
                .deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        after.setOperator(AppSec.getLoginUser().getFullName());
        after.setOperateIp(IpUtil.getRemoteIp(AppSec.getCurrentRequest()));
        after.setOperateTime(Calendar.getInstance().getTime());
        sysUserMapper.updateByPrimaryKeySelective(after);
    }

    public boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }

    public SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }
    public SysUser findByUserName(String userName) {
        return sysUserMapper.findByName(userName);
    }

    public PageVO<SysUser> getPageByDeptId(int deptId, QueryBean page)  throws ApplicationException {
        BeanValidator.check(page);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
            return PageVO.<SysUser>builder().total(count).result(list).build();
        }
        return PageVO.<SysUser>builder().build();
    }

    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }
}