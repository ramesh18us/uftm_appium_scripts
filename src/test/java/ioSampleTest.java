import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class ioSampleTest
{
    public static void main(String[] args) {

        try
        {

            DesiredCapabilities capabilities = new DesiredCapabilities();

            capabilities.setCapability("platformName", "Android");

            capabilities.setCapability("oauthClientId", "oauth2-qxauKY0JGUYVL1N4PhRH@microfocus.com");

            capabilities.setCapability("oauthClientSecret", "wayqpthbSWt6v7ijVt6Y");

            capabilities.setCapability("tenantId", "999999999");

            capabilities.setCapability("appPackage", "com.Advantage.aShopping");

            capabilities.setCapability("appActivity", "com.Advantage.aShopping.SplashActivity");


         //   capabilities.setCapability("source", "UFTM|HOSTED");
          //  capabilities.setCapability("appWaitDuration", "3000000");
         //   capabilities.setCapability("adbExecTimeout", "1000000");

            AndroidDriver AWebDriver = null;  //Android Driver

                // Create Web Driver
                AWebDriver = new AndroidDriver(new URL("https://uftm-biogen.saas.microfocus.com/wd/hub"), capabilities);

                //Create a session to the UFTM server

                System.out.println("UFTM session was successfully created [Android Device]");

            if (! StringUtils.containsIgnoreCase(AWebDriver.currentActivity(), "SplashActivity")) {
                Activity activity = new Activity("com.Advantage.aShopping", "com.Advantage.aShopping.SplashActivity");
                activity.setAppWaitPackage("com.Advantage.aShopping");
                activity.setAppWaitActivity("com.Advantage.aShopping.*");
                AWebDriver.startActivity(activity);

            }

            runAdvantageAppScript(AWebDriver, "LAPTOPS");

            runAdvantageAppScript(AWebDriver, "SPEAKERS");

            runAdvantageAppScript(AWebDriver, "TABLETS");

            AWebDriver.quit();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void runAdvantageAppScript(AndroidDriver<WebElement> driver, String btnName) {
        WebElement element = waitForElementToShow(driver, 15,
                MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"" + btnName + "\")"));
        element.click();
        sleep(10000);
        driver.navigate().back();
    }

    //	---------------------------------------------------------------------------------------------------------
    // Appium util functions
    public static WebElement getElementIfExist(AppiumDriver<WebElement> driver, By by) {
        try {
            return driver.findElement(by);
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    public static boolean isElementExist(AppiumDriver<WebElement> driver, By by) {
        return (getElementIfExist(driver, by) != null);
    }

    public static void clickElementIfExists(AppiumDriver<WebElement> driver, By by) {
        WebElement element = getElementIfExist(driver, by);
        if (element != null) {
            element.click();
        }
    }

    public static WebElement waitForElementToShow(AppiumDriver<WebElement> driver,
                                                  long timeOutInSeconds, By by) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutInSeconds);
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated (by));
    }

    public static boolean waitForEitherElementToShow(AndroidDriver<WebElement> driver,
                                                     long timeInSeconds, By[] bys) {
        for (int i = 0; i < timeInSeconds*2; i++) {
            for (By by: bys) {
                if (isElementExist(driver, by)) return true;
            }
            sleep(500);
        }

        return false;
    }

    public static WebElement waitForEitherElementToShowAndReturnElement(AndroidDriver<WebElement> driver,
                                                                        long timeInSeconds, By[] bys) {
        for (int i = 0; i < timeInSeconds*2; i++) {
            for (By by: bys) {
                if (isElementExist(driver, by))
                    return driver.findElement(by);
            }
            sleep(500);
        }

        return null;
    }

    public static WebElement scrollToElementByName(AndroidDriver<WebElement> driver, String elementName) {
        String str = "new UiScrollable(new UiSelector().scrollable(true).instance(0))."
                + "scrollIntoView(new UiSelector().textContains(\""+elementName+"\").instance(0))";
        return driver.findElementByAndroidUIAutomator(str);
    }

    public static void hideKeyboard(AndroidDriver<WebElement> driver) {
        try {
            driver.hideKeyboard();
        }
        catch (WebDriverException e) {}
    }

    public static WebElement getElemntByIdFromPossibleList(
            AndroidDriver<WebElement> driver, String[] possibleIDs) {
        NoSuchElementException nsee = null;

        for (String id : possibleIDs) {
            try {
                WebElement element = driver.findElement(By.id(id));
                return element;
            }
            catch (NoSuchElementException e) {
                nsee = e;
            }
        }
        throw nsee;
    }

    private static void sleep(long millis){
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}