app.directive('paneContainer', function() {
    return {
        restrict: 'E',
        scope: {},
        controller: function($scope) {
            var panes = $scope.panes = [];
            var displayPanes = $scope.displayPanes = [];
            var me = this;

            this.select = $scope.select = function(pane) {
                angular.forEach(panes, function(pane) {
                    pane.selected = false;
                });
                pane.selected = true;
                if (pane.refresh) {
                    pane.refresh();
                }
            };

            this.addPane = function(pane) {
                panes.push(pane);
                if (pane.hasLink === true) {
                    if (displayPanes.length === 0 ) {
                        me.select(pane);
                    }
                    displayPanes.push(pane);
                }
            };

            $scope.$on('VoteEvent', function(event, success) {
                for (var i = 0; i < panes.length; i++) {
                    if (panes[i].type === 'notification') {
                        panes[i].success = success;
                        panes[i].text = success ? 'Your vote is saved!' : 'You cannot vote twice on one advertisement!';
                        me.select(panes[i]);
                        break;
                    }
                }
            });

            $scope.isVisible = function() {
                return true;//currentLocation === '/home';
            }
        },
        transclude: true,
        templateUrl: '/resources/partials/paneContainer.html'
    }
});

app.directive('lastActivity', function() {
    return {
        require: '^paneContainer',
        restrict: 'E',
        scope: {
            title: '@'
        },
        controller: function($rootScope, $scope) {
            $scope.hasLink = true;
            $scope.events = [];

            $scope.addEvent = function (event) {
                if ($scope.events.length < 5) {
                    $scope.events.splice(0, 0, event);
                }
                else {
                    $scope.events.pop();
                    $scope.events.unshift(event);
                }
            };

            $scope.addVoteEvent = function(value, advertisement) {
                $scope.addEvent({
                    date: new Date(),
                    user: $rootScope.authenticatedUser,
                    value: value,
                    advertisement: advertisement,
                    eventType: 'VoteEvent'
                });
            };

            $scope.addLoginEvent = function(user) {
                $scope.addEvent({
                    date: new Date(),
                    user: user,
                    eventType: 'LoginEvent'
                });
            };

            $scope.addLogoutEvent = function(user) {
                $scope.addEvent({
                    date: new Date(),
                    user: user,
                    eventType: 'LogoutEvent'
                });
            };

            $scope.addSignOnEvent = function(username) {
                $scope.addEvent({
                    date: new Date(),
                    username: username,
                    eventType: 'SignOnEvent'
                });
            };

            $scope.addDeleteAdvertisementEvent = function(advertisement) {
                $scope.addEvent({
                    date: new Date(),
                    user: $rootScope.authenticatedUser,
                    advertisement: advertisement,
                    eventType: 'DeleteAdvertisementEvent'
                });
            }
        },
        link: function(scope, element, attrs, tabsCtrl) {
            tabsCtrl.addPane(scope);

            scope.$on('VoteEvent', function(event, success, value, advertisement) {
                if (success) {
                    scope.addVoteEvent(value, advertisement);
                }
            });

            scope.$on('LoginEvent', function(event, user) {
                scope.addLoginEvent(user);
                tabsCtrl.select(scope);
            });

            scope.$on('LogoutEvent', function(event, user) {
                scope.addLogoutEvent(user);
                tabsCtrl.select(scope);
            });

            scope.$on('SignOnEvent', function(event, username) {
                scope.addSignOnEvent(username);
            });

            scope.$on('DeleteAdvertisementEvent', function(event, advertisement) {
                scope.addDeleteAdvertisementEvent(advertisement)
                tabsCtrl.select(scope);
            });
        },
        templateUrl: '/resources/partials/lastActivity.html'
    }
});

app.directive('topRatedAdvertisementsPane', function() {
    return {
        require: '^paneContainer',
        restrict: 'E',
        scope: {
            title: '@'
        },
        controller: function($scope, $timeout, Advertisement) {
            $scope.hasLink = true;
            $scope.isUpdateFinished = true;

            $scope.topAds;

            $scope.refresh = function() {
                $scope.isUpdateFinished = false;
                Advertisement.queryTopRated(function(response) {
                    if (response.success) {
                        $scope.topAds = response.data;
                        for (var i = 0; i < $scope.topAds.length; i++) {
                            $scope.topAds[i].dateTime = new Date($scope.topAds[i].dateTime);
                        }
                    }
                    else {
                        console.log('Top Rated query FAILED');
                    }
                    $timeout(function() {
                        $scope.isUpdateFinished = true;
                    }, 1000);
                });
            }
        },
        link: function(scope, element, attrs, tabsCtrl) {
            tabsCtrl.addPane(scope);
        },
        templateUrl: '/resources/partials/topRatedAdvertisementsPane.html'
    }

});

app.directive('notificationPane', function() {
    return {
        require: '^paneContainer',
        restrict: 'E',
        scope: { },
        controller: function($scope) {
            $scope.type = 'notification';
            $scope.text = '';
            $scope.success = true;
        },
        link: function(scope, element, attrs, tabsCtrl) { tabsCtrl.addPane(scope); },
        templateUrl: '/resources/partials/notificationPane.html'
    }
});
