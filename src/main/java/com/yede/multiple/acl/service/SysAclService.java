package com.yede.multiple.acl.service;

import com.google.common.base.Preconditions;
import com.yede.multiple.acl.dao.SysAclMapper;
import com.yede.multiple.acl.entity.SysAcl;
import com.yede.multiple.acl.entity.bean.AclParam;
import com.yede.multiple.exception.ApplicationException;
import com.yede.multiple.model.PageVO;
import com.yede.multiple.model.QueryBean;
import com.yede.multiple.utils.AppSec;
import com.yede.multiple.utils.BeanValidator;
import com.yede.multiple.utils.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    public void save(AclParam param) throws ApplicationException {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ApplicationException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setCode(generateCode());
        acl.setOperator(AppSec.getLoginUser().getUserName());
        acl.setOperateTime(new Date());
        acl.setOperateIp(IpUtil.getRemoteIp(AppSec.getCurrentRequest()));
        sysAclMapper.insertSelective(acl);
//        sysLogService.saveAclLog(null, acl);
    }

    public void update(AclParam param) throws ApplicationException {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ApplicationException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        after.setOperator(AppSec.getLoginUser().getUserName());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(AppSec.getCurrentRequest()));

        sysAclMapper.updateByPrimaryKeySelective(after);
//        sysLogService.saveAclLog(before, after);
    }

    public boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    public String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }

    public PageVO<SysAcl> getPageByAclModuleId(int aclModuleId, QueryBean page) throws ApplicationException {
        BeanValidator.check(page);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0) {
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, page);
            return PageVO.<SysAcl>builder().result(aclList).total(count).build();
        }
        return PageVO.<SysAcl>builder().build();
    }
}
