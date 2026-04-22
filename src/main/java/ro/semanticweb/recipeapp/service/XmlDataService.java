package ro.semanticweb.recipeapp.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ro.semanticweb.recipeapp.model.DifficultyLevel;
import ro.semanticweb.recipeapp.model.Recipe;
import ro.semanticweb.recipeapp.model.UserProfile;

public class XmlDataService {
    private static final String SOURCE_URL = "https://www.bbcgoodfood.com/recipes/collection/budget-autumn";
    private static final List<String> CUISINE_TYPES = List.of(
            "Italian", "Asian", "Mexican", "French", "Indian", "Greek", "Japanese", "American");
    private static final List<String> DIFFICULTY_TYPES = List.of("Beginner", "Intermediate", "Advanced");
    private static final List<String> FALLBACK_BBC_TITLES = List.of(
            "Sausage & squash traybake",
            "Pumpkin soup",
            "Creamy chicken, bacon & squash casserole",
            "Bean & halloumi stew",
            "Harvest vegetable soup",
            "Mushroom & potato hash",
            "Roast cauliflower with caper dressing",
            "Leek, potato & spinach bake",
            "Chicken, squash & barley stew",
            "Sausage pasta bake",
            "Lentil ragu",
            "Root veg soup",
            "Spiced carrot & lentil soup",
            "Butternut squash risotto",
            "Vegetable traybake",
            "Chickpea curry",
            "Chicken casserole",
            "Tomato pasta bake",
            "Mushroom stroganoff",
            "Vegetable chilli");
    private static final String INITIAL_SCHEMA = """
            <?xml version="1.0" encoding="UTF-8"?>
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
              <xs:simpleType name="cuisineType">
                <xs:restriction base="xs:string">
                  <xs:enumeration value="Italian"/>
                  <xs:enumeration value="Asian"/>
                  <xs:enumeration value="Mexican"/>
                  <xs:enumeration value="French"/>
                  <xs:enumeration value="Indian"/>
                  <xs:enumeration value="Greek"/>
                  <xs:enumeration value="Japanese"/>
                  <xs:enumeration value="American"/>
                </xs:restriction>
              </xs:simpleType>

              <xs:simpleType name="difficultyType">
                <xs:restriction base="xs:string">
                  <xs:enumeration value="Beginner"/>
                  <xs:enumeration value="Intermediate"/>
                  <xs:enumeration value="Advanced"/>
                </xs:restriction>
              </xs:simpleType>

              <xs:element name="recipeApplicationData">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element name="users">
                      <xs:complexType>
                        <xs:sequence>
                          <xs:element name="user" maxOccurs="unbounded">
                            <xs:complexType>
                              <xs:sequence>
                                <xs:element name="name" type="xs:string"/>
                                <xs:element name="surname" type="xs:string"/>
                                <xs:element name="cookingSkillLevel" type="difficultyType"/>
                                <xs:element name="preferredCuisineType" type="cuisineType"/>
                              </xs:sequence>
                              <xs:attribute name="id" type="xs:string" use="required"/>
                            </xs:complexType>
                          </xs:element>
                        </xs:sequence>
                      </xs:complexType>
                    </xs:element>
                    <xs:element name="recipes">
                      <xs:complexType>
                        <xs:sequence>
                          <xs:element name="recipe" maxOccurs="unbounded">
                            <xs:complexType>
                              <xs:sequence>
                                <xs:element name="title" type="xs:string"/>
                                <xs:element name="cuisineTypes">
                                  <xs:complexType>
                                    <xs:sequence>
                                      <xs:element name="cuisine" type="cuisineType" minOccurs="2" maxOccurs="2"/>
                                    </xs:sequence>
                                  </xs:complexType>
                                </xs:element>
                                <xs:element name="difficultyLevel" type="difficultyType"/>
                              </xs:sequence>
                              <xs:attribute name="id" type="xs:string" use="required"/>
                            </xs:complexType>
                          </xs:element>
                        </xs:sequence>
                      </xs:complexType>
                    </xs:element>
                  </xs:sequence>
                </xs:complexType>
              </xs:element>
            </xs:schema>
            """;
    private static final String INITIAL_XSL = """
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
            """;

    private final File dataFile;
    private final File schemaFile;
    private final File xslFile;
    private final XPath xPath;
    private org.w3c.dom.Document xmlDocument;

    public XmlDataService(String basePath) {
        File dataDirectory = new File(basePath, "data");
        if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
            throw new IllegalStateException("Could not create data directory.");
        }
        this.dataFile = new File(dataDirectory, "app-data.xml");
        this.schemaFile = new File(dataDirectory, "schema.xsd");
        this.xslFile = new File(dataDirectory, "recipes.xsl");
        this.xPath = XPathFactory.newInstance().newXPath();
    }

    public synchronized void initialize() {
        try {
            ensureSupportFiles();
            if (!dataFile.exists()) {
                createInitialDataXml();
            }
            this.xmlDocument = parseValidatedDocument();
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize XML data service.", ex);
        }
    }

    public synchronized List<Recipe> getAllRecipes() {
        return evaluateRecipeList("//recipes/recipe");
    }

    public synchronized List<UserProfile> getAllUsers() {
        List<UserProfile> users = new ArrayList<>();
        try {
            XPathExpression expression = xPath.compile("//users/user");
            NodeList nodes = (NodeList) expression.evaluate(xmlDocument, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                users.add(readUser(nodes.item(i)));
            }
            return users;
        } catch (XPathExpressionException ex) {
            throw new IllegalStateException("Could not read users from XML.", ex);
        }
    }

    public synchronized Recipe getRecipeById(String recipeId) {
        if (recipeId == null || recipeId.isBlank()) {
            return null;
        }
        List<Recipe> recipes = evaluateRecipeList("//recipes/recipe[@id=" + escapeForXPath(recipeId) + "]");
        return recipes.isEmpty() ? null : recipes.get(0);
    }

    public synchronized List<Recipe> getRecipesByCuisine(String cuisineType) {
        if (cuisineType == null || cuisineType.isBlank()) {
            return getAllRecipes();
        }
        String expression = "//recipes/recipe[cuisineTypes/cuisine=" + escapeForXPath(cuisineType) + "]";
        return evaluateRecipeList(expression);
    }

    public synchronized List<Recipe> getRecipesBySkillForFirstUser() {
        UserProfile firstUser = getFirstUser();
        if (firstUser == null) {
            return List.of();
        }
        String expression = "//recipes/recipe[difficultyLevel='" + firstUser.getCookingSkillLevel().name() + "']";
        return evaluateRecipeList(expression);
    }

    public synchronized List<Recipe> getRecipesBySkillAndCuisineForFirstUser() {
        UserProfile firstUser = getFirstUser();
        if (firstUser == null) {
            return List.of();
        }
        String expression = "//recipes/recipe[difficultyLevel='" + firstUser.getCookingSkillLevel().name()
                + "' and cuisineTypes/cuisine=" + escapeForXPath(firstUser.getPreferredCuisineType()) + "]";
        return evaluateRecipeList(expression);
    }

    public synchronized UserProfile getUserById(String userId) {
        if (userId == null || userId.isBlank()) {
            return null;
        }
        try {
            String expression = "//users/user[@id=" + escapeForXPath(userId) + "]";
            Node node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
            if (node == null) {
                return null;
            }
            return readUser(node);
        } catch (XPathExpressionException ex) {
            throw new IllegalStateException("Could not load user by id.", ex);
        }
    }

    public synchronized Set<String> getAllCuisineTypes() {
        return new LinkedHashSet<>(CUISINE_TYPES);
    }

    public synchronized void addRecipe(String title, String cuisineA, String cuisineB, String difficultyLevel) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (cuisineA == null || cuisineB == null || cuisineA.equals(cuisineB)) {
            throw new IllegalArgumentException("Two distinct cuisine types are required.");
        }
        if (!isValidCuisineType(cuisineA) || !isValidCuisineType(cuisineB)) {
            throw new IllegalArgumentException("Both cuisine types must be selected from the predefined list.");
        }
        if (!DifficultyLevel.isValid(difficultyLevel)) {
            throw new IllegalArgumentException("Invalid difficulty level.");
        }

        Node recipesNode = getSingleNode("//recipes");
        org.w3c.dom.Element recipeElement = xmlDocument.createElement("recipe");
        recipeElement.setAttribute("id", generateRecipeId());
        appendSimpleElement(recipeElement, "title", title.trim());
        org.w3c.dom.Element cuisinesElement = xmlDocument.createElement("cuisineTypes");
        appendSimpleElement(cuisinesElement, "cuisine", cuisineA);
        appendSimpleElement(cuisinesElement, "cuisine", cuisineB);
        recipeElement.appendChild(cuisinesElement);
        appendSimpleElement(recipeElement, "difficultyLevel", difficultyLevel);
        recipesNode.appendChild(recipeElement);
        persist();
    }

    public synchronized void addUser(String name, String surname, String skillLevel, String preferredCuisineType) {
        if (name == null || name.isBlank() || surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("Name and surname are required.");
        }
        if (!DifficultyLevel.isValid(skillLevel)) {
            throw new IllegalArgumentException("Invalid cooking skill level.");
        }
        if (preferredCuisineType == null || preferredCuisineType.isBlank() || !isValidCuisineType(preferredCuisineType)) {
            throw new IllegalArgumentException("Preferred cuisine must be selected from the predefined list.");
        }

        Node usersNode = getSingleNode("//users");
        org.w3c.dom.Element userElement = xmlDocument.createElement("user");
        userElement.setAttribute("id", generateUserId());
        appendSimpleElement(userElement, "name", name.trim());
        appendSimpleElement(userElement, "surname", surname.trim());
        appendSimpleElement(userElement, "cookingSkillLevel", skillLevel);
        appendSimpleElement(userElement, "preferredCuisineType", preferredCuisineType);
        usersNode.appendChild(userElement);
        persist();
    }

    public synchronized String transformRecipesWithXsl(String selectedUserId) {
        try {
            UserProfile selectedUser = getUserById(selectedUserId);
            if (selectedUser == null) {
                selectedUser = getFirstUser();
            }
            if (selectedUser == null) {
                return "<div class='alert alert-warning'>No users available.</div>";
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(xslFile));
            transformer.setParameter("selectedSkillLevel", selectedUser.getCookingSkillLevel().name());
            transformer.setParameter("selectedUserName", selectedUser.getFullName());
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
            return writer.toString();
        } catch (Exception ex) {
            throw new IllegalStateException("Could not transform recipe list with XSLT.", ex);
        }
    }

    public synchronized UserProfile getFirstUser() {
        List<UserProfile> users = getAllUsers();
        return users.isEmpty() ? null : users.get(0);
    }

    private void ensureSupportFiles() throws IOException {
        Files.writeString(schemaFile.toPath(), INITIAL_SCHEMA, StandardCharsets.UTF_8);
        Files.writeString(xslFile.toPath(), INITIAL_XSL, StandardCharsets.UTF_8);
    }

    private void createInitialDataXml() throws IOException {
        List<String> titles = scrapeRecipeTitles();
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<recipeApplicationData>\n");
        builder.append("  <users>\n");
        builder.append("    <user id=\"u1\">\n");
        builder.append("      <name>Ana</name>\n");
        builder.append("      <surname>Popescu</surname>\n");
        builder.append("      <cookingSkillLevel>Intermediate</cookingSkillLevel>\n");
        builder.append("      <preferredCuisineType>Italian</preferredCuisineType>\n");
        builder.append("    </user>\n");
        builder.append("  </users>\n");
        builder.append("  <recipes>\n");

        Random random = new Random();
        for (int i = 0; i < titles.size(); i++) {
            List<String> randomCuisines = pickTwoDistinctCuisineTypes(random);
            String randomDifficulty = DIFFICULTY_TYPES.get(random.nextInt(DIFFICULTY_TYPES.size()));
            builder.append("    <recipe id=\"r").append(i + 1).append("\">\n");
            builder.append("      <title>").append(escapeXml(titles.get(i))).append("</title>\n");
            builder.append("      <cuisineTypes>\n");
            builder.append("        <cuisine>").append(randomCuisines.get(0)).append("</cuisine>\n");
            builder.append("        <cuisine>").append(randomCuisines.get(1)).append("</cuisine>\n");
            builder.append("      </cuisineTypes>\n");
            builder.append("      <difficultyLevel>").append(randomDifficulty).append("</difficultyLevel>\n");
            builder.append("    </recipe>\n");
        }

        builder.append("  </recipes>\n");
        builder.append("</recipeApplicationData>\n");
        Files.writeString(dataFile.toPath(), builder.toString(), StandardCharsets.UTF_8);
    }

    private org.w3c.dom.Document parseValidatedDocument() throws Exception {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaFile);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        builderFactory.setSchema(schema);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(dataFile);
    }

    private List<String> scrapeRecipeTitles() {
        Set<String> titles = new LinkedHashSet<>();
        try {
            Document document = Jsoup.connect(SOURCE_URL).get();
            Elements links = document.select("a[href*=/recipes/]");
            for (Element link : links) {
                String text = link.text().trim();
                if (text.length() < 5 || text.length() > 120) {
                    continue;
                }
                if (text.toLowerCase().contains("budget autumn")) {
                    continue;
                }
                if (text.toLowerCase().contains("bbc")) {
                    continue;
                }
                titles.add(text);
                if (titles.size() >= 20) {
                    break;
                }
            }
        } catch (IOException ignored) {
            // Fallback below uses titles already curated from the assignment source page.
        }

        for (String fallbackTitle : FALLBACK_BBC_TITLES) {
            if (titles.size() >= 20) {
                break;
            }
            titles.add(fallbackTitle);
        }
        if (titles.size() < 20) {
            throw new IllegalStateException("Could not build the required 20 BBC recipe titles.");
        }
        return new ArrayList<>(titles).subList(0, 20);
    }

    private List<Recipe> evaluateRecipeList(String expression) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            NodeList nodes = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                recipes.add(readRecipe(nodes.item(i)));
            }
            return recipes;
        } catch (XPathExpressionException ex) {
            throw new IllegalStateException("Could not evaluate XPath query: " + expression, ex);
        }
    }

    private Recipe readRecipe(Node recipeNode) {
        String id = recipeNode.getAttributes().getNamedItem("id").getNodeValue();
        String title = textAt("./title", recipeNode);
        String cuisineOne = textAt("./cuisineTypes/cuisine[1]", recipeNode);
        String cuisineTwo = textAt("./cuisineTypes/cuisine[2]", recipeNode);
        String difficulty = textAt("./difficultyLevel", recipeNode);
        return new Recipe(id, title, List.of(cuisineOne, cuisineTwo), DifficultyLevel.valueOf(difficulty));
    }

    private UserProfile readUser(Node userNode) {
        String id = userNode.getAttributes().getNamedItem("id").getNodeValue();
        String name = textAt("./name", userNode);
        String surname = textAt("./surname", userNode);
        String skillLevel = textAt("./cookingSkillLevel", userNode);
        String preferredCuisine = textAt("./preferredCuisineType", userNode);
        return new UserProfile(id, name, surname, DifficultyLevel.valueOf(skillLevel), preferredCuisine);
    }

    private String textAt(String expression, Node node) {
        try {
            return xPath.compile(expression).evaluate(node);
        } catch (XPathExpressionException ex) {
            throw new IllegalStateException("Could not evaluate expression: " + expression, ex);
        }
    }

    private Node getSingleNode(String expression) {
        try {
            Node node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
            if (node == null) {
                throw new IllegalStateException("Node not found: " + expression);
            }
            return node;
        } catch (XPathExpressionException ex) {
            throw new IllegalStateException("Could not evaluate expression: " + expression, ex);
        }
    }

    private void appendSimpleElement(org.w3c.dom.Element parent, String elementName, String value) {
        org.w3c.dom.Element element = xmlDocument.createElement(elementName);
        element.setTextContent(value);
        parent.appendChild(element);
    }

    private String generateRecipeId() {
        List<Recipe> recipes = getAllRecipes();
        return "r" + (recipes.size() + 1);
    }

    private String generateUserId() {
        List<UserProfile> users = getAllUsers();
        return "u" + (users.size() + 1);
    }

    private void persist() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(dataFile));
            this.xmlDocument = parseValidatedDocument();
        } catch (Exception ex) {
            throw new IllegalStateException("Could not persist XML document.", ex);
        }
    }

    private List<String> pickTwoDistinctCuisineTypes(Random random) {
        int first = random.nextInt(CUISINE_TYPES.size());
        int second = random.nextInt(CUISINE_TYPES.size());
        while (second == first) {
            second = random.nextInt(CUISINE_TYPES.size());
        }
        return List.of(CUISINE_TYPES.get(first), CUISINE_TYPES.get(second));
    }

    private boolean isValidCuisineType(String value) {
        return CUISINE_TYPES.contains(value);
    }

    private String escapeXml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private String escapeForXPath(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        String[] parts = value.split("'");
        StringBuilder expression = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                expression.append(", \"'\", ");
            }
            expression.append("'").append(parts[i]).append("'");
        }
        expression.append(")");
        return expression.toString();
    }

    public synchronized List<Recipe> getRecipesBySkillForUser(UserProfile user) {
        String expression = "//recipes/recipe[difficultyLevel='" + user.getCookingSkillLevel().name() + "']";
        return evaluateRecipeList(expression);
    }

    public synchronized List<Recipe> getRecipesBySkillAndCuisineForUser(UserProfile user) {
        String expression = "//recipes/recipe[difficultyLevel='" + user.getCookingSkillLevel().name()
                + "' and cuisineTypes/cuisine=" + escapeForXPath(user.getPreferredCuisineType()) + "]";
        return evaluateRecipeList(expression);
    }
}
