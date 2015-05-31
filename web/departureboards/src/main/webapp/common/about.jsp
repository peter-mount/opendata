<%@page contentType="text/html" pageEncoding="UTF-8"%><!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div id="home">

    <p>
        This application provides real time departure board information for every railway station in England, Scotland and Wales.
        When you navigate through the site, you will be shown the trains for up to the next hour.
    </p>

    <p>
        The application is a view on the new real time feed from National Rail Enquiries thats now available to
        <a href="//uktra.in/">uktra.in</a>.
        This feed provides both timetabled times for services but also actual as well as the predicted arrival &amp; departure times for services,
        so when a service is delayed then we will show the same predicted times as National Rail Enquiries have access to.
    </p>

    <p>
        Note: Unlike some other sites which use a webservice at NRE to present the boards, this is not. What you see here is what we've received from
        the feed so it's an example of the data which we now have available for other purposes.
    </p>

    <p>
        For more information on the open data:
    </p>

    <ul>
        <li><a href="http://uktra.in/">Parent uktra.in website.</a></li>
        <li><a href="http://nrodwiki.rockshore.net/index.php/Main_Page">Open Rail Data wiki.</a></li>
        <li><a href="http://nrodwiki.rockshore.net/index.php/Darwin:Push_Port">Darwin Push Port feed details.</a></li>
        <li><a href="https://groups.google.com/forum/#!forum/openraildata-talk">Open Rail Data forum</a></li>
    </ul>

</div>