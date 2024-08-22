
<html>
<head>
<link rel="stylesheet" href="css/welcome.css">
</head>
<body>
	<div class="container">
		<h2>Welcome, player!</h2>
		<form action="/hangMan" method="get">
			<button type="submit" name="action" value="newGame">Start New Game</button>
		</form>
		<form action="/onGoingGames" method="get">
			<button type="submit" value="submit">Ongoing Games</button>
		</form>
	</div>
</body>
</html>
