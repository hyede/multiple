package com.yede.multiple.aclModule.controller;


import com.yede.multiple.aclModule.entity.bean.AclModuleParam;
import com.yede.multiple.aclModule.service.SysAclModuleService;
import com.yede.multiple.common.BaseController;
import com.yede.multiple.common.ResponseVO;
import com.yede.multiple.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sysAclModule")
@Slf4j
public class SysAclModuleController extends BaseController{

    @Resource
    private SysAclModuleService sysAclModuleService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseVO saveAclModule(@RequestBody  AclModuleParam param) throws ApplicationException{
        sysAclModuleService.save(param);
        return this.successResponse();
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    public ResponseVO updateAclModule(@PathVariable int id,@RequestBody  AclModuleParam param) throws ApplicationException{
        sysAclModuleService.update(param);
        return this.successResponse();
    }

    @RequestMapping(value = "/tree",method = RequestMethod.GET)
    public ResponseVO tree() {
        return this.successResponse(sysAclModuleService.aclModuleTree());
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public ResponseVO delete(@PathVariable int id)  throws ApplicationException  {
        sysAclModuleService.delete(id);
        return this.successResponse();
    }
}
