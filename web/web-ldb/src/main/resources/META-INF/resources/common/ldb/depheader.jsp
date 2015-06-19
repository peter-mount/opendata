<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<img id="settings" src="/images/search.png" />
<div class="ldbWrapper">
    <%--
    <div class="ldbUpdated">Last updated: <t:time value="${lastUpdated}"/></div>
    --%>
    <div class="ldbTable">
        <div class="ldbLoc">
            <div class="ldbCont">${location.location}</div>
        </div>
        <div class="ldbHead">
            <div class="ldbCol ldbForecast">Expected</div>
            <div class="ldbCol ldbSched">Departs</div>
            <div class="ldbCol ldbPlat">Plat.</div>
            <div class="ldbCont">Destination</div>
        </div>
    </div>
</div>

<div id="settingsPanel">
    <div class="settingsInner">
        <h2>Options</h2>
        The following options are available:
        <table>
            <tr><th class="center" colspan="2">Services</th></tr>
            <tr>
                <th>Show services terminating here</th>
                <td><input id="settingTerm" name="ldbTerm" default="f" type="checkbox"/></td>
            </tr>
            
            <tr><th class="center" colspan="2">Calling points</th></tr>
            <tr>
                <th>Show for running services</th>
                <td><input id="settingCall" name="ldbCall" default="t" type="checkbox"/></td>
            </tr>
            <tr>
                <th>Show for terminated services</th>
                <td><input id="settingTermCall" name="ldbTermCall" default="t" type="checkbox"/></td>
            </tr>
            <tr>
                <th>Show for cancelled services</th>
                <td><input id="settingCanCall" name="ldbCanCall" default="t" type="checkbox"/></td>
            </tr>
        </table>
        <div>
            <a id="settingsCancel" class="ldbbutton">Cancel</a>
            <a id="settingsSave" class="ldbbutton">Save</a>
        </div>
    </div>
</div>