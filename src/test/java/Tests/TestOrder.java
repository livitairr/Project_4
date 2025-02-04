package Tests;

import static org.junit.Assert.assertTrue;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import prakt.mainPage;
import prakt.orderPage;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

// тут тесты в файрфокс

@RunWith(Parameterized.class)
public class TestOrder {

    public static WebDriver driver;
    public static mainPage objMainPage;
    public orderPage objOrderPage;
    private final int indexButton;
    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final String dateOrder;
    private final String period;
    private final String color;
    private final String comment;

    public TestOrder(int indexButton, String name, String surname, String address, String metro,
                     String phone, String dateOrder, String period, String color, String comment) {
        this.indexButton = indexButton;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.dateOrder = dateOrder;
        this.period = period;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Оформление заказа: " +
            "Способ вызова: {0}; " +
            "Имя: {1}; " +
            "Фамилия: {2}; " +
            "Адрес: {3}; " +
            "Метро: {4}; " +
            "Телефон: {5}; " +
            "Когда нужен: {6}; " +
            "Срок аренды: {7}; " +
            "Цвет: {8}; " +
            "Комментарий: {9}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {0, "Пупа", "Бубовая", "Москва", "ЗИЛ", "79000000000", "10.01.2025", "сутки", "grey", "Тест раз"},
                {1, "Лупа", "Садовая", "Москва", "Бульвар Рокоссовского", "79000000000", "04.02.2025", "двое суток", "black", "Тест два"},
        };
    }

//    @BeforeClass
//    public static void initialOrder()
//    } убрала его, тк эта часть тестов проходит в фаерфокс

    @Before
    public void startUp() {
        WebDriverManager.firefoxdriver().setup();  // Устанавливаем драйвер для Firefox
        driver = new FirefoxDriver();
        String site = "https://qa-scooter.praktikum-services.ru/";
        driver.get(site);
    }

    @Test
    public void testOrder() {
        objMainPage = new mainPage(driver);
        objMainPage.waitForLoadPage();
        objMainPage.clickGetCookie();

        objMainPage.clickOrder(indexButton);
        objOrderPage = new orderPage(driver);
        objOrderPage.waitForLoadOrderPage();
        objOrderPage.setDataFieldsAndClickNext(name, surname, address, metro, phone);
        objOrderPage.waitForLoadRentPage();
        objOrderPage.setOtherFieldsAndClickOrder(dateOrder, period, color, comment);


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement orderPlacedElement = wait.until(ExpectedConditions.visibilityOfElementLocated(objOrderPage.orderPlaced));

        assertTrue("Заказ оформлен", orderPlacedElement.isDisplayed());
    }

    @After
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }
}
