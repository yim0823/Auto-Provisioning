package com.bespinglobal.dcos.ap.api.controller;

import com.bespinglobal.dcos.ap.api.controller.document.ApiDocumentUtils;
import com.bespinglobal.dcos.ap.api.controller.document.DocumentFormatGenerator;
import com.bespinglobal.dcos.ap.api.dto.PersonDto;
import com.bespinglobal.dcos.ap.api.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Project : Auto-Provisioning
 * Class : PersonControllerTest
 * Version :
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @Test
    public void a1_findAll() throws Exception {

        // given
        List<PersonDto.Response> responseList = Arrays.asList(
                PersonDto.Response.builder()
                        .id(1L)
                        .firstName("taehyoung")
                        .lastName("Yim")
                        .gender("Male")
                        .birthDate(LocalDate.of(1983,8,23))
                        .hobby("play football")
                        .build(),
                PersonDto.Response.builder()
                        .id(1L)
                        .firstName("kyounghee")
                        .lastName("Kim")
                        .gender("Female")
                        .birthDate(LocalDate.of(1983,8,23))
                        .hobby("play with me")
                        .build()
        );

        given(personService.findAll()).willReturn(responseList);

        // when
        ResultActions resultActions = this.mockMvc.perform(
                get("/rest/persons").accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("persons-find-all",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("persons[].id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("persons[].firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("persons[].lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("persons[].age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("persons[].birthDate").type(JsonFieldType.STRING).attributes(DocumentFormatGenerator.getDateFormat()).description("생년월일"),
                                fieldWithPath("persons[].hobby").type(JsonFieldType.STRING).description("취미"),
                                fieldWithPath("persons[].gender").type(JsonFieldType.STRING).description("성별")
                            )

                ));
    }

    @Test
    public void a2_findByID() throws Exception {

        // given
        given(personService.findById(1L))
                .willReturn(
                        PersonDto.Response.builder()
                                .id(1L)
                                .firstName("lionel")
                                .lastName("Yim")
                                .gender("Male")
                                .birthDate(LocalDate.of(2018, 11, 20))
                                .hobby("play with his mom")
                                .build()
        );

        // when
        ResultActions resultActions = this.mockMvc.perform(
                get("/rest/persons/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("persons-find",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("person.id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("person.firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("person.lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("person.age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("person.birthDate").type(JsonFieldType.STRING).attributes(DocumentFormatGenerator.getDateFormat()).description("생년월일"),
                                fieldWithPath("person.gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("person.hobby").type(JsonFieldType.STRING).description("취미")
                        )
                ));
    }

    @Test
    public void a3_add() throws Exception {

        // given
        PersonDto.Response response = PersonDto.Response.builder()
                .id(1L)
                .firstName("호진")
                .lastName("이")
                .gender("Male")
                .birthDate(LocalDate.of(1983, 7, 4))
                .hobby("놀기")
                .build();

        given(personService.add(any(PersonDto.Create.class))).willReturn(response);

        //when
        PersonDto.Create create = new PersonDto.Create();
        create.setFirstName("taehyoung");
        create.setLastName("Yim");
        create.setBirthDate(LocalDate.of(1983, 8, 23));
        create.setGender("Male");
        create.setHobby("play football");

        ResultActions resultActions = this.mockMvc.perform(
                post("/rest/persons")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("persons-add",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestFields(
                                fieldWithPath("firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).attributes(DocumentFormatGenerator.getDateFormat()).description("생년월일"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("hobby").type(JsonFieldType.STRING).description("취미").optional()
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("person.id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("person.firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("person.lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("person.age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("person.birthDate").type(JsonFieldType.STRING).attributes(DocumentFormatGenerator.getDateFormat()).description("생년월일"),
                                fieldWithPath("person.gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("person.hobby").type(JsonFieldType.STRING).description("취미")
                        )
                ));
    }

    @Test
    public void a4_delete_by_id() throws Exception {

        //given
        doNothing().when(personService).delete(1L);

        //when
        ResultActions resultActions = this.mockMvc.perform(
                delete("/rest/persons/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("persons-delete",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        )
                ));
    }

    @Test
    public void a5_update() throws Exception {

        //given
        PersonDto.Response response = PersonDto.Response.builder()
                .id(1L)
                .firstName("taehyoung")
                .lastName("Yim")
                .birthDate(LocalDate.of(1985, 2, 1))
                .gender("Male")
                .hobby("play with along")
                .build();

        given(personService.update(eq(1L), any(PersonDto.Update.class))).willReturn(response);

        //when
        PersonDto.Update update = new PersonDto.Update();
        update.setFirstName("taehyoung");
        update.setLastName("Yim");
        update.setBirthDate(LocalDate.of(1983, 8, 23));
        update.setHobby("play football");


        ResultActions resultActions = this.mockMvc.perform(
                put("/rest/persons/{id}", 1L)
                .content(objectMapper.writeValueAsString(update))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("persons-update",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        requestFields(
                                fieldWithPath("firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).attributes(DocumentFormatGenerator.getDateFormat()).description("생년월일"),
                                fieldWithPath("hobby").type(JsonFieldType.STRING).description("취미").optional()
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("person.id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("person.firstName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("person.lastName").type(JsonFieldType.STRING).description("성"),
                                fieldWithPath("person.age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("person.birthDate").type(JsonFieldType.STRING).attributes(DocumentFormatGenerator.getDateFormat()).description("생년월일"),
                                fieldWithPath("person.gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("person.hobby").type(JsonFieldType.STRING).description("취미")
                        )
                ));
    }
}