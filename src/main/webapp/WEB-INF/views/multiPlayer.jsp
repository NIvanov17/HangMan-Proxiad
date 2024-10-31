<%@ page import="enums.CategoryName"%>
<%@ page import="model.Word"%>
<html>
<head>
<link rel="stylesheet" href="/css/multiPlayer.css">
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
}

label[for="category"] {
	color: #333;
	display: block;
	margin-bottom: 8px;
}

select#category {
	width: 100%;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	font-size: 16px;
	background-color: #f9f9f9;
	color: #333;
	transition: border-color 0.3s ease;
}
</style>
</head>
<body>

	<%
	Boolean isValid = (Boolean) request.getAttribute("isWordValid");
	String errorMessage = (String) request.getAttribute("errorMessage");
	%>
	<div class="container">
		<h2>Multiplayer Game Started!</h2>

		<%
		if (isValid != null && !isValid) {
		%>
		<div class ="error-msg" style="color: red; font-weight: bold;">
			<%=errorMessage%>
		</div>
		<%
		}
		%>

		<form action="${pageContext.request.contextPath}/${giverId}/${guesserId}/multiplayer" method="post">
			<label for="wordToGuess">Enter word:</label> 
			<input type="text" id="wordToGuess" name="wordToGuess" /> 
			<label for="category">Select
				Category:</label> <select id="category" name="category" required>
				<%
				for (CategoryName category : CategoryName.values()) {
				%>
				<option value="<%=category.name()%>"><%=category.name()%></option>
				<%
				}
				%>
			</select><br> <br>
			<button id="submit-word" type="submit" class="styled-button" name="action"
				value="newGame"  >Submit</button>
		</form>

		<form action="/welcome" method="get">
			<button id="homeButton" type="submit" name="toHomePage">Home
				Page</button>
		</form>

	</div>
</body>
</html>
