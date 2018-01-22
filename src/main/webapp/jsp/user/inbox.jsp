<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="custom" uri="http://cheltsov.net" %>
<fmt:setLocale value="${locale}"/>

<html>
<head>
    <title>${message["mess.title.inbox"]}</title>
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
            <a class="btn btn-default"
               href="<c:url value="${pageContext.request.contextPath}/controller?command=redirect&nextPage=jsp.create-topic" />">
                <c:choose>
                    <c:when test="${user.role.roleID == 2}">
                        ${message["mess.button.create-topic-admin"]}
                    </c:when>
                    <c:otherwise>
                        ${message["mess.button.create-topic-user"]}
                    </c:otherwise>
                </c:choose>
            </a>
            <h2>${message["mess.h.messages"]}</h2>
            <table class="table table-striped">
                <tbody>
                <c:forEach var="conversation" items="${conversations}">
                    <tr>
                        <td class="col1">
                            <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${conversation.lastMessage.date}"/>
                        </td>
                        <c:if test="${user.role.roleID == 2}">
                            <td><p>${conversation.user}</p></td>
                        </c:if>
                        <td class="list-group-item">
                            <a href="<c:url value="${pageContext.request.contextPath}/controller?command=show-messages&conversationId=${conversation.conversationId}" />">
                                <h3 class="list-group-item-heading">${conversation.topic}</h3>
                            </a>
                                <p class="list-group-item-text">${conversation.lastMessage.user}: <custom:trim maxCharacters="60" value="${conversation.lastMessage.text}" /></p>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <p class="bg-warning">${error}</p>
        </div>
    </div>
</div>


<%@ include file="/jsp/common/footer.jspf" %>
</body>
</html>
