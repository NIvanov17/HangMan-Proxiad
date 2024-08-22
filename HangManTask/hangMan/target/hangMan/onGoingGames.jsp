<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
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
				<th>Word State</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<%
			Map<String, String> onGoingWords = (Map<String, String>) session.getAttribute("onGoingWords");
			Integer triesLeft = (Integer) session.getAttribute("triesLeft");
			Set<Character> usedCharacters = (Set<Character>) session.getAttribute("usedCharacters");
			int id = 1;
			Iterator<Entry<String, String>> iterator = onGoingWords.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String originalWord = entry.getKey();
				String wordState = entry.getValue();
			%>
			<tr>
				<td><%=id++%></td>
				<td><%=triesLeft%></td>
				<td><%=wordState%></td>
				<td>
					<form action="/hangMan" method="get">
						<input type="hidden" name="currentWord" value="<%= originalWord %>" />
    					<input type="hidden" name="currentWordState" value="<%= wordState %>" />
    					<input type="hidden" name="triesLeft" value="<%= triesLeft %>" />
    					<input type="hidden" name="usedCharacters" value="<%=usedCharacters %>"/>
						<button type="submit" name="action" value="resume">Resume Game</button>
					</form>
					<%System.out.println(wordState); %>
				</td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
</body>
</html>