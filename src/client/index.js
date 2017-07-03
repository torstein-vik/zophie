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
    })
    .when('/login', {
        templateUrl: 'templates/login.html',
        controller: undefined
    })
    .when('/register', {
        templateUrl: 'templates/register.html',
        controller: undefined
    })
    .when('/mypage', {
        templateUrl: 'templates/mypage.html',
        controller: undefined
    })
    .when('/logout', {
        templateUrl: 'templates/logout.html',
        controller: undefined
    });

    $locationProvider.html5Mode(true);
})

.controller('MainController', function ($scope, $route, $routeParams, $location){

})

.directive('tabOption', function() {

    return {
        restrict: 'E',
        transclude: true,
        scope: { for: '@' },
        controller: function($scope, $element, $route, $routeParams, $location){

            $scope.select = function () {
                $location.path($scope.for);
            };

            $scope.isActive = function () {
                return $location.path().substr(1, $scope.for.length) == $scope.for;
            };

        },
        template: '<div class="taboption" ng-class="{active: isActive()}" ng-click="select()" ng-transclude> </div>',
        replace: true
    }
})

.controller('LoginController', function($scope, $route, $routeParams, $location) {
    $scope.submit = function() {
        firebase.auth().signInWithEmailAndPassword($scope.email, $scope.password).then(function(result){
            alert("Logged in!");
        }).catch(function(error){
            alert("An error occured! " + error);
        });
    };
})

.controller('RegisterController', function($scope, $route, $routeParams, $location) {
    $scope.submit = function() {
        if($scope.password1 === $scope.password2){
            firebase.auth().createUserWithEmailAndPassword($scope.email, $scope.password1).then(function(result){
                alert("Registered!");
            }).catch(function(error){
                alert("An error occured! " + error);
            });
        } else {
            alert("Mismatch between password and password confirm!");
        }
    };
})
