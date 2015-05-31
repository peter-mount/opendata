<%-- 
    Document   : mobiledepartures
    Created on : 20-May-2015, 08:39:33
    Author     : peter
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div id="board"></div>
<div id="message"></div>
<script>
    var ldb, ui;
    $(document).ready(function () {
        setTimeout(function () {
            if (!ui)
                ui = new UI();
            if (!ldb)
                ldb = new LDB('${location.crs}');
        }, 250);
    });
</script>