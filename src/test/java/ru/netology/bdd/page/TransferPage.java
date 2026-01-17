package ru.netology.bdd.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountField = $("[data-test-id=amount] input");
    private final SelenideElement fromField = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public TransferPage() {
        amountField.shouldBe(Condition.visible);
    }

    public DashboardPage makeValidTransfer(String amount, String fromCardNumber) {
        amountField.setValue(amount);
        fromField.setValue(fromCardNumber);
        transferButton.click();
        return new DashboardPage();
    }

    public void makeTransferWithInsufficientFunds(String amount, String fromCardNumber) {
        amountField.setValue(amount);
        fromField.setValue(fromCardNumber);
        transferButton.click();
        errorNotification.shouldBe(Condition.visible);
    }

    public DashboardPage cancelTransfer() {
        cancelButton.click();
        return new DashboardPage();
    }
}