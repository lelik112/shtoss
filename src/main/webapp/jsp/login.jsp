<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${message["mess.title.login"]}</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
        <input type="hidden" name="command" value="login"/>
        <div class="form-group">
            <label for="log">${message["mess.label.login"]}</label>
            <input id="log" class="form-control" type="text" name="login" required maxlength="20"
                   placeholder="${message["mess.placeholder.login"]}"/>
        </div>
        <div class="form-group">
            <label for="pass">${message["mess.label.password"]}</label>
            <input id="pass" class="form-control" type="password" name="password" required maxlength="30"/>
        </div>
        <div class="bg-danger">${errorLoginPassMessage}</div>
        <div class="bg-danger">${wrongAction}</div>
        <div class="bg-danger">${nullPage}</div>
        <button class="btn btn-default" type="submit">${message["mess.button.login"]}</button>
        <br/>
        <a class="btn btn-link" href="<c:url value="${pageContext.request.contextPath}/controller?command=register" />">
            ${message["mess.label.register"]}
        </a>
    </form>
</div>
</body>
</html>
