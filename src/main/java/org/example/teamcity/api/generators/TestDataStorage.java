package org.example.teamcity.api.generators;

import org.example.teamcity.api.enums.Endpoint;
import org.example.teamcity.api.models.BaseModel;
import org.example.teamcity.api.requests.unchecked.UncheckedBase;
import org.example.teamcity.api.spec.Specifications;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TestDataStorage {
    private static TestDataStorage testDataStorage;
    private final EnumMap<Endpoint, Set<String>> createdEntities;


    public TestDataStorage() {
        createdEntities = new EnumMap<>(Endpoint.class);
    }

    public void addCreatedEntity(Endpoint endpoint, String id) {
        if (id != null) {
            createdEntities.computeIfAbsent(endpoint, key_ -> new HashSet<>()).add(id);
        }
    }

    private String getEntityIdOrLocator(BaseModel model) {
        try {
            var idField = model.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            var idFieldValue = Objects.toString(idField.get(model), null);
            idField.setAccessible(false);
            return idFieldValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            try {
                var locatorField = model.getClass().getDeclaredField("locator");
                locatorField.setAccessible(true);
                var locatorFieldValue = Objects.toString(locatorField.get(model), null);
                locatorField.setAccessible(false);
                return locatorFieldValue;
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new RuntimeException("Cannot get Locator of entity" + ex);
            }
        }
    }

    public void addCreatedEntity(Endpoint endpoint, BaseModel model) {
        addCreatedEntity(endpoint, getEntityIdOrLocator(model));
    }

    public static TestDataStorage getTestDataStorage() {
        if (testDataStorage == null) {
            testDataStorage = new TestDataStorage();
        }
        return testDataStorage;
    }

    public void deleteCreatedEntities() {
        createdEntities.forEach(((endpoint, ids) ->
                        ids.forEach(id ->
                                new UncheckedBase(Specifications.superUserSpec(), endpoint).delete(id)
                        )));
        createdEntities.clear();
    }
}
