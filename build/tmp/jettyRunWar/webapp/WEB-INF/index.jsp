<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Speedy Clicks!</title>
<script src="/WEB-INF/jquery.js"></script>
<script src="/WEB-INF/canvasjs.min.js"></script>
<script src="/WEB-INF/clicks.js"></script>

<style media="screen" type="text/css">
div {
	-webkit-touch-callout: none;
	-webkit-user-select: none;
	-khtml-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}
</style>
</head>
<body onload="setFocus()">
	<div>
		<form>
			<h4>
				What's your name? <input type="text" name="user" id="user"
					onkeypress="startCapturingClicks(event)">
			</h4>
		</form>
	</div>
	<div id="error" style="color: red;"></div>
	<div>
		<table>
			<tr>
				<td style="width: 250px; height: 400px;"><div id="clicks"
						style="overflow: scroll; height: 400px; width: 200px; overflow-x: hidden; overflow-y: hidden; display: none">
						<h3>Start clicking now!</h3>
					</div></td>
				<td><div id="chartContainer" style="height: 300px; width: 100;">
					</div></td>
			</tr>
		</table>
	</div>
</body>
</html>