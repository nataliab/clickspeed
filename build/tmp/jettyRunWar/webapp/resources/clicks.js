$(document).ready(function() {

	var dps = [];

	var chart = new CanvasJS.Chart("chartContainer", {
		title : {
			text : "Speed - clicks per second"
		},
		axisY : {
			suffix : " cps",
			minimum : 0,
			maximum: 20
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
			url : window.location.pathname+"clicks",
			type : 'GET',
			success : function(data) {
				dps.length = 0;
				$.each(data, function(index, player) {
					var yVal = player.average > 0 ? player.average : 0;
					dps[index] = {
						label : player.userId,
						y : yVal,
						color : "#FF2500"
					};
				});
			},
			error : function(xhr, ajaxOptions, thrownError) {
				errdiv.innerHTML = xhr.status + ' ' + thrownError + '<br/>';
			}
		});

		// for (var i = 0; i < dps.length; i++) {
		//
		// // generating random variation deltaY
		// var deltaY = Math.round(20 + Math.random() * (-40));
		// var yVal = deltaY + dps[i].y > 0 ? dps[i].y + deltaY : 0;
		// var boilerColor;
		//
		// // color of dataPoint dependent upon y value.
		//
		// boilerColor = yVal > 200 ? "#FF2500"
		// : yVal >= 170 ? "#FF6000" : yVal < 170 ? "#6B8E23 "
		// : null;
		//
		// // updating the dataPoint
		// dps[i] = {
		// label : "player" + (i + 1),
		// y : yVal,
		// color : boilerColor
		// };
		//
		// }
		// ;

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
			var timeString = time.getHours() + ':' + time.getMinutes() + ':'
					+ time.getSeconds() + '.' + time.getMilliseconds();
			var div = document.getElementById('clicks');
			var errdiv = document.getElementById('error');
			
			var timestamp = {
				timestamp : time.getTime()
			};
			$.ajax({
				url : window.location.pathname+"clicks/" + inp,
				type : "POST",
				data : JSON.stringify(timestamp),
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				success : function(data) {
					alert('ok');
				},
				error : function(xhr, ajaxOptions, thrownError) {
					if (xhr.status > 202)
						errdiv.innerHTML = xhr.status + ' ' + thrownError
								+ '<br/>';
					else{
						div.innerHTML = div.innerHTML + timeString + '<br/>';
						div.scrollTop = div.scrollHeight;
					}
				}
			});
		}
	}
}