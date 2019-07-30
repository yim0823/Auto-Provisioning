package com.bespinglobal.dcos.ap.api.service;

import com.bespinglobal.dcos.ap.api.dto.PersonDto;
import com.bespinglobal.dcos.ap.api.exception.ApplicationException;
import com.bespinglobal.dcos.ap.api.repositories.basic.domain.Person;
import com.bespinglobal.dcos.ap.api.repositories.basic.repository.PersonRepository;
import com.bespinglobal.dcos.ap.utils.RtCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Project : Information-Collector
 * Class : PersonService
 * Version : 2019.07.16 v0.1
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@Service
public class PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDto.Response> findAll() {
        return personRepository.findAll().stream()
                .map(PersonDto.Response::of)
                .collect(Collectors.toList());
    }

    public PersonDto.Response findById(Long id) {
        return personRepository.findById(id)
                .map(PersonDto.Response::of)
                .orElseThrow(() ->
                        new ApplicationException(RtCode.RT_NOT_EXIST, "There are no Person with the ID : " + id));
    }

    public PersonDto.Response add(PersonDto.Create create) {

        Person target = create.toEntity();
        Person created = personRepository.save(target);

        return PersonDto.Response.of(created);
    }

    public void delete(Long id) {

        Person person = personRepository.findById(id)
                .orElseThrow(() ->
                        new ApplicationException(RtCode.RT_NOT_EXIST, "There are no Person with the ID : " + id));

        personRepository.delete(person);
    }

    @Transactional
    public PersonDto.Response update(Long id, PersonDto.Update update) {

        Person updated = personRepository.findById(id)
                .map(update::apply)
                .orElseThrow(() ->
                        new ApplicationException(RtCode.RT_NOT_EXIST, "There are no Person with the ID : " + id));

        return PersonDto.Response.of(updated);
    }
}
