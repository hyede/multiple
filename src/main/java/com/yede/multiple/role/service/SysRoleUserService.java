package com.yede.multiple.role.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yede.multiple.role.dao.SysRoleMapper;
import com.yede.multiple.role.dao.SysRoleUserMapper;
import com.yede.multiple.role.entity.SysRole;
import com.yede.multiple.role.entity.SysRoleUser;
import com.yede.multiple.user.dao.SysUserMapper;
import com.yede.multiple.user.entity.SysUser;
import com.yede.multiple.utils.AppSec;
import com.yede.multiple.utils.IpUtil;
import com.yede.multiple.utils.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired

    private SysRoleMapper sysRoleMapper;
//    @Resource
//    private SysLogMapper sysLogMapper;

    public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
//        saveRoleUserLog(roleId, originUserIdList, userIdList);
    }

    @Transactional
    public void updateRoleUsers(int roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId).operator(AppSec.getLoginUser().getUserName())
                    .operateIp(IpUtil.getRemoteIp(AppSec.getCurrentRequest())).operateTime(new Date()).build();
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }

    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

        public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = Lists.newArrayList();
        for (SysRole sysRole : roleList){
            roleIdList.add(sysRole.getId());
        }

        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }


//    private void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
//        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
//        sysLog.setType(LogType.TYPE_ROLE_USER);
//        sysLog.setTargetId(roleId);
//        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
//        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
//        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
//        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//        sysLog.setOperateTime(new Date());
//        sysLog.setStatus(1);
//        sysLogMapper.insertSelective(sysLog);
//    }
}
