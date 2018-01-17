<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${message["mess.title.congratulation"]}</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<h1 class="bg-success">${congratulation}</h1>
<p class="bg-success">${success}</p>
<p class="bg-danger">${notSuccess}</p>
<a class="btn btn-link" href="<c:url value="${pageContext.request.contextPath}/controller?command=redirect&nextPage=jsp.main" />">
    ${message["mess.label.main-page"]}
</a>
</body>
</html>
