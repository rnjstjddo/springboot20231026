package org.example.springboot231026.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Custom403Handler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //System.out.println("security-handler클래스 Custom403Handler 오버라이딩 handle() 진입");

        response.setStatus(HttpStatus.FORBIDDEN.value());

        String contentType = request.getHeader("Content-Type");
        //System.out.println("security-handler클래스 Custom403Handler 오버라이딩 handle() 진입 Content-Type확인 -> "+ contentType);

        boolean jsonRequest = contentType.startsWith("application/json");

        if(!jsonRequest){
            //System.out.println("security-handler클래스 Custom403Handler 오버라이딩 handle() 진입 - Content-Type json이 아닌 경우 진입");
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }

        //System.out.println("security-handler클래스 Custom403Handler 오버라이딩 handle() 진입 - Content-Type json인 경우 진입 ");


    }
}
