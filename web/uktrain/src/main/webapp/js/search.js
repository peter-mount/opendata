var Search = (function () {
    function Search(now, hr, start, end) {
        Search.now = now;
        Search.start = start;
        Search.end = end;

        Search.hr = hr;
        setHour();
        
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

        keypress($('#searchDate'), function (c) {
            setHour();
            $('#searchTime').focus();
        });
        $('#searchDate').change(setHour);

        keypress($('#searchTime'), search);

        $('#searchSubmit').click(search);

        // RID search
        var rid = $('#railid');
        keypress(rid, ridSearch);
        $('#submitRailId').click(ridSearch);

        // Clear the form. Handles case of someone using back button
        var clearcomps = [
            '#station', '#stationCrs', '#searchTime',
            '#railid'
        ];
        setTimeout(function () {
            $.each(clearcomps, function (v) {
                $(v).val('');
            });
            $('stationDate').val(Search.end);
            setHour();
            $('#searchStation').focus();
        }, 250);
    }

    function keypress(c, f) {
        c.keypress(function (e) {
            if (e.which === 13)
                f(c);
        });
    }
    var f = function (v) {
        return (v < 10 ? '0' : '') + v;
    };
    var setHour = function () {
        var c = $('#searchTime');
        var h = c.val();
        if (h === null)
            h = Search.hr;
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
                    attr({value: i < 10 ? ('0' + i) : i}).
                    text(f(i) + '-' + f(i === 23 ? 0 : i + 1));
            if (i === h)
                o.attr({selected: 'selected'});
        }
    };

    Search.search = function () {
        var dt = $('#searchDate').val(), hr = $('#searchTime').val();
        if (crs !== null && dt !== null && typeof dt !== 'undefined' && hr !== null && hr !== 'undefined') {
            UI.showLoader();
            document.location = '/rtt/trains/' + crs + '/' + dt + '/' + hr;
        }
    };

        var ridSearch = function (c) {
            if (rid.val() !== '') {
                UI.showLoader();
                document.location = "/rtt/train/" + rid.val();
            }
        };
        
    return Search;
})();
