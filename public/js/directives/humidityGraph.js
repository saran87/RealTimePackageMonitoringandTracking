angular.module('myDirectives')
	.directive('humiditychart', function() {
	    return {
	        restrict: 'A',
	        link: function(scope, elem, attrs) {

	        	var graph=null;
	        	
	        	var options = {
	        		xaxis: {
		        		mode: "time"	    				
    				}
	        	};            
	            	            

	            scope.$watch('scope[attrs.ngModel]',function(){

	            	if(scope[attrs.ngModel]==[]){
	            		console.log("Loading data");
	            	}
	            	else{

	            		graph=$.plot("#"+attrs.id, scope[attrs.ngModel], options);	            		
	            		graph.setData(scope[attrs.ngModel]);	            	
                    	graph.setupGrid();                    
                    	graph.draw();
	            	}

	            });
	            
	           
	        }
	    };
	});