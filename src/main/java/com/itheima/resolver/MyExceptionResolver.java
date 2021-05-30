package com.itheima.resolver;

//import org.springframework.security.access.AccessDeniedException;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class MyExceptionResolver implements HandlerExceptionResolver {
//
//    /**
//     *
//     * @param httpServletRequest
//     * @param httpServletResponse
//     * @param o 出现异常的对象
//     * @param e 出现异常的信息
//     * @return
//     */
//    @Override
//    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
//        ModelAndView model = new ModelAndView();
//        // model.addObject("errorMsg",e.getMessage());
//
//        // 指定不同的跳转页面 错误类型不要导错是springSecurity
//        if (e instanceof AccessDeniedException) {
//            model.setViewName("forward:/403.jsp");
//        }else{
//            model.setViewName("forward:/500.jsp");
//        }
//
//        return model;
//    }
//}



import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 添加专门的注释
@ControllerAdvice
public class MyExceptionResolver {

    // 权限不足
    @ExceptionHandler(AccessDeniedException.class)
    public String handleException() {
        return "redirect:/403.jsp";
    }

    // 连接超时
    @ExceptionHandler(RuntimeException.class)
    public String runtimeException() {
        return "redirect:/500.jsp";
    }

}