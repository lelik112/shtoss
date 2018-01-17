<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>

<html>
<head>
    <title>${message["mess.title.operation-history"]}</title>
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
            <h4>${message["mess.button.operation-history"]}</h4>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th>${message["mess.th.amount"]}</th>
                    <th>${message["mess.th.description"]}</th>
                    <th>${message["mess.th.date"]}</th>
                    <th>${message["mess.th.balance"]}</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="operation" items="${operations}">
                    <tr>
                        <c:choose>
                            <c:when test="${operation.amount < 0}">
                                <td class="red">${operation.amount}</td>
                                <td>${message["mess.td.lost"]}</td>
                            </c:when>
                            <c:otherwise>
                                <td class="green">+${operation.amount}</td>
                                <td><c:if test="${operation.type eq 'game'}"> ${message["mess.td.won"]}</c:if>
                                    <c:if test="${operation.type eq 'payment'}"> ${message["mess.td.payment"]}</c:if>
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${operation.date}"/></td>
                        <td>${operation.balance}</td>
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
