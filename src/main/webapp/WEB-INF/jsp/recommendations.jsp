<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jspf" %>

<h1 class="h3 mb-3">Recommendations (XPath)</h1>

<form method="get" action="${pageContext.request.contextPath}/recommendations" class="card p-3 shadow-sm mb-3">
    <label class="form-label">Select user</label>
    <div class="d-flex gap-2">
        <select class="form-select" name="userId">
            <c:forEach var="user" items="${users}">
                <option value="${user.id}" <c:if test="${selectedUserId == user.id}">selected</c:if>>
                    ${user.fullName} - ${user.cookingSkillLevel} - ${user.preferredCuisineType}
                </option>
            </c:forEach>
        </select>
        <button class="btn btn-primary" type="submit">Apply</button>
    </div>
</form>

<c:if test="${empty selectedUser}">
    <div class="alert alert-warning">No users available yet. Add one in User Profiles.</div>
</c:if>

<c:if test="${not empty selectedUser}">
    <div class="alert alert-info">
        Using user: <strong>${selectedUser.fullName}</strong>,
        skill <strong>${selectedUser.cookingSkillLevel}</strong>,
        preferred cuisine <strong>${selectedUser.preferredCuisineType}</strong>.
    </div>

    <div class="row g-3">
        <div class="col-lg-6">
            <div class="card shadow-sm p-3 h-100">
                <h2 class="h5">By Skill Level</h2>
                <ul class="list-group">
                    <c:forEach var="recipe" items="${skillRecommendations}">
                        <li class="list-group-item">${recipe.title}</li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="card shadow-sm p-3 h-100">
                <h2 class="h5">By Skill + Preferred Cuisine</h2>
                <ul class="list-group">
                    <c:forEach var="recipe" items="${skillCuisineRecommendations}">
                        <li class="list-group-item">${recipe.title}</li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</c:if>

<%@ include file="footer.jspf" %>