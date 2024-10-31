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
	List<String> games = (List<String>) request.getAttribute("games");
	Double attempts = (Double) request.getAttribute("attempts");
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	String ratio = (String) request.getAttribute("win-loss-ratio");
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
					<th>Number</th>
					<th>Word</th>
				</tr>
			</thead>
			<tbody>
				<%
				int id = 1;
				for (String game : games) {
				%>
				<tr>
					<td><%= id %></td>
					<td><%=game%></td>
				</tr>
				<%
				id++;
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