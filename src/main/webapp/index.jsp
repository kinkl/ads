<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ADs</title>
    <script src="resources/js/angular.js" type="text/javascript"></script>
    <script src="resources/js/angular-route.js" type="text/javascript"></script>
    <script src="resources/js/app.js"></script>
    <script src="resources/js/services.js"></script>
    <link href="resources/css/bootstrap.css" rel="stylesheet"/>
    <style type="text/css">
        [ng\:cloak], [ng-cloak], .ng-cloak {
            display: none !important;
        }
    </style>
</head>
<body ng-app="Ads" ng-cloak class="ng-cloak">
    <div ng-controller="navigationCtrl" class="container">
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <ul class="nav nav-pills" role="tablist">
                        <li class="active"><a href="#/">home</a></li>
                    </ul>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right">
                        <li ng-hide="authenticatedUser"><a href="#/login">login</a></li>
                        <li ng-show="authenticatedUser"><a href="" ng-click="logout()">logout (<b><i>{{authenticatedUser.name}}</i></b>)</a></li>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
    <div ng-view class="container"></div>

</body>
</html>