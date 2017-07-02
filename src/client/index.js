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

.controller('MainController', function ($scope, $route, $routeParams, ){

})

.directive('tabs', function() {

    return {
        restrict: 'E',
        transclude: false,
        scope: { group: '@' },
        controller : function($scope, $element) {
            var tabs = $scope.tabs = {};
            var buttons = $scope.buttons = [];
            var current = $scope.current = false;

            this.select = function (tabid, button) {
                current = tabid;

                angular.forEach(buttons, function(button) {
                    button.selected = false;
                });

                button.selected = true;

                angular.forEach(tabs, function(tab) {
                    tab.hide();
                });

                tabs[tabid].show();
            }

            this.addTab = function (tabid, controller){
                tabs[tabid] = controller;
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

.directive('tab', function() {
    return {
        restrict: 'E',
        transclude: true,
        scope: { group: '@', id: '@' },
        controller: function($scope, $element){
            $scope.visible = false;

            angular.element("tabs[group="+$scope.group+"]").controller('tabs').addTab($scope.id, this);

            this.hide = function(){
                $scope.visible = false;

            }

            this.show = function(){
                $scope.visible = true;

            }
        },
        template: '<div ng-show="visible" ng-transclude> </div>',
        replace: true
    }
})
