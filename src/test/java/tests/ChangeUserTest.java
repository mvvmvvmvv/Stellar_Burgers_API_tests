package tests;

import io.restassured.response.Response;
import lib.*;

import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ChangeUserTest {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String accessToken;
    String email;

    @BeforeEach
    public void createUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response createUser = apiCoreRequests.makePostRequest(
                Constants.REGISTER,
                userData
        );
        this.accessToken = createUser.path("accessToken");
        this.email = userData.get("email");
    }

    @Test
    @DisplayName("Edit all fields of user data with authentication")
    public void testEditUserPositive() {
        Map<String, String> editData = new HashMap<>();
        editData.put("email", DataGenerator.getRandomEmail());
        editData.put("password", "changedpassword");
        editData.put("name", "Changed Name");

        Response responseEditUser = apiCoreRequests.makePatchRequest(
                Constants.USER,
                this.accessToken,
                editData
        );
        responseEditUser.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Edit all fields of user data without auth")
    public void testEditUserNegative() {
        Map<String, String> editData = new HashMap<>();
        editData.put("email", DataGenerator.getRandomEmail());
        editData.put("password", "changedpassword");
        editData.put("name", "Changed Name");

        Response responseEditUser = apiCoreRequests.makePatchRequest(
                Constants.USER,
                "",
                editData
        );
        responseEditUser.then().assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @AfterEach
    public void deleteUser() {
        apiCoreRequests.makeDeleteRequest(
                Constants.USER,
                this.accessToken
        );
    }
}
