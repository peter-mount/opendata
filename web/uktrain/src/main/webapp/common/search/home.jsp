<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="cms" uri="http://uktra.in/tld/cms" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%--
<cms:page page="Main_Page" strip="false"/>
--%>
<h1>Welcome to uktra.in</h1>

<p>
    From this page you can search for any train that's due to run from any station today,
    look at the time table up to a year ahead (${end}) or look back historically back to ${start}.
</p>
<p>For train's that are actually running you can even watch it's progress in real time.</p>
<p>
    Please Note: London Underground (Tube) and Docklands Light Railway (DLR) are only
    available as departure boards, no historical/future data is available for those services.
</p>

<c:if test="${not empty msg}">
    <div class="error"><c:out value="${msg}" escapeXml="true"/></div>
</c:if>

    
<tiles:insertAttribute name="search"/>

<script src="/js/ldb/mobile.js"></script>
<script>
            $(document).ready(function () {
                new UI();
            });
</script>
