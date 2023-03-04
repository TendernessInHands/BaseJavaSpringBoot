package com.mars.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL过滤
 * @author mars
 */
public class SqlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest request = servletRequest;
        ServletResponse response = servletResponse;
        // 从request中获取当前请求中所有的参数名称
        Enumeration<String> names = request.getParameterNames();
//        String sql = "";
        StringBuffer sql = new StringBuffer();
        while (names.hasMoreElements()) {
            // 从name中取出所有的参数名称
            String name = names.nextElement().toString();
            // 根据参数名称获取所有的参数对应的值
            String[] values = request.getParameterValues(name);
            for (int i = 0; i < values.length; i++) {
                sql.append(values[i]);
            }
        }
        String sqlStr = sql.toString();
        if (sqlValidate(sqlStr)) {
            //  拦截后的处理可以直接响应
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(500);
            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(new String("非法请求".getBytes("utf-8"), "utf-8").getBytes("utf-8"));
            outputStream.flush();
        }
        /* else {
            filterChain.doFilter(request, response);
        }*/
    }

    /**
     * sql验证
     * @param str
     * @return
     */
    protected static boolean sqlValidate(String str) {
        String badStr =
                "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
                        + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute|\\>|\\<)\\b)";
        Pattern compile = Pattern.compile(badStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(str);
        //使用正则表达式进行匹配
        boolean b = matcher.find();
        return b;
    }

    @Override
    public void destroy() {

    }
}
