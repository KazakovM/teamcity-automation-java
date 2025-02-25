package org.example.teamcity.common.provider;

import org.testng.annotations.DataProvider;

import java.util.Arrays;
import java.util.List;

/**
 * Требования к id:
 * начинается с латинской буквы,
 * содержит только латиницу, цифры и знак подчеркивания,
 * не длиннее 255 сивмволов
 */
public class InvalidIdDataProvider {

    @DataProvider(name = "invalidIds")
    public static Object[][] provideInvalidIds() {
        List<String> invalidIds = Arrays.asList(
                // Начинается с цифры
                "123abc",

                // Начинается с символа подчёркивания
                "_invalid",
                "_test7",

                // Содержит недопустимые символы
                "test@123", // Символ @
                "user#name", // Символ #
                "test-123", // Символ -
                "test!", // Символ !
                "test$", // Символ $
                "test%", // Символ %
                "test^", // Символ ^
                "test&", // Символ &
                "test*", // Символ *
                "test(", // Символ (
                "test)", // Символ )
                "test+", // Символ +
                "test=", // Символ =
                "test{", // Символ {
                "test}", // Символ }
                "test[", // Символ [
                "test]", // Символ ]
                "test|", // Символ |
                "test\\", // Символ \
                "test/", // Символ /
                "test:", // Символ :
                "test;", // Символ ;
                "test'", // Символ '
                "test\"", // Символ "
                "test<", // Символ <
                "test>", // Символ >
                "test,", // Символ ,
                "test.", // Символ .
                "test?", // Символ ?

                // Содержит пробелы
                "test 123",
                " user_name",
                "test\t123", // Табуляция
                "test\n123", // Новая строка

                // Содержит кириллицу
                "тест123",
                "userИмя",


                // Слишком длинная строка (более 225 символов)
                "a".repeat(226), // 226 символов
                "a".repeat(300), // 300 символов

                // Содержит только цифры
                "123456",

                // Содержит только символы подчёркивания
                "_____"
        );

        // Преобразуем список в двумерный массив Object[][]
        Object[][] data = new Object[invalidIds.size()][1];
        for (int i = 0; i < invalidIds.size(); i++) {
            data[i][0] = invalidIds.get(i);
        }
        return data;
    }
}
