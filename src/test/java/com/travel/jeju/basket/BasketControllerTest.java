package com.travel.jeju.basket;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.BaseTest;
import com.travel.jeju.mapper.BasketMapper;
import com.travel.jeju.model.member.TokenModel;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
@ActiveProfiles("local")
@Transactional
public class BasketControllerTest extends BaseTest{

    @Autowired
    private BasketMapper basketMapper;

    private final String BASE_URL = "/api/basket";

    // @Test
    public void deleteBasket() throws Exception {
        String trnum = "2";
        String bk_pk_num = "12";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            delete(this.BASE_URL + "/delete_basket?trnum=" + trnum + "&bk_pk_num=" + bk_pk_num)
            .contextPath("/api")
            // .param("trnum", trnum)
            // .param("bk_pk_num", bk_pk_num)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "basket/delete_basket", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                queryParameters(
                    parameterWithName("trnum").description("여행 번호"),
                    parameterWithName("bk_pk_num").description("장바구니 일련번호")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값[장바구니 삭제 후 남은 장바구니 리스트]"),

                    fieldWithPath("body.lList").type(JsonFieldType.ARRAY).description("숙박 장바구니 리스트").optional(),
                    fieldWithPath("body.lList[].bk_pk_num").type(JsonFieldType.NUMBER).description("장바구니 일련번호"),
                    fieldWithPath("body.lList[].bk_fk_tnum").type(JsonFieldType.NUMBER).description("여행 번호"),
                    fieldWithPath("body.lList[].bk_fk_cnum").type(JsonFieldType.STRING).description("상품 업체의 사업자 번호"),
                    fieldWithPath("body.lList[].bk_fk_id").type(JsonFieldType.STRING).description("장바구니 추가한 회윈 아이디"),
                    fieldWithPath("body.lList[].bk_fk_num").type(JsonFieldType.NUMBER).description("상품 일련 번호"),
                    fieldWithPath("body.lList[].bk_goods").type(JsonFieldType.STRING).description("장바구니 상품 이름"),
                    fieldWithPath("body.lList[].bk_price").type(JsonFieldType.NUMBER).description("장바구니 상품 금액"),
                    fieldWithPath("body.lList[].bk_name").type(JsonFieldType.STRING).description("장바구니 상품 예약자 이름").optional(),
                    fieldWithPath("body.lList[].bk_number").type(JsonFieldType.STRING).description("장바구니 상품 예약자 번호").optional(),
                    fieldWithPath("body.lList[].bk_carch").type(JsonFieldType.STRING).description("장바구니 상품 예약자 차량 유무").optional(),
                    fieldWithPath("body.lList[].bk_in").type(JsonFieldType.STRING).description("장바구니 상품 예약 시작 시간").optional(),
                    fieldWithPath("body.lList[].bk_out").type(JsonFieldType.STRING).description("장바구니 상품 예약 끝 시간").optional(),
                    fieldWithPath("body.lList[].bk_paych").type(JsonFieldType.STRING).description("장바구니 결제유무"),
                    fieldWithPath("body.lList[].bk_people").type(JsonFieldType.NUMBER).description("장바구니 상품 예약자 수"),
                    fieldWithPath("body.lList[].c_name").type(JsonFieldType.STRING).description("업체 명"),
                    fieldWithPath("body.lList[].bk_create_dt").type(JsonFieldType.STRING).description("장바구니 생성일시"),
                    
                    fieldWithPath("body.aList").type(JsonFieldType.ARRAY).description("레저 장바구니 리스트").optional(),
                    fieldWithPath("body.aList[].bk_pk_num").type(JsonFieldType.NUMBER).description("장바구니 일련번호"),
                    fieldWithPath("body.aList[].bk_fk_tnum").type(JsonFieldType.NUMBER).description("여행 번호"),
                    fieldWithPath("body.aList[].bk_fk_cnum").type(JsonFieldType.STRING).description("상품 업체의 사업자 번호"),
                    fieldWithPath("body.aList[].bk_fk_id").type(JsonFieldType.STRING).description("장바구니 추가한 회윈 아이디"),
                    fieldWithPath("body.aList[].bk_fk_num").type(JsonFieldType.NUMBER).description("상품 일련 번호"),
                    fieldWithPath("body.aList[].bk_goods").type(JsonFieldType.STRING).description("장바구니 상품 이름"),
                    fieldWithPath("body.aList[].bk_price").type(JsonFieldType.NUMBER).description("장바구니 상품 금액"),
                    fieldWithPath("body.aList[].bk_in").type(JsonFieldType.STRING).description("장바구니 상품 예약 시작 시간").optional(),
                    fieldWithPath("body.aList[].bk_out").type(JsonFieldType.STRING).description("장바구니 상품 예약 끝 시간").optional(),
                    fieldWithPath("body.aList[].bk_paych").type(JsonFieldType.STRING).description("장바구니 결제유무"),
                    fieldWithPath("body.aList[].bk_people").type(JsonFieldType.NUMBER).description("장바구니 상품 예약자 수"),
                    fieldWithPath("body.aList[].c_name").type(JsonFieldType.STRING).description("업체 명"),
                    fieldWithPath("body.aList[].bk_create_dt").type(JsonFieldType.STRING).description("장바구니 생성일시"),
                    
                    fieldWithPath("body.fList").type(JsonFieldType.ARRAY).description("식당 장바구니 리스트").optional(),
                    fieldWithPath("body.fList[].bk_pk_num").type(JsonFieldType.NUMBER).description("장바구니 일련번호"),
                    fieldWithPath("body.fList[].bk_fk_tnum").type(JsonFieldType.NUMBER).description("여행 번호"),
                    fieldWithPath("body.fList[].bk_fk_cnum").type(JsonFieldType.STRING).description("상품 업체의 사업자 번호"),
                    fieldWithPath("body.fList[].bk_fk_id").type(JsonFieldType.STRING).description("장바구니 추가한 회윈 아이디"),
                    fieldWithPath("body.fList[].bk_fk_num").type(JsonFieldType.NUMBER).description("상품 일련 번호"),
                    fieldWithPath("body.fList[].bk_goods").type(JsonFieldType.STRING).description("장바구니 상품 이름"),
                    fieldWithPath("body.fList[].bk_price").type(JsonFieldType.NUMBER).description("장바구니 상품 금액"),
                    fieldWithPath("body.fList[].bk_in").type(JsonFieldType.STRING).description("장바구니 상품 예약 시작 시간").optional(),
                    fieldWithPath("body.fList[].bk_out").type(JsonFieldType.STRING).description("장바구니 상품 예약 끝 시간").optional(),
                    fieldWithPath("body.fList[].bk_paych").type(JsonFieldType.STRING).description("장바구니 결제유무"),
                    fieldWithPath("body.fList[].bk_people").type(JsonFieldType.NUMBER).description("장바구니 상품 예약자 수"),
                    fieldWithPath("body.fList[].c_name").type(JsonFieldType.STRING).description("업체 명"),
                    fieldWithPath("body.fList[].bk_create_dt").type(JsonFieldType.STRING).description("장바구니 생성일시")
                )
            )
        )
        .andExpect(status().isOk());
    }

    // @Test
    public void insertBasket() throws Exception {
        String jsonRQ = """
            {   
                "bk_fk_cnum" : 12,
                "bk_fk_num" : 2,
                "bk_fk_tnum" : 2,
                "bk_goods" : "다이브",
                "bk_people" : 5,
                "date" : "2024-01-16 00:00:00"
            }
        """;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            post(this.BASE_URL + "/insert_basket")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "basket/insert_basket",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("bk_fk_cnum").type(JsonFieldType.NUMBER).description("영업장 일련번호 번호"),
                    fieldWithPath("bk_fk_num").type(JsonFieldType.NUMBER).description("상품 일련 번호"),
                    fieldWithPath("bk_fk_tnum").type(JsonFieldType.NUMBER).description("여행 일련번호"),
                    fieldWithPath("bk_goods").type(JsonFieldType.STRING).description("상품 이름"),
                    fieldWithPath("bk_people").type(JsonFieldType.NUMBER).description("주문한 사람 숫자"),
                    fieldWithPath("date").type(JsonFieldType.STRING).description("예약 날짜")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());

        int pk_num = basketMapper.selectMaxId();
        basketMapper.deleteBasketByLast(pk_num);
    }

    // @Test
    public void insertBasketReserve() throws Exception {

        String jsonRQ = """
            {   
                "bk_people" : 5,
                "bk_in" : "2024-01-13 00:00:00",
                "bk_out" : "2024-01-17 00:00:00",
                "bk_number" : "01083703435",
                "bk_name" : "이경도",
                "bk_fk_num" : 2,
                "bk_carch" : "차량",
                "bk_fk_cnum" : 3,
                "bk_goods" : "다이브",
                "bk_fk_tnum" : 2 
            }
            """;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            post(this.BASE_URL + "/insert_basket_reserve")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "basket/insert_basket_reserve",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("bk_people").type(JsonFieldType.NUMBER).description("주문한 사람 숫자"),
                    fieldWithPath("bk_in").type(JsonFieldType.STRING).description("예약 시작 시간"),
                    fieldWithPath("bk_out").type(JsonFieldType.STRING).description("예약 끝 시간"),
                    fieldWithPath("bk_number").type(JsonFieldType.STRING).description("예약자 번호"),
                    fieldWithPath("bk_name").type(JsonFieldType.STRING).description("예약자 이름"),
                    fieldWithPath("bk_fk_num").type(JsonFieldType.NUMBER).description("상품 일련 번호"),
                    fieldWithPath("bk_carch").type(JsonFieldType.STRING).description("차량 여부"),
                    fieldWithPath("bk_fk_cnum").type(JsonFieldType.NUMBER).description("사업자 번호"),
                    fieldWithPath("bk_goods").type(JsonFieldType.STRING).description("상품 이름"),
                    fieldWithPath("bk_fk_tnum").type(JsonFieldType.NUMBER).description("여행 일련번호")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());

        int pk_num = basketMapper.selectMaxId();
        basketMapper.deleteBasketByLast(pk_num);
    }


}
