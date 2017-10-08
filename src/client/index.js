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
        scope: { title: '@', id: '@' },
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

            $scope.$on('modal-open', function(event, id) {
                if(id === undefined || (id !== undefined && id === $scope.id)) {
                   $scope.open();
                }
            });

            $scope.$on('modal-close', function(event, id) {
                if(id === undefined || (id !== undefined && id === $scope.id)) {
                    $scope.close();
                }
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

    window.addEventListener('resize', function(){
        canvas.width = canvas.clientWidth;
        canvas.height = canvas.clientHeight;

        draw();
    });

    // width and height of machine
    var mw = 100, mh = 100;


    canvas.addEventListener('mousemove', function(event){
        var x = event.offsetX;
        var y = event.offsetY;

        domachinepath(canvas.width, canvas.height);

        if (ctx.isPointInPath(x, y)){
             document.body.style.cursor = "pointer";
        } else {
             document.body.style.cursor = "initial";
        }
    });

    function domachinepath(w, h){

        // anchor coordinates, center of machine
        var ax = w/2, ay = h/2;


        // main rectangle of machine
        ctx.beginPath();
        ctx.moveTo(ax - mw / 2, ay - mh / 2);

        ctx.lineTo(ax + mw / 2, ay - mh / 2);
        ctx.lineTo(ax + mw / 2, ay + mh / 2);
        ctx.lineTo(ax - mw / 2, ay + mh / 2);
        ctx.lineTo(ax - mw / 2, ay - mh / 2);

        ctx.closePath();
    }

    function drawmachine(w, h){

        domachinepath(w, h);

        ctx.strokeStyle = "black";
        ctx.stroke();

        ctx.textAlign = "center";
        ctx.textBaseline = "middle";
        ctx.font = (mw / 2) + 'px serif';
        ctx.fillText("⚙", w / 2, h / 2);
    }

    function drawline(index, array, cardsid, w, h, flipy){
        // retrive the dom element, +2 to ignore addcard-balancer
        var inputDOM = $("#" + cardsid + " > div:nth-child(" + (index + 2) + ")")[0];

        // amount of inputs
        var l = array.length;

        // start and finish coords
        var sx, sy, fx, fy;

        // control points coords
        var cpx1, cpy1, cpx2, cpy2;

        sx = inputDOM.offsetLeft + (inputDOM.clientWidth / 2) - canvas.offsetLeft;
        sy = 0;

        fx = w / 2 + ((index - (l - 1) / 2) / l * mw / 2);
        fy = h / 2 - mh / 2 - 10;

        cpx1 = sx;
        cpy1 = fy + Math.abs(index - (l - 1) / 2) * 10;

        cpx2 = fx;
        cpy2 = sy + Math.abs(index - (l - 1) / 2) * 10;

        // stroking coords
        ctx.beginPath();
        ctx.moveTo(sx, flipy ? h - sy : sy);
        ctx.bezierCurveTo(cpx1, flipy ? h - cpy1 : cpy1, cpx2, flipy ? h - cpy2 : cpy2, fx, flipy ? h - fy : fy);

        ctx.strokeStyle = "black";
        ctx.stroke();
    }

    function draw(){

        var w = canvas.width;
        var h = canvas.height;

        ctx.clearRect(0, 0, w, h);

        drawmachine(w, h);


        $scope.data.inputs.forEach(function(input, index, array){
            drawline(index, array, "inputs",  w, h, false);
        });

        $scope.data.outputs.forEach(function(input, index, array){
            drawline(index, array, "outputs", w, h, true);
        });
    }

    $scope.$on('update_cards', function(event, value){
        event.stopPropagation();
        $scope.data[event.targetScope.id] = value;

        $scope.$evalAsync(draw);
    });
});
