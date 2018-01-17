<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>${message["mess.title.game"]}</title>
    <%@ include file="/jsp/common/head.jspf"%>

</head>
<body>
<%@ include file="/jsp/common/navbar.jspf" %>

<div class="container-fluid text-center">
    <div class="row content">
        <div class="col-sm-3 sidenav">
            <%@ include file="/jsp/common/news.jspf" %>
        </div>
        <div class="col-sm-9">
            <div class="row">
                <div class="col-sm-12 text-center">
                    <div class="well">
                        <p class="card-class">&#${card}; ${message["mess.info.bid"]} ${bid}</p>
                    </div>
                    <a class="btn btn-link"
                       href="<c:url value="${pageContext.request.contextPath}/controller?command=redirect&nextPage=jsp.start-game" />">
                        ${message["mess.label.start-game-again"]}
                    </a>
                </div>
                <div class="col-sm-6 text-left">
                    <div class="well">
                        <c:forEach var="current" begin="0" step="2" items="${result}">
                            <p class="card-class">&#${current};</p>
                        </c:forEach>
                        <c:if test="${fn:length(result) % 2 != 0}">
                            <div class="alert alert-danger">
                                <h1>${message["mess.info.lost"]}</h1>
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="col-sm-6 text-right">
                    <div class="well">
                        <c:forEach var="current" begin="1" step="2" items="${result}">
                            <p class="card-class">&#${current};</p>
                        </c:forEach>
                        <c:if test="${fn:length(result) % 2 == 0}">
                            <div class="alert alert-success">
                                <h1>${message["mess.info.won"]}</h1>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>
</body>
</html>