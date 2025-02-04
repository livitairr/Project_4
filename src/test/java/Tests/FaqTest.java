package Tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import prakt.mainPage;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertTrue;
import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;
import org.openqa.selenium.remote.Augmenter;

// Тут тесты в хром


@RunWith(Parameterized.class)
public class FaqTest {

    public static WebDriver driver;
    public static mainPage objMainPage; // Главная страница
    public static List<WebElement> faqElements; // Список вопросов
    private final int index; // Индекс из списка вопросов
    private final String questionText; // Сам вопрос
    private final String checkedText; // Проверяемый текст
    private static boolean isDebugging; // Это процесс отладки

    // Конструктор, который инициализирует параметры: индекс, текст вопроса и ответ
    public FaqTest(int index, String questionText, String checkedText) {
        this.index = index;
        this.questionText = questionText;
        this.checkedText = checkedText;
    }

    @Parameterized.Parameters(name = "Проверка вопросов и ответов: " +
            "Индекс вопроса: {0}; " +
            "Текст вопроса: {1}; " +
            "Текст ответа: {2}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {0, "Сколько это стоит? И как оплатить?", "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {1, "Хочу сразу несколько самокатов! Так можно?", "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
                {2, "Как рассчитывается время аренды?", "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
                {3, "Можно ли заказать самокат прямо на сегодня?", "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {4, "Можно ли продлить заказ или вернуть самокат раньше?", "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
                {5, "Вы привозите зарядку вместе с самокатом?", "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
                {6, "Можно ли отменить заказ?", "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
                {7, "Я жизу за МКАДом, привезёте?", "Да, обязательно. Всем самокатов! И Москве, и Московской области."},
        };
    }

    @BeforeClass
    public static void initialSetup() {

        isDebugging = false;

        // Открываем страничку
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver = new Augmenter().augment(driver);
        driver.get("https://qa-scooter.praktikum-services.ru/");
        objMainPage = new mainPage(driver);
        objMainPage.waitForLoadFaq();

        // найдем все вопросы
        faqElements = objMainPage.getFaqItems();

        if (isDebugging)
            System.out.println("Количество вопросов: "+faqElements.size());
    }

//    @Before
//    public void startUp() {
//        WebDriverManager.firefoxdriver().setup();
//        driver = new FirefoxDriver();
//        String site = "https://qa-scooter.praktikum-services.ru/";
//        driver.get(site);
//    }

    @Test
    public void myFaqTest()  {

        WebElement faqElement = faqElements.get(index);

        boolean buttonClickable = objMainPage.isButtonClickable(faqElement);
        assertTrue("Элемент "+index+" не кликабелен", buttonClickable);

        if (!buttonClickable) return;

        faqElement.click();

        String faqQuestion;
        faqQuestion = objMainPage.getQuestion(faqElement);
        String faqAnswer;
        faqAnswer = objMainPage.getAnswer(faqElement);

        if (isDebugging) {
            System.out.println(faqQuestion);
            System.out.println(faqAnswer);
        }

        MatcherAssert.assertThat("Текст вопроса не совпадает: ", faqQuestion, containsString(questionText));
        MatcherAssert.assertThat("Текст ответа не совпадает: ", faqAnswer, containsString(checkedText));
    }

    @AfterClass
    public static void teardown() {
        if (driver!=null)
            driver.quit();
    }

}