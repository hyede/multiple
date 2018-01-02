package com.yede.multiple.user.controller;

import com.yede.multiple.common.BaseController;
import com.yede.multiple.common.ResponseVO;
import com.yede.multiple.exception.ApplicationException;
import com.yede.multiple.model.PageVO;
import com.yede.multiple.model.QueryBean;
import com.yede.multiple.user.entity.SysUser;
import com.yede.multiple.user.entity.bean.UserParam;
import com.yede.multiple.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysUsers")
public class SysUserController extends BaseController {


    @Autowired
    private SysUserService sysUserService;

//    @Resource
//    private SysTreeService sysTreeService;
//
//
//    @Resource
//    private SysRoleService sysRoleService;


    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseVO saveUser(@RequestBody UserParam param) throws ApplicationException{
        sysUserService.save(param);
        return this.successResponse();
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ResponseVO updateUser(@RequestBody  UserParam param) throws ApplicationException{
        sysUserService.update(param);
        return this.successResponse();
    }

    @RequestMapping(value = "/page/{deptId}",method = RequestMethod.GET)
    public ResponseVO<PageVO<SysUser>> page(@PathVariable int deptId, QueryBean pageQuery) throws ApplicationException{
        PageVO<SysUser> result = sysUserService.getPageByDeptId(deptId, pageQuery);
        return this.successResponse(result);
    }

//    @RequestMapping("/acls.json")
//    public ResponseVO acls(@RequestParam("userId") int userId) {
//        Map<String, Object> map = Maps.newHashMap();
//        map.put("acls", sysTreeService.userAclTree(userId));
//        map.put("roles", sysRoleService.getRoleListByUserId(userId));
//        return JsonData.success(map);
//    }
}
