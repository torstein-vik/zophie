angular.module('zophie', ['ngRoute'])

// Setting up routing
.config(function($routeProvider, $locationProvider){

    // Use zophieRoutes from routes.js to declare all the $routeProvider.when's.
    $.each(zophieRoutes, function(path, data){
        var route = {
            templateUrl: data.url,
            template:    data.template,
            controller:  data.controller || 'ViewController',
            redirectTo:  data.redirectTo
        };

        if(data.reqlogin){
            route.resolve = { load: function($q, $location){
                var deferred = $q.defer();

                // This is very hacky but AFAIK firebase provides no promise object for simply having loaded currentUser
                firebase.auth().getRedirectResult().then(function(){
                    if(firebase.auth().currentUser){
                        deferred.resolve();
                    } else {
                        $location.path('/reqlogin');
                        deferred.reject();
                    }
                });

                return deferred.promise;
            }};
        }

        $routeProvider.when(path, route);
    });

    $routeProvider.otherwise({
        templateUrl: 'templates/404.html'
    });

    $locationProvider.html5Mode(true);
})

.directive('tabOption', function() {

    return {
        restrict: 'A',
        scope: { href: '@' },
        controller: function($scope, $location, $rootScope){

            $scope.isActive = function () {
                return $location.path() == $scope.href;
            };

        },
        replace: true,
        transclude: true,
        template: '<a ng-class="{active: isActive()}" ng-transclude></a>'
    }
})

.directive('markdown', function() {

    let markdown = new showdown.Converter();

    return {
        restrict: 'E',
        controller: function($element){
            MathJax.Hub.Queue(["Typeset", MathJax.Hub, $element[0]]);
        },
        template: function(element){
            return "<div class='markdown'>" + markdown.makeHtml(element.html().escapeHTML()) + "</div>";
        },
        replace: true
    }
})

.controller('MainController', function ($scope, $route, $routeParams, $location){
    $scope.loggedin = false;
    $scope.reqlogin = false;

    firebase.auth().onAuthStateChanged(function(user) {
        $scope.loggedin = (user != null);
        $scope.$apply();
    });

    firebase.auth().getRedirectResult().then(function(result) {
        if(result.user != null){
            $location.path("/home");
        }
    }).catch(function(error) {
        alert(error.message);
        $location.path("/login");
    });
})

.controller('ViewController', function($scope, $route, $routeParams, $location){
    MathJax.Hub.Queue(["Typeset", MathJax.Hub]);
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

.controller('AuthProviderController', function($scope, $route, $routeParams, $location) {

    let providerclasses = {
        google   : firebase.auth.GoogleAuthProvider,
        facebook : firebase.auth.FacebookAuthProvider,
        twitter  : firebase.auth.TwitterAuthProvider,
        github   : firebase.auth.GithubAuthProvider,
    };

    let providername = $routeParams.authprovider;

    if(providerclasses[providername]){
        let providerclass = providerclasses[providername];

        let provider = new providerclass();

        $location.path("/wait");
        firebase.auth().signInWithRedirect(provider);
    } else {
        $location.path("/login");
    }

})

.directive('cards', function() {

    return {
        restrict: 'E',
        scope: {},
        controller: function($scope, $element){
            $scope.cards = [];

            $scope.addcard = function(){
                $scope.cards.push({text: "text"});
            };
        },
        template: function(element){
            return '<div class="cards"> <div class="addcard-balancer ignore"></div> <div ng-repeat="card in cards"> <div class="card-title"> <h4> ' + (element.attr("title") || "{{card.title}}") + ' </h4> <a ng-click="cards.splice($index, 1)"> X </a> </div> <div class="card-content"> ' + element.html() + ' </div> </div> <div class="addcard ignore" ng-click="addcard()"> + </div> </div>';
        },
        replace: true
    };
})

.controller('MachineController', function($scope){

});
