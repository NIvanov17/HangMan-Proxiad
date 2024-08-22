<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="model.Word"%>
<%@ page import="model.OngoingGame"%>
<%@ page import="java.util.Map.Entry"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/onGoing.css">
<title>Insert title here</title>
</head>
<body>

	<h2>Ongoing Hangman Games</h2>
	<form action="/welcome" method="get">
		<button id="homeButton" type="submit" name="toHomePage">Home
			Page</button>
	</form>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>Tries Left</th>
				<th>Categories</th>
				<th>Word State</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<%
			Map<Word, OngoingGame> onGoingWords = (Map<Word, OngoingGame>) session.getAttribute("onGoingWords");
			Integer triesLeft = (Integer) session.getAttribute("triesLeft");
			Set<Character> usedCharacters = (Set<Character>) session.getAttribute("usedCharacters");
			int id = 1;
			Iterator<Entry<Word, OngoingGame>> iterator = onGoingWords.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Word, OngoingGame> entry = iterator.next();
				Word originalWord = entry.getKey();
				OngoingGame ongoingGame = entry.getValue();
			%>
			<tr>
				<td><%=id++%></td>
				<td><%= ongoingGame.getTriesLeft()%></td>
				<td><%=ongoingGame.getCategory() %></td>
				<td><%=ongoingGame.getWordState()%></td>
				<td>
					<form action="/hangMan" method="get">
						<input type="hidden" name="currentWord" value="<%= originalWord.getName() %>" />
    					<input type="hidden" name="currentWordState" value="<%= ongoingGame.getWordState() %>" />
    					<input type="hidden" name="triesLeft" value="<%= ongoingGame.getTriesLeft() %>" />
    					<input type="hidden" name="usedCharacters" value="<%= ongoingGame.getUsedChars() %>"/>
    					<input type="hidden" name="category" value="<%= ongoingGame.getCategory()%>"/>
						<button type="submit" name="action" value="resume">Resume Game</button>
					</form>
					<%System.out.println(originalWord.getName() + " -.-* " +ongoingGame.getWordState()); %>
				</td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
</body>
</html>