<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>

<html>
<head>
    <title>${message["mess.title.profile"]}</title>
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
            <c:choose>
                <c:when test="${user.role.roleID == 2}">
                    <a class="button"
                       href="<c:url value="${pageContext.request.contextPath}/controller?command=admin-inbox" />">«${message["mess.h.messages"]}</a>
                </c:when>
                <c:otherwise>
                    <a class="button"
                       href="<c:url value="${pageContext.request.contextPath}/controller?command=inbox" />">«${message["mess.h.messages"]}</a>
                </c:otherwise>
            </c:choose>

            <h2>${conversation.topic}</h2>
            <table>
                <tbody>
                <c:forEach var="mess" items="${messages}">
                    <tr>
                        <c:choose>
                            <c:when test="${mess.user.ID == conversation.user.ID}">
                                <td class="me">
                                    <span class="small red"><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${mess.date}"/>
                                        ${mess.user}</span><br> ${mess.text}
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="another">
                                    <span class="small red"><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${mess.date}"/>
                                            ${mess.user}</span><br> ${mess.text}
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <p class="bg-warning">${error}</p>

            <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
                <input type="hidden" name="command" value="add-message"/>
                <div class="form-group">
                    <label for="comment">${message["mess.label.text"]}</label>
                    <textarea class="form-control" rows="5" id="comment" name="text" required></textarea>
                </div>
                <button class="btn btn-default" type="submit">${message["mess.button.write"]}</button>
                <br/>
            </form>

        </div>
    </div>
</div>


<%@ include file="/jsp/common/footer.jspf" %>
</body>
</html>
