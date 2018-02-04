<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${message["mess.title.game"]}</title>
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
            <div class="bg-danger">${error}</div>
            <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
                <input type="hidden" name="command" value="game">
                <div class="form-group">
                    <label for="bid">${message["mess.label.bid"]}</label>
                    <input id="bid" class="form-control" type="text" name="bid" required
                           pattern="^[\d]+[\.,]?[\d]{0,2}$"/>
                </div>
                <div class="form-group">
                    <label for="card">${message["mess.label.card"]}</label>
                    <select class="form-control" name="card" id="card">
                        <c:forEach var="card" items="${deck.getUserDeck(suit)}">
                            <option class="card-class ${suit.code}" value="${card}">&#${card};</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="bg-warning">${wrongAction}</div>
                <c:if test="${wrongAction != null}">
                    <a class="btn btn-link"
                       href="<c:url value="${pageContext.request.contextPath}/controller?command=redirect&nextPage=jsp.payment" />">
                            ${message["mess.button.add-funds"]}</a>
                    <br>
                </c:if>
                <div>
                    <button class="btn btn-default" type="submit">${message["mess.button.game"]}</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>
</body>
</html>
