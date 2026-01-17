package ru.netology.bdd.test;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    private DashboardPage dashboardPage;
    private static final String FIRST_CARD = "5559 0000 0000 0001";
    private static final String SECOND_CARD = "5559 0000 0000 0002";

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "true"));
        Configuration.timeout = 15000;
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin("vasya", "qwerty123");
        dashboardPage = verificationPage.validVerify("12345");
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Успешный перевод 1000 рублей с первой карты на вторую")
    void shouldSuccessfullyTransferFromFirstToSecond() {
        int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
        int secondCardInitialBalance = dashboardPage.getSecondCardBalance();
        int transferAmount = 1000;

        var transferPage = dashboardPage.selectSecondCardToDeposit();

        dashboardPage = transferPage.makeValidTransfer(String.valueOf(transferAmount), FIRST_CARD);

        int firstCardFinalBalance = dashboardPage.getFirstCardBalance();
        int secondCardFinalBalance = dashboardPage.getSecondCardBalance();

        assertEquals(firstCardInitialBalance - transferAmount, firstCardFinalBalance);
        assertEquals(secondCardInitialBalance + transferAmount, secondCardFinalBalance);
    }

    @Test
    @DisplayName("Успешный перевод 500 рублей со второй карты на первую")
    void shouldSuccessfullyTransferFromSecondToFirst() {
        int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
        int secondCardInitialBalance = dashboardPage.getSecondCardBalance();
        int transferAmount = 500;

        var transferPage = dashboardPage.selectFirstCardToDeposit();

        dashboardPage = transferPage.makeValidTransfer(String.valueOf(transferAmount), SECOND_CARD);

        int firstCardFinalBalance = dashboardPage.getFirstCardBalance();
        int secondCardFinalBalance = dashboardPage.getSecondCardBalance();

        assertEquals(firstCardInitialBalance + transferAmount, firstCardFinalBalance);
        assertEquals(secondCardInitialBalance - transferAmount, secondCardFinalBalance);
    }

    @Test
    @DisplayName("Нельзя перевести сумму, превышающую баланс")
    void shouldNotTransferWhenAmountExceedsBalance() {
        int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
        int excessiveAmount = firstCardInitialBalance + 5000;

        var transferPage = dashboardPage.selectSecondCardToDeposit();

        transferPage.makeTransferWithInsufficientFunds(String.valueOf(excessiveAmount), FIRST_CARD);

        dashboardPage = new DashboardPage();
        assertEquals(firstCardInitialBalance, dashboardPage.getFirstCardBalance());
    }

    @Test
    @DisplayName("Отмена перевода не изменяет балансы")
    void shouldCancelTransfer() {
        int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
        int secondCardInitialBalance = dashboardPage.getSecondCardBalance();

        var transferPage = dashboardPage.selectSecondCardToDeposit();
        dashboardPage = transferPage.cancelTransfer();

        assertEquals(firstCardInitialBalance, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardInitialBalance, dashboardPage.getSecondCardBalance());
    }
}
