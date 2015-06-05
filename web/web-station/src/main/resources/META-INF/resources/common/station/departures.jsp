<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div>
    <h4>Live departure Boards</h4>
    <div id="board">
        <div class="ldbWrapper">
            <div class="ldbTable">
                <div class="ldbHead">
                    <div class="ldbCol ldbForecast">Expected</div>
                    <div class="ldbCol ldbSched">Departs</div>
                    <div class="ldbCol ldbPlat">Plat.</div>
                    <div class="ldbCont">Destination</div>
                </div>
                <div class="ldb-enttop">
                    <span class="centered">
                        Please wait...
                    </span>
                </div>
                <div class="ldb-entbot">
                    <span>&nbsp;</span>
                </div>
            </div>
        </div>
    </div>
    <div id="message"></div>
</div>
<script>
    var ldb, ui;
    $(document).ready(function () {
        setTimeout(function () {
            if (!ui)
                ui = new UI();
            if (!ldb)
                ldb = new LDB('${location.crs}', true);
        }, 1250);
    });
</script>