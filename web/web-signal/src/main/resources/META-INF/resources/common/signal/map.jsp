<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="map">
    <c:if  test="${empty param.js}">
        <c:import url="/common/signal/svg/${area.area}.svg"/>
    </c:if>
</div>
<c:choose>
    <c:when test="${not empty param.js}">
        <script src="/js/signalmaps/${area.area}.js"></script>
        <script>
            /* TEST MODE ${param} */
            $(document).ready(function () {
                var map = new SignalMap('#map', '${area.area}', '${area.comment}');
                SignalAreaMap.plot(map);
                // Convert raphael's verbose styling to css reducing the size of thwe output
                var ap = function (c, s) {
                    return (typeof c === 'undefined' || c === '') ? s : (c + ' ' + s);
                };
                // If p is present in a then add class s to c
                var eq = function (c, a, p, s) {
                    if (a.indexOf(p) > -1) {
                        return ap(c, s);
                    }
                    return c;
                };
                // If p is present in a and value ends with px then add class q + px value to c
                var px = function (c, a, p, q) {
                    var i = a.indexOf(p);
                    if (i > -1) {
                        i += p.length;
                        var j = a.indexOf('px', i);
                        if (j > i)
                            return ap(c, q + (a.substr(i, j - i).trim()));
                    }
                    return c;
                };
                var fix = function (i, v) {
                    var c = $(v);
                    var a = c.attr('style'), cl = c.attr('class');
                    cl = px(cl, a, 'font-size:', 'sig');
                    if (cl !== '' && typeof cl !== 'undefined')
                        c.attr('class', cl);
                    c.attr('style', '');
                };
                // Now run fix against all path, text and tspan elements
                $('#map svg path').each(fix);
                $('#map svg text').each(fix);
                $('#map svg rect').each(fix);
                $('#map svg tspan').attr('style','');
                // Run mode, ?js=test will run with live data, otherwise just show the map
            <c:choose>
                <c:when test="${param.js eq 'test'}">map.update(map);</c:when>
                <c:otherwise>map.refreshBerths(map, {});</c:otherwise>
            </c:choose>
                    });</script>
        </c:when>
        <c:otherwise>
        <script>
            $(document).ready(function () {
                var map = new SignalMap('#map', '${area.area}');
                map.update(map);
            });
        </script>
    </c:otherwise>
</c:choose>
