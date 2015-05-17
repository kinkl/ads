<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ADs</title>
    <script src="resources/js/angular.js" type="text/javascript"></script>
    <script src="resources/js/angular-route.js" type="text/javascript"></script>
    <script src="resources/js/app.js"></script>
    <link href="resources/css/bootstrap.css" rel="stylesheet"/>
    <style type="text/css">
        [ng\:cloak], [ng-cloak], .ng-cloak {
            display: none !important;
        }
    </style>
</head>
<body ng-app="Ads" ng-cloak class="ng-cloak">
    <div ng-controller="navigation" class="container">
        <ul class="nav nav-pills" role="tablist">
            <li class="active"><a href="#/">home</a></li>
            <li><a href="#/login">login</a></li>
            <li ng-show="authenticated"><a href="" ng-click="logout()">logout</a></li>
        </ul>
    </div>
    <div ng-view class="container"></div>
<!--<div class="container">
    <h1>Greeting</h1>
    <div ng-controller="home" ng-cloak class="ng-cloak">
        <p>The ID is {{greeting.id}}</p>
        <p>The content is {{greeting.content}}</p>
    </div>
</div>-->

</body>
</html>