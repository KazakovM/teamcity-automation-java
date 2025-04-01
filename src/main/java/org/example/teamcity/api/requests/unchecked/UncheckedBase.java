package org.example.teamcity.api.requests.unchecked;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.teamcity.api.enums.Endpoint;
import org.example.teamcity.api.models.BaseModel;
import org.example.teamcity.api.requests.CrudInterface;
import org.example.teamcity.api.requests.Request;

import java.util.Map;

import static io.qameta.allure.Allure.step;

public class UncheckedBase extends Request implements CrudInterface {
    public UncheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    @Step("Create {model.getClass().getSimpleName()}")
    public Response create(BaseModel model) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .post(endpoint.getUrl());

    }

    @Override
    @Step("Read entity with locator: {locator}")
    public Response read(String locator) {
        return RestAssured
                .given()
                .spec(spec)
                .get(endpoint.getUrl() + "/" + locator);
    }

    // Перегруженный метод для параметров пути
    @Step("Read entity")
    public Response read(Map<String, Object> pathParams) {
        return RestAssured
                .given()
                .spec(spec)
                .urlEncodingEnabled(false)
                .pathParams(pathParams)
                .get(endpoint.getUrl());
    }

    @Override
    @Step("Update {model.getClass().getSimpleName()}")
    public Response update(String locator, BaseModel model) {
        return RestAssured
                .given()
                .body(model)
                .spec(spec)
                .put(endpoint.getUrl() + "/" + locator);
    }

    @Override
    @Step("Delete entity with locator: {locator}")
    public Response delete(String locator) {
        return RestAssured
                .given()
                .spec(spec)
                .delete(endpoint.getUrl() + "/" + locator);
    }
}
