var app = angular.module('Ads', ['ngRoute']);

app.config(function($routeProvider, $httpProvider) {
    $httpProvider.interceptors.push('responseObserver');
    $routeProvider.when('/', {
        templateUrl: 'resources/partials/home.html',
        controller: 'homeCtrl'
    }).when('/login', {
        templateUrl: 'resources/partials/login.html',
        controller: 'navigationCtrl'
    }).when('/forbidden', {
        templateUrl: 'resources/partials/forbidden.html',
        controller: 'forbiddenCtrl'
    }).otherwise('/');
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});

app.controller('homeCtrl', function($scope, $http) {
    $http.get('/random_data').success(function(data) {
        $scope.greeting = data;
        console.log('success');
    }).error(function(data) {
        console.log(data);
    });
});

app.controller('navigationCtrl', function($rootScope, $scope, $http, $location) {
    var getAuthenticatedUser = function() {
        $http.get('authenticated_user').success(function(data) {
            $rootScope.authenticatedUser = data;
        }).error(function() {
            $rootScope.authenticatedUser = null;
        });
    };

    getAuthenticatedUser();

    $scope.credentials = {};
    $scope.login = function() {
        $http.post('login', $scope.credentials).success(function(data) {
            getAuthenticatedUser();
            $scope.error = false;
            $location.path("/");
        }).error(function() {
//            $rootScope.authenticated = false;
            $scope.error = true;
        });
    };

    $scope.logout = function() {
        $http.post('logout', {}).success(function() {
            getAuthenticatedUser();
            $location.path("/");
        }).error(function(data) {
            getAuthenticatedUser();
        });
    };
});

app.controller('forbiddenCtrl', ['$scope', function($scope) {
    $scope.message = 'You have no access for this resource';
}]);
