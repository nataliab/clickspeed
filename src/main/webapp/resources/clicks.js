$(document).ready(function() {
	var dps = [];
	var playerColors = [];
	var chart = new CanvasJS.Chart("chartContainer", {
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

	var updateInterval = 100;

	var updateChart = function() {
		var errdiv = document.getElementById('error');
		$.ajax({
			url : window.location.pathname + "clicks",
			type : 'GET',
			success : function(data) {
				dps.length = 0;
				dps.splice();
				var backupPlayerColors = $.extend(true, [], playerColors);
				playerColors = [];
				$.each(data, function(index, player) {
					var color = getRandomColor();
					var yVal = player.average > 0 ? player.average : 0;
					if (backupPlayerColors[player.userId]) {
						color = backupPlayerColors[player.userId];
					}
					dps[index] = {
						label : player.userId,
						y : yVal,
						color : color
					};
					playerColors[player.userId] = color;
				});
				
				backupPlayerColors = [];
			},
			error : function(xhr, ajaxOptions, thrownError) {
				errdiv.innerHTML = xhr.status + ' ' + thrownError + '<br/>';
			}
		});
		chart.render();
	};

	updateChart();
	setInterval(function() {
		updateChart()
	}, updateInterval);
});

function setFocus() {
	document.getElementById("user").focus();
}

function startCapturingClicks(e) {
	var inp = $("#user").val();
	if (jQuery.trim(inp).length > 0) {
		$("#clicks").show();
		document.captureEvents(Event.MOUSEDOWN);
		document.onmousedown = function(event) {
			var time = new Date();
			var timeString = dateToTime(time);
			var div = document.getElementById('clicks');
			var errdiv = document.getElementById('error');
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
						errdiv.innerHTML = xhr.status + ' ' + thrownError
								+ '<br/>';
					else {
						div.innerHTML = div.innerHTML + timeString + '<br/>';
						div.scrollTop = div.scrollHeight;
					}
				}
			});
		}
	}
}

function getRandomColor() {
	return "#" + (Math.round(Math.random() * 0XFFFFFF)).toString(16);
}

function dateToTime(time) {
	var h = time.getHours();
	var m = time.getMinutes();
	var s = time.getSeconds();
	var ms = time.getMilliseconds();
	return '' + (h <= 9 ? '0' + h : h) + ':' + (m <= 9 ? '0' + m : m) + ':'
			+ (s <= 9 ? '0' + s : s) + '.' + ms;
}