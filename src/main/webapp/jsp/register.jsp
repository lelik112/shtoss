<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${message["mess.title.register"]}</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
        <input type="hidden" name="command" value="register"/>
        <div class="form-group">
            <label for="login">${message["mess.label.login"]}*</label>
            <input id="login" class="form-control" type="text" name="login" maxlength="20"
                   title="${message["mess.prompt.login"]}" required
                   value="${requestScope.login}" pattern="^(\w|\d){1,20}$"  />
        </div>
        <div class="form-group">
            <label for="password">${message["mess.label.password"]}*</label>
            <input id="password" class="form-control" type="password" name="password" maxlength="30"
                   title="${message["mess.prompt.password"]}" required
                   pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}"/>
        </div>
        <div class="form-group">
            <label for="confirm-password">${message["mess.label.repeat-password"]}*</label>
            <input id="confirm-password" class="form-control" type="password" name="repeat-password" />
        </div>
        <div class="form-group">
            <label for="email">${message["mess.label.email"]}*</label>
            <input id="email" class="form-control" type="email" name="email" maxlength="30" required
                   value="${requestScope.email}"/>
        </div>
        <div class="form-group">
            <label for="first-name">${message["mess.label.fname"]}</label>
            <input id="first-name" class="form-control" type="text" name="firstName" maxlength="20"
                   value="${requestScope.firstName}"/>
        </div>
        <div class="form-group">
            <label for="last-name">${message["mess.label.lname"]}</label>
            <input id="last-name" class="form-control" type="text" name="lastName" maxlength="20"
                   value="${requestScope.lastName}"/>
        </div>
        <div class="bg-warning">${errorLoginPassMessage}</div>
        <div class="bg-warning">${wrongAction}</div>
        <button class="btn btn-default" type="submit">${message["mess.button.register"]}</button>
        <br/>
        <a class="btn btn-link" href="<c:url value="${pageContext.request.contextPath}/controller?command=login" />">
            ${message["mess.label.login"]}
        </a>
    </form>

</div>
<script src="${pageContext.request.contextPath}/js/confirm-pass.js"></script>
</body>
</html>
