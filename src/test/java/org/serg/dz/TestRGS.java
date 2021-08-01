package org.serg.dz;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;


public class TestRGS {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
//      Для Windows
        System.setProperty("webdriver.chrome.driver", "src/test/resources/webdriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10, 1000);

        String baseUrl = "http://www.rgs.ru";
        driver.get(baseUrl);
    }



    @Test
    public void exampleScenario() {

        //закрыть frame
       driver.switchTo().frame("fl-498072");
       WebElement frameClose=driver.findElement(By.xpath("//div[@data-fl-close='1800']"));
       waitUtilElementToBeClickable(frameClose);
       frameClose.click();
       driver.switchTo().defaultContent();

        String cookiesClose = "//div[@class='btn btn-default text-uppercase']";
        WebElement cookiesBtnClose = driver.findElement(By.xpath(cookiesClose));
        cookiesBtnClose.click();


        // выбрать меню
        String menuSelectionXPath = "//a[@data-toggle='dropdown'][contains(text(),'Меню')]";
        WebElement menuSelection = driver.findElement(By.xpath(menuSelectionXPath));
        menuSelection.click();


        // выбрать здоровье
        String chooseHealthXPath = "//a[@class='hidden-xs'][contains(text(),'Здоровье')]";
        WebElement chooseHealth = driver.findElement(By.xpath(chooseHealthXPath));
        chooseHealth.click();


        // выбрать добровольное мед страхование
        String voluntaryMedicalInsuranceXPath = "//a[contains(text(),'Добровольное')]";
        WebElement voluntaryMedicalInsurance = driver.findElement(By.xpath(voluntaryMedicalInsuranceXPath));
        voluntaryMedicalInsurance.click();

        // проверить наличие заголовка – Добровольное медицинское страхование
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "ДМС 2021 | Рассчитать стоимость добровольного медицинского страхования и оформить ДМС в Росгосстрах"
                , driver.getTitle());


        // нажать кнопку - отправить заявку
        String buttonSendRequestXPath = "//a[@class='btn btn-default text-uppercase hidden-xs " +
                "adv-analytics-navigation-desktop-floating-menu-button']";

        WebElement buttonSendRequest = driver.findElement(By.xpath(buttonSendRequestXPath));
        buttonSendRequest.click();

        // проверка открытия страницы "Заявка на добровольное медицинское страхование"
        String pageTitleXPath = "//div[@class='modal-header']//h4";
        waitUtilElementToBeVisible(By.xpath(pageTitleXPath));
        WebElement pageTitle = driver.findElement(By.xpath(pageTitleXPath));
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Заявка на добровольное медицинское страхование", pageTitle.getText());


        // заполнить поля данными
        fillInputField(driver.findElement(By.xpath("//input[@name='LastName']")), "Воробьев");
        fillInputField(driver.findElement(By.xpath("//input[@name='FirstName']")), "Антон");
        fillInputField(driver.findElement(By.xpath("//input[@name='MiddleName']")), "Васильевич");
        fillInputField(driver.findElement(By.xpath("//input[@name='Email']")), "qwertyqwerty");
        fillInputField(driver.findElement(By.xpath("//textarea")), "Какой то коммент");
        // нажать чекбокс - Я согласен на обработку
        String checkBoxXPath = "//input[@class='checkbox']";
        WebElement checkBox = driver.findElement(By.xpath(checkBoxXPath));
        checkBox.click();
        //Ввод номера телефона
        WebElement fillInputFieldPhone = driver.findElement(By.xpath("//div/div[5]/input"));
        fillInputFieldPhone.click();
        fillInputFieldPhone.sendKeys("9999999999", Keys.ENTER);
        //Ввод даты
        WebElement fillInputFieldDate = driver.findElement(By.xpath("//input[@name='ContactDate']"));
        fillInputFieldDate.click();
        fillInputFieldDate.sendKeys("19.09.2021", Keys.ENTER);
        // выбрать регион
        WebElement insuranceButtonRegion = driver.findElement(By.xpath("//select/option[2]"));
        insuranceButtonRegion.click();
        // нажать отправить
        String pressSendXPath = "//button[@id='button-m']";
        WebElement pressSend = driver.findElement(By.xpath(pressSendXPath));
        pressSend.click();


        //проверка заполнены ли поля
        //Фамилия
        try {
            WebElement lastName = driver.findElement(By.xpath("//span[text()='Введите Фамилию']"));
            waitUtilElementToBeVisible(lastName);
            Assert.assertEquals("Поле пустое",
                    "Введите Фамилию", lastName.getText());
        } catch (Exception e) {
        }

        //Имя

        try {
            WebElement firstName = driver.findElement(By.xpath("//span[text()='Введите Имя']"));
            waitUtilElementToBeVisible(firstName);
            Assert.assertEquals("Поле пустое",
                    "Введите Имя", firstName.getText());
        } catch (Exception e) {
        }


        //Проверка введен ли email
        String errorAlertXPath = "//span[text()='Введите адрес электронной почты']";
        WebElement errorAlert = driver.findElement(By.xpath(errorAlertXPath));
        waitUtilElementToBeVisible(errorAlert);
        Assert.assertEquals("Проверка ошибки на странице не была пройдено",
                "Введите адрес электронной почты", errorAlert.getText());


    }


    @After
    public void after() {
        driver.quit();
    }

    /**
     * Явное ожидание того что элемент станет кликабельный
     *
     * @param element - веб элемент до которого нужно проскролить
     */
    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Явное ожидание того что элемент станет видимым
     *
     * @param locator - локатор до веб элемент который мы ожидаем найти и который виден на странице
     */
    private void waitUtilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Явное ожидание того что элемент станет видимым
     *
     * @param element - веб элемент который мы ожидаем что будет  виден на странице
     */
    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Заполнение полей определённым значений
     *
     * @param element - веб элемент (поле какое-то) которое планируем заполнить)
     * @param value   - значение которы мы заполняем веб элемент (поле какое-то)
     */
    private void fillInputField(WebElement element, String value) {
        waitUtilElementToBeClickable(element);
        element.click();
        element.clear();
        element.sendKeys(value);
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(element, "value", value));
        Assert.assertTrue("Поле было заполнено некорректно", checkFlag);
    }


//    public void closeDinamicPopUp(){
//        driver.manage().timeouts().implicitlyWait(500,TimeUnit.MILLISECONDS);
//        try{
//            WebElement element=driver.findElement(By.xpath("//div[@data-fl-close='1800']"));
//            element.click();
//        }catch (Exception e){
//
//        }finally {
//            driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
//        }
//    }


}
