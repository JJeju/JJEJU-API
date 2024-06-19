package com.travel.jeju.trip;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.util.ExceptionCollector;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.BaseTest;
import com.travel.jeju.mapper.TravelrouteMapper;
import com.travel.jeju.model.member.TokenModel;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
public class TripControllerTest extends BaseTest{

    @Autowired
    private TravelrouteMapper travelrouteMapper;

    private final String BASE_URL = "/api/travel";

    @Test
    public void travelPlanSelect() throws Exception{

        String tr_pk_num = "1";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get(this.BASE_URL + "/plan/select/{tr_pk_num}", tr_pk_num)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "travel/plan/select",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                pathParameters(
                    parameterWithName("tr_pk_num").description("여행계획 요소를 가져올 여정 일련번호")
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값").optional(),
                    fieldWithPath("body.planList").type(JsonFieldType.ARRAY).description("여정의 일정에 맞는 계획요소 리스트 [데이터 뿌려주기 용도]"),
                    fieldWithPath("body.planList[].day").type(JsonFieldType.STRING).description("보여줄 계획요소의 여정 일자"),
                    fieldWithPath("body.planList[].dayPlanList").type(JsonFieldType.ARRAY).description("일자의 계획요소 리스트").optional(),
                    fieldWithPath("body.planList[].dayPlanList[].tp_pk_num").type(JsonFieldType.NUMBER).description("계획요소 일련번호"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_tnum").type(JsonFieldType.NUMBER).description("계획요소가 속한 여정일련번호"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info").type(JsonFieldType.OBJECT).description("영업장 정보"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_pk_num").type(JsonFieldType.NUMBER).description("영업장 일련번호"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_fk_id").type(JsonFieldType.STRING).description("영업장 사업자 아이디"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_category").type(JsonFieldType.STRING).description("영업장 종류 [ 숙박, 레저, 식당 ]"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_condition").type(JsonFieldType.STRING).description("영업장 노출여부"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_check").type(JsonFieldType.STRING).description("영업장 승인여부"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_img").type(JsonFieldType.STRING).description("영업장 대문사진"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_type").type(JsonFieldType.STRING).description("영업장 주 종목"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_lat").type(JsonFieldType.NUMBER).description("영업장 위도"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_lon").type(JsonFieldType.NUMBER).description("영업장 경도"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_fk_company_info.c_file_group_no").type(JsonFieldType.NUMBER).description("영업장 파일 그룹번호"),

                    fieldWithPath("body.planList[].dayPlanList[].tp_item_category").type(JsonFieldType.STRING).description("영업장 종류 [ 숙박, 레저 인지 .. ]"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_plan_start_date_time").type(JsonFieldType.STRING).description("계획요소 시작 날짜시간"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_plan_end_date_time").type(JsonFieldType.STRING).description("계획요소 끝 날짜시간"),
                    fieldWithPath("body.planList[].dayPlanList[].tp_rm").type(JsonFieldType.STRING).description("계획요소 메모").optional(),
                    fieldWithPath("body.planList[].dayPlanList[].create_dt").type(JsonFieldType.STRING).description("계획요소 생성일시"),
                    fieldWithPath("body.planList[].dayPlanList[].update_dt").type(JsonFieldType.STRING).description("계획요소 수정일시").optional(),

                    fieldWithPath("body.planItemList").type(JsonFieldType.ARRAY).description("여정의 계획요소 리스트"),
                    fieldWithPath("body.planItemList[].tp_pk_num").type(JsonFieldType.NUMBER).description("계획요소 일련번호"),
                    fieldWithPath("body.planItemList[].tp_fk_tnum").type(JsonFieldType.NUMBER).description("계획요소가 속한 여정일련번호"),
                    fieldWithPath("body.planItemList[].tp_fk_company_num").type(JsonFieldType.NUMBER).description("아이템 일련번호"),
                    fieldWithPath("body.planItemList[].tp_item_category").type(JsonFieldType.STRING).description("아이템 종류 [ 숙박, 레저 인지 .. ]"),
                    fieldWithPath("body.planItemList[].tp_plan_start_date_time").type(JsonFieldType.STRING).description("계획요소 시작 날짜시간"),
                    fieldWithPath("body.planItemList[].tp_plan_end_date_time").type(JsonFieldType.STRING).description("계획요소 끝 날짜시간"),
                    fieldWithPath("body.planItemList[].tp_rm").type(JsonFieldType.STRING).description("계획요소 메모").optional(),
                    fieldWithPath("body.planItemList[].create_dt").type(JsonFieldType.STRING).description("계획요소 생성일시"),
                    fieldWithPath("body.planItemList[].update_dt").type(JsonFieldType.STRING).description("계획요소 수정일시").optional(),

                    fieldWithPath("body.travelroute").type(JsonFieldType.OBJECT).description("여정정보"),
                    fieldWithPath("body.travelroute.tr_pk_num").type(JsonFieldType.NUMBER).description("여정 일련번호"),
                    fieldWithPath("body.travelroute.tr_fk_id").type(JsonFieldType.STRING).description("여정 생성한 유저 아이디"),
                    fieldWithPath("body.travelroute.tr_title").type(JsonFieldType.STRING).description("여정 타이틀"),
                    fieldWithPath("body.travelroute.tr_in").type(JsonFieldType.STRING).description("여정 시작 날짜"),
                    fieldWithPath("body.travelroute.tr_out").type(JsonFieldType.STRING).description("여정 종료 날짜"),
                    fieldWithPath("body.travelroute.tr_relationship").type(JsonFieldType.STRING).description("동행여부 [ 혼자, 커플 등 .. ]"),
                    fieldWithPath("body.travelroute.tr_tf").type(JsonFieldType.BOOLEAN).description("여정 완료여부")
                    
                    
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void planUpdate() throws Exception{

        String jsonRQ = """
            {
                "updatePlanList" : [
                    {
                        "tr_pk_num" : 21,
                        "plan_start_date_time" : "2024-01-02T08:00:00",
                        "plan_end_date_time" : "2024-01-03T10:00:00",
                        "rm" : "바뀜 ㅋzz"
                    },
                    {
                        "tr_pk_num" : 22,
                        "plan_start_date_time" : "2024-01-05T08:00:00",
                        "plan_end_date_time" : "2024-01-06T10:00:00",
                        "rm" : "바뀜 ㅋ"
                    }
                ],
                "t_pk_num" : 1
            }
        """;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            put(this.BASE_URL + "/plan/update")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "travel/plan/update",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("updatePlanList").type(JsonFieldType.ARRAY).description("변경할 계획요소 리스트"),
                    fieldWithPath("updatePlanList[].tr_pk_num").type(JsonFieldType.NUMBER).description("변경할 계획요소 일련번호"),
                    fieldWithPath("updatePlanList[].plan_start_date_time").type(JsonFieldType.STRING).description("변경할 계획요소 시작날짜").optional(),
                    fieldWithPath("updatePlanList[].plan_end_date_time").type(JsonFieldType.STRING).description("변경할 계획요소 끝날짜").optional(),
                    fieldWithPath("updatePlanList[].rm").type(JsonFieldType.STRING).description("변경할 계획요소 메모").optional(),

                    fieldWithPath("t_pk_num").type(JsonFieldType.NUMBER).description("여정 일련번호")
                    
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
    @Transactional
    public void planDelete() throws Exception{

        Integer tp_pk_num = 210000000;

        
        TokenModel tokenModel = this.getAdminToken();

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete(this.BASE_URL + "/plan/delete/{tp_pk_num}", tp_pk_num)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "travel/plan/delete",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                pathParameters(
                    parameterWithName("tp_pk_num").description("삭제할 여행 계획 일련번호")
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
    @Transactional
    public void planInsert() throws Exception{
        String jsonRQ = """
            {
                "planList" : [
                    {
                        "category" : "레저",
                        "company_pk_num" : 1,
                        "plan_start_date_time" : "2024-01-03T08:00:00",
                        "plan_end_date_time" : "2024-01-03T10:00:00",
                        "rm" : ""
                    },
                    {
                        "category" : "레저",
                        "company_pk_num" : 2,
                        "plan_start_date_time" : "2024-01-07T08:00:00",
                        "plan_end_date_time" : "2024-01-07T10:00:00",
                        "rm" : ""
                    }
                ],
                "t_pk_num" : 1
            }
        """;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            post(this.BASE_URL + "/plan/insert")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "travel/plan/insert",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("planList").type(JsonFieldType.ARRAY).description("여행계획 리스트"),
                    fieldWithPath("planList[].category").type(JsonFieldType.STRING).description("여행계획요소 영업장 카테고리[숙박, 레저, 식당 등..]"),
                    fieldWithPath("planList[].company_pk_num").type(JsonFieldType.NUMBER).description("여행계획요소 영업장 번호[상품 일련 번호]"),
                    fieldWithPath("planList[].plan_start_date_time").type(JsonFieldType.STRING).description("여행계획 요소 시작일"),
                    fieldWithPath("planList[].plan_end_date_time").type(JsonFieldType.STRING).description("여행계획 요소 종료일"),
                    fieldWithPath("planList[].rm").type(JsonFieldType.STRING).description("메모요소").optional(),

                    fieldWithPath("t_pk_num").type(JsonFieldType.NUMBER).description("이행 일련번호")
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
    public void travelInsert() throws Exception{

        String tr_title = "TEST__" + System.currentTimeMillis();

        String jsonRQ = """
            {
                "tr_title" : "%s",
                "tr_in" : "2024-02-26",
                "tr_out" : "2024-03-11",
                "tr_relationship" : "혼자"
            }
        """.formatted(tr_title);

        TokenModel tokenModel = this.getAdminToken();

        this.mockMvc.perform(
            post(this.BASE_URL + "/insert")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "travel/insert",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("tr_title").type(JsonFieldType.STRING).description("여정 제목"),
                    fieldWithPath("tr_in").type(JsonFieldType.STRING).description("여정 시작일"),
                    fieldWithPath("tr_out").type(JsonFieldType.STRING).description("여정 종료일"),
                    fieldWithPath("tr_relationship").type(JsonFieldType.STRING).description("여정 동행여부")
                ),  
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.createTravelPK").type(JsonFieldType.NUMBER).description("생성된 여정일련번호")
                )
            )
        )
        .andExpect(status().isOk());

        travelrouteMapper.deleteTravelByTitle(tr_title);
    }
}
