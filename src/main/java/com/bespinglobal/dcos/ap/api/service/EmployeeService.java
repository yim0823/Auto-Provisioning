package com.bespinglobal.dcos.ap.api.service;

import com.bespinglobal.dcos.ap.api.dto.EmployeeDto;

import java.util.List;

/**
 * Project : Information-Collector
 * Class : EmployeeService
 * Version : 2019.07.17 v0.1
 * Created by taehyoung.yim on 2019-07-17.
 * *** 저작권 주의 ***
 */
public interface EmployeeService {

    List<EmployeeDto.Response> findAll();

    EmployeeDto.Response findById(String id);

    EmployeeDto.Response add(EmployeeDto.Create create);

    EmployeeDto.Response update(String id, EmployeeDto.Update update);

    void delete(String id);
}
