package com.yede.multiple.acl.controller;

import com.google.common.collect.Maps;
import com.yede.multiple.acl.entity.bean.AclParam;
import com.yede.multiple.acl.service.SysAclService;
import com.yede.multiple.common.BaseController;
import com.yede.multiple.common.ResponseVO;
import com.yede.multiple.exception.ApplicationException;
import com.yede.multiple.model.QueryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sysAcl")
@Slf4j
public class SysAclController extends BaseController {

    @Resource
    private SysAclService sysAclService;
//    @Resource
//    private SysRoleService sysRoleService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseVO saveAclModule(@RequestBody  AclParam param) throws ApplicationException {
        sysAclService.save(param);
        return this.successResponse();
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ResponseVO updateAclModule(AclParam param) throws ApplicationException {
        sysAclService.update(param);
        return this.successResponse();
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public ResponseVO list(@RequestParam("aclModuleId") Integer aclModuleId, QueryBean pageQuery) throws ApplicationException {
        return this.successResponse(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }

//    @RequestMapping(value = "acls",method = RequestMethod.GET)
//    public ResponseVO acls(@RequestParam("aclId") int aclId) {
//        Map<String, Object> map = Maps.newHashMap();
//        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
//        map.put("roles", roleList);
//        map.put("users", sysRoleService.getUserListByRoleList(roleList));
//        return JsonData.success(map);
//    }
}
