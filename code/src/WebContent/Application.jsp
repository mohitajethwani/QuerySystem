<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Web Services Assignment 3</title>
</head>
<body>
	<h1>
		<b><font size=6>WEBSERVICES PROGRAMMING ASSIGNMENT 3</font></b>
	</h1>

	<form name="form" action="MyServlet" method="GET">
		<fieldset>
			<fieldset>
				<legend>SEARCH APIs/MASHUPs:</legend>
				<br> <label>Select: <select name=apiOrMashup>
						<option value="api">Apis</option>
						<option value="mashup">Mashups</option>
				</select>
				<br><br>
				</label> <label> Search by keywords or criteria<br>
				<input type="radio" name="criteria" value=keywords checked> Keywords<br>
				<input type="radio" name="criteria" value=year>
					Updated year<br> 
					<input type="radio" name="criteria"
					value=protocols> Protocols<br> 
					<input type="radio" name="criteria" value=category> Category<br>
					 <input type="radio" name="criteria" value=tags> Tags<br> 
					 <input type="radio" name="criteria" value=usedApis> Used Apis<br>
					<input type="radio" name="criteria" value=rating> Rating
					&nbsp&nbsp&nbsp 
					<select name=ratingOptions>
						<option value="equalTo">Equal To</option>
						<option value="lessThan">Less than</option>
						<option value="greaterThan">Greater than</option>
				</select><br>
				</label> <br> 
				
				<label>Enter value: <input type="text"
					name="value" /> <br> <br>
				</label>
					<input type="submit" value="Submit" />
					
			</fieldset>
			</fieldset>
				
	</form>
</body>
</html>