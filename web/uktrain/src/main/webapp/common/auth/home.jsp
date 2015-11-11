<%-- 
    Document   : topmenu_right
    Created on : Jun 2, 2014, 9:52:52 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="homebox">
    <div class="ui-widget">
        <h2>Welcome ${sessionScope.user.name}</h2>
        <p>
            You have access to the following roles:
        </p>
        <p>
            <c:forEach var="role" items="${sessionScope.user.roles}" varStatus="stat">
                <c:if test="${not stat.first}">, </c:if>${role}
            </c:forEach>
        </p>
    </div>
</div>
<div class="homebox">
    <div class="ui-widget">
        <h2>Add social networks</h2>
        <p>You can add the following social networks as login alternatives</p>
        <c:forEach var="network" items="<%=uk.trainwatch.web.auth.SocialNetwork.values()%>">
            <div>
                <c:set var="label" value="${network.getLabel()}"/>
                <label for="${network}">${label}</label>
                <c:set var="net" value="${sessionScope.user.getSocialNetwork(label)}"/>
                <c:choose>
                    <c:when test="${not empty net}">
                        <c:choose>
                            <c:when test="${label eq 'Twitter'}">
                                @${net.screenName}
                            </c:when>
                            <c:otherwise>
                                ${network.label} attached
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <a href="${network.login}">
                            <img src="${network.image}" alt="${network.label}"/>
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
    </div>
</div>

<div class="homebox">
    <div class="ui-widget">
        <h2>Update my details</h2>
        <form method="POST" action="/updateMyDetails">
            <div>
                <label for="username">Username:</label>
                <input type="text" name="username" value="${sessionScope.user.name}" readonly="true"/>
            </div>
            <div>
                <label for="firstName">First Name</label>
                <input type="text" name="firstName" value="${sessionScope.user.firstName}"/>
            </div>
            <div>
                <label for="lastName">Last Name</label>
                <input type="text" name="lastName" value="${sessionScope.user.lastName}"/>
            </div>
            <div>
                <label for="fullName">Full Name</label>
                <input type="text" name="fullName" value="${sessionScope.user.fullName}"/>
            </div>
            <div>
                <label for="email">Email</label>
                <input type="text" name="email" value="${sessionScope.user.email}"/>
            </div>
            <div>
                <label for="homePage">Home Page</label>
                <input type="text" name="homepage" value="${sessionScope.user.homepage}"/>
            </div>
            <div>
                <label for="password">Password:</label>
                <input type="password" name="password" autocomplete="false"/>
            </div>
            <div>
                <label for="password2">confirm:</label>
                <input type="password" name="password2" autocomplete="false"/>
            </div>
            <div>
                <label for="submit"></label>
                <input type="submit" name="submit" value="Update"/>
            </div>
        </form>
    </div>
</div>
