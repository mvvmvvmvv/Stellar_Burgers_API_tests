package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Constants;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

public class CreateUserTest {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String accessToken;

    @Test
    @DisplayName("Create user with relevant data")
    public void createUserSuccess() {
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests.makePostRequest(
                Constants.REGISTER,
                userData
        );
        this.accessToken = responseCreateUser.path("accessToken");

        responseCreateUser.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

        apiCoreRequests.makeDeleteRequest(
                Constants.USER,
                this.accessToken
        );
    }

    @Test
    @DisplayName("Create user twice/duplicate")
    public void createUserDuplicate() {
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests.makePostRequest(
                Constants.REGISTER,
                userData
        );
        this.accessToken = responseCreateUser.path("accessToken");

        Response responseCreateDuplicateUser = apiCoreRequests.makePostRequest(
                Constants.REGISTER,
                userData
        );
        responseCreateDuplicateUser.then().assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);

        apiCoreRequests.makeDeleteRequest(
                Constants.USER,
                this.accessToken
        );
    }

    @Test
    @DisplayName("Create user without login and password")
    public void createUserIncompleteData() {
        String email = DataGenerator.getRandomEmail();

        Map<String,String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "");
        userData.put("name", "");

        Response responseCreateUser = apiCoreRequests.makePostRequest(
                Constants.REGISTER,
                userData
        );
        responseCreateUser.then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}
