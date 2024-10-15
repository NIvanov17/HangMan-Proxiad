<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="enums.Category"%>
<%@ page import="model.Word"%>
<%@ page isELIgnored="false"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/gameStarted.css">
<title>Game Started!</title>
<style type="text/css">
body {
	background-color: #f4f4f4;
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
</head>
<body>

	<%
	Word word = (Word) session.getAttribute("word");
	Integer triesLeft = (Integer) session.getAttribute("triesLeft");
	Boolean isFinished = (Boolean) session.getAttribute("isFinished");
	String currentState = (String) session.getAttribute("currentState");
	String gameStatus = (String) session.getAttribute("gameStatus");
	Set<Character> usedCharacters = (Set<Character>) session.getAttribute("usedCharacters");
	Category wordCategory = (Category) session.getAttribute("wordCategory");
	String mode = (String) session.getAttribute("mode");
	%>
	<div class="container">

		<h1>Word to Guess: ${currentState}</h1>
		<h2 id="tries-left">Tries left: ${triesLeft}</h2>
		<h3>Category: ${wordCategory}</h3>
		<h3>Mode: ${mode}</h3>
		<h2>${gameStatus}</h2>
		<div>
			<%
			for (char letter = 'A'; letter <= 'Z'; letter++) {
				char lowerCaseLetter = Character.toLowerCase(letter);
			%>
			<form action="/hangMan" method="post">
				<input type="hidden" name="guess" value="<%=lowerCaseLetter%>" />
				<button type="submit" name="guess-btn" value="<%=lowerCaseLetter%>"
					<%if (usedCharacters.contains(lowerCaseLetter) || isFinished == true) {
	//using out.print("disabled"); within the button element dynamically adds the disabled attribute to the HTML <button> element
	out.print("disabled");
}%>><%=letter%></button>
			</form>
			<%
			}
			%>
		</div>
		<img src="img/<%=triesLeft%>.png" alt="Hangman State">
		<%
		if (isFinished) {
		%>

		<%
		if (mode.equals("Single Player")) {
		%>
		<form action="/hangMan" method="get">
			<button type="submit" name="action" value="restart">Restart
				Single Player Game</button>
		</form>
		<%
		} else {
		%>
		<form action="/multiPlayer" method="get">
			<button type="submit" name="action" value="restart">Restart
				Multiplayer Game</button>
		</form>
		<%
		}
		%>
		<%
		}
		%>
		<form action="/welcome" method="get">
			<button id="homeButton" type="submit" name="toHomePage">Home
				Page</button>
		</form>

	</div>
</body>
</html>
