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
            <input class="form-control" id="myInput" type="text" placeholder="${message["mess.placeholder.search"]}">
            <br>
            <div class="bg-danger">${error}</div>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>
                        <a href="<c:url value="${pageContext.request.contextPath}/controller?command=show-users&order=userId" />">${message["mess.user.userId"]}</a>
                    </th>
                    <th><a href="<c:url value="${pageContext.request.contextPath}/controller?command=show-users&order=login" />">${message["mess.user.login"]}</a></th>
                    <th><a href="<c:url value="${pageContext.request.contextPath}/controller?command=show-users&order=email" />">${message["mess.user.email"]}</a></th>
                    <th><a href="<c:url value="${pageContext.request.contextPath}/controller?command=show-users&order=balance" />">${message["mess.user.balance"]}</a></th>
                    <th><a href="<c:url value="${pageContext.request.contextPath}/controller?command=show-users&order=role" />">${message["mess.user.role"]}</a></th>
                    <th><a href="<c:url value="${pageContext.request.contextPath}/controller?command=show-users&order=fname" />">${message["mess.user.fname"]}</a></th>
                    <th><a href="<c:url value="${pageContext.request.contextPath}/controller?command=show-users&order=lname" />">${message["mess.user.lname"]}</a></th>
                </tr>
                </thead>
                <tbody id="myTable">
                    <c:forEach var="user" items="${users}" varStatus="i">

                            <tr>
                                <td>${user.userId}</td>
                                <td>${user.login}</td>
                                <td>${user.email}</td>
                                <td>${user.balance}</td>
                                <td><a title="${message["mess.button.change"]}"
                                       href="<c:url value="${pageContext.request.contextPath}/controller?command=show-status&user-id=${user.userId}" />">${user.role}</a>
                                </td>
                                <td>${user.firstName}</td>
                                <td>${user.lastName}</td>
                            </tr>

                    </c:forEach>
                </tbody>
            </table>



        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/filter.js"></script>
<%@ include file="/jsp/common/footer.jspf"%>
</body>
</html>



