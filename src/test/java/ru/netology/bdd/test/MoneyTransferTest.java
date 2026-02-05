package ru.netology.bdd.test;

import org.junit.jupiter.api.*;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;
import ru.netology.bdd.page.TransferPage;

import static com.codeborne.selenide.Selenide.*;

class MoneyTransferTest {
    private DashboardPage dashboardPage;
    private int firstCardStartBalance;
    private int secondCardStartBalance;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var authInfo = DataHelper.getAuthInfo();

        dashboardPage = new LoginPage()
                .validLogin(authInfo.getLogin(), authInfo.getPassword())
                .validVerify(DataHelper.getVerificationCodeFor(authInfo).getCode());

        firstCardStartBalance = dashboardPage.getFirstCardBalance();
        secondCardStartBalance = dashboardPage.getSecondCardBalance();
    }

    @AfterEach
    void restoreState() {
        int difference = dashboardPage.getFirstCardBalance() - firstCardStartBalance;
        if (difference == 0) return;

        int cardIndex = difference > 0 ? 1 : 0;
        String cardNumber = difference > 0 ?
                DataHelper.getFirstCardInfo().getCardNumber() :
                DataHelper.getSecondCardInfo().getCardNumber();

        TransferPage transferPage = dashboardPage.selectCardToDeposit(cardIndex);
        dashboardPage = transferPage.makeValidTransfer(
                String.valueOf(Math.abs(difference)),
                cardNumber
        );
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        int amount = 5000;

        dashboardPage = dashboardPage.selectCardToDeposit(0)
                .makeValidTransfer(String.valueOf(amount), DataHelper.getSecondCardInfo().getCardNumber());

        Assertions.assertEquals(firstCardStartBalance + amount, dashboardPage.getFirstCardBalance());
        Assertions.assertEquals(secondCardStartBalance - amount, dashboardPage.getSecondCardBalance());
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        int amount = 3000;

        dashboardPage = dashboardPage.selectCardToDeposit(1)
                .makeValidTransfer(String.valueOf(amount), DataHelper.getFirstCardInfo().getCardNumber());

        Assertions.assertEquals(firstCardStartBalance - amount, dashboardPage.getFirstCardBalance());
        Assertions.assertEquals(secondCardStartBalance + amount, dashboardPage.getSecondCardBalance());
    }

    @Test
    @Disabled("Bug #1: Application allows transferring amount exceeding card balance. Issue: https://github.com/cybdar/bdd/issues/1")
    void shouldNotTransferMoreThanBalance() {
        int amount = secondCardStartBalance + 10000;

        var transferPage = dashboardPage.selectCardToDeposit(0);
        dashboardPage = transferPage.makeValidTransfer(
                String.valueOf(amount),
                DataHelper.getSecondCardInfo().getCardNumber()
        );

        Assertions.assertEquals(firstCardStartBalance, dashboardPage.getFirstCardBalance());
    }
}