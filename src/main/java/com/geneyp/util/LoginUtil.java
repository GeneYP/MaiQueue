package com.geneyp.util;

import com.alibaba.fastjson.JSON;
import com.geneyp.common.R;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class LoginUtil {

    public static void loginRequired(HttpServletResponse response) {
        R<Void> result = R.error("未登录");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSON.toJSON(result));
            writer.flush();
        } catch (IOException e) {
            log.error("登录异常", e);
        }
    }
}
