<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>${message["mess.title.guest"]}</title>
</head>
<body>
<h2>${message["mess.h.welcom"]}</h2>
<p>${message["mess.info.guest"]}</p>
<a class="btn btn-link" href="<c:url value="${pageContext.request.contextPath}/controller?command=register" />">
    ${message["mess.label.register"]}
</a>
<br/>
<a class="btn btn-link" href="<c:url value="${pageContext.request.contextPath}/controller?command=login" />">
    ${message["mess.label.login"]}
</a>
</body>
</html>
