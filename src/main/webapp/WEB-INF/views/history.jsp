<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="model.Game"%>
<%@ page import="model.Word"%>
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
			Map<Word, Game> allHistory = (Map<Word, Game>) session.getAttribute("history");
			Iterator<Entry<Word, Game>> iterator = allHistory.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Word, Game> entry = iterator.next();
				Word word = entry.getKey();
				Game game = entry.getValue();
			%>
			<tr>
				<td><%=word.getId()%></td>
				<td><%=game.getMode()%></td>
				<td><%=game.getTriesLeft()%></td>
				<td><%=game.getCategory()%></td>
				<td><%=game.isFinished() ? word.getWord() : game.getWordState()%></td>
				<td><%=game.isFinished()%></td>
				<td>
					<%
					if (game.getMode().equals("Single Player")) {
					%>
					<form action="/hangMan" method="get">
						<input type="hidden" name="currentWord"value="<%=word.getWord()%>" /> 
						<input type="hidden" name="currentWordState" value="<%=game.getWordState()%>" /> 
						<input type="hidden" name="triesLeft" value="<%=game.getTriesLeft()%>" />
						<input type="hidden" name="usedCharacters" value="<%=game.getUsedChars()%>" /> 
						<input type="hidden" name="category" value="<%=game.getCategory()%>" />
						<button type="submit" name="action" value="resume"
							<%=game.isFinished() ? "disabled" : ""%>>Resume Game</button>
					</form> 
					<%
 						} else {
 					%>
					<form action="/multiPlayer" method="post">
						<input type="hidden" name="currentWord" value="<%=word.getWord()%>" /> 
						<input type="hidden" name="currentWordState" value="<%=game.getWordState()%>" /> 
						<input type="hidden" name="triesLeft" value="<%=game.getTriesLeft()%>" /> 
						<input type="hidden" name="usedCharacters" value="<%=game.getUsedChars()%>" /> 
						<input type="hidden" name="category" value="<%=game.getCategory()%>" />
						<button type="submit" name="action" value="resume"<%=game.isFinished() ? "disabled" : ""%>>Resume Game</button>
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