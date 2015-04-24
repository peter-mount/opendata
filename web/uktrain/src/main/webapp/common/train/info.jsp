<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertAttribute name="details"/>

<c:if test="${train.schedulePresent}">
    <tiles:insertAttribute name="schedule"/>
</c:if>

<tiles:insertAttribute name="forecast"/>
