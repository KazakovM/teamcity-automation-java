package org.example.teamcity.api.requests.unchecked;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.teamcity.api.enums.Endpoint;
import org.example.teamcity.api.models.BaseModel;
import org.example.teamcity.api.requests.CrudInterface;
import org.example.teamcity.api.requests.Request;

import java.util.Map;

public class UncheckedBase extends Request implements CrudInterface {
    public UncheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Response create(BaseModel model) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .post(endpoint.getUrl());
    }

    @Override
    public Response read(String id) {
        return RestAssured
                .given()
                .spec(spec)
                .get(endpoint.getUrl() + "/id:" + id);
    }

    // Перегруженный метод для параметров пути
    public Response read(Map<String, Object> pathParams) {
        return RestAssured
                .given()
                .spec(spec)
                .urlEncodingEnabled(false)
                .pathParams(pathParams)
                .get(endpoint.getUrl());
    }

    @Override
    public Response update(String id, BaseModel model) {
        return RestAssured
                .given()
                .body(model)
                .spec(spec)
                .put(endpoint.getUrl() + "/id:" + id);
    }

    @Override
    public Response delete(String id) {
        return RestAssured
                .given()
                .spec(spec)
                .delete(endpoint.getUrl() + "/id:" + id);
    }
}
