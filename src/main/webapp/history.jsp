<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="model.Game"%>
<%@ page import="model.History"%>
<%@ page import="java.util.Map.Entry"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/history.css">
<title>Insert title here</title>
</head>
<body>

	<h2 id="title">Hangman Games History</h2>
	<form action="/welcome" method="get">
		<button id="homeButton" type="submit" name="toHomePage">Home
			Page</button>
	</form>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>Mode</th>
				<th>Tries Left</th>
				<th>Categories</th>
				<th>Word State</th>
				<th>Is Finished</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<%
			Map<Game, History> allHistory = (Map<Game, History>) session.getAttribute("history");
			Iterator<Entry<Game, History>> iterator = allHistory.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Game, History> entry = iterator.next();
				Game game = entry.getKey();
				History history = entry.getValue();
			%>
			<tr>
				<td><%=game.getId()%></td>
				<td><%=history.getMode()%></td>
				<td><%=history.getTriesLeft()%></td>
				<td><%=history.getCategory()%></td>
				<td><%=history.isFinished() ? game.getWord() : history.getWordState()%></td>
				<td><%=history.isFinished()%></td>
				<td>
					<%
					if (history.getMode().equals("Single Player")) {
					%>
					<form action="/hangMan" method="get">
						<input type="hidden" name="currentWord"value="<%=game.getWord()%>" /> 
						<input type="hidden" name="currentWordState" value="<%=history.getWordState()%>" /> 
						<input type="hidden" name="triesLeft" value="<%=history.getTriesLeft()%>" />
						<input type="hidden" name="usedCharacters" value="<%=history.getUsedChars()%>" /> 
						<input type="hidden" name="category" value="<%=history.getCategory()%>" />
						<button type="submit" name="action" value="resume"
							<%=history.isFinished() ? "disabled" : ""%>>Resume Game</button>
					</form> 
					<%
 						} else {
 					%>
					<form action="/multiPlayer" method="post">
						<input type="hidden" name="currentWord" value="<%=game.getWord()%>" /> 
						<input type="hidden" name="currentWordState" value="<%=history.getWordState()%>" /> 
						<input type="hidden" name="triesLeft" value="<%=history.getTriesLeft()%>" /> 
						<input type="hidden" name="usedCharacters" value="<%=history.getUsedChars()%>" /> 
						<input type="hidden" name="category" value="<%=history.getCategory()%>" />
						<button type="submit" name="action" value="resume"<%=history.isFinished() ? "disabled" : ""%>>Resume Game</button>
					</form> 
					<%}%>
				</td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
</body>
</html>