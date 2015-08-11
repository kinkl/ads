var app = angular.module('Ads', ['ngRoute', 'ngResource']);

app.config(function($routeProvider, $httpProvider) {
    $httpProvider.interceptors.push('responseObserver');
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

    $routeProvider.when('/home', {
        templateUrl: 'resources/partials/home.html',
        controller: 'homeCtrl'
    }).when('/login', {
        templateUrl: 'resources/partials/login.html',
        controller: 'navigationCtrl'
    }).when('/sign_on', {
        templateUrl: 'resources/partials/signOn.html',
        controller: 'signOnCtrl'
    }).when('/new_advertisement', {
        templateUrl: 'resources/partials/newAdvertisement.html',
        controller: 'newAdvertisementCtrl'
    }).when('/forbidden', {
        templateUrl: 'resources/partials/forbidden.html',
        controller: 'forbiddenCtrl'
    }).when('/unauthorized', {
        templateUrl: 'resources/partials/unauthorized.html',
        controller: 'unauthorizedCtrl'
    }).when('/unknown_error', {
        templateUrl: 'resources/partials/unknown_error.html',
        controller: 'unknownErrorCtrl'
    }).otherwise('/home');
});

app.controller('rootCtrl', ['$scope', '$location', function($scope, $location) {
    $scope.pagesWithSidebar = ['/home', '/new_advertisement'];

    $scope.isSidebarVisible = function() {
        var currentLocation = $location.path();
        for (var i = 0; i < $scope.pagesWithSidebar.length; i++) {
            if (currentLocation === $scope.pagesWithSidebar[i]) {
                return true;
            }
        }
        return false;
    };
}]);

app.controller('homeCtrl', ['$rootScope', '$scope', '$http', 'Advertisement', function($rootScope, $scope, $http, Advertisement) {
    $scope.advertisements = {};
    $scope.orderProp = 'username';

    $scope.fetchAdvertisements = function() {
        Advertisement.query(function(ads) {
            $scope.advertisements = ads;
            for (var i = 0; i < $scope.advertisements.length; i++) {
                $scope.advertisements[i].dateTime = new Date($scope.advertisements[i].dateTime);
            }
        });
    };

    $scope.fetchAdvertisements();

    $scope.deleteAdvertisement = function(advertisement) {
        Advertisement.delete({ id: advertisement.id }, function() {
            $scope.fetchAdvertisements();
            $rootScope.$broadcast('DeleteAdvertisementEvent', advertisement);
        });
    };

    $scope.adminActionStub = function() {
        $http.post('admin_action_stub', {});
    };

    $scope.upvote = function(advertisement) {
        Advertisement.upvote(advertisement, function(response) {
            if (response.success) {
                if (response.data.isNewVoteRegistered) {
                    advertisement.rate++;
                    $rootScope.$broadcast('VoteEvent', true, 1, advertisement);
                }
                else {
                    $rootScope.$broadcast('VoteEvent', false);
                }
            }
            else {
                console.log('Record upvote FAILED! Reason: ' + response.message);
            }
        });
    };

    $scope.downvote = function(advertisement) {
        Advertisement.downvote(advertisement, function(response) {
            if (response.success) {
                if (response.data.isNewVoteRegistered) {
                    advertisement.rate--;
                    $rootScope.$broadcast('VoteEvent', true, -1, advertisement);
                }
                else {
                    $rootScope.$broadcast('VoteEvent', false);
                }
            }
            else {
                console.log('Record downvote FAILED! Reason: ' + response.message);
            }
        });
    };
}]);

app.controller('newAdvertisementCtrl', ['$scope', '$location', 'Advertisement', function($scope, $location, Advertisement) {
    $scope.text = '';

    $scope.submitButtonClass = 'disabled';

    $scope.textChange = function() {
        if ($scope.text.trim().length == 0) {
            $scope.submitButtonClass = 'disabled';
        }
        else {
            $scope.submitButtonClass = '';
        }
    };

    $scope.post = function() {
        Advertisement.save({ text: $scope.text.trim() }, function() {
            $location.path("/");
        });
    };
}]);

app.controller('navigationCtrl', ['$rootScope', '$scope', '$http', '$location', function($rootScope, $scope, $http, $location) {
    var getAuthenticatedUser = function() {
        return $http.get('authenticated_user').success(function(data) {
            $rootScope.authenticatedUser = data;
        }).error(function() {
            $rootScope.authenticatedUser = null;
        });
    };

    getAuthenticatedUser();

    $scope.credentials = {};
    $scope.login = function() {
        $http.post('login', $scope.credentials).success(function(data) {
            getAuthenticatedUser().then(function() {
                $scope.error = false;
                $location.path("/");
                $rootScope.$broadcast('LoginEvent', $rootScope.authenticatedUser);
            });
        }).error(function() {
            $scope.error = true;
        });
    };

    $scope.logout = function() {
        $http.post('logout', {}).success(function() {
            var loggedOutUser = $rootScope.authenticatedUser;
            getAuthenticatedUser().then(function() {
                $location.path('/home');
                $rootScope.$broadcast('LogoutEvent', loggedOutUser);
            });
        }).error(function(data) {
            getAuthenticatedUser();
        });
    };
}]);

app.controller('forbiddenCtrl', ['$scope', function($scope) {
    $scope.message = 'You have no access for this resource';
}]);

app.controller('unauthorizedCtrl', ['$scope', function($scope) {
    $scope.message = 'You are not authorized (probably bad credentials)';
}]);

app.controller('unknownErrorCtrl', ['$scope', function($scope) {
    $scope.message = 'Unhandled error!';
}]);

app.controller('signOnCtrl', ['$rootScope', '$scope', '$location', '$http', function($rootScope, $scope, $location, $http) {
    $scope.username = '';
    $scope.password = '';
    $scope.passwordRepeat = '';

    $scope.isFieldsValid = false;

    $scope.submitButtonClass = 'disabled';

    $scope.isUserExists = false;
    $scope.isUsernameEmpty = true;

    $scope.isPasswordRepeatValid = true;

    var checkFieldValidity = function() {
        $scope.isFieldsValid = !$scope.isUserExists && !$scope.isUsernameEmpty && $scope.password.length > 0 && $scope.isPasswordRepeatValid;
        if ($scope.isFieldsValid) {
            $scope.submitButtonClass = '';
        }
        else {
            $scope.submitButtonClass = 'disabled';
        }
    };

    $scope.cancelSignOn = function() {
        $location.path('/home');
    };

    $scope.usernameChange = function() {
        $scope.username = $scope.username.trim();
        $http.get('is_user_exists', { params: { username: $scope.username } } ).success(function(data) {
            $scope.isUserExists = data.isUserExists;
            $scope.isUsernameEmpty = $scope.username.length === 0;
            checkFieldValidity();
        });
    };

    $scope.passwordChange = function() {
        $scope.isPasswordRepeatValid = $scope.password === $scope.passwordRepeat && $scope.password.length > 0;
        checkFieldValidity();
    };

    $scope.signOn = function() {
        $http.post('sign_on', { username: $scope.username, password: $scope.password }).success(function() {
            $rootScope.$broadcast('SignOnEvent', $scope.username);
            $location.path('/login');
        });
    };
}]);
