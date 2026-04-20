<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="selectedSkillLevel" />
    <xsl:param name="selectedUserName" />
    <xsl:template match="/">
        <div class="card shadow-sm">
            <div class="card-body">
                <h2 class="h4 mb-2">XSL Recipe Rendering</h2>
                <p class="text-muted mb-3">
                    Active profile: <xsl:value-of select="$selectedUserName" />
                    (<xsl:value-of select="$selectedSkillLevel" />)
                </p>
                <table class="table table-bordered ">
                    <thead class="table-dark">
                        <tr>
                            <th>Title</th>
                            <th>Cuisines</th>
                            <th>Difficulty</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="recipeApplicationData/recipes/recipe">
                            <tr>
                                <xsl:attribute name="style">
                                    <xsl:choose>
                                        <xsl:when test="difficultyLevel = $selectedSkillLevel">background-color: #fff3cd !important;</xsl:when>
                                        <xsl:otherwise>background-color: #d1e7dd !important;</xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>
                                <td><xsl:value-of select="title"/></td>
                                <td>
                                    <xsl:value-of select="cuisineTypes/cuisine[1]"/> /
                                    <xsl:value-of select="cuisineTypes/cuisine[2]"/>
                                </td>
                                <td><xsl:value-of select="difficultyLevel"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </div>
        </div>
    </xsl:template>
</xsl:stylesheet>
