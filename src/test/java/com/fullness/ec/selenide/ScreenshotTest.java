package com.fullness.ec.selenide;

import static com.codeborne.selenide.Selenide.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.TextReportExtension;
import com.codeborne.selenide.logevents.SelenideLogger;

import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;

@ExtendWith(TextReportExtension.class)
@Epic("Selenide + Allureのテスト")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScreenshotTest {
	@LocalServerPort private int port;
	private static ChromeDriver driver;
    @BeforeAll
    public static void setUp() {
    	// Configuration.browser = WebDriverRunner.
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
    	ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        Map<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", "test-result/download");
        chromePrefs.put("download.prompt_for_download", false);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
    	driver = new ChromeDriver(chromeOptions);
        driver.manage().window().setSize(new Dimension(1400,1080));
        WebDriverRunner.setWebDriver(driver);

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
        WebDriverRunner.addListener(new AbstractWebDriverEventListener() {
            @Override
            //要素をクリックする直前の処理
            public void beforeClickOn(WebElement element, WebDriver driver){
                screenshot();
                System.out.println("beforeClickOn:" + driver.getCurrentUrl());
            }
            @Override
            //要素をクリックした直後の処理
            public void afterClickOn(WebElement element, WebDriver driver){
                screenshot();
                System.out.println("afterClickOn:" + driver.getCurrentUrl());
            }
            @Attachment(type = "image/png")
            // スクリーンショットを取得
            public byte[] screenshot() {
                return Selenide.screenshot(OutputType.BYTES);
            }
        });
    }

    @Test
    @Description("Selenideを使ったE2Eテスト")
    @Story("トップ画面からメニュー画面へ遷移")
    @Step("トップ画面からメニュー画面へ遷移")
    public void トップ画面からメニュー画面へ遷移するテスト() {
    	openSite();
        inputUserNameAndPassword();
        clickSendButton();
        checkMenuPage();
    }

    @Step("トップページにアクセス")
    private void openSite() {
    	open("http://localhost:"+port);
        screenshot();
    }

    @Step("ユーザ・パスワードを入力")
    private void inputUserNameAndPassword() {
    	$(By.name("userName")).val("takahashi");
        $(By.name("password")).val("password");
        screenshot();
    }

    @Step("送信ボタンをクリック")
    private void clickSendButton() {
    	$(By.name("submit")).click();
    }

    @Step("メニュー画面を確認")
    private void checkMenuPage() {
    	$("#message").shouldHave(Condition.text("hello takahashi"));
        screenshot();
    }

    @Attachment(type = "image/png")
    // スクリーンショットを取得
    public byte[] screenshot() {
        return Selenide.screenshot(OutputType.BYTES);
    }

    @AfterAll
    public static void after() {
        driver.close();
    }
}
