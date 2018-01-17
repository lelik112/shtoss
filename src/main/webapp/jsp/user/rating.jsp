<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>

<html>
<head>
    <title>${message["mess.title.rating"]}</title>
    <%@ include file="/jsp/common/head.jspf" %>
</head>
<body>
<%@ include file="/jsp/common/navbar.jspf" %>

<div class="container-fluid text-center">
    <div class="row content">
        <div class="col-sm-3 sidenav">
            <%@ include file="/jsp/common/news.jspf" %>
        </div>
        <div class="col-sm-9 text-left">
            <h4>${message["mess.button.rating"]}</h4>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th>${message["mess.th.plase"]}</th>
                    <th>${message["mess.th.user"]}</th>
                    <th>${message["mess.th.win-games"]}</th>
                    <th>${message["mess.th.all-games"]}</th>
                    <th>${message["mess.th.total-bid"]}</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="rating" varStatus="i" items="${ratings}">
                    <c:choose>
                        <c:when test="${rating.userLogin eq user.login}">
                            <tr class="red">
                        </c:when>
                        <c:otherwise>
                            <tr>
                        </c:otherwise>
                    </c:choose>
                    <td>${i.count}</td>
                    <td>${rating.userLogin}</td>
                    <td>${rating.winGames}</td>
                    <td>${rating.allGames}</td>
                    <td>${rating.totalBid}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <p>${error}</p>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>
</body>
</html>
