<%-- 
    Document   : topmenu
    Created on : Jun 2, 2014, 9:50:46 AM
    Author     : Peter T Mount
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${requestScope.euCookie}">
</c:if>
<div id="eu_cookie_notice">
    <img src="/images/cookie.png" class="left" width="48" height="48"/>
    This site uses cookies for session &amp; settings management.
    By continuing to use our website, you are agreeing to our use of cookies.
    [<a href="//uktra.in/Cookies">More Info</a>]
    <div class="clear"></div>
</div>
<script>
    $(document).ready(function () {
        var a = $.cookie('euCookieMonster') !== 'accepted';
        var c = $('#eu_cookie_notice');
        c.css({display: a ? 'block' : 'none'});
        if (a) {
            // Set the cookie & briefly show the notice
            $.cookie('euCookieMonster', 'accepted', {expires: 365, path: '/'});
            c.click(function () {
                c.hide(200);
            });
            setTimeout(function () {
                c.hide(1000);
            }, 5000);
        }
    });
</script>
