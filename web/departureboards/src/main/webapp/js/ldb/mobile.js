var LDB = (function () {

    function LDB(crs) {
        LDB.url = '/vldb/' + crs;
        LDB.messageSpan = $('<div></div>');
        LDB.message = $('#message').append(LDB.messageSpan);
        LDB.board = $('#board');
        reload();
    }

    var showLoaderImpl = function () {
        $.mobile.loading("show");
    };

    var showLoader = function () {
        hideError();
        LDB.timer = setTimeout(showLoaderImpl, 250);
    };

    var hideLoader = function () {
        if (LDB.timer) {
            clearTimeout(LDB.timer);
            delete(LDB.timer);
        }
        $.mobile.loading('hide');
    };

    var reloadIn = function (timeout) {
        if (timeout)
            setTimeout(reload, timeout);
        else
            LDB.reload();
    };

    var hideError = function () {
        LDB.message.removeClass('messageVisible').addClass('messageHidden');
        LDB.messageSpan.empty();
    };

    var showError = function (msg) {
        LDB.message.addClass('messageVisible').removeClass('messageHidden');
        LDB.messageSpan.empty().append(msg);
    };

    var success = function (v) {
        hideLoader();
        reloadIn(60000);
        LDB.board.empty().append(v);
    };
    
    var notModified = function(v) {
        hideLoader();
        reloadIn(10000);
    }

    var failure = function (v) {
        hideLoader();
        showError("Failed to connect to server<br />Will attempt again shortly.");
        reloadIn(10000);
    };

    var reload = function () {
        showLoader();
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
                // apache is up but no tomcat
                503: failure,
            }
        });
    };

    return LDB;
})();
