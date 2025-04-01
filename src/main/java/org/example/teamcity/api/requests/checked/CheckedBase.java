package org.example.teamcity.api.requests.checked;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.example.teamcity.api.enums.Endpoint;
import org.example.teamcity.api.generators.TestDataStorage;
import org.example.teamcity.api.models.BaseModel;
import org.example.teamcity.api.requests.CrudInterface;
import org.example.teamcity.api.requests.Request;
import org.example.teamcity.api.requests.unchecked.UncheckedBase;

import java.util.Map;

@SuppressWarnings("unchecked")
public final class CheckedBase<T extends BaseModel> extends Request implements CrudInterface {
    private final UncheckedBase uncheckedBase;
    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
        this.uncheckedBase = new UncheckedBase(spec, endpoint);
    }

    @Override
    @Step("Creating {model}")
    public T create(BaseModel model) {
        var createdModel = (T) uncheckedBase
                .create(model)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
        TestDataStorage.getTestDataStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    @Override
    @Step("Reading entity with locator: {locator}")
    public T read(String locator) {
        return (T) uncheckedBase
                .read(locator)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    // Новый метод для чтения с несколькими параметрами пути
    @Step("Reading entity")
    public T read(Map<String, Object> pathParams) {
        return (T) uncheckedBase
                .read(pathParams)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    @Step("Updating {model}")
    public T update(String locator, BaseModel model) {
        return (T) uncheckedBase
                .update(locator, model)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    @Step("Deleting {model}")
    public Object delete(String locator) {
        return uncheckedBase
                .delete(locator)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }
}
