<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
body {
	font-family: 'Arial', sans-serif;
	background-color: #f2f2f2;
	color: #333;
	text-align: center;
	padding-top: 50px;
}

.container {
	background-color: #fff;
	padding: 30px;
	border-radius: 10px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	display: inline-block;
	max-width: 500px;
}

h1 {
	font-size: 36px;
	color: #ff6347; 
	margin-bottom: 10px;
}

p {
	font-size: 18px;
	margin-bottom: 20px;
}

.emoji {
	font-size: 100px;
	margin-bottom: 20px;
}

.details {
	color: #555;
	font-size: 14px;
}
</style>
<title>Page Not Found</title>
</head>
<body>
	<h1>Congratulations, you broke it! :D</h1>
	<p>Sorry, the page you are looking for does not exist.</p>
	<form action="/welcome" method="get">
		<button id="homeButton" type="submit" name="toHomePage">Home
			Page</button>
	</form>
</body>
</html>