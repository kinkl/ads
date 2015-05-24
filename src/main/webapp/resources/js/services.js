app.factory('responseObserver', function($q, $location) {
    return {
        'responseError': function(rejection) {
            if (rejection.status === 403) {
                $location.path('/forbidden');
            }
            else {
                $location.path('/unknown_error');
            }
            return $q.reject(rejection);
        }
    }
});