angular.module('zophie', ['ngRoute'])

.config(function($routeProvider, $locationProvider){
    $routeProvider
    .when('/', {
        redirectTo: '/home'
    })
    .when('/home', {
        templateUrl: 'templates/home.html',
        controller: 'ViewController'
    })
    .when('/ask', {
        templateUrl: 'templates/ask.html',
        controller: 'ViewController'
    })
    .when('/ask/q=:query', {
        templateUrl: 'templates/query.html',
        controller: 'ViewController'
    })
    .when('/answer', {
        templateUrl: 'templates/answer.html',
        controller: 'ViewController',
        reqlogin: true
    })
    .when('/addcontent', {
        templateUrl: 'templates/addcontent.html',
        controller: 'ViewController',
        reqlogin: true
    })
    .when('/visualize', {
        templateUrl: 'templates/visualize.html',
        controller: 'ViewController'
    })
    .when('/about', {
        templateUrl: 'templates/about.html',
        controller: 'ViewController'
    })
    .when('/login', {
        templateUrl: 'templates/login.html',
        controller: 'ViewController'
    })
    .when('/register', {
        templateUrl: 'templates/register.html',
        controller: 'ViewController'
    })
    .when('/mypage', {
        templateUrl: 'templates/mypage.html',
        controller: 'ViewController',
        reqlogin: true
    })
    .when('/logout', {
        templateUrl: 'templates/logout.html',
        controller: 'ViewController',
        reqlogin: true
    })
    .when('/requirelogin', {
        templateUrl: 'templates/reqlogin.html',
        controller: 'ViewController'
    })
    .otherwise({
        templateUrl: 'templates/404.html'
    });

    $locationProvider.html5Mode(true);
})

.run(function($rootScope, $location){
    $rootScope.$on('$routeChangeStart', function(event, next, current){
        if(!$rootScope.loggedin && next.reqlogin){
            event.preventDefault();
            $location.path('/requirelogin');
        }
    });
})

.controller('MainController', function ($scope, $route, $routeParams, $location){
    $scope.loggedin = false;
    $scope.reqlogin = false;

    firebase.auth().onAuthStateChanged(function(user) {
        $scope.loggedin = (user != null);
        $scope.$apply();
    });
})

.controller('ViewController', function($scope, $route, $routeParams, $location){

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

.controller('LogoutController', function($scope, $route, $routeParams, $location) {
    $scope.submit = function() {
        firebase.auth().signOut().then(function(result){
            $location.path("/home");
        }).catch(function(error){
            alert("An error occured! " + error);
        });
    };
})
