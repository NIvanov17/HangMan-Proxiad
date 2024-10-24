<html>
<head>
<link rel="stylesheet" href="css/welcome.css">
<style type="text/css">
body {
	background-color: #f4f4f4;
}

.container {
	display: flex;
	justify-content: center;
	flex-direction: column;
	align-items: center;
}

button {
	background-color: #4CAF50;
	border: none;
	color: white;
	padding: 10px 18px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	margin: 10px 0;
	cursor: pointer;
	border-radius: 12px;
	box-shadow: 0 4px #999;
	transition: background-color 0.3s, box-shadow 0.3s;
}
</style>
</head>
<body>
	<div class="container">
		<h2>Welcome To Best HangMan app!</h2>
		<form action="/username" method="get">
			<button id="single" type="submit" class="styled-button">New Single Player Game</button>
		</form>
		<form action="/word-giver" method="get">
			<button id="multi" type="submit" class="styled-button" name="action"
				value="newGame">New Multiplayer Game</button>
		</form>
		<form action="/history" method="get">
			<button id="history" type="submit" class="styled-button" value="submit">Games
				History</button>
		</form>
		<form action="/statistic" method="get">
			<button id="history" type="submit" class="styled-button" value="submit">Load Statistic</button>
		</form>
		<form action="/ranking" method="get">
			<button id="history" type="submit" class="styled-button" value="submit">Rankings</button>
		</form>
	</div>
</body>
</html>
