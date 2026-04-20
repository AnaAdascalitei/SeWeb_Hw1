<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jspf" %>

<h1 class="h3 mb-3">Recipe Details</h1>

<c:if test="${empty recipe}">
    <div class="alert alert-warning">Recipe not found.</div>
</c:if>

<c:if test="${not empty recipe}">
    <div class="card shadow-sm p-3">
        <h2 class="h4">${recipe.title}</h2>
        <p><strong>ID:</strong> ${recipe.id}</p>
        <p><strong>Difficulty:</strong> ${recipe.difficultyLevel}</p>
        <p><strong>Cuisine Types:</strong> ${recipe.cuisineTypes[0]} / ${recipe.cuisineTypes[1]}</p>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/recipes">Back to list</a>
    </div>
</c:if>

<%@ include file="footer.jspf" %>
