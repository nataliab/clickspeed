var dps = [];
var playerColors = [];
var chart = null;
var updateInterval = 200;
var errdiv;

$(document).ready(function() {
	errdiv = document.getElementById('error');
	chart = createChart();
	setInterval(function() {
		updateChart()
	}, updateInterval);

});

function createChart() {
	return new CanvasJS.Chart("chartContainer", {
		title : {
			text : "Speed - clicks per second"
		},
		axisY : {
			suffix : " cps",
			minimum : 0,
			maximum : 20
		},
		legend : {
			verticalAlign : 'bottom',
			horizontalAlign : "center"
		},
		data : [ {
			type : "column",
			bevelEnabled : true,
			indexLabel : "{y} cps",
			dataPoints : dps
		} ]
	});
}

function updateChart() {
	var color;
	var backupPlayerColors;
	$.ajax({
		url : window.location.pathname + "clicks",
		type : 'GET',
		success : redrawChart,
		error : function(xhr, ajaxOptions, thrownError) {
			errdiv.innerHTML = xhr.status + ' ' + thrownError + '<br/>';
		}
	});
}

function redrawChart(data) {
	dps.splice(0);
	backupPlayerColors = $.extend(true, [], playerColors);
	playerColors = [];
	$.each(data, function(index, player) {
		var yVal = player.average > 0 ? player.average : 0;
		if (backupPlayerColors[player.userId]) {
			color = backupPlayerColors[player.userId];
		} else {
			color = getRandomColor();
		}
		dps[index] = {
			label : player.userId,
			y : yVal,
			color : color
		};
		playerColors[player.userId] = color;
	});
	backupPlayerColors = [];
	try {
		chart.render();
	} catch (err) {
		errdiv.innerHTML = err + "<br/>" + err.stack;
		playerColors.splice(0);
		dps.splice(0);
	}
}

function setFocus() {
	document.getElementById("user").focus();
}

function startCapturingClicks(e) {
	if (jQuery.trim($("#user").val()).length > 0) {
		$("#clicks").show();
		document.captureEvents(Event.MOUSEDOWN);
		document.onmousedown = registerClick;
	}
}

function registerClick() {
	var time = new Date();
	var timeString = dateToTime(time);
	var div = document.getElementById('clicks');
	var timestamp = {
		timestamp : time.getTime()
	};
	$.ajax({
		url : window.location.pathname + "clicks/" + $("#user").val(),
		type : "POST",
		data : JSON.stringify(timestamp),
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		success : function(data) {
		},
		error : function(xhr, ajaxOptions, thrownError) {
			if (xhr.status > 202)
				errdiv.innerHTML = xhr.status + ' ' + thrownError + '<br/>';
			else {
				div.innerHTML = div.innerHTML + timeString + '<br/>';
				div.scrollTop = div.scrollHeight;
			}
		}
	});
}

function getRandomColor() {
	return "#000000".replace(/0/g, function() {
		return (~~(Math.random() * 16)).toString(16);
	});
}

function dateToTime(time) {
	var h = time.getHours();
	var m = time.getMinutes();
	var s = time.getSeconds();
	var ms = time.getMilliseconds();
	return '' + (h <= 9 ? '0' + h : h) + ':' + (m <= 9 ? '0' + m : m) + ':'
			+ (s <= 9 ? '0' + s : s) + '.' + ms;
}