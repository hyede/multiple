package com.yede.multiple.role.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yede.multiple.common.BaseController;
import com.yede.multiple.common.ResponseVO;
import com.yede.multiple.exception.ApplicationException;
import com.yede.multiple.role.entity.bean.RoleParam;
import com.yede.multiple.role.service.SysRoleAclService;
import com.yede.multiple.role.service.SysRoleService;
import com.yede.multiple.role.service.SysRoleUserService;
import com.yede.multiple.user.entity.SysUser;
import com.yede.multiple.user.service.SysUserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sysRole")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;
//    @Resource
//    private SysTreeService sysTreeService;
    @Resource
    private SysRoleAclService sysRoleAclService;
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysUserService sysUserService;


    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseVO saveRole(@RequestBody RoleParam param) throws ApplicationException{
        sysRoleService.save(param);
        return this.successResponse();
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ResponseVO updateRole(RoleParam param) throws ApplicationException {
        sysRoleService.update(param);
        return this.successResponse();
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResponseVO list() {
        ArrayList list = Lists.newArrayList(sysRoleService.getAll());
        return this.successResponse(list);
    }

    @RequestMapping(value = "/{roleId}/roleTree",method = RequestMethod.GET)
    public ResponseVO roleTree(@PathVariable int roleId) {
        return this.successResponse(sysRoleService.roleTree(roleId));
    }

    @RequestMapping(value = "/{roleId}/changeAcls",method = RequestMethod.PUT)
    public ResponseVO changeAcls(@PathVariable int roleId, @RequestBody(required = false) List<Integer> aclIdList) {
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return this.successResponse();
    }

    @RequestMapping(value = "/{roleId}/changeUsers",method = RequestMethod.PUT)
    public ResponseVO changeUsers(@PathVariable int roleId, @RequestBody(required = false) List<Integer> userIds) {
        sysRoleUserService.changeRoleUsers(roleId, userIds);
        return this.successResponse();
    }

    @RequestMapping(value = "/users/{roleId}",method = RequestMethod.GET)
    public ResponseVO users(@PathVariable int roleId) {
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
        List<SysUser> allUserList = sysUserService.getAll();
        List<SysUser> unselectedUserList = Lists.newArrayList();
        Set<Integer> selectedUserIdSet= Sets.newHashSet();
        for (SysUser sysUser:allUserList){
           Integer userId = sysUser.getId();
           selectedUserIdSet.add(userId);
        }

        for(SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unselectedUserList.add(sysUser);
            }
        }
        HashMap<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);
        return this.successResponse(map);
    }
}
