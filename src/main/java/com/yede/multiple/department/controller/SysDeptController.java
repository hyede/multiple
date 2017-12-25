package com.yede.multiple.department.controller;

import com.yede.multiple.common.BaseController;
import com.yede.multiple.common.ResponseVO;
import com.yede.multiple.department.dto.DeptLevelDto;
import com.yede.multiple.department.entity.SysDept;
import com.yede.multiple.department.entity.bean.DeptParam;
import com.yede.multiple.department.service.SysDeptService;
import com.yede.multiple.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
@RequestMapping("/sysDept")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysDeptService;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseVO saveSysDept(@RequestBody DeptParam param) throws ApplicationException {
        sysDeptService.save(param);
       return this.successResponse();
    }
    @RequestMapping(value = "/{sysDeptById}",method = RequestMethod.PUT)
    public ResponseVO updateSysDept(@PathVariable Long sysDeptById,@RequestBody DeptParam param) throws ApplicationException {
        param.setId(sysDeptById);
        sysDeptService.update(param);
        return this.successResponse();
    }

    @RequestMapping(value = "/{sysDeptById}",method = RequestMethod.GET)
    public ResponseVO getSysDeptById(@PathVariable Long sysDeptById) throws ApplicationException {
       SysDept sysDept= sysDeptService.getById(sysDeptById);
        return this.successResponse(sysDept);
    }

    @RequestMapping(value = "/sysDeptTree",method = RequestMethod.GET)
    public ResponseVO tree() {
        ArrayList<DeptLevelDto> dtoList = sysDeptService.deptTree();
        return this.successResponse(dtoList);
    }


}
