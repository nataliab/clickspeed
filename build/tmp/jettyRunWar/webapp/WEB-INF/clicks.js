$(document).ready(
		function() {
			jQuery.support.cors = true;
			// initial value of dataPoints
			var dps = [ {
				label : "player1",
				y : 206
			}, {
				label : "player2",
				y : 163
			}, {
				label : "player3",
				y : 154
			}, {
				label : "player4",
				y : 176
			}, {
				label : "player5",
				y : 184
			}, {
				label : "player6",
				y : 122
			} ];

			var chart = new CanvasJS.Chart("chartContainer", {
				title : {
					text : "Speed - clicks per second"
				},
				axisY : {
					suffix : " cps"
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

			var updateInterval = 1000;

			var updateChart = function() {
				jQuery.support.cors = true;
				var errdiv = document.getElementById('error');
				$.ajax({
					url : "http://localhost:8080/devday/clicks",
					type : 'GET',
					success : function(data) {
						alert(data);
					},
					error : function(xhr, ajaxOptions, thrownError) {
						errdiv.innerHTML = xhr.status + ' ' + thrownError
								+ '<br/>';
					}
				});

				for (var i = 0; i < dps.length; i++) {

					// generating random variation deltaY
					var deltaY = Math.round(20 + Math.random() * (-40));
					var yVal = deltaY + dps[i].y > 0 ? dps[i].y + deltaY : 0;
					var boilerColor;

					// color of dataPoint dependent upon y value.

					boilerColor = yVal > 200 ? "#FF2500"
							: yVal >= 170 ? "#FF6000" : yVal < 170 ? "#6B8E23 "
									: null;

					// updating the dataPoint
					dps[i] = {
						label : "player" + (i + 1),
						y : yVal,
						color : boilerColor
					};

				}
				;

				chart.render();
			};

			updateChart();

			// update chart after specified interval
			setInterval(function() {
				updateChart()
			}, updateInterval);
		});

function setFocus() {
	document.getElementById("user").focus();
}

function startCapturingClicks(e) {
	var inp = $("#user").val();
	if (jQuery.trim(inp).length + 1 > 0) {
		$("#clicks").show();
		document.captureEvents(Event.MOUSEDOWN);
		document.onmousedown = function(event) {
			var time = new Date();// .getTime();
			var timeString = time.getHours() + ':' + time.getMinutes() + ':'
					+ time.getSeconds() + '.' + time.getMilliseconds();
			var div = document.getElementById('clicks');
			var errdiv = document.getElementById('error');
			div.innerHTML = div.innerHTML + timeString + '<br/>';
			div.scrollTop = div.scrollHeight;
		}
	}
}