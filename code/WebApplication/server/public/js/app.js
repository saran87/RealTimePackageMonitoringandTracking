'use strict';

// Declare app level module which depends on filters, and services

angular.module('myApp', ['myModule','ngRoute','myServices','myDirectives','nvd3ChartDirectives','myApp.filters'])    
    .config(['$routeProvider','$httpProvider', function($routeProvider,$httpProvider) {

    /*
        Routes and Partials related to /
    */

    $routeProvider.when('/',
    {
        templateUrl: 'partials/dashboardGeneral.html',
    });

    /*
        Routes and Partials related to Dashboard
    */

    $routeProvider.when('/dashboard', 
    {
        templateUrl: 'partials/dashboardGeneral.html'
        
    });

    $routeProvider.when('/dashboard/:truck_id/:package_id', 
    {
        templateUrl: 'partials/dashboard.html'
        
    });

    /*
        Routes and Partials related to Map
    */

    $routeProvider.when('/map',
    {
        templateUrl: 'partials/mapGeneral.html'
        
    });

    $routeProvider.when('/map/:truck_id/:package_id',
    {
        templateUrl: 'partials/map.html',
        controller: 'mapController'
    });


    /*
        Routes and Partials related to Temperature
    */

    $routeProvider.when('/temperature',
    {
        templateUrl: 'partials/temperature.html'
    });

    $routeProvider.when('/temperature/:truck_id/:package_id',
    {
        templateUrl: 'partials/temperatureSpecific.html'
    });

    $routeProvider.when('/temperature/:package_id',
    {
        templateUrl: 'partials/temperatureSpecific.html'
    });


    /*
        Routes and Partials related to Humidity
    */

    $routeProvider.when('/humidity', 
    {
        templateUrl: 'partials/humidity.html'
    });

    $routeProvider.when('/humidity/:package_id',
    {
        templateUrl: 'partials/humiditySpecific.html'
    });

    $routeProvider.when('/humidity/:truck_id/:package_id',
    {
        templateUrl: 'partials/humiditySpecific.html'
        
    });


    /*
        Routes and Partials related to Vibration
    */

    $routeProvider.when('/vibration',
    {
        templateUrl: 'partials/vibration.html'
    });

    $routeProvider.when('/vibration/:package_id',
    {
        templateUrl: 'partials/vibrationSpecific.html'
    });

    $routeProvider.when('/vibration/:truck_id/:package_id',
    {
        templateUrl: 'partials/vibrationSpecific.html'
    });


    /*
        Routes and Partials related to Shock
    */

    $routeProvider.when('/shock', 
    {
        templateUrl: 'partials/shock.html'
    });

    $routeProvider.when('/shock/:package_id',
    {
        templateUrl: 'partials/shockSpecific.html'
    });

    $routeProvider.when('/shock/:truck_id/:package_id',
    {
        templateUrl: 'partials/shockSpecific.html'
    });


    /*
        If all else fails go back and show the dashboard
    */
    
    $routeProvider.otherwise(
    {
        redirectTo: '/'
    });
    

    $httpProvider.interceptors.push(function($q, $rootScope) {
      return {
        
        // optional method
        'request': function(config) {
          // do something on success
          
          if(config.url.indexOf("bg")<0){
            $rootScope.showSpinner=true;
          } 
          
          return config || $q.when(config);
        },
     
        // optional method
       'requestError': function(rejection) {
          // do something on error
          if (canRecover(rejection)) {
            return responseOrNewPromise
          }
          return $q.reject(rejection);
        },
     
     
     
        // optional method
        'response': function(response) {
          // do something on success
          $rootScope.showSpinner=false;
          return response || $q.when(response);
        },
     
        // optional method
       'responseError': function(rejection) {
          // do something on error
          if (canRecover(rejection)) {
            return responseOrNewPromise
          }
          return $q.reject(rejection);
        }

      };
    });

}]);

     /*var $http,
        interceptor = ['$q', '$injector', function ($q, $injector) {
            var rootScope;

            function success(response) {
                // get $http via $injector because of circular dependency problem
                $http = $http || $injector.get('$http');
                // don't send notification until all requests are complete
                if ($http.pendingRequests.length < 1) {
                    // get $rootScope via $injector because of circular dependency problem
                    rootScope = rootScope || $injector.get('$rootScope');
                    // send a notification requests are complete

                    rootScope.$broadcast(_END_REQUEST_);
                    //request.url
                    
                } 
                return response;
            }

            function error(response) {
                // get $http via $injector because of circular dependency problem
                $http = $http || $injector.get('$http');
                // don't send notification until all requests are complete
                if ($http.pendingRequests.length < 1) {
                    // get $rootScope via $injector because of circular dependency problem
                    rootScope = rootScope || $injector.get('$rootScope');
                    // send a notification requests are complete
                    rootScope.$broadcast(_END_REQUEST_);
                    
                }
                return $q.reject(response);
            }

            return function (promise) {
                // get $rootScope via $injector because of circular dependency problem
                rootScope = rootScope || $injector.get('$rootScope');
                // send notification a request has started
                rootScope.$broadcast(_START_REQUEST_);                
                
                return promise.then(success, error);
            }
        }];*/


        //$httpProvider.responseInterceptors.push(interceptor);

//}])
/*.directive('loadingWidget', ['_START_REQUEST_', '_END_REQUEST_', function (_START_REQUEST_, _END_REQUEST_) {
    return {
        restrict: "A",
        link: function (scope, element) {
            // hide the element initially
            element.hide();

            scope.$on(_START_REQUEST_, function () {
                // got the request start notification, show the element
                element.show();
            });

            scope.$on(_END_REQUEST_, function () {
                // got the request end notification, hide the element
                element.hide();
            });
        }
    };
}]);*/
