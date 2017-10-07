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

    $scope.$on('modal-cover', function () {
        $scope.modalcover = true;
    });
    $scope.$on('modal-uncover', function () {
        $scope.modalcover = false;
    });

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

.directive('modal', function() {

    return {
        restrict: 'E',
        scope: { title: '@' },
        controller: function($scope){
            $scope.opened = false;

            $scope.close = function () {
                $scope.opened = false;
                $scope.$emit('modal-uncover');
            };

            $scope.open = function() {
                $scope.opened = true;
                $scope.$emit('modal-cover');
            };

            $scope.$on('modal-open', function() {
                $scope.open();
            });

            $scope.$on('modal-close', function() {
                $scope.close();
            });

        },
        replace: true,
        transclude: true,
        template: "<div class='modal' ng-show='opened'> <div class='modal-title'> <h2> {{title}} </h2> <a ng-click='close(); $event.stopPropagation()'> X </a> </div> <div class='modal-content' ng-transclude> </div> </div>"

    }
})

.directive('cards', function() {

    return {
        restrict: 'E',
        scope: { title: '@', id: '@' },
        controller: function($scope, $element){
            $scope.cards = [];

            $scope.lastnum = 0;

            $scope.addcard = function(){
                $scope.lastnum++;
                $scope.cards.push({title: $scope.title + " " + $scope.lastnum});
            };

            $scope.$watchCollection('cards', function(cards){
                $scope.$emit('update_cards', cards);
            });

        },
        template: function(element){
            return '<div class="cards"> <div class="addcard-balancer ignore"></div> <div ng-repeat="card in cards"> <div class="card-title"> <h4> {{card.title}} </h4> <a ng-click="cards.splice($index, 1)"> X </a> </div> <div class="card-content"> ' + element.html() + ' </div> </div> <div class="addcard ignore" ng-click="addcard()"> + </div> </div>';
        },
        replace: true
    };
})

.controller('MachineController', function($scope, $element){
    var canvas = document.getElementById("addmachine-canvas");
    var ctx = canvas.getContext('2d');

    $scope.data = { inputs: [], outputs: [] };

    canvas.width = canvas.clientWidth;
    canvas.height = canvas.clientHeight;

    function drawmachine(w, h, mw, mh){

        // anchor coordinates, center of machine
        var ax = w/2, ay = h/2;


        // main rectangle of machine
        ctx.moveTo(ax - mw / 2, ay - mh / 2);

        ctx.lineTo(ax + mw / 2, ay - mh / 2);
        ctx.lineTo(ax + mw / 2, ay + mh / 2);
        ctx.lineTo(ax - mw / 2, ay + mh / 2);
        ctx.lineTo(ax - mw / 2, ay - mh / 2);

        ctx.fillStyle = "red";
        ctx.strokeStyle = "black";
        ctx.fill();
        ctx.stroke();
    }

    function draw(){

        var w = canvas.width;
        var h = canvas.height;

        // width and height of machine
        var mw = 100, mh = 100;

        drawmachine(w, h, mw, mh);


        $scope.data.inputs.forEach(function(input, index, array){
            // retrive the dom element, +2 to ignore addcard-balancer
            var inputDOM = $("#inputs > div:nth-child(" + (index + 2) + ")");

            // amount of inputs
            var l = array.length;

            // start and finish coords
            var sx, sy, fx, fy;

            // control points coords
            var cpx1, cpy1, cpx2, cpy2;

            sx = 0;
            sy = 0;

            fx = 0;
            fy = w / 2 - mw / 2;

            // stroking coords
            ctx.moveTo(sx, sy);
            ctx.bezierCurveTo(cpx1, cpy1, cpx2, cpy2, fx, fy);

            ctx.strokeStyle = "black";
            ctx.stroke();
        });
    }

    $scope.$on('update_cards', function(event, value){
        $scope.data[event.targetScope.id] = value;

        draw();
    });
});
