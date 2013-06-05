<%@ include file="kundeHeader.jsp" %>

<%-- alle filme anzeigen --%>
<h1>Filmübersicht</h1>
<h3>Wählen Sie einen Film</h3>

<jsp:useBean id="sb" class="de.medieninf.webanw.sakila.SakilaBean" />
<table>
	<tr>
		<th>ID</th>
		<th>Titel</th>
		<th>Kategorie</th>
		<th>Beschreibung</th>
	</tr>
	<c:forEach var="f" items="${sb.getFilms()}" >
		<tr>
			<td><c:out value="#${f.getFilmId()}" /></td>
			<td><a href="KundeFilmDetail?id=${f.getFilmId()}"><c:out value="${f.getTitle()}" /></a></td>
			<td><c:out value="${f.getCategory().getName()}" /></td>
			<td><c:out value="${f.getDescription()}" /></td>
		</tr>
	</c:forEach>
</table>

</body>
</html>