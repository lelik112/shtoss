<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${message["mess.title.create-topic"]}</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <a class="button"
       href="<c:url value="${pageContext.request.contextPath}/controller?command=inbox" />">«${message["mess.h.messages"]}</a>
    <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
        <c:choose>
            <c:when test="${user.role.roleID == 2}">
                <input type="hidden" name="command" value="admin-create-topic"/>
                <div class="form-group">
                    <label for="user">${message["mess.label.user"]}</label>
                    <input id="user" class="form-control" type="text" name="userLogin" required/>
                </div>
            </c:when>
            <c:otherwise>
                <input type="hidden" name="command" value="create-topic"/>
            </c:otherwise>
        </c:choose>
        <div class="form-group">
            <label for="topic">${message["mess.label.topic"]}</label>
            <input id="topic" class="form-control" type="text" name="topic" value="${requestScope.topic}" required/>
        </div>
        <div class="form-group">
            <label for="comment">${message["mess.label.text"]}</label>
            <textarea class="form-control" rows="5" id="comment" name="text" value="${requestScope.text}"
                      required></textarea>
        </div>
        <div class="bg-danger">${error}</div>
        <button class="btn btn-default" type="submit">${message["mess.button.write"]}</button>
        <br/>
        <a class="btn btn-link"
           href="<c:url value="${pageContext.request.contextPath}/controller?command=redirect&nextPage=jsp.main" />">
            ${message["mess.label.main-page"]}
        </a>
    </form>
</div>
</body>
</html>
