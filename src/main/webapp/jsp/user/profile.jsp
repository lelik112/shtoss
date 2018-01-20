<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${message["mess.title.profile"]}</title>
    <%@ include file="/jsp/common/head.jspf" %>
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

            <h2>${mmessage["mess.info.profile"]}</h2>
            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#home">${message["mess.button.pers-data"]}</a></li>
                <li><a id="change-password" data-toggle="tab" href="#menu1">${message["mess.button.password"]}</a></li>
                <li><a id="change-email" data-toggle="tab" href="#menu2">${message["mess.button.email"]}</a></li>
            </ul>

            <div class="tab-content">
                <div id="home" class="tab-pane fade in active">

                    <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
                        <input type="hidden" name="command" value="change_name"/>
                        <div class="form-group">
                            <label for="first-name">${message["mess.label.fname"]}</label>
                            <input id="first-name" class="form-control" type="text" name="firstName" maxlength="20"
                                   value="${user.firstName}"/>
                        </div>
                        <div class="form-group">
                            <label for="last-name">${message["mess.label.lname"]}</label>
                            <input id="last-name" class="form-control" type="text" name="lastName" maxlength="20"
                                   value="${user.lastName}"/>
                        </div>
                        <div class="bg-success">${successChangedNames}</div>
                        <div class="bg-warning">${errorChangedNames}</div>
                        <button class="btn btn-default" type="submit">${message["mess.button.save"]}</button>
                    </form>

                </div>
                <div id="menu1" class="tab-pane fade">

                    <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
                        <input type="hidden" name="command" value="change-password"/>

                        <div class="form-group">
                            <label for="old_password">${message["mess.label.old-password"]}*</label>
                            <input id="old_password" class="form-control" type="password" name="oldPassword"
                                   maxlength="30" required/>
                        </div>
                        <div class="form-group">
                            <label for="password">${message["mess.label.new-password"]}*</label>
                            <input id="password" class="form-control" type="password" name="password" maxlength="30"
                                   title="${message["mess.prompt.password"]}" required
                                   pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}"/>
                        </div>
                        <div class="form-group">
                            <label for="confirm-password">${message["mess.label.repeat-password"]}*</label>
                            <input id="confirm-password" class="form-control" type="password"
                                   name="repeat-password"/>
                        </div>
                        <div class="bg-success">${successChangedPassword}</div>
                        <div class="bg-warning">${errorChangedPassword}</div>
                        <button class="btn btn-default" type="submit">${message["mess.button.change"]}</button>
                    </form>

                </div>
                <div id="menu2" class="tab-pane fade">
                    <label for="currentEmail">${message["mess.label.email"]}</label>
                    <p id="currentEmail">${user.email}</p>
                    <form method="POST" action="<c:url value="${pageContext.request.contextPath}/controller" />">
                        <input type="hidden" name="command" value="change-email"/>

                        <div class="form-group">
                            <label for="email">${message["mess.label.email-new"]}</label>
                            <c:set var="email" value="${user.email}" scope="session" />
                            <input id="email" class="form-control" type="email" name="email" value="${email}"
                                   maxlength="30" required/>
                        </div>
                        <div class="form-group">
                            <label for="pass">${message["mess.label.password"]}*</label>
                            <input id="pass" class="form-control" type="password" name="password" maxlength="30"
                                   title="${message["mess.prompt.password"]}" required />
                        </div>
                        <div class="bg-success">${successChangedEmail}</div>
                        <div class="bg-warning">${errorChangedEmail}</div>
                        <button class="btn btn-default" type="submit">${message["mess.button.change"]}</button>
                    </form>
                </div>

            </div>

        </div>
    </div>
</div>


<%@ include file="/jsp/common/footer.jspf" %>
<script src="${pageContext.request.contextPath}/js/confirm-pass.js"></script>

</body>
</html>
