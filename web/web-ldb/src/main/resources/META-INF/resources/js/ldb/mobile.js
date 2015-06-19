// Common UI functions
var UI = (function () {
    function UI() {
        UI.messageSpan = $('<div></div>');
        UI.message = $('#message').empty().append(UI.messageSpan);
        UI.loader = $('#loading');

        UI.settings = $('#settings');
        if (UI.settings.length > 0) {
            UI.settings.click(showSettings);
            UI.settingsPanel = $('#settingsPanel');
            $('#settingsCancel').click(hideSettings);
            $('#settingsSave').click(saveSettings);
            UI.defaultSettings();
        }
    }

    var set = function (v, d) {
        if ((typeof v === 'undefined' || v === null) && typeof d !== 'undefined')
            v = d;
        return v === 't';
    };

    UI.enabled = function (n) {
        return $.cookie(n) === 't';
    }

    UI.show = function (c, n, t, f) {
        c.css({display: $.cookie(n) === 't' ? t : f});
    };

    var showSettings = function () {
        var cookies = $.cookie();
        $.each(UI.settingsPanel.find('input'), function (i, c) {
            c = $(c);
            c.prop('checked', set(cookies[$(c).attr('name')], $(c).attr('default')));
        });
        UI.settingsPanel.css({display: 'block'});
    };

    var hideSettings = function () {
        UI.settingsPanel.css({display: 'none'});
    };

    UI.opts = {
        expires: 365,
        path: '/'
    };

    UI.defaultSettings = function () {
        if (typeof UI.settingsPanel !== 'undefined') {
            var cookies = $.cookie();
            $.each(UI.settingsPanel.find('input'), function (i, c) {
                c = $(c);
                var n = $(c).attr('name');
                if (typeof cookies[n] === 'undefined')
                    $.cookie(n, $(c).attr('default'), UI.opts);
            });
        }
    };

    var saveSettings = function () {
        $.each(UI.settingsPanel.find('input'), function (i, c) {
            c = $(c);
            $.cookie($(c).attr('name'), $(c).is(':checked') ? 't' : 'f', UI.opts);
        });
        if (typeof UI.settingsSaved !== 'undefined')
            UI.settingsSaved();
        hideSettings();
    };

    var showLoaderImpl = function () {
        UI.loader.css({display: 'block'});
    };

    /**
     * Hide the error message
     */
    UI.hideError = function () {
        UI.message.removeClass('messageVisible').addClass('messageHidden');
        UI.messageSpan.empty();
    };

    /**
     * Show an error message
     * @param {type} msg Message to show
     */
    UI.showError = function (msg) {
        UI.message.addClass('messageVisible').removeClass('messageHidden');
        UI.messageSpan.empty().append(msg);
    };

    /**
     * Shows the loading icon when connection is slow
     */
    UI.showLoader = function () {
        UI.hideError();
        UI.timer = setTimeout(showLoaderImpl, 25);
    };

    /**
     * Hine the loader icon
     */
    UI.hideLoader = function () {
        if (UI.timer) {
            clearTimeout(UI.timer);
            delete(UI.timer);
        }
        UI.loader.css({display: 'none'});
    };

    /**
     * Configure the station search box
     */
    UI.search = function () {
        $("#stations").autocomplete({
            source: "/search",
            minLength: 3,
            autoFocus: true,
            select: function (event, ui) {
                document.location = "/mldb/" + ui.item.crs;
            }
        });

        // Give the search focus
        setTimeout(function () {
            $('#stations').val('').focus();
        }, 250);
    };

    return UI;
})();

// Live Departure Boards, refresh every 60s
var LDB = (function () {

    function LDB(crs, small) {
        LDB.url = (small ? '/sldb/' : '/vldb/') + crs;
        LDB.board = $('#board');
        UI.settingsSaved = LDB.applySettings;
        reload();
    }

    var reloadIn = function (timeout) {
        if (timeout)
            setTimeout(reload, timeout);
        else
            LDB.reload();
    };

    var success = function (v) {
        UI.hideLoader();
        reloadIn(60000);
        LDB.board.empty().append(v);
        LDB.applySettings();
    };

    LDB.applySettings = function () {
        UI.show($('.trainTerminated'), 'ldbTerm', 'block', 'none');
        UI.show($('.callList'), 'ldbCall', 'inline-block', 'none');
        UI.show($('.callListTerminated'), 'ldbTermCall', 'inline-block', 'none');
        UI.show($('.callListCancelled'), 'ldbCanCall', 'inline-block', 'none');

        var r = 0;
        $.each($('.row'), function (i, c) {
            c = $(c);
            if (r % 2 === 0)
                c.addClass('altrow');
            else
                c.removeClass('altrow');
            if (!((c.hasClass('trainTerminated') && !UI.enabled('ldbTerm'))
                    || (c.hasClass('callList') && !UI.enabled('ldbCall'))
                    || (c.hasClass('callListTerminated') && !UI.enabled('ldbTermCall'))
                    || (c.hasClass('callListCancelled') && !UI.enabled('ldbCanCall'))
                    ))
                r++;
        });
    };

    var notModified = function (v) {
        UI.hideLoader();
        reloadIn(10000);
    }

    var failure = function (v) {
        UI.hideLoader();
        UI.showError("Failed to connect to server<br />Will attempt again shortly.");
        reloadIn(10000);
    };

    var reload = function () {
        UI.showLoader();
        $.ajax({
            url: LDB.url,
            type: 'GET',
            async: true,
            statusCode: {
                200: success,
                // Shouldn't but may occur, retry in 10 seconds
                304: notModified,
                // tomcat is up but no app
                404: failure,
                // Internal error
                500: failure,
                // Proxy error
                502: failure,
                // apache is up but no tomcat
                503: failure,
            }
        });
    };

    return LDB;
})();

// Train details - refresh every 60s
var Train = (function () {
    function Train(rid, rtt) {
        Train.url = (rtt ? '/rtt/vtrain/' : '/vtrain/') + rid;
        Train.board = $('#board');
        Train.rtt = rtt;
        if (rtt)
            reloadIn(60000);
        else
            reload();
    }

    var reloadIn = function (timeout) {
        if (timeout)
            setTimeout(reload, timeout);
        else
            Train.reload();
    };

    var success = function (v) {
        UI.hideLoader();
        reloadIn(60000);
        Train.board.empty().append(v);
        if (Train.rtt)
            Track.update();
    };

    var notModified = function (v) {
        UI.hideLoader();
        reloadIn(10000);
    };

    var failure = function (v) {
        UI.hideLoader();
        UI.showError("Failed to connect to server<br />Will attempt again shortly.");
        reloadIn(10000);
    };

    var reload = function () {
        UI.showLoader();
        $.ajax({
            url: Train.url,
            type: 'GET',
            async: true,
            statusCode: {
                200: success,
                // Shouldn't but may occur, retry in 10 seconds
                304: notModified,
                // tomcat is up but no app
                404: failure,
                // Internal error
                500: failure,
                // Proxy error
                502: failure,
                // apache is up but no tomcat
                503: failure,
            }
        });
    };

    return Train;
})();