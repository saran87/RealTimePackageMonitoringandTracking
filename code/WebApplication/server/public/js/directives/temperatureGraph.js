angular.module('myDirectives')
	.directive('chart', function() {
	    return {
	        restrict: 'A',
	        link: function(scope, elem, attrs) {

	        	var graph=null;
	        	
	        	var options = {

	        		xaxis: {
		        		mode: "time"    				
    				},
    				series: {
    					lines : { show: true },
    					points : { show: true }
    				},
    				grid: {
    					hoverable: true,
    					clickable: true,
    					borderWidth: 1,
						minBorderMargin: 20,
						labelMargin: 10
    				},
    				legend: {
    					show: true
    				}
	        	};

	        	var elemid = '';

	        	function showTooltip(x, y, contents) {
	        		
					$("<div id='tooltip'>" + contents + "</div>").css({
						position: "absolute",
						display: "none",
						top: y + 5,
						left: x + 5,
						border: "1px solid #fdd",
						padding: "2px",
						"background-color": "#fee",
						opacity: 0.80
					}).appendTo('body').fadeIn(200);
				} 

				
	            scope.$watch('scope[attrs.ngModel]',function(){

	            	if(scope[attrs.ngModel]==[]){
	            		console.log("Loading data");
	            	}
	            	else{

	            		//elemid=attrs.id;
	            		graph=$.plot("#"+attrs.id, scope[attrs.ngModel], options);	            		
	            		graph.setData(scope[attrs.ngModel]);	            	
                    	graph.setupGrid();                    
                    	graph.draw();

                    	var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
							.text("Temperature (F)")
							.appendTo("#"+attrs.id);

						yaxisLabel.css("margin-top", yaxisLabel.width() / 2 - 20);   

                    	$("#"+attrs.id).bind("plothover", function (event, pos, item) {
							
							var str = "(" + pos.x + ", " + pos.y + ")";

							//console.log("str " + str);
							//$("#hoverdata").text(str);
							
							if (item) {
								if (previousPoint != item.dataIndex) {

									previousPoint = item.dataIndex;

									$("#tooltip").remove();

									var x = item.datapoint[0],
									y = item.datapoint[1];									
									showTooltip(item.pageX, item.pageY, x + " = " + y);

									$("#hoverdata").text(new Date(x) + "Temperature: " + y);
								}
							} else {
								$("#tooltip").remove();
								previousPoint = null;            
							}
							
						});

	            	}

	            });
	            
	           
	        }
	    };
	});