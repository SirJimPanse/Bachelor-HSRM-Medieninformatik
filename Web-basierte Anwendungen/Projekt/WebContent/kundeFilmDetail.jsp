<%@ include file="kundeHeader.jsp" %>

<p><a href="Kunde">zurück zur Übersicht</a></p>

<%-- film details anzeigen --%>
<jsp:useBean id="fb" class="a4.FilmBean" />
<jsp:setProperty property="film" name="fb" value="${sessionScope.filmID}" />

<h1>Filmdetails</h1>
<h3>#${fb.filmId} | ${fb.title}</h3>

<h2>Informationen</h2>
<table>
	<tr>
		<td>Beschreibung</td> <td>${fb.description}</td>
	</tr>
	<tr>
		<td>Kategorie</td> <td>${fb.category}</td>
	</tr>
	<tr>
		<td>Ausleih-Dauer</td> <td>${fb.rentalDuration}</td>
	</tr>
	<tr>
		<td>Ausleihgebühr</td> <td>${fb.rentalRate}</td>
	</tr>
	<tr>
		<td>Länge</td> <td>${fb.length}</td>
	</tr>
	<tr>
		<td>Bewertung</td> <td>${fb.rating}</td>
	</tr>
	<tr>
		<td>Pfandkosten</td> <td>${fb.replacementCost}</td>
	</tr>
</table>

<%-- freie kopie ausleihen oder fehlermeldung --%>
<h2>Kopien</h2>
<c:if test="${fb.freeInventories != null}">
	<form method="post">
		<p>
			<input type="hidden" name="rentFilmID" value="${fb.filmId}" />
			<input type="submit" name="ausleihen" value="beliebige Kopie ausleihen" />
		</p>
	</form>
</c:if>
<c:if test="${fb.freeInventories == null}">
	<h4>Es sind z.Z. keine freien Kopien vorhanden</h4>
</c:if>

<%-- alle kopien des films anzeigen / mit meldung, ob ausgeliehen / ggf. von mir ausgeliehen --%>
<h3>Übersicht aller Kopien</h3>
<table>
	<c:forEach var="copy" items="${fb.inventories}" >
		<tr>
			<td>${copy.inv.inventoryId}</td>
			<td>
				
				<c:if test="${copy.rented == true}">
					(<c:if test="${copy.customer.customerId == cb.customerId }">von mir </c:if>ausgeliehen)
				</c:if>
				
			</td>
		</tr>
	</c:forEach>
</table>

</body>
</html>