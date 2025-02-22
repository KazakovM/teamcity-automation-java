package org.example.teamcity.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
/**
 * Поля с этой аннотацией будут заполняться рандомными данными.
 */
public @interface Random {
}
