
package com.travel.jeju.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.jeju.constant.AuthConstants;
import com.travel.jeju.constant.FailType;
import com.travel.jeju.dto.MemberDto;
import com.travel.jeju.mapper.MemberMapper;
import com.travel.jeju.model.common.BaseErrorModel;
import com.travel.jeju.model.member.LoginRQ;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{

    private MemberMapper memberMapper;

    public CustomAuthenticationFailureHandler(MemberMapper memberMapper){
        this.memberMapper = memberMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        
        LoginRQ loginRQ = (LoginRQ)request.getSession().getAttribute(AuthConstants.LOGIN_INFO_SESSION.getValue());

        log.warn("exception => {}", exception.getMessage());
        log.warn("users => {}", loginRQ);

        FailType failType = FailType.fromMessage(exception.getMessage());

        // Logging logging = new Logging();
        // logging.setUsername(loginRQ.getUsername());
        // logging.setCredentials(loginRQ.getPassword());
        // logging.setIp(request.getRemoteAddr());

        switch (failType) {
            case USER_NOT_FOUNT:
                // logging.setType(LoggingType.LOGIN_NOT_USER.getValue());
                // logging.setMessage("유저를 찾을 수 없습니다.");

                log.warn("유저를 찾을 수 없습니다.");
                break;
            case ACOUNT_DISABLE:
                // logging.setType(LoggingType.LOGIN_DISABLED.getValue());
                // logging.setMessage("계정이 비활성화 되었습니다.");

                log.warn("계정이 비활성화 되었습니다.");
                break;
            case ACOUNT_EXPRIED:
                // logging.setType(LoggingType.LOGIN_EXPIRED.getValue());
                // logging.setMessage("계정이 만료되었습니다.");
                // logging.setCredentials("[PROTECT PASSWORD]");

                log.warn("계정이 만료되었습니다. [PROTECT PASSWORD]");
                break;
            case ACOUNT_LOCK:
                // logging.setType(LoggingType.LOGIN_LOCKED.getValue());
                // logging.setMessage("계정이 잠겼습니다.");
                // logging.setCredentials("[PROTECT PASSWORD]");

                log.warn("계정이 잠겼습니다. [PROTECT PASSWORD]");
                break;
            case PASS_NOT_MATCH:
                // logging.setType(LoggingType.LOGIN_WRONG_PW.getValue());
                // logging.setMessage("비밀번호를 틀렸습니다.");

                MemberDto users = memberMapper.memberSelect(loginRQ.getUsername());

                int loginFailCount = users.getM_pass_fail_count();

                if(loginFailCount >= 4){
                    users.setM_account_non_lock(false);
                } else {
                    users.setM_account_non_lock(true);
                }

                // TODO :: 로그인 5회 잠금 기능 해제 추후 다시 주석 해제 할 것
                // users.setM_pass_fail_count(loginFailCount+1);
                users.setM_pass_fail_count(loginFailCount);
                memberMapper.loginFailUpdate(users);
                break;
        }

        responseFail(failType, response);
        // loggingQueryRepository.insert(logging);
    }

    private void responseFail(FailType failType, HttpServletResponse response){

        BaseErrorModel baseErrorModel = new BaseErrorModel();
        baseErrorModel.setCode("400");
        baseErrorModel.setDesc(failType.getValue());

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(baseErrorModel));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
