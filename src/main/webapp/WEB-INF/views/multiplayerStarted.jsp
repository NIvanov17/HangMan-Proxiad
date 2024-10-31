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
	Long id = (Long) request.getAttribute("id");
	List<Character> letters = (List<Character>) request.getAttribute("letters");
	Game game = (Game) request.getAttribute("game");
	Long guesserId = (Long) request.getAttribute("guesserId");
	Long giverId = (Long) request.getAttribute("giverId");
	%>

	<div class="container">
		<h1 id="currentState">
			Word to Guess:
			<%=game.getCurrentState()%>
		</h1>
		<h2 id="tries-left">
			Tries left:
			<%=game.getTriesLeft()%>
		</h2>
		<h3 id="category">
			Category:
			<%=game.getWord().getCategory().getCategoryName()%>
		</h3>
		<h3 id="mode">
			Mode:
			<%=game.getMode()%>
		</h3>
		<h2 id="status">${gameStatus}</h2>

		<div>
			<%
			for (Character letter : letters) {
				char lowerCaseLetter = Character.toLowerCase(letter);
			%>
			<form action="/<%= giverId %>/<%= guesserId %>/multiplayer/guess" method="post">
				<input type="hidden" name="letter" value="<%=lowerCaseLetter%>" />
				<input type="hidden" name="gameId" value="<%=game.getId()%>" />
				<button type="submit" name="guess-btn" value="<%=lowerCaseLetter%>"
                    <%= game.getUsedChars().contains(lowerCaseLetter) || game.isFinished() ? "disabled" : "" %>>
                    <%= letter %>
				</button>
			</form>
			<%
			}
			%>
		</div>

		<img src="/img/<%=game.getTriesLeft()%>.png" alt="Hangman State">

		<%
		if (game.isFinished()) {
		%>
		<form action="/word-giver" method="get">
			<button type="submit" name="action" value="restart">Restart
				Multiplayer Game</button>
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
