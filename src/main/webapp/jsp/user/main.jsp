<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${message["mess.title.main"]}</title>
    <%@ include file="/jsp/common/head.jspf"%>
</head>
<body>
<%@ include file="/jsp/common/navbar.jspf"%>

<div class="container-fluid text-center">
    <div class="row content">
        <div class="col-sm-3 sidenav">
            <%@ include file="/jsp/common/news.jspf"%>
        </div>
        <div class="col-sm-9 text-left">
            <a class="btn btn-link" href="<c:url value="${pageContext.request.contextPath}/controller?command=redirect&nextPage=jsp.start-game" />">
                ${message["mess.label.start-game"]}
            </a>
            <hr>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf"%>
</body>
</html>