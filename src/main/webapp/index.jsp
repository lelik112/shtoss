<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="language" value="${pageContext.request.locale.language}" scope="session"/>

<html>
<head>
    <title>${messages["mess.title.index"]}</title>
</head>
<body>
<jsp:forward page="${pageContext.request.contextPath}/jsp/login.jsp"/>
</body>
</html>