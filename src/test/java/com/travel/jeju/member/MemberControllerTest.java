package com.travel.jeju.member;

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

import java.util.Arrays;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.travel.jeju.BaseTest;
import com.travel.jeju.constant.AuthConstants;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.dto.SmsDto;
import com.travel.jeju.jwt.JwtTokenProvider;
import com.travel.jeju.mapper.MemberMapper;
import com.travel.jeju.model.member.TokenModel;
import com.travel.jeju.util.RandomUtil;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
public class MemberControllerTest extends BaseTest{

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    private final String BASE_URL = "/api/member";

    private final String TEST_ID = "TEST";

    private final String TEST_SEND_PHONE = "01099991111";

    private final String TEST_CERT_PHONE = "01099992222";

    @Test
    public void profile() throws Exception{
        
        TokenModel tokenModel = this.getUserToken();

        this.mockMvc.perform(
            get(this.BASE_URL + "/profile")
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "member/profile",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.memberProfile").type(JsonFieldType.OBJECT).description("유저의 프로필"),
                    fieldWithPath("body.memberProfile.username").type(JsonFieldType.STRING).description("유저 ID"),
                    fieldWithPath("body.memberProfile.name").type(JsonFieldType.STRING).description("회원 이름"),
                    fieldWithPath("body.memberProfile.gender").type(JsonFieldType.STRING).description("성별"),
                    fieldWithPath("body.memberProfile.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                    fieldWithPath("body.memberProfile.birth").type(JsonFieldType.STRING).description("생년월일"),
                    fieldWithPath("body.memberProfile.phone").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("body.memberProfile.addr").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("body.memberProfile.email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("body.memberProfile.category").type(JsonFieldType.STRING).description("유저 타입 [ 일반유저, 업체유저, 관리자유저 ]")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    public void login() throws Exception{

        String jsonRQ = """
            {
                "username" : "lkd9125",
                "password" : "1234"
            }  
        """;

        this.mockMvc.perform(
            post("/api/loginProc")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "member/loginProc",
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("username").type(JsonFieldType.STRING).description("회원 아이디"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("회원 ")
                ),
                responseFields(
                    fieldWithPath("token").type(JsonFieldType.OBJECT).description("인증객체"),
                    fieldWithPath("token.grantType").type(JsonFieldType.STRING).description("인증타입"),
                    fieldWithPath("token.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                    fieldWithPath("token.refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰")
                )
            )
        )
        .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void certSms() throws Exception {
        
        String phone = this.TEST_CERT_PHONE;
        
        int[] randomArray = RandomUtil.randomIntegerArray(9, 6);

        String certNum = Arrays.toString(randomArray).replaceAll("[^0-9]", "");
        String msg = "인증번호[" + certNum + "]입니다. 타인에게 알려주지 마세요.";

        SmsDto smsDto = new SmsDto();
        smsDto.setIdx(null);
        smsDto.setType(AuthConstants.REG_CERTIFICATE.getValue());
        smsDto.setContent(msg);
        smsDto.setCert_num(certNum);
        smsDto.setPhone_num(phone);
        smsDto.setCert_yn(false);

        memberMapper.insertSms(smsDto);

        String jsonRQ = """
            {
                "phone_num" : "%s",
                "cert_num" : "%s"
            }
        """.formatted(phone, certNum);

        this.mockMvc.perform(
            post(this.BASE_URL + "/cert_sms")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "member/cert_sms",
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("phone_num").type(JsonFieldType.STRING).description("인증번호 받은 휴대폰번호"),
                    fieldWithPath("cert_num").type(JsonFieldType.STRING).description("인증번호")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());

        this.memberMapper.deleteTestSms(this.TEST_CERT_PHONE);
    }

    @Test
    @Transactional
    public void sendSms() throws Exception{
        String phone = this.TEST_SEND_PHONE;

        this.mockMvc.perform(
            get(this.BASE_URL + "/send_sms")
            .param("phone_num", phone)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "member/send_sms",
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("phone_num").description("인증 메세지 발송할 전화번호")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값[ture : 사용가능, false : 사용불가능]")
                )
            )
        )    
        .andExpect(status().isOk());

        this.memberMapper.deleteTestSms(this.TEST_SEND_PHONE);
    }

    @Test
    public void nickCheck() throws Exception{

        String valid_nick_name = "경경도";

        this.mockMvc.perform(
            get(this.BASE_URL + "/nick_check")
            .param("m_nickname", valid_nick_name)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "member/nick_check",
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("m_nickname").description("중복확인 할 별칭[닉네임]")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값[ture : 사용가능, false : 사용불가능]")
                )
            )
        )    
        .andExpect(status().isOk());

    }

    @Test
    public void idCheck() throws Exception{

        String valid_id = "lkd9125";

        this.mockMvc.perform(
            get(this.BASE_URL + "/id_check")
            .param("m_username", valid_id)
            .contextPath("/api")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "member/id_check",
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("m_username").description("중복확인 할 아이디")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값[ture : 사용가능, false : 사용불가능]")
                )
            )
        )    
        .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void logout() throws Exception{

        TokenModel tokenModel = this.getUserToken();
        
        String refresh_token = tokenModel.getRefreshToken();
        String username = jwtTokenProvider.getSubject(refresh_token);

        MemberDto dto = memberMapper.memberSelect(username);

        dto.setM_pass_fail_count(0);
        dto.setM_refresh_token(tokenModel.getRefreshToken());
        
        memberMapper.loginSuccessUpdate(dto);

        this.mockMvc.perform(
            get(this.BASE_URL + "/logout")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contextPath("/api")
            .header("Authorization", tokenModel.getGrantType() + " " + tokenModel.getAccessToken())
        )
        .andDo(print())
        .andDo(
            document(
                "member/logout",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Access토큰 [로그인 발급]")
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
    public void refreshToken() throws Exception{
        
        TokenModel tokenModel = this.getUserToken();
        String refresh_token = tokenModel.getRefreshToken();
        String username = jwtTokenProvider.getSubject(refresh_token);

        MemberDto dto = memberMapper.memberSelect(username);

        dto.setM_pass_fail_count(0);
        dto.setM_refresh_token(tokenModel.getRefreshToken());
        
        memberMapper.loginSuccessUpdate(dto);

        String jsonRQ = """
            {
                "refresh_token" : "%s"
            }
        """.formatted(refresh_token);

        this.mockMvc.perform(
            post(this.BASE_URL + "/refresh_token")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .contextPath("/api")
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "member/refresh_token",
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("리프레쉬 토큰")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.OBJECT).description("결과 값"),
                    fieldWithPath("body.access_token").type(JsonFieldType.STRING).description("엑세스 토큰")
                )
            )
        )
        .andExpect(status().isOk());

    }

    @Test
    @Transactional
    public void memberInsert() throws Exception {

        String id = this.TEST_ID + System.currentTimeMillis();
        // String id = this.TEST_ID;

        String jsonRQ = """
            {
                "m_username" : "%s",
                "m_pass" : "1234",
                "m_name" : "테스터",
                "m_gender" : "M",
                "m_nickname" : "Tester",
                "m_birth" : "19990907",
                "m_phone" : "01083703435",
                "m_addr" : "인천 서구",
                "m_email" : "lkd9125@naver.com"
            }
        """.formatted(id);

        this.mockMvc.perform(
            post(this.BASE_URL + "/insert")
            .contextPath("/api")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRQ)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andDo(
            document(
                "member/insert",
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("m_username").type(JsonFieldType.STRING).description("회원 아이디"),
                    fieldWithPath("m_pass").type(JsonFieldType.STRING).description("암호"),
                    fieldWithPath("m_name").type(JsonFieldType.STRING).description("회원 이름"),
                    fieldWithPath("m_gender").type(JsonFieldType.STRING).description("회원 성별[M : 남자 , F : 여자]"),
                    fieldWithPath("m_nickname").type(JsonFieldType.STRING).description("회원 별칭"),
                    fieldWithPath("m_birth").type(JsonFieldType.STRING).description("회원 생년월일"),
                    fieldWithPath("m_phone").type(JsonFieldType.STRING).description("회원 연락처"),
                    fieldWithPath("m_addr").type(JsonFieldType.STRING).description("회원 주소"),
                    fieldWithPath("m_email").type(JsonFieldType.STRING).description("회원 이메일")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("http status 상태값"),
                    fieldWithPath("body").type(JsonFieldType.BOOLEAN).description("결과 값")
                )
            )
        )
        .andExpect(status().isOk());

        this.memberMapper.deleteTestAuth(id);
        this.memberMapper.deleteTestId(id);
    }

    

}   
