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

<script src="/js/ldb/mobile.js"></script>
<script>
    $(document).ready(function () {
        new UI();
    });
</script>

    <h2>Welcome to uktra.in</h2>

    <p>
        From this page you can search for any train for any time from two months ago to a year ahead.
    </p>
    <p>For train's that are actually running you can even watch it's progress in real time.</p>
    <%--
    <p>
        Please Note: London Underground (Tube) and Docklands Light Railway (DLR) are only
        available as departure boards, no historical/future data is available for those services.
    </p>
    --%>

<div class="homebox">
    <tiles:insertAttribute name="search"/>
    Alternatively use the <a href="/timetable/">Timetable</a> or <a href="/rtt/">Train tracker</a> tools.
</div>

    <%-- Hide station search for now, reinstate post release
<div class="homebox">
    <h2>Search for stations</h2>
    Search for a station by name or Post Code
    <div class="ui-widget">
        <label id="stationlabel" for="station">Where:</label>
        <input id="station"/>
    </div>

    <div class="ui-widget">
        <label for="stationSubmit"></label>
        <input id="stationSubmit" name="submitStation" type="submit" value="Search"/>
    </div>

        Alternatively you can use the <a href="/station/">Station Index</a>
</div>
    --%>

<div class="clear"></div>
