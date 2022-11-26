import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test of Ya.Practicum")
class PankSuTests {
    lateinit var driver: WebDriver

    companion object {
        @JvmStatic
        fun users() = listOf(Arguments.of("frostkslo1@yandex.ru", "qwertyqwerty"))
    }

    /**
     * Открытие Chrome Driver, с необходимыми параметрами
     */
    @BeforeAll
    internal fun createDriver() {
        driver = ChromeDriver()
        driver.get("https://qa-mesto.praktikum-services.ru")
    }

    @BeforeTest
    fun returnBack() {
        driver.get("https://qa-mesto.praktikum-services.ru")
        try {
            val el = driver.findElement(By.className("header__logout"))
            el.click()
            WebDriverWait(
                driver,
                Duration.ofSeconds(3)
            ).until(ExpectedConditions.elementToBeClickable(By.className("auth-form__button")))

        } catch (e: Exception) {

        }


    }


    /**
     * Поиск по классу, элемент страницы с надписью "Вход"
     */
    @Test
    fun findEnter() {
        assertEquals(driver.findElement(By.className("auth-form__title")).text, "Вход")
    }

    /**
     * Найди все элементы с тэгом <img> по XPath
     */
    @Test
    fun findAllImg() {
        assertEquals(driver.findElements(By.ByTagName("img")).size, 3)
    }

    /**
     * Найди кнопку «Войти» и кликни по ней — сделай это через поиск по XPath, используй класс и относительный путь.
     */
    @Test
    fun findEnter2() {
        assertEquals(driver.findElement(By.ByXPath("//*[@id=\"root\"]/div/div[1]/form/div/h3")).text, "Вход")
    }

    /**
     * Войди на сайт https://qa-mesto.praktikum-services.ru/ с помощью пользователя, которого тебе удалось создать в уроке про локаторы.
     */
    @ParameterizedTest
    @MethodSource("users")
    fun logIn(email: String, password: String) {
        driver.findElement(By.id("email")).sendKeys(email)
        driver.findElement(By.id("password")).sendKeys(password)
        driver.findElement(By.className("auth-form__button")).click()
        WebDriverWait(
            driver,
            Duration.ofSeconds(3)
        ).until(ExpectedConditions.elementToBeClickable(By.className("profile__add-button")))
        assertEquals(driver.findElement(By.className("header__user")).text, email)
    }


    /**
     * Найди кнопку выхода из профиля через поиск по имени класса. Получи текст кнопки и выведи на экран.
     */
    @ParameterizedTest
    @MethodSource("users")
    fun logInAndFindLogOut(email: String, password: String) {
        driver.findElement(By.id("email")).sendKeys(email)
        driver.findElement(By.id("password")).sendKeys(password)
        driver.findElement(By.className("auth-form__button")).click()
        WebDriverWait(
            driver,
            Duration.ofSeconds(3)
        ).until(ExpectedConditions.elementToBeClickable(By.className("profile__add-button")))
        assertEquals(driver.findElement(By.className("header__logout")).text, "Выйти")
        println("Выйти")
    }

    /**
     * Напиши программу, которая сделает скролл до первой найденной карточки контента, используй поиск по CSS-селектору.
     */
    @ParameterizedTest
    @MethodSource("users")
    fun logInAndFindFirstContent(email: String, password: String) {
        driver.findElement(By.id("email")).sendKeys(email)
        driver.findElement(By.id("password")).sendKeys(password)
        driver.findElement(By.className("auth-form__button")).click()
        WebDriverWait(
            driver,
            Duration.ofSeconds(5)
        ).until(ExpectedConditions.elementToBeClickable(By.className("card__like-button")))
        (driver as JavascriptExecutor).executeScript(
            "arguments[0].scrollIntoView();",
            driver.findElement(By.ByClassName("card__image"))
        )
    }

    /**
     * Моя программа, которая ищет все уникальные имена мест.
     */
    @ParameterizedTest
    @MethodSource("users")
    fun logInAndFindAllPlaces(email: String, password: String) {
        val places = hashSetOf<String>()
        driver.findElement(By.id("email")).sendKeys(email)
        driver.findElement(By.id("password")).sendKeys(password)
        driver.findElement(By.className("auth-form__button")).click()
        WebDriverWait(
            driver,
            Duration.ofSeconds(5)
        ).until(ExpectedConditions.elementToBeClickable(By.className("card__like-button")))
        // Thread.sleep(1100)
        val allPlaces = driver.findElements(By.className("card__title"))
        for (place in allPlaces) {
            (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView();", place)
            places.add(place.text)
        }
        println("Всего мест ${places.size}")
        assertEquals(places.size, 254)

    }

    /**
     * Закрытие браузера после тестов
     */
    @AfterAll
    internal fun closePage() {
        Thread.sleep(1000)
        driver.close()
    }

}