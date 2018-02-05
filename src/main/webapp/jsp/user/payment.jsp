<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${message["mess.title.payment"]}</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
        <input type="hidden" name="command" value="payment"/>
        <div class="form-group">
            <label for="amount">${message["mess.label.amount"]}</label>
            <input id="amount" class="form-control" type="text" name="amount" required
                   pattern="^[1-9][\d]{0,3}([\.,][\d]{0,2})?$"/>
        </div>
        <button class="btn btn-default" type="submit">${message["mess.button.payment"]}</button>
        <br/>
        <a class="btn btn-link" href="<c:url value="${pageContext.request.contextPath}/controller?command=redirect&nextPage=jsp.main" />">
            ${message["mess.label.main-page"]}
        </a>
    </form>
</div>
</body>
</html>
