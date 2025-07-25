package com.example;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Patent_Automation_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");

		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		try {

			driver.get("https://patinformed.wipo.int/");
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();

			Actions actions = new Actions(driver);
			actions.moveByOffset(0, 0).click().perform();

			String searchText = args[0];
			System.out.println(" Search text received: " + searchText);

			// Provider Input through arguments
			wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//div[@class='c-search flex fullWidth center-all']/input")))
					.sendKeys(searchText);
			// wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='c-search
			// flex fullWidth center-all']/input"))).sendKeys("para");

			// wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'margin-right')]"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//button[contains(text(),'I have read and agree to the terms')]"))).click();

			List<WebElement> results = driver.findElements(By.xpath("//table//tr/td[3]//li"));
			results.get(0).click();

			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//li[contains(@class,'result card container')]")));
			List<WebElement> Cardblock = driver
					.findElements(By.xpath("//li[contains(@class,'result card container')]"));

			for (WebElement card : Cardblock) {
				List<String> allDates = new ArrayList<>();

				String text = card.getText();
				Matcher matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(text);
				while (matcher.find()) {
					allDates.add(matcher.group());
				}

				if (allDates.size() >= 2) {

					LocalDate d1 = LocalDate.parse(allDates.get(0));
					LocalDate d2 = LocalDate.parse(allDates.get(1));

					Period periodcalculate = Period.between(d2, d1);
					;
					int differenceinyears = periodcalculate.getYears();
					int differenceinmonth = periodcalculate.getMonths();
					int differenceindays = periodcalculate.getDays();
					System.out.println("Differnce between in both date" + " " + differenceinyears + "years" + " "
							+ differenceinmonth + " month" + " " + differenceindays + "days");

					long diffofyears = Math.abs(ChronoUnit.YEARS.between(d1, d2));
					long diffofmonth = Math.abs(ChronoUnit.MONTHS.between(d1, d2));
					long daysDiff = Math.abs(ChronoUnit.DAYS.between(d1, d2));
					System.out.println("years found: " + allDates);
					System.out.println("year1 :" + d1);
					System.out.println("year2: " + d2);
					System.out.println("year: " + diffofyears + " " + "month" + diffofmonth + " " + "days" + daysDiff);

					break;
				} else {
					System.out.println("Not enough dates found in the list!");
				}
			}
		} catch (Exception e) {
			System.out.println(" Exception occurred: " + e.getMessage());
			e.printStackTrace();
		} finally {
			driver.quit();
		}

	}

}
