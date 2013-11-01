angular.module('myDirectives')
	.directive('modaldir', function(){
	    return {
	      restrict: 'A',
	      // The linking function will add behavior to the template
	      link: function(scope, element, attrs) {
	        //console.log("inside modal");
	         element.click(function(e){
	            e.preventDefault();
	            var href = element.attr('data-target');
	            console.log(href);
	            $(href).modal();
	        });
	        
	      }
	    }
	});