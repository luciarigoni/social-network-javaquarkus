package my.groupId.quarkussocial.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.json.bind.JsonbBuilder;
import my.groupId.quarkussocial.rest.dto.CreateUserRequest;
import my.groupId.quarkussocial.rest.dto.ResponseError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserResourceTest {

    @Test
    @DisplayName("should create an user successfully")
    public void createUserTest() {
        var createUserRequest = new CreateUserRequest();
        createUserRequest.setName("Fulano");
        createUserRequest.setAge(30);

        Response response = given()
                .contentType(ContentType.JSON).body(JsonbBuilder.create().toJson(createUserRequest))
                .when()
                .post("/users")
                .then()
                .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Should return error when json is not valid.")
    public void createUserValidationErrorTest() {
        var user = new CreateUserRequest();
        user.setAge(null);
        user.setName(null);

        var response = given().contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));
    }

}