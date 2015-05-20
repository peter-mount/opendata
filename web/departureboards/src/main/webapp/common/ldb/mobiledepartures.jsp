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
<script>
    $(document).ready(function () {
        var reload = function () {
            $.mobile.loading("show");
            setTimeout(reload, 60000);
            $.ajax({
                url: '/vldb/${location.crs}',
                type: 'GET',
                async: true,
                statusCode: {
                    200: function (v) {
                        $.mobile.loading('hide');
                        $('#board').empty().append(v);
                    }
                }
            });
        };
        reload();
    });
</script>