'use strict';

/* Filters */

angular.module('myApp.filters', [])
  .filter('timeago', function(){
  return function(date){
    return moment(date).fromNow();
  };
});