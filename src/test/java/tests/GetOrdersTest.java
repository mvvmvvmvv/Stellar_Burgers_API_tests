package tests;

import io.qameta.allure.junit4.DisplayName;
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
        responseGetOrder.assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
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
        responseGetOrder.assertThat().body("message", equalTo("You should be authorised"))
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
