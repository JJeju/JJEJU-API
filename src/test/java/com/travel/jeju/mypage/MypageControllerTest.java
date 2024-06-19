package com.travel.jeju.mypage;

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

import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.BaseTest;
import com.travel.jeju.model.member.TokenModel;
import com.travel.jeju.model.travel.TravelInsertRQ;
import com.travel.jeju.model.travel.TravelInsertRS;
import com.travel.jeju.service.TripService;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
@Transactional
public class MypageControllerTest extends BaseTest {

    @Autowired
    private TripService tripService;

    private final String BASE_URL = "/api/mypage";


    @Test
    public void favoriteDelete() throws Exception{
        String fa_pk_num = "50000";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete(this.BASE_URL + "/favorite/delete/{fa_pk_num}", fa_pk_num)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/favorite/delete",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                pathParameters(
                    parameterWithName("fa_pk_num").description("삭제할 즐겨찾기 아이디[고유값]")
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
    public void travel() throws Exception{

        Boolean tr_tf = false; 

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/travel")
            .param("tr_tf", tr_tf.toString())
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                    "mypage/travel",
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                    ),
                    queryParameters(
                        parameterWithName("tr_tf").description("여정 완료여부").optional()
                    ),  
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                        fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                        fieldWithPath("body.travel").type(JsonFieldType.ARRAY).description("여정 리스트").optional(),
                        fieldWithPath("body.travel[].tr_pk_num").type(JsonFieldType.NUMBER).description("여정 일련번호"),
                        fieldWithPath("body.travel[].tr_fk_id").type(JsonFieldType.STRING).description("여정을 생성한 회원 아이디"),
                        fieldWithPath("body.travel[].tr_title").type(JsonFieldType.STRING).description("여정 제목"),
                        fieldWithPath("body.travel[].tr_in").type(JsonFieldType.STRING).description("여정 시작일"),
                        fieldWithPath("body.travel[].tr_out").type(JsonFieldType.STRING).description("여정 종료일"),
                        fieldWithPath("body.travel[].tr_relationship").type(JsonFieldType.STRING).description("여정 동행정보"),
                        fieldWithPath("body.travel[].tr_tf").type(JsonFieldType.BOOLEAN).description("여정 완료여부")
                )
            )
        )
        .andExpect(status().isOk());

    }

    @Test
    public void travleUpdateComplate() throws Exception{

        TokenModel tokenModel = this.getUserToken();

        TravelInsertRQ insertRQ = new TravelInsertRQ();
        insertRQ.setTr_title("테스트 여행제목");
        insertRQ.setTr_in(LocalDate.now().plusDays(1));
        insertRQ.setTr_out(LocalDate.now().plusDays(3));
        insertRQ.setTr_relationship("가족");

        TravelInsertRS insertRS = tripService.travelInsert(insertRQ);

        String jsonRQ = """
            {
                "tr_pk_num" : %s
            }
        """.formatted(insertRS.getCreateTravelPK());

        this.mockMvc.perform(
            put(this.BASE_URL + "/travel/update/complate")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/travel/update/complate",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("tr_pk_num").type(JsonFieldType.NUMBER).description("수정할 생년월일").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());

    }
    
    @Test
    public void travelDelete() throws Exception{
        String tr_pk_num = "50000";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete(this.BASE_URL + "/travel/delete/{tr_pk_num}", tr_pk_num)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/travel/delete",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                pathParameters(
                    parameterWithName("tr_pk_num").description("삭제할 여정아이디[고유값]")
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
    public void travelDetail() throws Exception{
        int tr_pk_num = 4;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get(this.BASE_URL + "/travel/{tr_pk_num}", tr_pk_num)
            .contextPath("/api")
            // .param("file_idx", String.valueOf(file_idx))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/travel/detail",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                pathParameters(
                    parameterWithName("tr_pk_num").description("상세보기할 여정 일련번호")
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값").optional(),
                    fieldWithPath("body.price").type(JsonFieldType.NUMBER).description("여정에서 사용한 예상금액"),
                    fieldWithPath("body.event").type(JsonFieldType.ARRAY).description("결과 값").optional(),
                    fieldWithPath("body.event[].title").type(JsonFieldType.STRING).description("업체 아이템 이름"),
                    fieldWithPath("body.event[].start").type(JsonFieldType.STRING).description("업체 아이템 시작날짜"),
                    fieldWithPath("body.event[].end").type(JsonFieldType.STRING).description("업체 아이템 끝날짜"),
                    fieldWithPath("body.event[].cnum").type(JsonFieldType.NUMBER).description("업체 일련번호"),
                    


                    fieldWithPath("body.travel").type(JsonFieldType.OBJECT).description("여정 정보").optional(),
                    fieldWithPath("body.travel.tr_pk_num").type(JsonFieldType.NUMBER).description("여정 일련번호"),
                    fieldWithPath("body.travel.tr_fk_id").type(JsonFieldType.STRING).description("여정을 만든 회원정보"),
                    fieldWithPath("body.travel.tr_title").type(JsonFieldType.STRING).description("여정 타이틀"),
                    fieldWithPath("body.travel.tr_in").type(JsonFieldType.STRING).description("여정 시작일"),
                    fieldWithPath("body.travel.tr_out").type(JsonFieldType.STRING).description("여정 종료일"),
                    fieldWithPath("body.travel.tr_relationship").type(JsonFieldType.STRING).description("여정 동행정보"),
                    fieldWithPath("body.travel.tr_tf").type(JsonFieldType.BOOLEAN).description("여정 완료여부")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void reviewDelete() throws Exception{
        String rv_pk_num = "50000";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            delete(this.BASE_URL + "/review/delete?r_num=" + rv_pk_num)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/review/delete",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                queryParameters(
                    parameterWithName("r_num").description("삭제할 리뷰아이디[고유값]")
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
    public void memberPasswordUpdate() throws Exception{

        String jsonRQ = """
            {
                "password" : "1234",
                "new_password" : "1234"
            }
        """;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            put(this.BASE_URL + "/member/password/update")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/member/password/update",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("password").type(JsonFieldType.STRING).description("확인할 암호"),
                    fieldWithPath("new_password").type(JsonFieldType.STRING).description("변경할 암호")
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
    public void memberPasswordCheck() throws Exception{
        String jsonRQ = """
            {
                "m_password" : "1234"
            }
        """;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            post(this.BASE_URL + "/member/password/check")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/member/password/check",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("m_password").type(JsonFieldType.STRING).description("확인할 암호")
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
    public void memberUpdate() throws Exception{

        String jsonRQ = """
            {
                "m_birth" : "19990907",
                "m_email" : "lkd912526@gmail.com"
            }                
        """;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            put(this.BASE_URL + "/member/update")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/member/update",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("m_birth").type(JsonFieldType.STRING).description("수정할 생년월일").optional(),
                    fieldWithPath("m_email").type(JsonFieldType.STRING).description("수정할 이메일").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void blogDeletePicture() throws Exception{
        int file_idx = 53;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete(this.BASE_URL + "/blog/delete/picture/{file_idx}", file_idx)
            .contextPath("/api")
            // .param("file_idx", String.valueOf(file_idx))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/blog/delete/picture",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                pathParameters(
                    parameterWithName("file_idx").description("후기글 삭제할 사진 일련번호")
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void blogAddPicture() throws Exception{

        MockMultipartFile b_img = new MockMultipartFile("b_img", "추가할 후기글 사진[여러장 가능].png","image/png", "<<png data>>".getBytes());
        MockMultipartFile metadata = new MockMultipartFile("metadata", "", "multipart/form-data", """
            {
                "b_pk_num": 1
            }
        """.getBytes());

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            multipart(this.BASE_URL + "/blog/add/picture")
            .file(b_img)
            .file(metadata)
            .contextPath("/api")
            .param("b_pk_num", "1")
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
            .characterEncoding("UTF-8")
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/blog/add/picture",
                preprocessRequest(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestParts(
                    partWithName("b_img").description("후기글에 추가할 이미지").optional(),
                    partWithName("metadata").description("후기글 추가할 사진데이터 폼")
                ),
                requestPartFields(
                    "metadata",
                    fieldWithPath("b_pk_num").type(JsonFieldType.NUMBER).description("후기글 일련번호")
                ),
                requestPartBody("metadata"),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());

    }

    @Test
    public void blogDelete() throws Exception{
        int b_pk_num = 3000;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete(this.BASE_URL + "/blog/delete/{b_pk_num}", b_pk_num)
            .contextPath("/api")
            // .param("file_idx", String.valueOf(file_idx))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/blog/delete",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                pathParameters(
                    parameterWithName("b_pk_num").description("후기글 삭제할 일련번호")
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());
    }
    
    @Test
    public void blogUpdate() throws Exception{

        MockMultipartFile b_img = new MockMultipartFile("b_img", "변경할 후기글 대표사진.png","image/png", "<<png data>>".getBytes());
        MockMultipartFile metadata = new MockMultipartFile("metadata", "", "multipart/form-data",
        """
            {
                "b_pk_num": "1",
                "b_title":"수정 ㅋㅋ",
                "b_contents":"수정한 후기글 ㅋㅋ"
            }
        """.getBytes());


        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            multipart(HttpMethod.PUT, this.BASE_URL + "/blog/update")
            .file(b_img)
            .file(metadata)
            .contextPath("/api")
            .param("b_pk_num", "1")
            .param("b_title", "수정 ㅋㅋ")
            .param("b_contents", "수정한 후기글 ㅋㅋ")
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
            .characterEncoding("UTF-8")
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/blog/update",
                preprocessRequest(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestParts(
                    partWithName("b_img").description("후기글 변경할 대표이미지").optional(),
                    partWithName("metadata").description("후기글 변경할 폼")
                ),
                requestPartFields(
                    "metadata",
                    fieldWithPath("b_pk_num").description("후기글 일련번호"),
                    fieldWithPath("b_title").description("후기글 제목"),
                    fieldWithPath("b_contents").description("후기글 내용")
                ),
                requestPartBody("metadata"),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void blogCreate() throws Exception{
        MockMultipartFile mainFile = new MockMultipartFile("mainFile", "후기글 대표 파일.png","image/png", "<<png data>>".getBytes());
        MockMultipartFile files = new MockMultipartFile("files", "후기 사진들[여러장 가능].png","image/png", "<<png data>>".getBytes());
        MockMultipartFile metadata = new MockMultipartFile("metadata", "", "multipart/form-data",
        """
            {
                "tnum" : 1,
                "title" : "더더욱 ~~ 즐거운 여행이였다~",
                "cost" : 200000,
                "contents" : "즐거운 여행이였던 내용이였다~ ㅋㅋ",
                "star" : 3,
                "publicCheck" : false
            }
        """.getBytes());


        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            multipart(this.BASE_URL + "/blog/create")
            .file(mainFile)
            .file(files)
            .file(metadata)
            .contextPath("/api")
            .param("tnum", "1")
            .param("title", "더더욱 ~~ 즐거운 여행이였다~")
            .param("cost", "200000")
            .param("contents", "즐거운 여행이였던 내용이였다~ ㅋㅋ")
            .param("star", "3")
            .param("publicCheck", "false")
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
            .characterEncoding("UTF-8")
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/blog/create",
                preprocessRequest(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestParts(
                    partWithName("mainFile").description("후기글 대표이미지").optional(),
                    partWithName("files").description("후기글 이미지").optional(),
                    partWithName("metadata").description("후기글 폼")
                ),
                requestPartFields(
                    "metadata",
                    fieldWithPath("tnum").type(JsonFieldType.NUMBER).description("여정 일련번호"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("후기글 제목"),
                    fieldWithPath("cost").type(JsonFieldType.NUMBER).description("후기 비용"),
                    fieldWithPath("contents").type(JsonFieldType.STRING).description("후기 내용"),
                    fieldWithPath("star").type(JsonFieldType.NUMBER).description("여행 별점 [만족도]"),
                    fieldWithPath("publicCheck").type(JsonFieldType.BOOLEAN).description("공개여부")
                ),
                requestPartBody("metadata"),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void blogDetail() throws Exception{

        long b_pk_bnum = 1;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get(this.BASE_URL + "/blog/{b_pk_bnum}",b_pk_bnum)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/blog_detail",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                pathParameters(
                    parameterWithName("b_pk_bnum").description("후기글 일련번호").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.blog").type(JsonFieldType.OBJECT).description("후기글 리스트"),
                    fieldWithPath("body.blog.b_pk_num").type(JsonFieldType.NUMBER).description("후기글 일련번호"),
                    fieldWithPath("body.blog.b_fk_tnum").type(JsonFieldType.NUMBER).description("후기글의 여행 일련번호"),
                    fieldWithPath("body.blog.b_fk_id").type(JsonFieldType.STRING).description("후기글 작성자 아이디"),
                    fieldWithPath("body.blog.b_title").type(JsonFieldType.STRING).description("후기글 제목"),
                    fieldWithPath("body.blog.b_cost").type(JsonFieldType.NUMBER).description("후기글 여행의 총 비용"),
                    fieldWithPath("body.blog.b_contents").type(JsonFieldType.STRING).description("후기글 내용"),
                    fieldWithPath("body.blog.file_group_no").type(JsonFieldType.NUMBER).description("후기글 파일 그룹번호"),
                    fieldWithPath("body.blog.b_public_check").type(JsonFieldType.BOOLEAN).description("후기글 공개여부"),
                    fieldWithPath("body.blog.b_create_dt").type(JsonFieldType.STRING).description("후기글 생성일시"),
                    fieldWithPath("body.blog.b_star").type(JsonFieldType.NUMBER).description("후기글 별점[만족도]"),

                    fieldWithPath("body.mainFile").type(JsonFieldType.OBJECT).description("후기글 메인 이미지").optional(),
                    fieldWithPath("body.mainFile.idx").type(JsonFieldType.NUMBER).description("후기글 메인 이미지 일련번호").optional(),
                    fieldWithPath("body.mainFile.file_group_no").type(JsonFieldType.NUMBER).description("후기글 메인 이미지 그룹번호").optional(),
                    fieldWithPath("body.mainFile.url").type(JsonFieldType.STRING).description("후기글 메인 이미지 URL").optional(),
                    fieldWithPath("body.mainFile.file_ori_nm").type(JsonFieldType.STRING).description("후기글 메인 이미지 원래이름").optional(),
                    fieldWithPath("body.mainFile.file_sys_nm").type(JsonFieldType.STRING).description("후기글 메인 이미지 저장용 시스템 파일명").optional(),
                    fieldWithPath("body.mainFile.extension").type(JsonFieldType.STRING).description("후기글 메인 이미지 확장자[.jpeg, .png]").optional(),
                    fieldWithPath("body.mainFile.description").type(JsonFieldType.STRING).description("후기글 메인 이미지 추가설명").optional(),
                    fieldWithPath("body.mainFile.file_size").type(JsonFieldType.NUMBER).description("후기글 메인 이미지 사이즈").optional(),
                    fieldWithPath("body.mainFile.file_size_unit").type(JsonFieldType.STRING).description("후기글 메인 이미지 사이즈 단위").optional(),
                    fieldWithPath("body.mainFile.create_dt").type(JsonFieldType.STRING).description("후기글 메인 이미지 생성일시").optional(),

                    fieldWithPath("body.files").type(JsonFieldType.ARRAY).description("후기글 이미지").optional(),
                    fieldWithPath("body.files[].idx").type(JsonFieldType.NUMBER).description("후기글 이미지 일련번호").optional(),
                    fieldWithPath("body.files[].file_group_no").type(JsonFieldType.NUMBER).description("후기글 이미지 그룹번호").optional(),
                    fieldWithPath("body.files[].url").type(JsonFieldType.STRING).description("후기글 이미지 URL").optional(),
                    fieldWithPath("body.files[].file_ori_nm").type(JsonFieldType.STRING).description("후기글 이미지 원래이름").optional(),
                    fieldWithPath("body.files[].file_sys_nm").type(JsonFieldType.STRING).description("후기글 이미지 저장용 시스템 파일명").optional(),
                    fieldWithPath("body.files[].extension").type(JsonFieldType.STRING).description("후기글 이미지 확장자[.jpeg, .png]").optional(),
                    fieldWithPath("body.files[].description").type(JsonFieldType.STRING).description("후기글 이미지 추가설명").optional(),
                    fieldWithPath("body.files[].file_size").type(JsonFieldType.NUMBER).description("후기글 이미지 사이즈").optional(),
                    fieldWithPath("body.files[].file_size_unit").type(JsonFieldType.STRING).description("후기글 이미지 사이즈 단위").optional(),
                    fieldWithPath("body.files[].create_dt").type(JsonFieldType.STRING).description("후기글 이미지 생성일시").optional(),

                    fieldWithPath("body.planList").type(JsonFieldType.ARRAY).description("후기글의 여정 코스 배열"),
                    fieldWithPath("body.planList[].day").type(JsonFieldType.STRING).description("여정 일자"),
                    fieldWithPath("body.planList[].dayPlanList").type(JsonFieldType.ARRAY).description("여정 일자별 코스").optional(),
                    fieldWithPath("body.planList[].dayPlanList[].tp_pk_num").type(JsonFieldType.NUMBER).description("여정 일자별 코스 일련번호"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_tnum").type(JsonFieldType.NUMBER).description("여정 일련번호"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info").type(JsonFieldType.OBJECT).description("여정 코스 관련 업체정보"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_pk_num").type(JsonFieldType.NUMBER).description("영업장 일련번호"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_fk_id").type(JsonFieldType.STRING).description("영업장 업주 아이디"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_category").type(JsonFieldType.STRING).description("영업장 종류 [ 숙박, 레저, 식당 ]"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_condition").type(JsonFieldType.STRING).description("영업장 노출여부"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_check").type(JsonFieldType.STRING).description("영업장 승인여부"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_img").type(JsonFieldType.STRING).description("영업장 대문사진 URL"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_type").type(JsonFieldType.STRING).description("영업장 종목"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_lat").type(JsonFieldType.NUMBER).description("영업장 위도"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_lon").type(JsonFieldType.NUMBER).description("영업장 경도"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_file_group_no").type(JsonFieldType.NUMBER).description("영업장 파일 그룹번호"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_item_category").type(JsonFieldType.STRING).description("여정 계획요소 종류"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_plan_start_date_time").type(JsonFieldType.STRING).description("여정 계획요소 시작시간"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_plan_end_date_time").type(JsonFieldType.STRING).description("여정 계획요소 종료시간"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_rm").type(JsonFieldType.STRING).description("여정 계획요소 메모"),
                    fieldWithPath("body.planList[].dayPlanList[].create_dt").type(JsonFieldType.STRING).description("여정 계획요소 생성일시"),
                    fieldWithPath("body.planList[].dayPlanList[].update_dt").type(JsonFieldType.STRING).description("여정 계획요소 수정일시").optional(),

                    fieldWithPath("body.travelroute").type(JsonFieldType.OBJECT).description("후기글의 여정 객체"),
                    fieldWithPath("body.travelroute.tr_pk_num").type(JsonFieldType.NUMBER).description("여정 일련번호"),
                    fieldWithPath("body.travelroute.tr_fk_id").type(JsonFieldType.STRING).description("회원 아이디"),
                    fieldWithPath("body.travelroute.tr_title").type(JsonFieldType.STRING).description("여정 제목"),
                    fieldWithPath("body.travelroute.tr_in").type(JsonFieldType.STRING).description("여정 시작날짜"),
                    fieldWithPath("body.travelroute.tr_out").type(JsonFieldType.STRING).description("여정 종료날짜"),
                    fieldWithPath("body.travelroute.tr_relationship").type(JsonFieldType.STRING).description("여정 동행정보"),
                    fieldWithPath("body.travelroute.tr_tf").type(JsonFieldType.BOOLEAN).description("여정 완료여부")
                )
            )
        )
        .andExpect(status().isOk());

    }    
    
    @Test
    public void blog() throws Exception{
        
        int pageNum = 1;
        String title = "진짜";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/blog")
            .contextPath("/api")
            .param("pageNum", String.valueOf(pageNum))
            .param("title", title)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/blog",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                queryParameters(
                    parameterWithName("title").description("검색할 후기글 제목").optional(),
                    parameterWithName("pageNum").description("페이지 번호").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.blog").type(JsonFieldType.ARRAY).description("후기글 리스트"),
                    fieldWithPath("body.blog[].b_pk_num").type(JsonFieldType.NUMBER).description("후기글 일련번호").optional(),
                    fieldWithPath("body.blog[].b_fk_tnum").type(JsonFieldType.NUMBER).description("후기글의 여행 일련번호").optional(),
                    fieldWithPath("body.blog[].b_fk_id").type(JsonFieldType.STRING).description("후기글 작성자 아이디").optional(),
                    fieldWithPath("body.blog[].b_title").type(JsonFieldType.STRING).description("후기글 제목").optional(),
                    fieldWithPath("body.blog[].b_cost").type(JsonFieldType.NUMBER).description("후기글 여행의 총 비용").optional(),
                    fieldWithPath("body.blog[].b_contents").type(JsonFieldType.STRING).description("후기글 내용").optional(),

                    fieldWithPath("body.blog.b_img").type(JsonFieldType.OBJECT).description("후기글 대표사진").optional(),
                    fieldWithPath("body.blog.b_img.idx").type(JsonFieldType.NUMBER).description("후기글 메인 이미지 일련번호").optional(),
                    fieldWithPath("body.blog.b_img.file_group_no").type(JsonFieldType.NUMBER).description("후기글 메인 이미지 그룹번호").optional(),
                    fieldWithPath("body.blog.b_img.url").type(JsonFieldType.STRING).description("후기글 메인 이미지 URL").optional(),
                    fieldWithPath("body.blog.b_img.file_ori_nm").type(JsonFieldType.STRING).description("후기글 메인 이미지 원래이름").optional(),
                    fieldWithPath("body.blog.b_img.file_sys_nm").type(JsonFieldType.STRING).description("후기글 메인 이미지 저장용 시스템 파일명").optional(),
                    fieldWithPath("body.blog.b_img.extension").type(JsonFieldType.STRING).description("후기글 메인 이미지 확장자[.jpeg, .png]").optional(),
                    fieldWithPath("body.blog.b_img.description").type(JsonFieldType.STRING).description("후기글 메인 이미지 추가설명").optional(),
                    fieldWithPath("body.blog.b_img.file_size").type(JsonFieldType.NUMBER).description("후기글 메인 이미지 사이즈").optional(),
                    fieldWithPath("body.blog.b_img.file_size_unit").type(JsonFieldType.STRING).description("후기글 메인 이미지 사이즈 단위").optional(),
                    fieldWithPath("body.blog.b_img.create_dt").type(JsonFieldType.STRING).description("후기글 메인 이미지 생성일시").optional()
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void review() throws Exception {
        String category = "레저";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/review")
            .contextPath("/api")
            .param("category", category)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/review",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                queryParameters(
                    parameterWithName("category").description("리뷰 조회할 영업장 카테고리[숙박, 레저, 식당] , 해당 값이 없으면 전체조회").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.review").type(JsonFieldType.ARRAY).description("리뷰 리스트"),
                    fieldWithPath("body.review[].rv_pk_num").type(JsonFieldType.NUMBER).description("리뷰 일련번호").optional(),
                    fieldWithPath("body.review[].rv_fk_cnum").type(JsonFieldType.NUMBER).description("리뷰 작성한 업체의 일련 번호").optional(),
                    fieldWithPath("body.review[].rv_fk_id").type(JsonFieldType.STRING).description("리뷰 작성한 유저 ID").optional(),
                    fieldWithPath("body.review[].rv_date").type(JsonFieldType.STRING).description("리뷰 작성일시").optional(),
                    fieldWithPath("body.review[].rv_contents").type(JsonFieldType.STRING).description("리뷰 내용").optional(),
                    fieldWithPath("body.review[].rv_star").type(JsonFieldType.NUMBER).description("별점").optional(),
                    fieldWithPath("body.review[].c_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호").optional(),
                    fieldWithPath("body.review[].c_name").type(JsonFieldType.STRING).description("영업장 이름").optional(),
                    fieldWithPath("body.review[].c_category").type(JsonFieldType.STRING).description("영업장 카테고리").optional()
                )
            )
        )
        .andExpect(status().isOk());
    }
    
    @Test
    public void favorite() throws Exception{

        String category = "숙박";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/favorite")
            .contextPath("/api")
            .param("category", category)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/favorite",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                queryParameters(
                    parameterWithName("category").description("즐겨찾기 영업장 카테고리[숙박, 레저, 식당]")
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.favorite").type(JsonFieldType.ARRAY).description("즐겨찾기 내역"),
                    fieldWithPath("body.favorite[].fa_pk_num").type(JsonFieldType.STRING).description("즐겨찾기 일련번호").optional(),
                    fieldWithPath("body.favorite[].cnum").type(JsonFieldType.NUMBER).description("영업장 사업자번호").optional(),
                    fieldWithPath("body.favorite[].name").type(JsonFieldType.STRING).description("영업장 이름").optional(),
                    fieldWithPath("body.favorite[].img").type(JsonFieldType.NUMBER).description("영업장 대문사진 URL").optional(),
                    fieldWithPath("body.favorite[].category").type(JsonFieldType.STRING).description("영업장 카테고리").optional()
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void deal() throws Exception{

        String category = "숙박";
        String name = "제주";
        int pageNum = 1;
        
        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/deal")
            .contextPath("/api")
            .param("category", category)
            .param("name", name)
            .param("pageNum", String.valueOf(pageNum))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/deal",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                queryParameters(
                    parameterWithName("category").description("장바구니 결제 영업장 카테고리[숙박, 레저]").optional(),
                    parameterWithName("name").description("영업장 이름").optional(),
                    parameterWithName("pageNum").description("페이지 번호").optional()
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.deal").type(JsonFieldType.ARRAY).description("거래내역").optional(),
                    fieldWithPath("body.payment[].bk_fk_cnum").type(JsonFieldType.NUMBER).description("상품 영업장 일련 번호").optional(),
                    fieldWithPath("body.payment[].bk_fk_num").type(JsonFieldType.NUMBER).description("상품 번호").optional(),
                    fieldWithPath("body.payment[].bk_goods").type(JsonFieldType.STRING).description("상품명").optional(),
                    fieldWithPath("body.payment[].bk_price").type(JsonFieldType.NUMBER).description("상품 금액").optional(),
                    fieldWithPath("body.payment[].bk_name").type(JsonFieldType.STRING).description("상품 예약자 이름").optional(),
                    fieldWithPath("body.payment[].bk_paych").type(JsonFieldType.BOOLEAN).description("장바구니 상품 결제 여부").optional(),
                    fieldWithPath("body.payment[].bk_paytime").type(JsonFieldType.STRING).description("장바구니 상품 결제 시간").optional(),
                    fieldWithPath("body.payment[].bk_fk_id").type(JsonFieldType.STRING).description("장바구니 매핑된 회원아이디").optional(),
                    fieldWithPath("body.payment[].c_category").type(JsonFieldType.STRING).description("상품 타입[숙박,레저]").optional(),
                    fieldWithPath("body.payment[].c_name").type(JsonFieldType.STRING).description("영업장 이름").optional(),
                    fieldWithPath("body.payment[].pageNum").type(JsonFieldType.NUMBER).description("페이지번호").optional(),
                    fieldWithPath("body.payment[].listCnt").type(JsonFieldType.NUMBER).description("총 조회건 수").optional()
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void mypageHome() throws Exception {

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/home")
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "mypage/home",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.payment").type(JsonFieldType.ARRAY).description("최근 거래내역").optional(),
                    fieldWithPath("body.payment[].bk_fk_cnum").type(JsonFieldType.NUMBER).description("상품 영업장 일련 번호").optional(),
                    fieldWithPath("body.payment[].bk_fk_num").type(JsonFieldType.NUMBER).description("상품 번호").optional(),
                    fieldWithPath("body.payment[].bk_goods").type(JsonFieldType.STRING).description("상품명").optional(),
                    fieldWithPath("body.payment[].bk_price").type(JsonFieldType.NUMBER).description("상품 금액").optional(),
                    fieldWithPath("body.payment[].bk_name").type(JsonFieldType.STRING).description("상품 예약자 이름").optional(),
                    fieldWithPath("body.payment[].bk_paych").type(JsonFieldType.BOOLEAN).description("장바구니 상품 결제 여부").optional(),
                    fieldWithPath("body.payment[].bk_paytime").type(JsonFieldType.STRING).description("장바구니 상품 결제 시간").optional(),
                    fieldWithPath("body.payment[].bk_fk_id").type(JsonFieldType.STRING).description("장바구니 매핑된 회원아이디").optional(),
                    fieldWithPath("body.payment[].c_category").type(JsonFieldType.STRING).description("상품 타입[숙박,레저]").optional(),
                    fieldWithPath("body.payment[].c_name").type(JsonFieldType.STRING).description("영업장 이름").optional(),
                    fieldWithPath("body.payment[].pageNum").type(JsonFieldType.NUMBER).description("페이지번호").optional(),
                    fieldWithPath("body.payment[].listCnt").type(JsonFieldType.NUMBER).description("총 조회건 수").optional(),

                    fieldWithPath("body.blog").type(JsonFieldType.ARRAY).description("지난 여행 후기").optional(),
                    fieldWithPath("body.blog[].b_pk_num").type(JsonFieldType.NUMBER).description("후기글 일련번호").optional(),
                    fieldWithPath("body.blog[].b_fk_tnum").type(JsonFieldType.NUMBER).description("후기 남기는 여행 일련번호").optional(),
                    fieldWithPath("body.blog[].b_fk_id").type(JsonFieldType.STRING).description("후기 작성자 아이디").optional(),
                    fieldWithPath("body.blog[].b_img").type(JsonFieldType.STRING).description("후기 이미지").optional(),
                    fieldWithPath("body.blog[].tr_title").type(JsonFieldType.STRING).description("여행 제목").optional(),
                    fieldWithPath("body.blog[].tr_in").type(JsonFieldType.STRING).description("여행 시작일").optional(),
                    fieldWithPath("body.blog[].tr_out").type(JsonFieldType.STRING).description("여행 끝일").optional(),
                    fieldWithPath("body.blog[].tr_tf").type(JsonFieldType.BOOLEAN).description("여행 완료여부").optional()   
                )
            )
        )
        .andExpect(status().isOk());
    }


}
