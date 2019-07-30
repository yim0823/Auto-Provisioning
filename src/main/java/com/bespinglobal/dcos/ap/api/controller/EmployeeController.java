package com.bespinglobal.dcos.ap.api.controller;

import com.bespinglobal.dcos.ap.api.component.ServiceComponent;
import com.bespinglobal.dcos.ap.api.response.ApiResponseDto;
import com.bespinglobal.dcos.ap.api.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Project : Information-Collector
 * Class : EmployeeController
 * Version : 2019.07.17 v0.1
 * Created by taehyoung.yim on 2019-07-17.
 * *** 저작권 주의 ***
 */
@RestController
@RequestMapping(value = EmployeeController.PATH)
public class EmployeeController {

    static final String PATH = "/rest/employees";

    private ServiceComponent service;

    @Autowired
    public void setServiceComponent(ServiceComponent service) { this.service = service; }

    @GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
    public String getHealthCheck() {
        return "{ \"isWorking\" : true }";
    }

    @GetMapping
    public ApiResponseDto<EmployeeDto.ResponseList> findAll() {
        return ApiResponseDto.createOK(new EmployeeDto.ResponseList(service.getEmployeeService().findAll()));
    }

    @GetMapping("{id}")
    public ApiResponseDto<EmployeeDto.ResponseOne> findById(@PathVariable("id") String id) {
        return ApiResponseDto.createOK(new EmployeeDto.ResponseOne(service.getEmployeeService().findById(id)));
    }

    @PostMapping
    public ApiResponseDto<EmployeeDto.ResponseOne> add(@RequestBody @Valid EmployeeDto.Create create) {
        return ApiResponseDto.createOK(new EmployeeDto.ResponseOne(service.getEmployeeService().add(create)));
    }

    @DeleteMapping("{id}")
    public ApiResponseDto delete(@PathVariable("id") String id) {
        service.getEmployeeService().delete(id);
        return ApiResponseDto.DEFAULT_OK;
    }

    @PutMapping("{id}")
    public ApiResponseDto<EmployeeDto.ResponseOne> update(@PathVariable("id") String id, @Valid @RequestBody EmployeeDto.Update update) {
        return ApiResponseDto.createOK(new EmployeeDto.ResponseOne(service.getEmployeeService().update(id, update)));
    }
}
