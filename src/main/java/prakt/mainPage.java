package prakt;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import java.util.List;

public class mainPage {

    private final WebDriver driver;  // Объект драйвера для управления браузером
    private final By buttonOrderTop = By.xpath(".//div[starts-with(@class,'Header_Nav')]//button[text()='Заказать']"); // локатор кнопки заказа верхней части страницы
    private final By buttonOrderBottom = By.xpath(".//div[contains(@class,'FinishButton')]//button[text()='Заказать']"); // локатор кнопки заказа внизу страницы
    private final By sectionFaq = By.xpath(".//div[starts-with(@class,'Home_FAQ')]"); // локатор секции "Вопросы о важном"
    private final By accordionItem = By.className("accordion__item"); // локатор для каждого элемента секции вопросов
    private final By accordionButton = By.className("accordion__button"); // локатор для кнопки с вопросом
    private final By accordionPanel = By.className("accordion__panel"); // локатор для панели с ответом на вопрос
    private final By imageScooter = By.xpath(".//img[@alt = 'Scooter blueprint']");  // локатор для изображения скутера
    private final By buttonAcceptCookie = By.id("rcc-confirm-button");  // локатор для кнопки "Принять cookies"

    // конструктор класса, принимает драйвер браузера
    public mainPage(WebDriver driver){
        this.driver = driver;
    }

    // Метод для ожидания загрузки секции "Вопросы о важном"
    public void waitForLoadFaq() {
        WebElement faqElement = driver.findElement(sectionFaq);  // Находим элемент секции
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(sectionFaq));  // Ждем, пока секция станет видимой
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", faqElement);  // Прокручиваем страницу до секции
    }

    // Метод для ожидания загрузки главной страницы
    public void waitForLoadPage() {
        WebElement imageElement = driver.findElement(imageScooter);  // Находим элемент изображения скутера
        new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOfElementLocated(imageScooter));  // Ждем, пока изображение не станет видимым
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", imageElement);  // Прокручиваем страницу до изображения
    }


    // Метод для проверки существования элемента на странице по локатору
    public boolean isElementExist(By locatorBy) {
        try {
            driver.findElement(locatorBy);
            return true; // Если элемент найден, возвращаем true
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    // Метод для получения всех элементов "вопрос-ответ" на странице
    public List<WebElement> getFaqItems(){
        return driver.findElements(accordionItem);
    }

    // Метод для проверки, можно ли нажать на кнопку в элементе "вопрос-ответ"
    public boolean isButtonClickable(WebElement faqElement) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.elementToBeClickable(faqElement.findElement(accordionButton)));
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
        //return faqElement.findElement(accordionButton).isEnabled();
    }

    // Метод для получения текста вопроса из элемента "вопрос-ответ"
    public String getQuestion(WebElement faqElement) {
        return faqElement.findElement(accordionButton).getText();
    }

    // Метод для получения текста ответа на вопрос из элемента "вопрос-ответ"
    public String getAnswer(WebElement faqElement) {
        return faqElement.findElement(accordionPanel).getText();
    }

    // Метод для клика по кнопке заказа в зависимости от позиции кнопки (верхняя или нижняя)
    public void clickOrder(int indexButton) {
        switch (indexButton) {
            case 0: // Если кнопка верхняя
                driver.findElement(buttonOrderTop).click();
                break;
            case 1: // Если кнопка нижняя
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                WebElement buttonOrder = driver.findElement(buttonOrderBottom);
                new WebDriverWait(driver, Duration.ofSeconds(10)).until(driver -> (buttonOrder.isDisplayed()));
                buttonOrder.click();
                break;
        }
    }
    // Клик по кнопке с куками
    public void clickGetCookie() {
        if (isElementExist(buttonAcceptCookie))
            driver.findElement(buttonAcceptCookie).click();
    }

}