<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2>UK Timetables</h2>
<p>
    From this page you can query the current timetable for any UK Rail station.
</p>

<p>
    The timetable shows every planned train from today and up to 12 months into the future.
</p>
<style>
    .ui-widget {
        padding-bottom: 0.25em;
    }
    label {
        width:10em;
        display: inline-block;
        text-align: right;
    }
</style>
<form method="GET" action="/timetable/search">
    <div class="ui-widget">
        <label id="stationlabel" for="stationLabel">Station:</label>
        <input id="stationLabel"/>
    </div>
    <div class="ui-widget">
        <label id="datelabel" for="date">Date to search:</label>
        <input id="date" name="date" type="date" value="${requestScope.date}"/>
    </div>
    <div class="ui-widget">
        <label></label>
        <input name="submit" type="submit" value="Search"/>
    </div>
    <input id="station" type="hidden" name="station"/>
</form>

<div class="notice">
    Although all efforts have been made to make this accurate,
    this functionality has been provided for research purposes only and
    should not be relied upon for normal daily use - although it will
    help.
</div>

<script>
    $(document).ready(function () {
        $("#stationLabel").autocomplete({
            source: "/api/rail/1/station/search",
            minLength: 3,
            autoFocus: true,
            select: function (event, ui) {
                $('#station').val(ui.item.crs);
                $('#date').focus();
            }
        });

        setTimeout(function () {
            $('#date').val('${requestScope.date}');
            $('#station').val('');
            $('#stationLabel').val('').focus();
        }, 250);
    });
</script>
