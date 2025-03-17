package ru.korobovs.base;

import org.testng.annotations.BeforeMethod;
import ru.korobovs.client.ApiClient;

public abstract class BaseTest {

    @BeforeMethod
    public void setUp() {
        ApiClient.getInstance().getRequestSpec();
    }
}
