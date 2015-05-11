<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <form method="post" class="signin" action="/static/j_spring_security_check">
        <table cellspacing="0">
            <tr>
                <th><label>Username or Email</label></th>
                <td>
                    <input id="username_or_email" name="j_username" type="text" />
                </td>
            </tr>
            <tr>
                <th><label>Password</label></th>
                <td>
                    <input id="password" name="j_password" type="password" />
                </td>
            </tr>
            <tr>
                <th></th>
                <td><input name="commit" type="submit" value="Sign In"/></td>
            </tr>
        </table>
        <alert type="ERROR">${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</alert>
    </form>
</body>
</html>
