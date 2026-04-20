<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jspf" %>

<h1 class="h3 mb-3">Add Recipe</h1>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/recipes/add" class="card shadow-sm p-3">
    <div class="mb-3">
        <label class="form-label">Title</label>
        <input class="form-control" type="text" name="title" required>
    </div>
    <div class="row">
        <div class="col-md-6 mb-3">
            <label class="form-label">Cuisine type 1</label>
            <select class="form-select" name="cuisineA" required>
                <c:forEach var="cuisine" items="${cuisineTypes}">
                    <option value="${cuisine}">${cuisine}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-6 mb-3">
            <label class="form-label">Cuisine type 2</label>
            <select class="form-select" name="cuisineB" required>
                <c:forEach var="cuisine" items="${cuisineTypes}">
                    <option value="${cuisine}">${cuisine}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="mb-3">
        <label class="form-label">Difficulty level</label>
        <select class="form-select" name="difficultyLevel" required>
            <option value="Beginner">Beginner</option>
            <option value="Intermediate">Intermediate</option>
            <option value="Advanced">Advanced</option>
        </select>
    </div>
    <button class="btn btn-success" type="submit">Save recipe</button>
</form>

<%@ include file="footer.jspf" %>
