// Common UI functions
var UI = (function () {
    function UI() {
        UI.messageSpan = $('<div></div>');
        UI.message = $('#message').empty().append(UI.messageSpan);
        UI.loader = $('#loader');
    }

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
            $('#stations').focus();
        }, 250);
    };

    return UI;
})();

// Live Departure Boards, refresh every 60s
var LDB = (function () {

    function LDB(crs) {
        LDB.url = '/vldb/' + crs;
        LDB.board = $('#board');
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
    function Train(rid) {
        Train.url = '/vtrain/' + rid;
        Train.messageSpan = $('<div></div>');
        Train.message = $('#message').append(Train.messageSpan);
        Train.board = $('#board');
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