<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="/css/player.css">
<style type="text/css">
button {
	background-color: #4CAF50; /* Green */
	border: none;
	color: white;
	padding: 10px 20px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 12px;
	margin: 4px 2px;
	cursor: pointer;
	border-radius: 5px;
}

#homeButton {
	background-color: orange;
	background-color: orange;
	border: none;
	color: white;
	padding: 5px 10px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 12px;
	margin: 4px 2px;
	cursor: pointer;
	border-radius: 5px;
}
</style>
<meta charset="UTF-8">
<title>User</title>
</head>
<body>

	<%
	Boolean isValid = (Boolean) request.getAttribute("isValid");
	String errorMessage = (String) request.getAttribute("errorMsg");

	%>
	<div class="container">
		<h1>Welcome, Player!</h1>
		<%
		if (isValid != null && !isValid) {
		%>
		<div class="error-msg" style="color: red; font-weight: bold;">
			<%=errorMessage%>
		</div>
		<%
		}
		%>
		<form action="/username" method="post">
			<label for="username">Please enter your username:</label> <input
				type="text" id="username" name="username">
			<button id="submit-username" type="submit">Submit</button>
		</form>
		<form action="/welcome" method="get">
			<button id="homeButton" type="submit" name="toHomePage">Home
				Page</button>
		</form>
	</div>
</body>
</html>