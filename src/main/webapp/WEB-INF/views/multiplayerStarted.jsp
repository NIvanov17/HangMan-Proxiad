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
	Game wordGiverGame = (Game) session.getAttribute("giverGame");
	Game wordGuesserGame = (Game) session.getAttribute("guesserGame");
	%>
	<div class="container">

		<h1 id="currentState">Word to Guess: ${word.getCurrentState()}</h1>
		<h2 id="tries-left">Tries left: ${triesLeft}</h2>
		<h3 id="category">Category: ${word.getCategory().getCategoryName()}</h3>
		<h3 id="mode">Mode: ${mode}</h3>
		<h2 id="status">${gameStatus}</h2>
		<div>
			<%
			for (char letter = 'A'; letter <= 'Z'; letter++) {
				char lowerCaseLetter = Character.toLowerCase(letter);
			%>
			<form action="/${giverUsername}/${guesserUsername}/multiplayer/guess" method="post">
				<input type="hidden" name="letter" value="<%=lowerCaseLetter%>" />
				<button type="submit" value="<%=lowerCaseLetter%>"
					<%if (wordGiverGame.getUsedChars().contains(lowerCaseLetter) || wordGiverGame.isFinished() == true) {
	//using out.print("disabled"); within the button element dynamically adds the disabled attribute to the HTML <button> element
	out.print("disabled");
}%>><%=letter%></button>
			</form>
			<%
			}
			%>
		</div>
		<img src="/img/<%=wordGiverGame.getTriesLeft()%>.png" alt="Hangman State">
		<%
		if (wordGiverGame.isFinished()) {
		%>
		<form action="/${giverUsername}/${guesserUsername}/multiplayer" method="get">
			<button type="submit" name="action" value="restart">Restart Multiplayer
				Game</button>
		</form>
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