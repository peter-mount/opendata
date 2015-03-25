<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<h1>Trust status</h1>
<p>
    The following shows the current train activity from Trust for train operator ${toc}.
</p>
<script>
    $(document).ready(function () {
        updateTrust(${toc});
    });
</script>
<div id="trustOuter">
    <div id="trustControl"></div>
    <div id="trust"></div>
</div>
