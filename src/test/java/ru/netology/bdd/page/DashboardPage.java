package ru.netology.bdd.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final ElementsCollection cards = $$(".list__item");

    public DashboardPage() {
        $("h1").shouldHave(Condition.text("Ваши карты"));
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
        cards.get(index)
                .$("div")
                .$("[data-test-id='action-deposit']")
                .click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        int start = text.indexOf("баланс: ");
        int finish = text.indexOf(" р.");
        String value = text.substring(start + "баланс: ".length(), finish);
        return Integer.parseInt(value.replaceAll("\\s+", ""));
    }
}