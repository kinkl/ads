var app = angular.module('Ads', ['ngRoute']);

app.config(function($routeProvider, $httpProvider) {
    $routeProvider.when('/', {
        templateUrl: 'resources/partials/home.html',
        controller: 'home'
    }).when('/login', {
        templateUrl: 'resources/partials/login.html',
        controller: 'navigation'
    }).otherwise('/');
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});

app.controller('home', function($scope, $http) {
    $http.get('/random_data').success(function(data) {
        $scope.greeting = data;
        console.log('success');
    }).error(function(data) {
        console.log(data);
    });
});

app.controller('navigation', function($rootScope, $scope, $http, $location) {
    var authenticate = function(credentials, callback) {
        var headers = credentials ? {authorization: "Basic " + btoa(credentials.username + ":" + credentials.password)
        } : {};

        $http.get('user', {headers: headers}).success(function(data) {
            if (data.name) {
                $rootScope.authenticated = true;
            }
            else {
                $rootScope.authenticated = false;
            }
            callback && callback();
        }).error(function() {
            $rootScope.authenticated = false;
            callback && callback();
        });
    };

    authenticate();
    $scope.credentials = {};
    $scope.login = function() {
        authenticate($scope.credentials, function() {
            if ($rootScope.authenticated) {
                $location.path("/");
                $scope.error = false;
            } else {
                $location.path("/login");
                $scope.error = true;
            }
        });
    };

    $scope.logout = function() {
        $http.post('logout', {}).success(function() {
            $rootScope.authenticated = false;
            $location.path("/");
        }).error(function(data) {
            $rootScope.authenticated = false;
        });
    };
});
