package ru.netology.bdd.test;

import org.junit.jupiter.api.*;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;

class MoneyTransferTest {
    private DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(
                DataHelper.getAuthInfo().getLogin(),
                DataHelper.getAuthInfo().getPassword()
        );
        dashboardPage = verificationPage.validVerify(
                DataHelper.getVerificationCodeFor(DataHelper.getAuthInfo()).getCode()
        );
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        int firstCardInitialBalance = dashboardPage.getFirstCardBalance();
        int secondCardInitialBalance = dashboardPage.getSecondCardBalance();

        var transferPage = dashboardPage.selectCardToDeposit(0); // Первая карта
        dashboardPage = transferPage.makeValidTransfer(
                "5000",
                DataHelper.getSecondCardInfo().getCardNumber()
        );

        int firstCardFinalBalance = dashboardPage.getFirstCardBalance();
        int secondCardFinalBalance = dashboardPage.getSecondCardBalance();

        Assertions.assertEquals(firstCardInitialBalance + 5000, firstCardFinalBalance);
        Assertions.assertEquals(secondCardInitialBalance - 5000, secondCardFinalBalance);
    }
}