package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatentAutomationTest {
	public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            driver.get("https://patinformed.wipo.int/");
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();

            Actions actions = new Actions(driver);
            actions.moveByOffset(0, 0).click().perform();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'margin-right')]"))).click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(),'I have read and agree to the terms')]"))).click();

            List<WebElement> results = driver.findElements(By.xpath("//table//tr/td[3]//li"));
            results.get(0).click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(@class,'result card container')]")));
            List<WebElement> Cardblock = driver.findElements(By.xpath("//li[contains(@class,'result card container')]"));

            List<String> allDates = new ArrayList<>();
            for (WebElement card : Cardblock) {
                String text = card.getText();
                Matcher matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(text);
                while (matcher.find()) {
                    allDates.add(matcher.group());
                }
            }

            if (allDates.size() >= 2) {
                LocalDate d1 = LocalDate.parse(allDates.get(0));
                LocalDate d2 = LocalDate.parse(allDates.get(1));
                long daysDiff = Math.abs(ChronoUnit.DAYS.between(d1, d2));
                System.out.println("Date 1: " + d1);
                System.out.println("Date 2: " + d2);
                System.out.println("Difference in days: " + daysDiff);
            } else {
                System.out.println("Not enough dates found!");
            }

        } catch (Exception e) {
            System.out.println("❌ Exception occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
           
            driver.quit();
        }
    }
	
	
	
	
        }

