package ch.heigvd.amt.jpa.resource;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.matcher.RestAssuredMatchers;
import java.time.Instant;
import java.util.Date;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
class LogoutResourceTest {

    @Test
    void testLogoutUnauthenticated() {
        given()
            .redirects().follow(false)
            .when()
            .get("/logout")
            .then()
            .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
            .header("Location", Matchers.endsWith("login.html"));
    }

    @Test
    void testLogoutAuthenticated() {
        // First authenticate
        String cookie = given()
            .formParam("j_username", "Mike")
            .formParam("j_password", "12345")
            .post("/j_security_check")
            .then()
            .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
            .extract()
            .cookie("quarkus-credential");

        // Then logout
        given()
            .cookie("quarkus-credential", cookie)
            .redirects().follow(false)
            .when()
            .get("/logout")
            .then()
            .statusCode(HttpStatus.SC_SEE_OTHER)
            .header("Location", Matchers.endsWith("login.html"))
            .cookie("quarkus-credential", RestAssuredMatchers.detailedCookie()
                .value("")
                .path("/")
                .maxAge(0)
                .expiryDate(Date.from(Instant.EPOCH))
            );
    }

    @Test
    void testLogoutRedirectsToConfiguredLoginPage() {
        given()
            .auth().form("Mike", "12345")
            .redirects().follow(false)
            .when()
            .get("/logout")
            .then()
            .statusCode(HttpStatus.SC_SEE_OTHER)
            .header("Location", Matchers.equalTo("http://localhost:8081/login.html"));
    }

    @Test
    void testLogoutClearsCookieWithCorrectAttributes() {
        given()
            .auth().form("Mike", "12345")
            .redirects().follow(false)
            .when()
            .get("/logout")
            .then()
            .statusCode(HttpStatus.SC_SEE_OTHER)
            .cookie("quarkus-credential", RestAssuredMatchers.detailedCookie()
                .value("")
                .path("/")
                .maxAge(0)
                .expiryDate(Date.from(Instant.EPOCH))
            );
    }

    @Test
    void testMultipleLogoutRequests() {
        // First logout
        String cookie = given()
            .auth().form("Mike", "12345")
            .redirects().follow(false)
            .when()
            .get("/logout")
            .then()
            .statusCode(HttpStatus.SC_SEE_OTHER)
            .extract()
            .cookie("quarkus-credential");

        // Second logout with expired cookie should still redirect to login
        given()
            .cookie("quarkus-credential", cookie)
            .redirects().follow(false)
            .when()
            .get("/logout")
            .then()
            .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
            .header("Location", Matchers.endsWith("login.html"));
    }

    @Test
    void testLogoutWithInvalidCookie() {
        given()
            .cookie("quarkus-credential", "invalid-cookie-value")
            .redirects().follow(false)
            .when()
            .get("/logout")
            .then()
            .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
            .header("Location", Matchers.endsWith("login.html"));
    }
}
