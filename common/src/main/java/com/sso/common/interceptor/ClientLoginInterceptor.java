package com.sso.common.interceptor;

import com.sso.common.api.ApiService;
import com.sso.common.constants.ClientCache;
import com.sso.common.constants.SsoConstants;
import com.sso.common.model.LoginUser;
import com.sso.common.util.HttpUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClientLoginInterceptor implements HandlerInterceptor {


    /**
     * 两者session的id不一样
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(httpServletRequest.getServletContext());

        ApiService apiService = webApplicationContext.getBean(ApiService.class);


        //获取session中登录成功标志
        HttpSession session = httpServletRequest.getSession();

        System.out.println("客户端拦截器上获取到的session ID: " + session.getId());

        //获取当前用户登录标记
        Object sessionAttribute = session.getAttribute(SsoConstants.SESSION_LOGIN_FLAG);

        System.out.println("客户端拦截器获取到的登录成功标志为： " + sessionAttribute);


        String tokenParam = httpServletRequest.getParameter(SsoConstants.TOKEN_PARAM_NAME);


        String requestContextPath = HttpUtil.getRequestContextPath(httpServletRequest);


        //检验是否存在登录成功标志
        if (sessionAttribute == null) {

            /**
             * 当第一次被拦截的时候，没有成功标志，也没有token，所以不能校验token
             * 当第二次被拦截到的时候，就是跳转回来的时候，此时还是没有成功标志，但是有token。
             */

            //检查参数是否携带token,     token验证成功后就在这次会话中加入登录标记

            System.out.println("客户端拦截器获取到的token： " + tokenParam);

            //带有token参数,调用远程sso sever方法验证token是否正确
            if (tokenParam != null) {

                //本站点的请求路径。如果token正确，就把该站点的会话信息保存到已经登录的表中

                String userParam = apiService.validateToken(tokenParam, requestContextPath);

                if (userParam != null) {

                    //token正确，就在session中增加登录成功标志.值就是token
                    session.setAttribute(SsoConstants.SESSION_LOGIN_FLAG, tokenParam);
                    System.out.println("客户端拦截器设置登录成功标志的session ID为：" + session.getId());

                    //模拟数据库

                    LoginUser loginUser = new LoginUser();
                    loginUser.setUserName(userParam);
                    loginUser.setPassword(userParam);

                    session.setAttribute(SsoConstants.LOGIN_USER_SESSION, loginUser);
                    return true;
                } else {
                    remoteLogin(httpServletRequest, httpServletResponse);
                    return false;
                }
            } else {
                remoteLogin(httpServletRequest, httpServletResponse);
                return false;
            }
        } else {

            /**
             * 当前用户登录了。存在了session 也有token。
             */



            //如果当前用户登录了，但是在其他地方被注销了，session中存在了要删除的标志。此时当再次跳转其他页面的时候，要清除session表示已经退出了。
            ClientCache clientCache = webApplicationContext.getBean(ClientCache.class);

            //是在发出注销通知后，增加进去的无效的登录标志 key
            boolean containsInvalidKey = clientCache.containsInvalidKey(sessionAttribute.toString());

            
            if (containsInvalidKey) {
                session.removeAttribute(SsoConstants.SESSION_LOGIN_FLAG);
                session.invalidate();
                remoteLogin(httpServletRequest, httpServletResponse);
                return false;
            } else {

                //如果用户登录了，但还是要去校验token，此时自带了token。

                String userParam = apiService.validateToken(tokenParam, requestContextPath);


                if(userParam!=null){
                    //token正确，就在session中设置登陆者
                    System.out.println("客户端拦截器设置登录成功标志的session ID为：" + session.getId());

                    //模拟数据库

                    LoginUser loginUser = new LoginUser();
                    loginUser.setUserName(userParam);
                    loginUser.setPassword(userParam);

                    session.setAttribute(SsoConstants.LOGIN_USER_SESSION, loginUser);
                    return true;
                }else{
                    remoteLogin(httpServletRequest, httpServletResponse);
                    return false;
                }
            }
        }
    }

    /**
     * 跳转到sso服务器去认证
     */
    private void remoteLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        //因为sso服务端的请求路径后面多出来sso
        String ssourl = "http://localhost:8554/sso/";


        String path = HttpUtil.encodeUrl(HttpUtil.getRequestFullPathNoParam(httpServletRequest));
        try {
            System.out.println("客户端浏览器上输入的url为： " + path);
            System.out.println("客户端拦截器向服务端跳转的url为： " + ssourl + "?" + SsoConstants.REDIRECT_PARAM_NAME + "=" + path);

            httpServletResponse.sendRedirect(ssourl + "?" + SsoConstants.REDIRECT_PARAM_NAME + "=" + path);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
