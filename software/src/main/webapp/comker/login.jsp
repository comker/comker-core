<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Comker Login</title>
    </head>
    <body>
        <div id="LoginPanel">
            <div class="section">
                <form action="../j_spring_security_check" method="post">
                    <div class="field">
                        <label for="j_username"><fmt:message key="login.username"/>:</label>
                        <input type="text" name="j_username" id="j_username" value="BNA00001"/>
                    </div>
                    <div class="field">
                        <label for="j_password"><fmt:message key="login.password"/>:</label>
                        <input type="password" name="j_password" id="j_password" />
                    </div>
                    <div class="field">
                        <label for="remember_me"><fmt:message key="login.rememberMe"/>:</label>
                        <input type="checkbox" name="_spring_security_remember_me" id="remember_me" />
                    </div>
                    <div class="button">
                        <input name="submit" id="submit" type="submit" value="<fmt:message key="button.login"/>" />
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>

