# Recipe Recommendation Web Application (XML-only data layer)

Java web app built with Servlets + JSP where all persistence and querying use XML technologies.

## Team Members

**Adascalitei Ana-Maria**
- Project setup and Maven configuration
- XML data layer: XSD schema design and validation
- Recipe scraping from BBC Good Food using Jsoup
- XmlDataService implementation (XPath queries, XML persistence)
- Recipe detail page (requirement 9)
- Cuisine filter (requirement 10)

**Solomon Miruna**
- UI design using Bootstrap (navbar, cards, forms)
- Home page with recipe list and XSL view (requirements 3, 8)
- Add recipe form with validation (requirement 4)
- Add user form with validation (requirement 5)
- Recommendations page - skill level and skill + cuisine (requirements 6, 7)
- XSLT rendering with yellow/green highlighting (requirement 8)

## Tech stack
- Java 17
- Jakarta Servlets/JSP + JSTL
- XML for storage (`data/app-data.xml`)
- XSD validation (`data/schema.xsd`)
- XPath queries (filters/recommendations/details)
- XSLT rendering (`data/recipes.xsl`)
- Jsoup for initial recipe-title scraping

## Features implemented
1. Bootstrap data generation from BBC Good Food page (at least 20 recipe titles), plus random cuisine pairs and random difficulty level.
2. XML schema validation on startup and after every write.
3. Full recipe list display.
4. Add-recipe form (server-side validation + XML persistence).
5. Add-user form (server-side validation + XML persistence).
6. Recommendations by first user's skill level (XPath).
7. Recommendations by first user's skill + preferred cuisine (XPath).
8. XSLT-rendered recipe table with per-row highlight:
   - Yellow: recipe difficulty matches selected user's skill
   - Green: does not match
9. Recipe detail page via XPath by id.
10. Cuisine filter via XPath.
11. Bootstrap-based navigation and UI polish.

## Endpoints
- `/recipes` - list + cuisine filter
- `/recipes/add` - add recipe
- `/users` - add/view users
- `/recommendations` - requirement 6 and 7
- `/recipes/xsl` - requirement 8
- `/recipes/detail?id=r1` - requirement 9

## Run
1. Build WAR:
   - `mvn clean package`
2. Deploy `target/recipe-recommender.war` to a Jakarta EE 10 compatible server (for example Tomcat 10+).
3. Open `/recipe-recommender/recipes`.

On first startup, the app creates a local `data` folder next to the deployed app path and writes:
- `app-data.xml`
- `schema.xsd`
- `recipes.xsl`
