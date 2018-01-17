<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${message["mess.title.news"]}</title>
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
            <h2>${message["mess.h.news"]}</h2>
            <c:if test="${user.role.roleID == 2}">
                <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
                    <input type="hidden" name="command" value="add-news"/>
                    <div class="form-group">
                        <label for="topic">${message["mess.label.topic"]}</label>
                        <input id="topic" class="form-control" type="text" name="topic" required/>
                    </div>
                    <div class="form-group">
                        <label for="comment">${message["mess.label.text"]}</label>
                        <textarea class="form-control" rows="5" id="comment" name="text" required></textarea>
                    </div>
                    <button class="btn btn-default" type="submit">${message["mess.button.add"]}</button>
                    <br/>
                </form>
                <hr/>
                <div class="bg-danger">${error}</div>
                <div class="bg-success">${success}</div>
            </c:if>
            <c:forEach var="newsOne" items="${news}" varStatus="i">

                <span class="date"><fmt:formatDate type="date" dateStyle="short" value="${newsOne.date}"/></span><br>
                <h4>${newsOne.caption}</h4>
                <p>${newsOne.text}</p>
                <c:if test="${user.role.roleID == 2}">
                    <button type="button" class="btn btn-default" data-toggle="modal"
                            data-target="#myModal${i.count}">${message["mess.button.edit"]}</button>
                </c:if>
                <p class="me">${newsOne.user}</p>

                <div class="modal fade" id="myModal${i.count}" role="dialog">
                    <div class="modal-dialog">

                        <div class="modal-content">
                            <div class="modal-body">

                                <form id="edit${i.count}" method="POST"
                                      action="<c:url value="${pageContext.request.contextPath}/controller" />">
                                    <input type="hidden" name="command" value="edit-news"/>
                                    <input type="hidden" name="news-id" value="${newsOne.newsID}"/>
                                    <div class="form-group">
                                        <label for="topic${i.count}">${message["mess.label.topic"]}</label>
                                        <input id="topic${i.count}" class="form-control" type="text" name="topic"
                                               value="${newsOne.caption}" required/>
                                    </div>
                                    <div class="form-group">
                                        <label for="comment${i.count}">${message["mess.label.text"]}</label>
                                        <textarea class="form-control" rows="5" id="comment${i.count}" name="text"
                                                  required>${newsOne.text}</textarea>
                                    </div>
                                </form>

                                <form id="delete${i.count}" method="POST"
                                      action="<c:url value="${pageContext.request.contextPath}/controller" />">
                                    <input type="hidden" name="command" value="delete-news"/>
                                    <input type="hidden" name="news-id" value="${newsOne.newsID}"/>
                                </form>

                            </div>
                            <div class="modal-footer">

                                <button form="edit${i.count}" class="btn btn-default"
                                        type="submit">${message["mess.button.edit"]}</button>
                                <button form="delete${i.count}" class="btn btn-default"
                                        type="submit">${message["mess.button.delete"]}</button>
                                <button class="btn btn-default" type="submit"
                                        data-dismiss="modal">${message["mess.button.cancel"]}</button>

                            </div>
                        </div>

                    </div>
                </div>


            </c:forEach>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>
</body>
</html>

