<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Game"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.time.LocalDateTime"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="/css/statistic.css">
<meta charset="UTF-8">
<title>Statistic</title>
</head>
<body>
	<%
	List<Game> games = (List<Game>) session.getAttribute("games");
	Double attempts = (Double) session.getAttribute("attempts");
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	String ratio = (String) session.getAttribute("win-loss-ratio");
	%>
	<form action="/welcome" method="get">
		<button id="homeButton" type="submit" name="toHomePage">Home
			Page</button>
	</form>
	<div class="container">
		<h1>Top 10 Most Used Words</h1>
		<table>
			<thead>
				<tr>
					<th>Game ID</th>
					<th>Finished At</th>
					<th>Word</th>
					<th>Tries Left</th>
					<th>Status</th>
				</tr>
			</thead>
			<tbody>
				<%
				for (Game game : games) {
				%>
				<tr>
					<td><%=game.getId()%></td>
					<td><%=LocalDateTime.parse(game.getStatistic().getFinishedAt().toString()).format(formatter)%></td>
					<td><%=game.getWord().getName()%></td>
					<td><%=game.getTriesLeft()%></td>
					<td><%=game.getStatistic().getStatus()%></td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>

		<div class="attempts">
			<h2>
				Average Attempts per Game: <span class="attempts-number"><%=attempts%></span>
			</h2>
		</div>
		<div class="ratio">
			<h2>Win/Loss Ratio:</h2>
			<p><%=ratio %></p>
		</div>
	</div>
</body>
</html>