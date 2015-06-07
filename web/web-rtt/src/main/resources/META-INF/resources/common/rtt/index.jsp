<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>

<style>
    .ui-widget {
        padding-bottom: 0.25em;
    }
    .ui-widget label {
        width:15em;
        display: inline-block;
        text-align: right;
    }
    .ui-widget input {
        width: 10em;
        display: inline-block;
    }
</style>

<h2>Live train monitoring</h2>
<p>
    From this page you can search for any train that's due to run from any station and monitor it's progress.
    You can even go back a few days as well to see how earlier trains performed.
</p>

<h2>Search</h2>

<c:if test="${not empty msg}">
    <div class="error"><c:out value="${msg}" escapeXml="true"/></div>
</c:if>

<h2>Search by station and date</h2>

<form method="POST" action="/rtt/search">
    <div>
        <div class="ui-widget">
            <label for="searchStation">Station name:</label>
            <input id="searchStation"/>
            <input id="searchCrs" name="crs" type="hidden"/>
        </div>
    </div>
    <div class="ui-widget">
        <label for="searchDate">Date to search:</label>
        <input id="searchDate" name="date" type="date" value="${end}" min="${start}" max="${end}"/>
        <span>You can select dates from ${start} to ${end}</span>
    </div>
    <div class="ui-widget">
        <label for="searchTime">Time</label>
        <input id="searchTime" name="time" type="time"/>
    </div>
    <div class="ui-widget">
        <label></label>
        <input name="submit" type="submit" value="Search"/>
    </div>
</form>

<h3>Alternative searches</h3>

<div>
    <div class="ui-widget">
        <label id="railidlabel" for="railid">Rail ID:</label>
        <input id="railid"/>
    </div>
    <input id="station" type="hidden" name="station"/>
</div>

<script>
    $(document).ready(function () {
        function keypress(c, f) {
            c.keypress(function (e) {
                if (e.which === 13)
                    f(c);
            });
        }
        
        $("#searchStation").autocomplete({
            source: "/api/rail/1/station/search",
            minLength: 3,
            autoFocus: true,
            select: function (event, ui) {
                //document.location = "/station/" + ui.item.crs;
                $('#searchCrs').val(ui.item.crs);
                $('#searchDate').focus();
            }
        });

        keypress($('#railid'), function (c) {
            // TODO add validation here
            document.location = "/rtt/train/"+c.val()
        });

        // Clear the form. Handles case of someone using back button
        var clearcomps = [
            '#station', '#stationCrs', '#searchTime'
            '#railid'
        ];
        setTimeout(function () {
            $.each(clearcomps, function (v) {
                $(v).val('');
            });
            $('stationDate').val('${end}');
            $('#searchStation').focus();
        }, 250);
    });
</script>
