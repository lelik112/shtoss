<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${message["mess.title.edit-user"]}</title>
    <%@ include file="/jsp/common/head.jspf"%>
    <script>
        window.onload = function () {
            document.getElementById('${activeWindow}').click();
        }
    </script>
</head>
<body>
<%@ include file="/jsp/common/navbar.jspf" %>

<div class="container-fluid text-center">
    <div class="row content">
        <div class="col-sm-3 sidenav">
            <%@ include file="/jsp/common/news.jspf" %>
        </div>
        <div class="col-sm-9 text-left">
            <div class="container">
                <a class="button"
                   href="<c:url value="${pageContext.request.contextPath}/controller?command=show-users" />">«${message["mess.button.users"]}</a>
                <h3>${message["mess.h.user"]}${changingUser}, ${message["mess.h.curent-status"]}${changingUser.role}</h3>
                <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
                    <input type="hidden" name="command" value="change-role"/>
                    <input type="hidden" name="user-id" value="${changingUser.ID}"/>
                    <div class="radio">
                        <label><input type="radio" name="role" value="guest" <c:if test="${changingUser.role eq 'GUEST'}">checked</c:if>>GUEST</label>
                    </div>
                    <div class="radio">
                        <label><input type="radio" name="role" value="user" <c:if test="${changingUser.role eq 'USER'}">checked</c:if>>USER</label>
                    </div>
                    <div class="radio">
                        <label><input type="radio" name="role" value="admin" <c:if test="${changingUser.role eq 'ADMIN'}">checked</c:if>>ADMIN</label>
                    </div>
                    <button class="btn btn-default" type="submit">${message["mess.button.change"]}</button>
                </form>
                <div class="bg-danger">${error}</div>
                <div class="bg-success">${success}</div>
            </div>
        </div>
    </div>
</div>


<%@ include file="/jsp/common/footer.jspf" %>
<script src="${pageContext.request.contextPath}/js/confirm-pass.js"></script>

</body>
</html>
