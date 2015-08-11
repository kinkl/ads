<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ADs</title>
    <script src="resources/js/angular.js" type="text/javascript"></script>
    <script src="resources/js/angular-route.js" type="text/javascript"></script>
    <script src="resources/js/angular-resource.js" type="text/javascript"></script>
    <script src="resources/js/app.js"></script>
    <script src="resources/js/directives.js"></script>
    <script src="resources/js/services.js"></script>
    <link href="resources/css/bootstrap.css" rel="stylesheet"/>
    <style type="text/css">
        [ng\:cloak], [ng-cloak], .ng-cloak {
            display: none !important;
        }
    </style>
</head>
<body ng-app="Ads" ng-cloak class="ng-cloak">
    <div class="col-md-10 col-md-offset-1" ng-controller="rootCtrl">
        <div class="col-sm-12">
            <nav class="navbar navbar-default" ng-controller="navigationCtrl">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <ul class="nav nav-pills" role="tablist">
                            <li class="active"><a href="#/">home</a></li>
                        </ul>
                    </div>
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul class="nav navbar-nav navbar-right">
                            <li ng-hide="authenticatedUser"><a href="#/sign_on">sign on</a></li>
                            <li ng-hide="authenticatedUser"><a href="#/login">login</a></li>
                            <li ng-show="authenticatedUser"><a href="" ng-click="logout()">logout (<b><i>{{authenticatedUser.name}}</i></b>)</a></li>
                        </ul>
                    </div>
                </div>
            </nav>
        </div>
        <div ng-class="isSidebarVisible() ? 'col-sm-9' : 'col-sm-12'" ng-view></div>
        <div class="col-sm-3" ng-show="isSidebarVisible()">
            <pane-container>
                <notification-pane></notification-pane>
                <top-rated-advertisements-pane title="Top"></top-rated-advertisements-pane>
                <last-activity title="Last Activity"></last-activity>
            </pane-container>
        </div>
    </div>

</body>
</html>