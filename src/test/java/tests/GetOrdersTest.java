package tests;

import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lib.ApiCoreRequests;
import lib.Constants;
import lib.DataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;

public class GetOrdersTest {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String accessToken;

    @BeforeEach
    public void createUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response createUser = apiCoreRequests.makePostRequest(
                Constants.REGISTER,
                userData
        );
        this.accessToken = createUser.path("accessToken");
    }

    @Test
    @DisplayName("Successful orders request scenario")
    public void testGetOrdersPositive() {
        String ingredients = Constants.VALID_INGREDIENTS;
        apiCoreRequests.makePostRequestWithToken(
                Constants.ORDERS,
                this.accessToken,
                ingredients
        );
        ValidatableResponse responseGetOrder = apiCoreRequests.makeGetRequest(
                Constants.ORDERS,
                accessToken
        );
        responseGetOrder.assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Successful orders request scenario")
    public void testGetOrdersNoAuth() {
        String ingredients = Constants.VALID_INGREDIENTS;
        apiCoreRequests.makePostRequestWithToken(
                Constants.ORDERS,
                this.accessToken,
                ingredients
        );
        ValidatableResponse responseGetOrder = apiCoreRequests.makeGetRequest(
                Constants.ORDERS,
                ""
        );
        responseGetOrder.assertThat().statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @AfterEach
    public void deleteUser() {
        apiCoreRequests.makeDeleteRequest(
                Constants.USER,
                this.accessToken
        );
    }
}
