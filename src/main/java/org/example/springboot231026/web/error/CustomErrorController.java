package org.example.springboot231026.web.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController  implements ErrorController {

    @Override
    public String getErrorPath() {
        return null;
    }

    @GetMapping("/error")
        public String handleError(HttpServletRequest request) {

            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            // error로 들어온 에러의 status를 불러온다 (ex:404,500,403...)

            if(status != null){
                int statusCode = Integer.valueOf(status.toString());

                if(statusCode == HttpStatus.NOT_FOUND.value()) {
                    return "error/error";
                } else {
                    return "error/error";
                }
            }

            return "error/error";
        }
    }
