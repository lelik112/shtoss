<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${message["mess.title.error"]}</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body  class="container">
<div class="col-sm-6">
    <img src="https://http.cat/500" alt="">
</div>
<%@ include file="/jsp/common/error.jspf" %>
</body>
</html>
