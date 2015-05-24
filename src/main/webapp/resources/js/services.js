app.factory('responseObserver', function($q, $location) {
    return {
        'responseError': function(rejection) {
            if (rejection.status === 403) {
                $location.path('/forbidden');
            }
            return $q.reject(rejection);
        }
    }
});
