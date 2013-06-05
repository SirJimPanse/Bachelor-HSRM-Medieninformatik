<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Kundenportal</title>
</head>
<body>

<%-- benutzer info --%>
<jsp:useBean id="cb" class="a4.CustomerBean" />
<jsp:setProperty property="customer" name="cb" value="${sessionScope.customerID}"/>
<form action="Kunde" method="post">
	<p>Sie sind angemeldet als ${cb.firstName} ${cb.lastName} <input type="submit" name="logout" value="abmelden" /></p>
</form>

<%-- zur zeit ausgeliehen --%>
<c:if test="${cb.notReturnedInventories != null}">
	<h3>zur Zeit geliehen</h3>
	<c:forEach var="copy" items="${cb.notReturnedInventories}">
		${copy.film.title} (Kopie #${copy.inventoryId})<br />
	</c:forEach>
</c:if>
<c:if test="${cb.notReturnedInventories == null}">
	<h4>Sie haben aktuell nichts ausgeliehen</h4>
</c:if>

<hr />
