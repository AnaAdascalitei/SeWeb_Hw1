<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jspf" %>

<h1 class="h3 mb-3">XSLT Rendered Recipes</h1>

<form method="get" action="${pageContext.request.contextPath}/recipes/xsl" class="card p-3 shadow-sm mb-3">
    <label class="form-label">Apply user profile</label>
    <div class="d-flex gap-2">
        <select class="form-select" name="userId">
            <c:forEach var="user" items="${users}">
                <option value="${user.id}" <c:if test="${selectedUserId == user.id}">selected</c:if>>
                        ${user.fullName} - ${user.cookingSkillLevel}
                </option>
            </c:forEach>
        </select>
        <button class="btn btn-primary" type="submit">Apply profile</button>
    </div>
</form>

<style>
    #xsl-output tr[style*="fff3cd"] { --bs-table-bg: transparent; background-color: #fff3cd !important; }
    #xsl-output tr[style*="d1e7dd"] { --bs-table-bg: transparent; background-color: #d1e7dd !important; }
</style>

<div id="xsl-output">${xslRenderedHtml}</div>

<%@ include file="footer.jspf" %>