package com.travel.jeju.svcenter;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;

import com.travel.jeju.BaseTest;
import com.travel.jeju.mapper.SvcenterMapper;
import com.travel.jeju.model.member.TokenModel;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
public class SvcenterControllerTest extends BaseTest {

    @Autowired
    private SvcenterMapper svcenterMapper;

    private final String BASE_URL = "/api/service-center";

    @Test
    public void qaDelete() throws Exception{

        int co_pk_conum = 300;

        TokenModel tokenModel = this.getAdminToken();

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete(this.BASE_URL + "/qa/delete/{co_pk_conum}", co_pk_conum)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "service-center/qa/delete",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                pathParameters(
                    parameterWithName("co_pk_conum").description("건의사항 일련번호").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값 [성공 : ture, 실패 : false]")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void qaDetail() throws Exception{
        int co_pk_conum = 3;

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get(this.BASE_URL + "/qa/detail/{co_pk_conum}", co_pk_conum)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "service-center/qa/detail",
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("co_pk_conum").description("건의사항 일련번호").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값").optional(),
                    fieldWithPath("body.co_pk_conum").type(JsonFieldType.NUMBER).description("건의사항 일련번호"),
                    fieldWithPath("body.co_fk_id").type(JsonFieldType.STRING).description("건의사항 작성자 아이디"),
                    fieldWithPath("body.co_title").type(JsonFieldType.STRING).description("건의사항 제목"),
                    fieldWithPath("body.co_contents").type(JsonFieldType.STRING).description("건의사항 내용"),
                    fieldWithPath("body.co_create_dt").type(JsonFieldType.STRING).description("건의사항 생성일시"),
                    fieldWithPath("body.co_check").type(JsonFieldType.BOOLEAN).description("건의사항 답변여부"),

                    fieldWithPath("body.co_reply").type(JsonFieldType.STRING).description("건의사항 답변내용").optional(),
                    fieldWithPath("body.co_re_creaet_dt").type(JsonFieldType.STRING).description("건의사항 답변작성 일시").optional()
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void qaMyList() throws Exception{
        int pageNum = 0;

        TokenModel tokenModel = this.getAdminToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/qa/my-list")
            .contextPath("/api")
            .param("pageNum", String.valueOf(pageNum))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "service-center/qa/my-list",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                queryParameters(
                    parameterWithName("pageNum").description("페이지 번호").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.complaint").type(JsonFieldType.ARRAY).description("건의사항 리스트").optional(),
                    fieldWithPath("body.complaint[].co_pk_conum").type(JsonFieldType.NUMBER).description("건의사항 일련번호"),
                    fieldWithPath("body.complaint[].co_fk_id").type(JsonFieldType.STRING).description("건의사항 작성자"),
                    fieldWithPath("body.complaint[].co_title").type(JsonFieldType.STRING).description("건의사항 제목"),
                    fieldWithPath("body.complaint[].co_contents").type(JsonFieldType.STRING).description("건의사항 내용"),
                    fieldWithPath("body.complaint[].co_create_dt").type(JsonFieldType.STRING).description("건의사항 생성일시"),
                    fieldWithPath("body.complaint[].co_check").type(JsonFieldType.BOOLEAN).description("건의사항 답변여부"),
                    fieldWithPath("body.complaint[].co_reply").type(JsonFieldType.STRING).description("건의사항 답변").optional(),
                    fieldWithPath("body.complaint[].co_re_create_dt").type(JsonFieldType.STRING).description("건의사항 답변 시간").optional()
                )
            )
        )
        .andExpect(status().isOk());

    }

    @Test
    public void qaInsert() throws Exception{

        String co_title = "TEST__" + LocalDateTime.now().toString() + System.currentTimeMillis();

        String jsonRQ = """
            {
                "co_title" : "%s",
                "co_contents" : "안방에서 불이나고 있어요"
            }
        """.formatted(co_title);

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            post(this.BASE_URL + "/qa/insert")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "service-center/qa/insert",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("co_title").type(JsonFieldType.STRING).description("등록할 건의사항 제목"),
                    fieldWithPath("co_contents").type(JsonFieldType.STRING).description("등록할 건의사항 내용")
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값 [성공 : ture, 실패 : false]")
                )
            )
        )
        .andExpect(status().isOk());

        svcenterMapper.deleteComplaintByTitle(co_title);
    }

    @Test
    public void qa() throws Exception{
        int pageNum = 0;

        this.mockMvc.perform(
            get(this.BASE_URL + "/qa")
            .contextPath("/api")
            .param("pageNum", String.valueOf(pageNum))
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "service-center/qa",
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("pageNum").description("페이지 번호").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.complaint").type(JsonFieldType.ARRAY).description("건의사항 리스트").optional(),
                    fieldWithPath("body.complaint[].co_pk_conum").type(JsonFieldType.NUMBER).description("건의사항 일련번호"),
                    fieldWithPath("body.complaint[].co_fk_id").type(JsonFieldType.STRING).description("건의사항 작성자"),
                    fieldWithPath("body.complaint[].co_title").type(JsonFieldType.STRING).description("건의사항 제목"),
                    fieldWithPath("body.complaint[].co_contents").type(JsonFieldType.STRING).description("건의사항 내용"),
                    fieldWithPath("body.complaint[].co_create_dt").type(JsonFieldType.STRING).description("건의사항 생성일시"),
                    fieldWithPath("body.complaint[].co_check").type(JsonFieldType.BOOLEAN).description("건의사항 답변여부"),
                    fieldWithPath("body.complaint[].co_reply").type(JsonFieldType.STRING).description("건의사항 답변").optional(),
                    fieldWithPath("body.complaint[].co_re_create_dt").type(JsonFieldType.STRING).description("건의사항 답변 시간").optional()
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void noticeDetail() throws Exception{

        int n_pk_num = 1;

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get(this.BASE_URL + "/notice/{n_pk_num}", n_pk_num)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "service-center/notice/detail",
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("n_pk_num").description("공지사항 일련번호").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.n_pk_num").type(JsonFieldType.NUMBER).description("공지사항 일련번호"),
                    fieldWithPath("body.n_title").type(JsonFieldType.STRING).description("공지사항 제목"),
                    fieldWithPath("body.n_contents").type(JsonFieldType.STRING).description("공지사항 내용"),
                    fieldWithPath("body.n_views").type(JsonFieldType.NUMBER).description("공지사항 조회수"),
                    fieldWithPath("body.n_date").type(JsonFieldType.STRING).description("공지사항 생성일시")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void notice() throws Exception{

        int pageNum = 0;

        this.mockMvc.perform(
            get(this.BASE_URL + "/notice")
            .contextPath("/api")
            .param("pageNum", String.valueOf(pageNum))
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "service-center/notice",
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("pageNum").description("페이지 번호").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.notice").type(JsonFieldType.ARRAY).description("공지사항 리스트"),
                    fieldWithPath("body.notice[].n_pk_num").type(JsonFieldType.NUMBER).description("공지사항 일련번호"),
                    fieldWithPath("body.notice[].n_title").type(JsonFieldType.STRING).description("공지사항 제목"),
                    fieldWithPath("body.notice[].n_contents").type(JsonFieldType.STRING).description("공지사항 내용"),
                    fieldWithPath("body.notice[].n_views").type(JsonFieldType.NUMBER).description("공지사항 조회수"),
                    fieldWithPath("body.notice[].n_date").type(JsonFieldType.STRING).description("공지사항 생성일시")
                )
            )
        )
        .andExpect(status().isOk());
    }


}
