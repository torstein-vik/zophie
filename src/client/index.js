load().done(() => {
    let app = firebase.app();

});

function load(){
    let DOMLoaded = new $.Deferred((def) => $(def.resolve));
    return DOMLoaded;
}





angular.module('zophie', ['ngRoute'])

.config(function($routeProvider, $locationProvider){
    $routeProvider
    .when('/home', {
        templateUrl: 'templates/home.html',
        controller: undefined
    })
    .when('/ask', {
        templateUrl: 'templates/ask.html',
        controller: undefined
    })
    .when('/ask/q=:query', {
        templateUrl: 'templates/query.html',
        controller: undefined
    })
    .when('/answer', {
        templateUrl: 'templates/answer.html',
        controller: undefined
    })
    .when('/addcontent', {
        templateUrl: 'templates/addcontent.html',
        controller: undefined
    })
    .when('/visualize', {
        templateUrl: 'templates/visualize.html',
        controller: undefined
    })
    .when('/about', {
        templateUrl: 'templates/about.html',
        controller: undefined
    });

    $locationProvider.html5Mode(true);
})

.controller('MainController', function ($scope, $route, $routeParams, $location){
    console.log("hi");
})

.directive('tabs', function() {

    return {
        restrict: 'E',
        transclude: false,
        scope: { group: '@' },
        controller : function($scope, $element, $route, $routeParams, $location) {
            var buttons = $scope.buttons = [];

            this.select = function (page, button) {

                angular.forEach(buttons, function(button) {
                    button.selected = false;
                });

                button.selected = true;

                $location.path(page);
            }

            this.addButton = function (button) {
                buttons.push(button);
            }
        }
    }
})

.directive('tabOption', function() {

    return {
        require: '^^tabs',
        restrict: 'E',
        transclude: true,
        scope: { for: '@' },
        link: function($scope, $element, attrs, tabsController){
            tabsController.addButton($scope);

            var selected = $scope.selected = false;

            $scope.select = function () {
                tabsController.select($scope.for, $scope);
            }

        },
        template: '<div class="taboption" ng-class="{active: selected}" ng-click="select()" ng-transclude> </div>',
        replace: true
    }
})
