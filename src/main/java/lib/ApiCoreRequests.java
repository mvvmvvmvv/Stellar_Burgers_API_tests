package lib;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make GET-request with token")
    public ValidatableResponse makeGetRequest(String url, String token) {
        return RestAssured.given()
                .header("Content-type", "Application/json")
                .and()
                .header("Authorization", token)
                .get(url)
                .then().log().all();
    }

    @Step("Make DELETE-request")
    public Response makeDeleteRequest(String url, String token) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .delete(url)
                .andReturn();
    }

    @Step("Make POST-request")
    public Response makePostRequest(String url, Map<String,String> data) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(data)
                .post(url)
                .andReturn();
    }

    @Step("Make POST-request with token")
    public ValidatableResponse makePostRequestWithToken(String url, String token, String data) {
        return RestAssured.given()
                .header("Content-type", "Application/json")
                .and()
                .header("Authorization", token)
                .body(data).log().all()
                .post(url)
                .then().log().all();
    }

    @Step("Make Patch-request with auth token")
    public Response makePatchRequest(String url, String token, Map<String,String> authData) {
        return RestAssured.given()
                .header("Content-type", "Application/json")
                .and()
                .header("Authorization", token)
                .body(authData)
                .patch(url)
                .andReturn();
    }
}
