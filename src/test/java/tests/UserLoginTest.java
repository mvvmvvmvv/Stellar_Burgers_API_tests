package tests;

import io.restassured.response.Response;
import lib.*;

import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

public class UserLoginTest {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String email;
    String password;
    String accessToken;

    @BeforeEach
    public void createUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response createUser = apiCoreRequests.makePostRequest(
                Constants.REGISTER,
                userData
        );
        this.accessToken = createUser.path("accessToken");
        this.email = userData.get("email");
        this.password = userData.get("password");
    }

    @Test
    @DisplayName("Successful log in")
    public void testLoginUserPositive() {
        Map<String,String> loginData = new HashMap<>();
        loginData.put("email", this.email);
        loginData.put("password", this.password);

        Response responseCheckLogin = apiCoreRequests
                .makePostRequest(
                        Constants.LOGIN,
                        loginData
                );
        responseCheckLogin.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Failed authorization without password")
    public void testLoginUserNegative() {
        Map<String,String> loginData = new HashMap<>();
        loginData.put("email", this.email);
        loginData.put("password", "");

        Response responseCheckLogin = apiCoreRequests
                .makePostRequest(
                        Constants.LOGIN,
                        loginData
                );
        responseCheckLogin.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false));
    }

    @AfterEach
    public void deleteUser() {
        apiCoreRequests.makeDeleteRequest(
                Constants.USER,
                this.accessToken
        );
    }
}
