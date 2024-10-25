<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="model.Category"%>
<%@ page import="model.Word"%>
<%@ page import="model.Game"%>
<%@ page isELIgnored="false"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/css/gameStarted.css">
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
	Game game = (Game) session.getAttribute("game");
	String gameStatus = (String) session.getAttribute("gameStatus");
	Long id = (Long)session.getAttribute("id");
	%>
	<div class="container">

		<h1>Word to Guess: ${game.getCurrentState()}</h1>
		<h2 id="tries-left">Tries left: ${game.getTriesLeft()}</h2>
		<h3>Category: ${word.getCategory().getCategoryName()}</h3>
		<h3>Mode: ${game.getMode()}</h3>
		<h2>${gameStatus}</h2>
		<div>
			<%
			for (char letter = 'A'; letter <= 'Z'; letter++) {
				char lowerCaseLetter = Character.toLowerCase(letter);
			%>
			<form action="/hangMan" method="post">
				<input type="hidden" name="guess" value="<%=lowerCaseLetter%>" />
				<button type="submit" name="guess-btn" value="<%=lowerCaseLetter%>"
					<%if (game.getUsedChars().contains(lowerCaseLetter) || game.isFinished() == true) {
	//using out.print("disabled"); within the button element dynamically adds the disabled attribute to the HTML <button> element
	out.print("disabled");
}%>><%=letter%></button>
			</form>
			<%
			}
			%>
		</div>
		<img src="/img/<%=game.getTriesLeft()%>.png" alt="Hangman State">
		<%
		if (game.isFinished()) {
		%>

		<%
		if (game.getMode().equals("Single Player")) {
		%>
		<form action="/username" method="get">
		 <input type="hidden" name="action" value="restart" />
			<button type="submit">Restart Single Player Game</button>
		</form>
		<%
		} else {
		%>
		<form action="/multiPlayer" method="get">
			<button type="submit" name="action" value="restart">Restart Multiplayer Game</button>
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
