<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jspf" %>

<h1 class="h3 mb-3">User Profiles</h1>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="row g-3">
    <div class="col-lg-6">
        <div class="card shadow-sm p-3">
            <h2 class="h5">Create User</h2>
            <form method="post" action="${pageContext.request.contextPath}/users">
                <div class="mb-2">
                    <label class="form-label">Name</label>
                    <input class="form-control" type="text" name="name" required>
                </div>
                <div class="mb-2">
                    <label class="form-label">Surname</label>
                    <input class="form-control" type="text" name="surname" required>
                </div>
                <div class="mb-2">
                    <label class="form-label">Cooking skill level</label>
                    <select class="form-select" name="cookingSkillLevel" required>
                        <option value="Beginner">Beginner</option>
                        <option value="Intermediate">Intermediate</option>
                        <option value="Advanced">Advanced</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Preferred cuisine type</label>
                    <select class="form-select" name="preferredCuisineType" required>
                        <c:forEach var="cuisine" items="${cuisineTypes}">
                            <option value="${cuisine}">${cuisine}</option>
                        </c:forEach>
                    </select>
                </div>
                <button class="btn btn-primary" type="submit">Save user</button>
            </form>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="card shadow-sm p-3">
            <h2 class="h5">Existing Users</h2>
            <ul class="list-group">
                <c:forEach var="user" items="${users}">
                    <li class="list-group-item">
                        <strong>${user.fullName}</strong><br/>
                        Skill: ${user.cookingSkillLevel} | Preferred cuisine: ${user.preferredCuisineType}
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>

<%@ include file="footer.jspf" %>
