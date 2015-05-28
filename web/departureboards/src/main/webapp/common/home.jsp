<%@page contentType="text/html" pageEncoding="UTF-8"%><!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<div class="ui-widget">
    <label id="stationlabel" for="station">Please enter the station you want to view below: </label>
    <input id="stations"/>
</div>

<p>
    Live departure boards for every UK Rail Station in a mobile friendly format by the team behind
    <a href="http://twitter.com/TrainWatch">@TrainWatch</a>
    and <a href="http://uktra.in">uktra.in</a>.
</p>

<p>
    To use simply enter the UK Rail station name in the box above and you will be shown the current boards for that station.
</p>

<p>
    By clicking the destination name on those boards you will be able to view the current progress of that service.
</p>

<script>
    $(document).ready(function () {
        LDB.search();
    });
</script>