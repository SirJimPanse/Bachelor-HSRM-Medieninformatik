<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Kundenportal - Login</title>
</head>
<body>

<h1>Kundenanmeldung</h1>
<h3>Bitte melden Sie sich an</h3>

<%-- kundenummer = paswort / username genannt, falls sich da mal was dran ändert --%>
<form action="Kunde" method="post">
	<label>Kundennummer</label> <input type="text" name="username" /><br />
	<label>Passwort</label> <input type="password" name="password" /><br />
	<input type="submit" name="login" value="Login" />
</form>

</body>
</html>