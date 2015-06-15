<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
</div>
<div class="ui-widget">
    <label></label>
    <span>You can select dates from ${start} to ${end}</span>
</div>
<div class="ui-widget">
    <label for="searchTime">Time</label>
    <select name="time" id="searchTime"></select>
</div>
<div class="ui-widget">
    <label></label>
    <input id="submit" name="submit" type="submit" value="Search"/>
</div>

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

        var crs = null;
        $("#searchStation").autocomplete({
            source: "/api/rail/1/station/search",
            minLength: 3,
            autoFocus: true,
            select: function (event, ui) {
                //document.location = "/station/" + ui.item.crs;
                crs = ui.item.crs;
                $('#searchCrs').val(crs);
                $('#searchDate').focus();
            }
        });
        var f = function (v) {
            return (v < 10 ? '0' : '') + v;
        };
        var setHour = function () {
            var c = $('#searchTime');
            var h = c.val();
            if (h === null)
                h =${time};
            else
                h = parseInt(h);
            var mh = 23;
            //var mh = $('#searchDate').val() === '${end}' ? ${time} : 23;
            //if (h > mh)
            //    h = mh;
            c.empty();
            for (var i = 0; i <= mh; i++) {
                var o = $('<option></option>').
                        appendTo(c).
                        attr({value: i}).
                        text(f(i) + '-' + f(i === 23 ? 0 : i + 1));
                if (i === h)
                    o.attr({selected: 'selected'});
            }
        };
        keypress($('#searchDate'), function (c) {
            setHour();
            $('#searchTime').focus();
        });
        $('#searchDate').change(setHour);

        var search = function () {
            var dt = $('#searchDate').val(), hr = $('#searchTime').val();
            if (crs !== null && dt !== null && typeof dt !== 'undefined' && hr !== null && hr !== 'undefined')
                document.location = '/rtt/trains/' + crs + '/' + dt + '/' + hr;
        };
        keypress($('#searchTime'), search);

        $('#searchSubmit').click(search);
        keypress($('#railid'), function (c) {
            // TODO add validation here
            document.location = "/rtt/train/" + c.val()
        });
        // Clear the form. Handles case of someone using back button
        var clearcomps = [
            '#station', '#stationCrs', '#searchTime',
            '#railid'
        ];
        setTimeout(function () {
            $.each(clearcomps, function (v) {
                $(v).val('');
            });
            $('stationDate').val('${end}');
            setHour();
            $('#searchStation').focus();
        }, 250);
    });
</script>
