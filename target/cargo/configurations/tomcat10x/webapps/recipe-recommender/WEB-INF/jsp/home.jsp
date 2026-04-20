<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h3 m-0">Recipe List</h1>
    <form class="d-flex gap-2" method="get" action="${pageContext.request.contextPath}/recipes">
        <select class="form-select" name="cuisine">
            <option value="">All cuisines</option>
            <c:forEach var="cuisine" items="${cuisineTypes}">
                <option value="${cuisine}" <c:if test="${selectedCuisine == cuisine}">selected</c:if>>${cuisine}</option>
            </c:forEach>
        </select>
        <button class="btn btn-outline-primary" type="submit">Filter by Cuisine</button>
    </form>
</div>

<div class="row g-3">
    <c:forEach var="recipe" items="${recipes}">
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">${recipe.title}</h5>
                    <p class="card-text mb-1"><strong>Cuisines:</strong> ${recipe.cuisineTypes[0]} / ${recipe.cuisineTypes[1]}</p>
                    <p class="card-text"><strong>Difficulty:</strong> ${recipe.difficultyLevel}</p>
                    <a class="btn btn-sm btn-primary" href="${pageContext.request.contextPath}/recipes/detail?id=${recipe.id}">View details</a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<div class="mt-5">
    <h2 class="h4 mb-3">XSL Recipe View</h2>
    <form method="get" action="${pageContext.request.contextPath}/recipes" class="card p-3 shadow-sm mb-3">
        <div class="row g-2 align-items-end">
            <div class="col-md-6">
                <label class="form-label">Select user profile for XSL highlighting</label>
                <select class="form-select" name="userId">
                    <c:forEach var="user" items="${users}">
                        <option value="${user.id}" <c:if test="${selectedUserId == user.id}">selected</c:if>>
                            ${user.fullName} - ${user.cookingSkillLevel}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <input type="hidden" name="cuisine" value="${selectedCuisine}">
                <button class="btn btn-primary" type="submit">Apply XSL Profile</button>
            </div>
        </div>
    </form>

    <style>
        #xsl-output tr[style*="fff3cd"] { --bs-table-bg: transparent; background-color: #fff3cd !important; }
        #xsl-output tr[style*="d1e7dd"] { --bs-table-bg: transparent; background-color: #d1e7dd !important; }
    </style>
    <div id="xsl-output">${xslRenderedHtml}</div>
</div>

<%@ include file="footer.jspf" %>