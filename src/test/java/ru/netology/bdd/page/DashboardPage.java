package ru.netology.bdd.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        $("[data-test-id=dashboard]").shouldBe(Condition.visible);
    }

    public int getCardBalance(int index) {
        String text = cards.get(index).text();
        return extractBalance(text);
    }

    public int getFirstCardBalance() {
        return getCardBalance(0);
    }

    public int getSecondCardBalance() {
        return getCardBalance(1);
    }

    public TransferPage selectCardToDeposit(int index) {
        cards.get(index).$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    public TransferPage selectFirstCardToDeposit() {
        return selectCardToDeposit(0);
    }

    public TransferPage selectSecondCardToDeposit() {
        return selectCardToDeposit(1);
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value.replaceAll("\\s+", ""));
    }
}