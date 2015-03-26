<%@page contentType="text/html" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UK Train Trust Workbench</title>
        <link rel="stylesheet" href="/css/tcmain.css" />
        <link rel="stylesheet" href="/css/uktrain.css" />
        <link rel="stylesheet" href="/css/trust.css" />
        <script src="/js/jquery/jquery.js"></script>
        <script src="/js/menu_jquery.js"></script>
        <script src="/js/jquery/jquery-ui.js"></script>
        <script src="/js/trust.js"></script>
        <script>
            var toc =${toc};
            $(document).ready(initTrust);
        </script>
    </head>
    <body>
        <div id="trust">
            <div id="trustTop">
                <div id="tocs">
                    <span>Operator</span>
                    <select>
                        <c:forEach var="t" items="${tocs.keySet()}">
                            <option value="${t}"<c:if test="${t==toc}"> selected="selected"</c:if>>${tocs.get(t)}</option>
                        </c:forEach>
                    </select>
                </div>
                <div id="refreshRate">
                    <span>Refresh rate</span>
                    <select>
                        <option value="1">1m</option>
                        <option value="5">5m</option>
                        <option value="10">10m</option>
                        <option value="15">15m</option>
                    </select>
                </div>
            </div>
        </div>
    </body>
</html>
