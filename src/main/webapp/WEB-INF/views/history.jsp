<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="model.Game"%>
<%@ page import="model.Word"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/css/history.css">
<title>Insert title here</title>
</head>
<body> 

<%	String username = (String)session.getAttribute("username"); %>
	<h2 id="title">Hangman Games History</h2>
	<h2 id="title">Username: <%= username %></h2>
	<form action="/welcome" method="get">
		<button id="homeButton" type="submit" name="toHomePage">Home Page</button>
	</form>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>Mode</th>
				<th>Tries Left</th>
				<th>Category</th>
				<th>Word State</th>
				<th>Is Finished</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<%
				// Retrieve the list of games from the session
				List<Game> games = (List<Game>) session.getAttribute("allGames");
				if (games != null) {
					for (Game entry : games) {
			%>
			<tr>
				<td><%= entry.getId() %></td>
				<td><%= entry.getMode() %></td>
				<td><%= entry.getTriesLeft() %></td>
				<td><%= entry.getWord().getCategory().getCategoryName() %></td>
				<td><%= entry.isFinished() ? entry.getWord().getName() : entry.getWord().getCurrentState() %></td>
				<td><%= entry.isFinished() %></td>
				<td>
					<%
						// Check if the game mode is "Single Player"
						if ("Single Player".equals(entry.getMode())) {
					%>
					<form action="/game/hangMan" method="get">
						<input type="hidden" name="gameId" value="<%= entry.getId() %>" />
						<button type="submit" name="action" value="resume" <%= entry.isFinished() ? "disabled" : "" %>>Resume Game</button>
					</form>
					<%
						} else { // Multiplayer mode
					%>
					<form action="${pageContext.request.contextPath}/${giverUsername}/${guesserUsername}/multiplayer" method="post">
						<input type="hidden" name="currentWord" value="<%= entry.getWord().getName() %>" />
						<input type="hidden" name="currentWordState" value="<%= entry.getWord().getCurrentState() %>" />
						<input type="hidden" name="triesLeft" value="<%= entry.getTriesLeft() %>" />
						<input type="hidden" name="usedCharacters" value="<%= entry.getUsedChars() %>" />
						<input type="hidden" name="category" value="<%= entry.getWord().getCategory() %>" />
						<button type="submit" name="action" value="resume" <%= entry.isFinished() ? "disabled" : "" %>>Resume Game</button>
					</form>
					<%
						}
					%>
				</td>
			</tr>
			<%}%>
	<%}%>
		</tbody>
	</table>
</body>
</html>
