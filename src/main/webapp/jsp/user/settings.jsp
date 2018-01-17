<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${message["mess.title.settings"]}</title>
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
            <h3>${message["mess.h.suit"]}</h3>
            <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
                <input type="hidden" name="command" value="change-suit"/>
                <div class="radio">
                    <label><input type="radio" name="suit" value="spades" <c:if test="${suit eq 'SPADES'}">checked</c:if>>	&spades;</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="suit" value="hearts" <c:if test="${suit eq 'HEARTS'}">checked</c:if>>	&hearts;</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="suit" value="diamonds" <c:if test="${suit eq 'DIAMONDS'}">checked</c:if>>	&diams;</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="suit" value="clubs" <c:if test="${suit eq 'CLUBS'}">checked</c:if>>	&clubs;</label>
                </div>
                <button class="btn btn-default" type="submit">${message["mess.button.change"]}</button>
            </form>
            <h3>${message["mess.h.deck"]}</h3>
            <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
                <input type="hidden" name="command" value="change-deck"/>
                <div class="radio">
                    <label><input type="radio" name="deck" value="small" <c:if test="${deck eq 'SMALL'}">checked</c:if>>36 ${message["mess.info.cards"]}</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="deck" value="big" <c:if test="${deck eq 'BIG'}">checked</c:if>>54 ${message["mess.info.cards"]}</label>
                </div>
                <button class="btn btn-default" type="submit">${message["mess.button.change"]}</button>
            </form>
            <div class="bg-success">${success}</div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf"%>
</body>
</html>
