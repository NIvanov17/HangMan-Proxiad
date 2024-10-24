<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Player"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="/css/statistic.css">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


	<%
	List<Player> players = (List<Player>) session.getAttribute("players");
	%>
	<form action="/welcome" method="get">
		<button id="homeButton" type="submit" name="toHomePage">Home
			Page</button>
	</form>
	<div class="container">
		<h1>Top 10 Players Ranking</h1>
		<table>
			<thead>
				<tr>
					<th>Player ID</th>
					<th>Username</th>
					<th>Total Wins</th>
				</tr>
			</thead>
			<tbody>
				<%
				for (Player player : players) {
				%>
				<tr>
					<td><%=player.getId()%></td>
					<td><%=player.getUsername()%></td>
					<td><%=player.getTotalWins()%></td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
</body>
</html>