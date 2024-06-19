package com.travel.jeju.main;

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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.BaseTest;
import com.travel.jeju.model.member.TokenModel;

// import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
@ActiveProfiles("local")
// @Slf4j
public class MainControllerTest extends BaseTest {

    private final String BASE_URL = "/api/main";


    @Test
    @Transactional
    public void getPublicBlog() throws Exception{

        this.mockMvc.perform(
            get(this.BASE_URL + "/public_blog")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contextPath("/api")
        )
        .andDo(print())
        .andDo(
            document(
                "main/public_blog",
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.publicBlogList").type(JsonFieldType.ARRAY).description("랜덤 후기글 리스트").optional(),
                    fieldWithPath("body.publicBlogList[].b_pk_num").type(JsonFieldType.NUMBER).description("후기글 일련번호"),
                    fieldWithPath("body.publicBlogList[].b_fk_tnum").type(JsonFieldType.NUMBER).description("여행 일련번호"),
                    fieldWithPath("body.publicBlogList[].b_fk_id").type(JsonFieldType.STRING).description("후기글 작성자 ID"),
                    fieldWithPath("body.publicBlogList[].b_title").type(JsonFieldType.STRING).description("후기글 제목"),
                    fieldWithPath("body.publicBlogList[].b_cost").type(JsonFieldType.NUMBER).description("여행 총 비용"),
                    fieldWithPath("body.publicBlogList[].b_contents").type(JsonFieldType.STRING).description("후기글 내용"),
                    fieldWithPath("body.publicBlogList[].file_group_no").type(JsonFieldType.NUMBER).description("후기글 파일 그룹번호"),
                    fieldWithPath("body.publicBlogList[].b_public_check").type(JsonFieldType.BOOLEAN).description("후기글 공개여부"),
                    fieldWithPath("body.publicBlogList[].b_create_dt").type(JsonFieldType.STRING).description("후기글 생성일시"),
                    fieldWithPath("body.publicBlogList[].b_star").type(JsonFieldType.NUMBER).description("후기글 별졈 [만족도]")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void insertFavorite() throws Exception{

        String jsonRQ = """
            {
                "fa_fk_id" : "lkd9125",
                "fa_fk_cnum" : "3238101162"
            }
            """;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            post(this.BASE_URL + "/insert_favorite")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "main/insert_favorite",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("fa_fk_id").type(JsonFieldType.STRING).description("회원 아이디"),
                    fieldWithPath("fa_fk_cnum").type(JsonFieldType.STRING).description("즐겨찾기 추가할 사업자 번호")
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
    @Transactional
    public void insertReview() throws Exception{

        MockMultipartFile r_img = new MockMultipartFile("r_img", "리뷰사진.png","image/png", "<<png data>>".getBytes());
        MockMultipartFile metadata = new MockMultipartFile("metadata", "", "multipart/form-data", 
        // "{\"id\":\"Tester\",\"password\":\"1234\",\"userType\":\"USER\",\"name\":\"테스트유저\",\"genderCd\":\"M\",\"hpno\":\"01012345678\",\"pridtfNo\":\"1234561234567\",\"career\":\"경력 1년차입니다.\",\"birth\":\"20000101\",\"certifications\":null}".getBytes());
        """
            {
                "rv_start": "5",
                "rv_contents":"녹물",
                "fv_fk_cnum":12
            }
        """.getBytes());

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            multipart(this.BASE_URL + "/insert_review")   
            .file(r_img)
            .file(metadata)
            .param("rv_start", "5")
            .param("rv_contents", "녹물")
            .param("fv_fk_cnum", "12")
            .contextPath("/api")
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
            .characterEncoding("UTF-8")
        )
        .andDo(print())
        .andDo(
            document(
                "main/insert_review",
                preprocessRequest(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestParts(
                    partWithName("r_img").description("리뷰 사진").optional(),
                    partWithName("metadata").description("유저 추가 폼")
                ),
                requestPartFields(
                    "metadata",
                    fieldWithPath("rv_start").description("리뷰 별점"),
                    fieldWithPath("rv_contents").description("리뷰 내용"),
                    fieldWithPath("fv_fk_cnum").description("리뷰쓰는 영업장 사업자 번호")
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
    public void optionCheck() throws Exception{
        
        String optionList = "l_seeview, l_pool";
        String category = "숙박";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/option_check")
            .contextPath("/api")
            .param("optionList", optionList)
            .param("category", category)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "main/option_check", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                queryParameters(
                    parameterWithName("optionList").description("옵션 리스트"),
                    parameterWithName("category").description("카테고리[숙박, 레저, 식당]")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    
                    fieldWithPath("body.cList").type(JsonFieldType.ARRAY).description("옵션의 맞는 업체리스트").optional(),
                    fieldWithPath("body.cList[].c_pk_num").type(JsonFieldType.NUMBER).description("업체 일련번호"),
                    fieldWithPath("body.cList[].c_cnum").type(JsonFieldType.STRING).description("업체 사업자번호"),
                    fieldWithPath("body.cList[].c_fk_id").type(JsonFieldType.STRING).description("업체 사업자회원 아이디"),
                    fieldWithPath("body.cList[].c_name").type(JsonFieldType.STRING).description("업체 명"),
                    fieldWithPath("body.cList[].c_phone").type(JsonFieldType.STRING).description("업체 사업자 연락처"),
                    fieldWithPath("body.cList[].c_category").type(JsonFieldType.STRING).description("업체 종류"),
                    fieldWithPath("body.cList[].c_addr").type(JsonFieldType.STRING).description("업체 주소"),
                    fieldWithPath("body.cList[].c_contents").type(JsonFieldType.STRING).description("업체 설명, 안내").optional(),
                    fieldWithPath("body.cList[].c_condition").type(JsonFieldType.STRING).description("업체 영업 여부"),
                    fieldWithPath("body.cList[].c_check").type(JsonFieldType.STRING).description("업체 관리자 승인여부"),
                    fieldWithPath("body.cList[].c_img").type(JsonFieldType.STRING).description("업체 대표사진"),
                    fieldWithPath("body.cList[].c_type").type(JsonFieldType.STRING).description("업체 주 종목"),
                    fieldWithPath("body.cList[].c_lat").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 위도 [ ex -> 33.516184958784 ]"),
                    fieldWithPath("body.cList[].c_lon").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 경도 [ ex -> 126.503483773385 ]"),
                    fieldWithPath("body.cList[].c_file_group_no").type(JsonFieldType.NUMBER).description("파일 그룹번호")

                       
                )
            )
        )
        .andExpect(status().isOk());

    }

    // @Test
    // @Transactional
    public void updatePay() throws Exception {

        String jsonRQ = """
            {
                "bk_pk_num" : 13,
                "bk_paych" : "1"
            }
        """;

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            post(this.BASE_URL + "/update_pay")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "main/update_pay",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                requestFields(
                    fieldWithPath("bk_pk_num").type(JsonFieldType.NUMBER).description("장바구니 일련번호"),
                    fieldWithPath("bk_paych").type(JsonFieldType.STRING).description("결제 업데이트 코드 [1 : 승인, 0 : 미승인]")
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
    public void itemInfomation() throws Exception{

        String itemNumber = "5";

        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/item_infomation")
            .contextPath("/api")
            .param("itemNumber", itemNumber)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "main/item_infomation", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                queryParameters(
                    parameterWithName("itemNumber").description("상품번호")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),

                    fieldWithPath("body.itemDetail").type(JsonFieldType.OBJECT).description("상품 정보"),
                    fieldWithPath("body.itemDetail.idx").type(JsonFieldType.NUMBER).description("상품 일련번호"),
                    fieldWithPath("body.itemDetail.p_fk_cnum").type(JsonFieldType.NUMBER).description("상품 관련 사업자 일련 번호"),
                    fieldWithPath("body.itemDetail.p_name").type(JsonFieldType.STRING).description("상품 이름"),
                    fieldWithPath("body.itemDetail.p_price").type(JsonFieldType.NUMBER).description("상품 금액"),
                    fieldWithPath("body.itemDetail.p_content").type(JsonFieldType.STRING).description("상품 설명내용"),
                    fieldWithPath("body.itemDetail.p_reserver_status").type(JsonFieldType.BOOLEAN).description("예약 가능한 상품인지 여부"),
                    fieldWithPath("body.itemDetail.p_exposure").type(JsonFieldType.BOOLEAN).description("상품 노출여부"),
                    fieldWithPath("body.itemDetail.file_group_no").type(JsonFieldType.NUMBER).description("파일 그룹번호"),
                    fieldWithPath("body.itemDetail.create_dt").type(JsonFieldType.STRING).description("상품 생성일시"),
                    fieldWithPath("body.itemDetail.update_dt").type(JsonFieldType.STRING).description("상품 수정일시"),
                    fieldWithPath("body.itemDetail.fileData").type(JsonFieldType.OBJECT).description("상품 대표 이미지"),

                    fieldWithPath("body.itemDetail.fileData.idx").type(JsonFieldType.NUMBER).description("이미지 일련번호"),
                    fieldWithPath("body.itemDetail.fileData.file_group_no").type(JsonFieldType.NUMBER).description("이미지 그룹번호"),
                    fieldWithPath("body.itemDetail.fileData.url").type(JsonFieldType.STRING).description("이미지 URL"),
                    fieldWithPath("body.itemDetail.fileData.file_ori_nm").type(JsonFieldType.STRING).description("이미지 원본이름"),
                    fieldWithPath("body.itemDetail.fileData.file_sys_nm").type(JsonFieldType.STRING).description("이미지 시스템이름"),
                    fieldWithPath("body.itemDetail.fileData.extension").type(JsonFieldType.STRING).description("이미지 확장자"),
                    fieldWithPath("body.itemDetail.fileData.description").type(JsonFieldType.STRING).description("이미지 설명"),
                    fieldWithPath("body.itemDetail.fileData.file_size").type(JsonFieldType.NUMBER).description("이미지 데이터 크기"),
                    fieldWithPath("body.itemDetail.fileData.file_size_unit").type(JsonFieldType.STRING).description("이미지 데이터 크기단위"),
                    fieldWithPath("body.itemDetail.fileData.create_dt").type(JsonFieldType.STRING).description("상품 대표 이미지 생성일시"),

                    fieldWithPath("body.itemFileList").type(JsonFieldType.ARRAY).description("상품 이미지 리스트").optional(),
                    fieldWithPath("body.itemFileList[].idx").type(JsonFieldType.NUMBER).description("이미지 일련번호"),
                    fieldWithPath("body.itemFileList[].file_group_no").type(JsonFieldType.NUMBER).description("이미지 그룹번호"),
                    fieldWithPath("body.itemFileList[].url").type(JsonFieldType.STRING).description("이미지 URL"),
                    fieldWithPath("body.itemFileList[].file_ori_nm").type(JsonFieldType.STRING).description("이미지 원본이름"),
                    fieldWithPath("body.itemFileList[].file_sys_nm").type(JsonFieldType.STRING).description("이미지 시스템이름"),
                    fieldWithPath("body.itemFileList[].extension").type(JsonFieldType.STRING).description("이미지 확장자"),
                    fieldWithPath("body.itemFileList[].description").type(JsonFieldType.STRING).description("이미지 설명"),
                    fieldWithPath("body.itemFileList[].file_size").type(JsonFieldType.NUMBER).description("이미지 데이터 크기"),
                    fieldWithPath("body.itemFileList[].file_size_unit").type(JsonFieldType.STRING).description("이미지 데이터 크기단위"),
                    fieldWithPath("body.itemFileList[].create_dt").type(JsonFieldType.STRING).description("상품 이미지 생성일시")

                    
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void businessItem() throws Exception {
        String cnum = "3";// 숙박
        // String cnum = "3238101162";// 레저

        this.mockMvc.perform(
            get(this.BASE_URL + "/business_item")
            .contextPath("/api")
            .param("cnum", cnum)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "main/business_item", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("cnum").description("사업자 일련번호")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),

                    fieldWithPath("body.company").type(JsonFieldType.OBJECT).description("영업장 정보"),
                    fieldWithPath("body.company.c_pk_num").type(JsonFieldType.NUMBER).description("영업장 일련 번호"),
                    fieldWithPath("body.company.c_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.company.c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.company.c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.company.c_category").type(JsonFieldType.STRING).description("영업장 종류[숙박영업, 레저영업, 식당영업 구분]"),
                    fieldWithPath("body.company.c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.company.c_contents").type(JsonFieldType.STRING).description("영업장 소개").optional(),
                    fieldWithPath("body.company.c_type").type(JsonFieldType.STRING).description("영업장 주 업종"),
                    fieldWithPath("body.company.c_lat").type(JsonFieldType.NUMBER).description("영업장 좌표 - 위도 [ ex -> 33.4706419288473] "),
                    fieldWithPath("body.company.c_lon").type(JsonFieldType.NUMBER).description("영업장 좌표 - 경도 [ ex -> 126.354447319405] "),
                    
                    fieldWithPath("body.company.fileData").type(JsonFieldType.OBJECT).description("영업장 메인사진 객체"),
                    fieldWithPath("body.company.fileData.idx").type(JsonFieldType.NUMBER).description("파일 일련번호"),
                    fieldWithPath("body.company.fileData.file_group_no").type(JsonFieldType.NUMBER).description("파일 그룹번호"),
                    fieldWithPath("body.company.fileData.url").type(JsonFieldType.STRING).description("파일 URL"),
                    fieldWithPath("body.company.fileData.file_ori_nm").type(JsonFieldType.STRING).description("파일 원본이름"),
                    fieldWithPath("body.company.fileData.file_sys_nm").type(JsonFieldType.STRING).description("파일 변경된 시스템 이름"),
                    fieldWithPath("body.company.fileData.extension").type(JsonFieldType.STRING).description("파일 확장자"),
                    fieldWithPath("body.company.fileData.description").type(JsonFieldType.STRING).description("파일 설명"),
                    fieldWithPath("body.company.fileData.file_size").type(JsonFieldType.NUMBER).description("파일 사이즈"),
                    fieldWithPath("body.company.fileData.file_size_unit").type(JsonFieldType.STRING).description("파일 사이즈 단위"),
                    fieldWithPath("body.company.fileData.create_dt").type(JsonFieldType.STRING).description("파일 생성일시"),

                    fieldWithPath("body.companyFileList").type(JsonFieldType.ARRAY).description("영업장 서브 사진 리스트").optional(),
                    fieldWithPath("body.companyFileList[].idx").type(JsonFieldType.NUMBER).description("파일 일련번호"),
                    fieldWithPath("body.companyFileList[].file_group_no").type(JsonFieldType.NUMBER).description("파일 그룹번호"),
                    fieldWithPath("body.companyFileList[].url").type(JsonFieldType.STRING).description("파일 URL"),
                    fieldWithPath("body.companyFileList[].file_ori_nm").type(JsonFieldType.STRING).description("파일 원본이름"),
                    fieldWithPath("body.companyFileList[].file_sys_nm").type(JsonFieldType.STRING).description("파일 변경된 시스템 이름"),
                    fieldWithPath("body.companyFileList[].extension").type(JsonFieldType.STRING).description("파일 확장자"),
                    fieldWithPath("body.companyFileList[].description").type(JsonFieldType.STRING).description("파일 설명"),
                    fieldWithPath("body.companyFileList[].file_size").type(JsonFieldType.NUMBER).description("파일 사이즈"),
                    fieldWithPath("body.companyFileList[].file_size_unit").type(JsonFieldType.STRING).description("파일 사이즈 단위"),
                    fieldWithPath("body.companyFileList[].create_dt").type(JsonFieldType.STRING).description("파일 생성일시"),

                    fieldWithPath("body.companyItemList").type(JsonFieldType.ARRAY).description("영업장 상품리스트"),
                    fieldWithPath("body.companyItemList[].idx").type(JsonFieldType.NUMBER).description("상품 일련번호"),
                    fieldWithPath("body.companyItemList[].p_fk_cnum").type(JsonFieldType.NUMBER).description("상품 영업장 일련번호"),
                    fieldWithPath("body.companyItemList[].p_price").type(JsonFieldType.NUMBER).description("상품 가격"),
                    fieldWithPath("body.companyItemList[].p_content").type(JsonFieldType.STRING).description("상품 소개글"),
                    fieldWithPath("body.companyItemList[].p_reserver_status").type(JsonFieldType.BOOLEAN).description("상품 예약가능한 상품인지 여부"),
                    fieldWithPath("body.companyItemList[].p_exposure").type(JsonFieldType.BOOLEAN).description("상품 노출여부"),
                    fieldWithPath("body.companyItemList[].create_dt").type(JsonFieldType.STRING).description("상품 생성일시"),
                    fieldWithPath("body.companyItemList[].update_dt").type(JsonFieldType.STRING).description("상품 수정일시"),

                    fieldWithPath("body.companyItemList[].fileData").type(JsonFieldType.OBJECT).description("상품 메인사진 객체"),
                    fieldWithPath("body.companyItemList[].fileData.idx").type(JsonFieldType.NUMBER).description("파일 일련번호"),
                    fieldWithPath("body.companyItemList[].fileData.file_group_no").type(JsonFieldType.NUMBER).description("파일 그룹번호"),
                    fieldWithPath("body.companyItemList[].fileData.url").type(JsonFieldType.STRING).description("파일 URL"),
                    fieldWithPath("body.companyItemList[].fileData.file_ori_nm").type(JsonFieldType.STRING).description("파일 원본이름"),
                    fieldWithPath("body.companyItemList[].fileData.file_sys_nm").type(JsonFieldType.STRING).description("파일 변경된 시스템 이름"),
                    fieldWithPath("body.companyItemList[].fileData.extension").type(JsonFieldType.STRING).description("파일 확장자"),
                    fieldWithPath("body.companyItemList[].fileData.description").type(JsonFieldType.STRING).description("파일 설명"),
                    fieldWithPath("body.companyItemList[].fileData.file_size").type(JsonFieldType.NUMBER).description("파일 사이즈"),
                    fieldWithPath("body.companyItemList[].fileData.file_size_unit").type(JsonFieldType.STRING).description("파일 사이즈 단위"),
                    fieldWithPath("body.companyItemList[].fileData.create_dt").type(JsonFieldType.STRING).description("파일 생성일시")
                    
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void businessPlace() throws Exception{

        String category = "레저";

        this.mockMvc.perform(
            get(this.BASE_URL + "/business_place")
            .contextPath("/api")
            .param("category", category)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "main/business_place", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("category").description("카테고리 값[ EX => 숙박, 레저, 식당 ]").optional()
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.companyRandomList").type(JsonFieldType.ARRAY).description("카테고리에 맞는 영업장 리스트"),
                    fieldWithPath("body.companyRandomList[].c_pk_num").type(JsonFieldType.NUMBER).description("영업장 일련 번호"),
                    fieldWithPath("body.companyRandomList[].c_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.companyRandomList[].c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.companyRandomList[].c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.companyRandomList[].c_category").type(JsonFieldType.STRING).description("영업장 종류[숙박영업, 레저영업, 식당영업 구분]"),
                    fieldWithPath("body.companyRandomList[].c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.companyRandomList[].c_contents").type(JsonFieldType.STRING).description("영업장 소개").optional(),
                    fieldWithPath("body.companyRandomList[].c_type").type(JsonFieldType.STRING).description("영업장 주 업종"),
                    fieldWithPath("body.companyRandomList[].c_lat").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 위도 [ ex -> 33.516184958784 ]"),
                    fieldWithPath("body.companyRandomList[].c_lon").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 경도 [ ex -> 126.503483773385 ]"),

                    fieldWithPath("body.companyRandomList[].fileData").type(JsonFieldType.OBJECT).description("파일 데이터"),
                    fieldWithPath("body.companyRandomList[].fileData.idx").type(JsonFieldType.NUMBER).description("파일 일련번호"),
                    fieldWithPath("body.companyRandomList[].fileData.file_group_no").type(JsonFieldType.NUMBER).description("파일 그룹번호"),
                    fieldWithPath("body.companyRandomList[].fileData.url").type(JsonFieldType.STRING).description("파일 URL"),
                    fieldWithPath("body.companyRandomList[].fileData.file_ori_nm").type(JsonFieldType.STRING).description("파일 원본이름"),
                    fieldWithPath("body.companyRandomList[].fileData.file_sys_nm").type(JsonFieldType.STRING).description("파일 변경된 시스템 이름"),
                    fieldWithPath("body.companyRandomList[].fileData.extension").type(JsonFieldType.STRING).description("파일 확장자"),
                    fieldWithPath("body.companyRandomList[].fileData.description").type(JsonFieldType.STRING).description("파일 설명"),
                    fieldWithPath("body.companyRandomList[].fileData.file_size").type(JsonFieldType.NUMBER).description("파일 사이즈"),
                    fieldWithPath("body.companyRandomList[].fileData.file_size_unit").type(JsonFieldType.STRING).description("파일 사이즈 단위"),
                    fieldWithPath("body.companyRandomList[].fileData.create_dt").type(JsonFieldType.STRING).description("파일 생성일시")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void homeComplaint() throws Exception{

        this.mockMvc.perform(
            get(this.BASE_URL + "/home/complaint")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "main/home/complaint", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.complaint").type(JsonFieldType.ARRAY).description("건의사항 리스트").optional(),
                    fieldWithPath("body.complaint[].co_pk_conum").type(JsonFieldType.NUMBER).description("건의사항 일련번호"),
                    fieldWithPath("body.complaint[].co_title").type(JsonFieldType.STRING).description("건의사항 제목"),
                    fieldWithPath("body.complaint[].co_create_dt").type(JsonFieldType.STRING).description("건의사항 생성일시"),
                    fieldWithPath("body.complaint[].co_check").type(JsonFieldType.BOOLEAN).description("건의사항 답변유무")
                )
            )
        )
        .andExpect(status().isOk());
    }
    
    @Test
    public void homeNotice() throws Exception{

        this.mockMvc.perform(
            get(this.BASE_URL + "/home/notice")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "main/home/notice", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.notice").type(JsonFieldType.ARRAY).description("공지사항 리스트").optional(),
                    fieldWithPath("body.notice[].n_pk_num").type(JsonFieldType.NUMBER).description("공지사항 일련번호"),
                    fieldWithPath("body.notice[].n_title").type(JsonFieldType.STRING).description("공지사항 제목"),
                    fieldWithPath("body.notice[].n_views").type(JsonFieldType.NUMBER).description("공지사항 조회수"),
                    fieldWithPath("body.notice[].n_date").type(JsonFieldType.STRING).description("공지사항 생성일시")
                )
            )
        )
        .andExpect(status().isOk());
    }
    
    @Test
    public void homeEventSpot() throws Exception{

        this.mockMvc.perform(
            get(this.BASE_URL + "/home/event-spot")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "main/home/event-spot", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.event").type(JsonFieldType.ARRAY).description("이벤트 리스트 [4개제한]").optional(),
                    fieldWithPath("body.event[].e_pk_enum").type(JsonFieldType.NUMBER).description("이벤트 일련번호"),
                    fieldWithPath("body.event[].e_date").type(JsonFieldType.STRING).description("이벤트 생성일시"),
                    fieldWithPath("body.event[].e_title").type(JsonFieldType.STRING).description("이벤트 이벤트 제목"),
                    fieldWithPath("body.event[].e_addr").type(JsonFieldType.STRING).description("이벤트 이벤트주소"),
                    fieldWithPath("body.event[].e_eday").type(JsonFieldType.STRING).description("이벤트 이벤트 기간"),
                    fieldWithPath("body.event[].e_img").type(JsonFieldType.STRING).description("이벤트 사진 URL"),
                    fieldWithPath("body.event[].e_info").type(JsonFieldType.STRING).description("이벤트 내용"),
                    fieldWithPath("body.event[].e_eventing").type(JsonFieldType.STRING).description("이벤트 활성화 유무[1 : 진행중 , 0 : 종료]"),
                    
                    fieldWithPath("body.spot").type(JsonFieldType.ARRAY).description("관광지 리스트 [4개제한]").optional(),
                    fieldWithPath("body.spot[].s_pk_num").type(JsonFieldType.NUMBER).description("관광지 일련번호]"),
                    fieldWithPath("body.spot[].s_tittle").type(JsonFieldType.STRING).description("관광지 제목[이름]"),
                    fieldWithPath("body.spot[].s_addr").type(JsonFieldType.STRING).description("관광지 주소"),
                    fieldWithPath("body.spot[].s_info").type(JsonFieldType.STRING).description("관광지 정보"),
                    fieldWithPath("body.spot[].s_img").type(JsonFieldType.STRING).description("관광지 이미지"),
                    fieldWithPath("body.spot[].s_guide").type(JsonFieldType.STRING).description("관광지 안내사항")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void homeBusiness() throws Exception{

        this.mockMvc.perform(
            get(this.BASE_URL + "/home/business_place")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "main/home/business_place", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.lodgment").type(JsonFieldType.ARRAY).description("랜덤 숙박 업체 4곳"),
                    fieldWithPath("body.lodgment[].c_pk_num").type(JsonFieldType.NUMBER).description("영업장 일련 번호"),
                    fieldWithPath("body.lodgment[].c_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.lodgment[].c_fk_id").type(JsonFieldType.STRING).description("영업체 업주 아이디"),
                    fieldWithPath("body.lodgment[].c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.lodgment[].c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.lodgment[].c_category").type(JsonFieldType.STRING).description("영업장 종류[숙박영업, 레저영업, 식당영업 구분]"),
                    fieldWithPath("body.lodgment[].c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.lodgment[].c_contents").type(JsonFieldType.STRING).description("영업장 소개").optional(),
                    fieldWithPath("body.lodgment[].c_condition").type(JsonFieldType.STRING).description("영업준비 유무"),
                    fieldWithPath("body.lodgment[].c_check").type(JsonFieldType.STRING).description("영업장 관리자 승인유무"),
                    fieldWithPath("body.lodgment[].c_img").type(JsonFieldType.STRING).description("영업장 대표 이미지"),
                    fieldWithPath("body.lodgment[].c_type").type(JsonFieldType.STRING).description("영업장 주 업종"),
                    fieldWithPath("body.lodgment[].c_lat").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 위도 [ ex -> 33.516184958784 ]"),
                    fieldWithPath("body.lodgment[].c_lon").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 경도 [ ex -> 126.503483773385 ]"),
                    fieldWithPath("body.lodgment[].c_file_group_no").type(JsonFieldType.NUMBER).description("파일 그룹번호"),
                    
                    fieldWithPath("body.leisure").type(JsonFieldType.ARRAY).description("랜덤 레저 업체 4곳"),
                    fieldWithPath("body.leisure[].c_pk_num").type(JsonFieldType.NUMBER).description("영업장 일련 번호"),
                    fieldWithPath("body.leisure[].c_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.leisure[].c_fk_id").type(JsonFieldType.STRING).description("영업체 업주 아이디"),
                    fieldWithPath("body.leisure[].c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.leisure[].c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.leisure[].c_category").type(JsonFieldType.STRING).description("영업장 종류[숙박영업, 레저영업, 식당영업 구분]"),
                    fieldWithPath("body.leisure[].c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.leisure[].c_contents").type(JsonFieldType.STRING).description("영업장 소개").optional(),
                    fieldWithPath("body.leisure[].c_condition").type(JsonFieldType.STRING).description("영업준비 유무"),
                    fieldWithPath("body.leisure[].c_check").type(JsonFieldType.STRING).description("영업장 관리자 승인유무"),
                    fieldWithPath("body.leisure[].c_img").type(JsonFieldType.STRING).description("영업장 대표 이미지"),
                    fieldWithPath("body.leisure[].c_type").type(JsonFieldType.STRING).description("영업장 주 업종"),
                    fieldWithPath("body.leisure[].c_lat").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 위도 [ ex -> 33.516184958784 ]"),
                    fieldWithPath("body.leisure[].c_lon").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 경도 [ ex -> 126.503483773385 ]"),
                    fieldWithPath("body.leisure[].c_file_group_no").type(JsonFieldType.NUMBER).description("파일 그룹번호"),
                    
                    fieldWithPath("body.restaurant").type(JsonFieldType.ARRAY).description("랜덤 식당 업체 4곳"),
                    fieldWithPath("body.restaurant[].c_pk_num").type(JsonFieldType.NUMBER).description("영업장 일련 번호"),
                    fieldWithPath("body.restaurant[].c_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.restaurant[].c_fk_id").type(JsonFieldType.STRING).description("영업체 업주 아이디"),
                    fieldWithPath("body.restaurant[].c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.restaurant[].c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.restaurant[].c_category").type(JsonFieldType.STRING).description("영업장 종류[숙박영업, 레저영업, 식당영업 구분]"),
                    fieldWithPath("body.restaurant[].c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.restaurant[].c_contents").type(JsonFieldType.STRING).description("영업장 소개").optional(),
                    fieldWithPath("body.restaurant[].c_condition").type(JsonFieldType.STRING).description("영업준비 유무"),
                    fieldWithPath("body.restaurant[].c_check").type(JsonFieldType.STRING).description("영업장 관리자 승인유무"),
                    fieldWithPath("body.restaurant[].c_img").type(JsonFieldType.STRING).description("영업장 대표 이미지"),
                    fieldWithPath("body.restaurant[].c_type").type(JsonFieldType.STRING).description("영업장 주 업종"),
                    fieldWithPath("body.restaurant[].c_lat").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 위도 [ ex -> 33.516184958784 ]"),
                    fieldWithPath("body.restaurant[].c_lon").type(JsonFieldType.NUMBER).description("업체 위치 좌표 - 경도 [ ex -> 126.503483773385 ]"),
                    fieldWithPath("body.restaurant[].c_file_group_no").type(JsonFieldType.NUMBER).description("파일 그룹번호")
                )
            )
        )
        .andExpect(status().isOk());
    }

    // @Test
    public void home() throws Exception {
        
        this.mockMvc.perform(
            get(this.BASE_URL + "/home")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "main/home", // gradle build를 하게 되면 generated-snippets에 만들어질 폴더이름
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.cdto").type(JsonFieldType.ARRAY).description("랜덤 숙박 업체 3곳"),
                    fieldWithPath("body.cdto[].c_pk_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.cdto[].c_fk_id").type(JsonFieldType.STRING).description("영업체 업주 아이디"),
                    fieldWithPath("body.cdto[].c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.cdto[].c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.cdto[].c_category").type(JsonFieldType.STRING).description("영업장 종류[숙박영업, 레저영업, 식당영업 구분]"),
                    fieldWithPath("body.cdto[].c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.cdto[].c_contents").type(JsonFieldType.STRING).description("영업장 소개"),
                    fieldWithPath("body.cdto[].c_condition").type(JsonFieldType.STRING).description("영업준비 유무"),
                    fieldWithPath("body.cdto[].c_check").type(JsonFieldType.STRING).description("영업장 관리자 승인유무"),
                    fieldWithPath("body.cdto[].c_img").type(JsonFieldType.STRING).description("영업장 대표 이미지"),
                    fieldWithPath("body.cdto[].c_type").type(JsonFieldType.STRING).description("영업장 주 업종"),
                    

                    fieldWithPath("body.mdl").type(JsonFieldType.ARRAY).description("광고할 숙박업체 1곳 [가장 크게 있음]"),
                    fieldWithPath("body.mdl[].c_pk_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.mdl[].c_fk_id").type(JsonFieldType.STRING).description("영업체 업주 아이디"),
                    fieldWithPath("body.mdl[].c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.mdl[].c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.mdl[].c_category").type(JsonFieldType.STRING).description("영업장 종류[숙박영업, 레저영업, 식당영업 구분]"),
                    fieldWithPath("body.mdl[].c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.mdl[].c_contents").type(JsonFieldType.STRING).description("영업장 소개"),
                    fieldWithPath("body.mdl[].c_condition").type(JsonFieldType.STRING).description("영업준비 유무"),
                    fieldWithPath("body.mdl[].c_check").type(JsonFieldType.STRING).description("영업장 관리자 승인유무"),
                    fieldWithPath("body.mdl[].c_img").type(JsonFieldType.STRING).description("영업장 대표 이미지"),
                    fieldWithPath("body.mdl[].c_type").type(JsonFieldType.STRING).description("영업장 주 업종"),
                    
                    fieldWithPath("body.adto").type(JsonFieldType.ARRAY).description("랜덤 레저 업체 3곳"),
                    fieldWithPath("body.adto[].c_pk_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.adto[].c_fk_id").type(JsonFieldType.STRING).description("영업체 업주 아이디"),
                    fieldWithPath("body.adto[].c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.adto[].c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.adto[].c_category").type(JsonFieldType.STRING).description("영업장 종류[숙박영업, 레저영업, 식당영업 구분]"),
                    fieldWithPath("body.adto[].c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.adto[].c_contents").type(JsonFieldType.STRING).description("영업장 소개"),
                    fieldWithPath("body.adto[].c_condition").type(JsonFieldType.STRING).description("영업준비 유무"),
                    fieldWithPath("body.adto[].c_check").type(JsonFieldType.STRING).description("영업장 관리자 승인유무"),
                    fieldWithPath("body.adto[].c_img").type(JsonFieldType.STRING).description("영업장 대표 이미지"),
                    fieldWithPath("body.adto[].c_type").type(JsonFieldType.STRING).description("영업장 주 업종"),
                    
                    fieldWithPath("body.mda").type(JsonFieldType.ARRAY).description("광고할 레저업체 1곳 [가장 크게 있음]"),
                    fieldWithPath("body.mda[].c_pk_cnum").type(JsonFieldType.STRING).description("영업장 사업자 번호"),
                    fieldWithPath("body.mda[].c_fk_id").type(JsonFieldType.STRING).description("영업체 업주 아이디"),
                    fieldWithPath("body.mda[].c_name").type(JsonFieldType.STRING).description("영업장 이름"),
                    fieldWithPath("body.mda[].c_phone").type(JsonFieldType.STRING).description("영업장 연락처"),
                    fieldWithPath("body.mda[].c_category").type(JsonFieldType.STRING).description("영업장 종류[숙박영업, 레저영업, 식당영업 구분]"),
                    fieldWithPath("body.mda[].c_addr").type(JsonFieldType.STRING).description("영업장 주소"),
                    fieldWithPath("body.mda[].c_contents").type(JsonFieldType.STRING).description("영업장 소개"),
                    fieldWithPath("body.mda[].c_condition").type(JsonFieldType.STRING).description("영업준비 유무"),
                    fieldWithPath("body.mda[].c_check").type(JsonFieldType.STRING).description("영업장 관리자 승인유무"),
                    fieldWithPath("body.mda[].c_img").type(JsonFieldType.STRING).description("영업장 대표 이미지"),
                    fieldWithPath("body.mda[].c_type").type(JsonFieldType.STRING).description("영업장 주 업종"),
                    
                    fieldWithPath("body.sdto").type(JsonFieldType.ARRAY).description("관광지 리스트 [2개 제한]"),
                    fieldWithPath("body.sdto[].s_pk_num").type(JsonFieldType.NUMBER).description("관광지 일련번호]"),
                    fieldWithPath("body.sdto[].s_tittle").type(JsonFieldType.STRING).description("관광지 제목[이름]"),
                    fieldWithPath("body.sdto[].s_addr").type(JsonFieldType.STRING).description("관광지 주소"),
                    fieldWithPath("body.sdto[].s_info").type(JsonFieldType.STRING).description("관광지 정보"),
                    fieldWithPath("body.sdto[].s_img").type(JsonFieldType.STRING).description("관광지 이미지"),
                    fieldWithPath("body.sdto[].s_guide").type(JsonFieldType.STRING).description("관광지 안내사항"),
                    
                    fieldWithPath("body.bigsdto").type(JsonFieldType.ARRAY).description("광고할 관광지 1곳 [1개 제한]"),
                    fieldWithPath("body.bigsdto[].s_pk_num").type(JsonFieldType.NUMBER).description("관광지 일련번호]"),
                    fieldWithPath("body.bigsdto[].s_tittle").type(JsonFieldType.STRING).description("관광지 제목[이름]"),
                    fieldWithPath("body.bigsdto[].s_addr").type(JsonFieldType.STRING).description("관광지 주소"),
                    fieldWithPath("body.bigsdto[].s_info").type(JsonFieldType.STRING).description("관광지 정보"),
                    fieldWithPath("body.bigsdto[].s_img").type(JsonFieldType.STRING).description("관광지 이미지"),
                    fieldWithPath("body.bigsdto[].s_guide").type(JsonFieldType.STRING).description("관광지 안내사항"),
                    
                    fieldWithPath("body.ndto").type(JsonFieldType.ARRAY).description("공지사항 리스트[5개 제한]"),
                    fieldWithPath("body.ndto[].n_pk_num").type(JsonFieldType.NUMBER).description("공지사항 일련번호"),
                    fieldWithPath("body.ndto[].n_title").type(JsonFieldType.STRING).description("공지사항 제목"),
                    fieldWithPath("body.ndto[].n_views").type(JsonFieldType.NUMBER).description("공지사항 조회수"),
                    fieldWithPath("body.ndto[].n_date").type(JsonFieldType.STRING).description("공지사항 생성일시"),
                    
                    fieldWithPath("body.edto").type(JsonFieldType.ARRAY).description("이벤트 리스트 [1개 제한]"),
                    fieldWithPath("body.edto[].e_pk_enum").type(JsonFieldType.NUMBER).description("이벤트 일련번호"),
                    fieldWithPath("body.edto[].e_img").type(JsonFieldType.STRING).description("이벤트 사진 URL"),

                    fieldWithPath("body.pldto").type(JsonFieldType.ARRAY).description("신고 사항 리스트 [5개 제한]"),
                    fieldWithPath("body.pldto[].co_pk_conum").type(JsonFieldType.NUMBER).description("신고 사항 일련번호"),
                    fieldWithPath("body.pldto[].co_title").type(JsonFieldType.STRING).description("신고 사항 제목"),
                    fieldWithPath("body.pldto[].co_create_dt").type(JsonFieldType.STRING).description("신고 사항 생성일시")
                    
                )
            )
        )
        .andExpect(status().isOk());
    }

    
}
