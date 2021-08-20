package net.scraplls.nvutibot.model;

import net.scraplls.nvutibot.Main;
import net.scraplls.nvutibot.util.ActionType;
import net.scraplls.nvutibot.util.Preferences;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NvutiBot {

    //The main browser driver class
    private WebDriver browser;

    //XPaths to create web elements from them
    private final Map<String, String> xpaths;

    private boolean isWorking;

    private Logger logger;

    private double initialBalance;

    private int bet;

    private int chance;

    private int wins;
    private int loses;
    private double overallProfit;

    private int maxProfit;

    private int maxBet;

    public NvutiBot(int initialBet, int initialChance, int initialMaxProfit, int maxBet){
        logger = Logger.getLogger(NvutiBot.class.getName());
        xpaths = new HashMap<>();

//        xpaths.put("login", "//*[@id=\"userLogin\"]");
//        xpaths.put("pass", "//*[@id=\"userPass\"]");
//        xpaths.put("enter", "//*[@id=\"enter_but\"]");
//        xpaths.put("balance", "//*[@id=\"userBalance\"]");
//        xpaths.put("BetSize", "//*[@id=\"BetSize\"]");
//        xpaths.put("BetX2", "//*[@id=\"controlBet\"]/div[1]/div/div/div[1]/span");
//        xpaths.put("Bet/2", "//*[@id=\"controlBet\"]/div[1]/div/div/div[2]/span");
//        xpaths.put("BetMax", "//*[@id=\"controlBet\"]/div[1]/div/div/div[3]/span");
//        xpaths.put("BetMin", "//*[@id=\"controlBet\"]/div[1]/div/div/div[4]/span");
//        xpaths.put("Chance", "//*[@id=\"BetPercent\"]");
//        xpaths.put("ChanceX2", "//*[@id=\"controlBet\"]/div[2]/div/div/div[1]/span");
//        xpaths.put("Chance/2", "//*[@id=\"controlBet\"]/div[2]/div/div/div[2]/span");
//        xpaths.put("ChanceMax", "//*[@id=\"controlBet\"]/div[2]/div/div/div[3]/span");
//        xpaths.put("ChanceMin", "//*[@id=\"controlBet\"]/div[2]/div/div/div[4]/span");
//        xpaths.put("overallProfit", "//*[@id=\"BetProfit\"]");
//        xpaths.put("buttonMin", "//*[@id=\"buttonMin\"]");
//        xpaths.put("buttonMax", "//*[@id=\"buttonMax\"]");

        xpaths.put("login", "//*[@id=\"userLogin\"]");
        xpaths.put("pass", "//*[@id=\"userPass\"]");
        xpaths.put("enter", "//*[@id=\"butEnter\"]");
        xpaths.put("balance", "//*[@id=\"userBalance\"]");
        xpaths.put("BetSize", "//*[@id=\"BetSize\"]");
        xpaths.put("BetX2", "//*[@id=\"dice-game\"]/div[1]/div/div[1]/div/div[2]/div[2]/span[1]");
        xpaths.put("Bet/2", "//*[@id=\"dice-game\"]/div[1]/div/div[1]/div/div[2]/div[2]/span[2]");
        xpaths.put("BetMax", "//*[@id=\"dice-game\"]/div[1]/div/div[1]/div/div[2]/div[3]/span[1]");
        xpaths.put("BetMin", "//*[@id=\"dice-game\"]/div[1]/div/div[1]/div/div[2]/div[3]/span[2]");
        xpaths.put("Chance", "//*[@id=\"BetPercent\"]");
        xpaths.put("ChanceX2", "//*[@id=\"dice-game\"]/div[1]/div/div[1]/div/div[3]/div[2]/span[1]");
        xpaths.put("Chance/2", "//*[@id=\"dice-game\"]/div[1]/div/div[1]/div/div[3]/div[2]/span[2]");
        xpaths.put("ChanceMax", "//*[@id=\"dice-game\"]/div[1]/div/div[1]/div/div[3]/div[3]/span[1]");
        xpaths.put("ChanceMin", "//*[@id=\"dice-game\"]/div[1]/div/div[1]/div/div[3]/div[3]/span[2]");
        xpaths.put("overallProfit", "//*[@id=\"BetProfit\"]");
        xpaths.put("buttonMin", "//*[@id=\"buttonMin\"]/a");
        xpaths.put("buttonMax", "//*[@id=\"buttonMax\"]/a");

        bet = initialBet;
        chance = initialChance;
        maxProfit = initialMaxProfit;
        this.maxBet = maxBet;
    }

    /**
     * Launch a browser.
     */
    public void launch() {
        log("Nvuti[Bot] job started!");
        Preferences preferences = Main.getPreferences();
        if(preferences.isCustomDriver()){
            System.setProperty("webdriver.chrome.driver", preferences.getDriverPath());
        } else {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\driver\\"+"chromedriver.exe");
        }
        browser = new ChromeDriver();
        isWorking = true;
    }

    public void login(String login, String password){
        log(String.format("Working; Login: %s, Password: %s.", login, password));
        browser.get(Main.getPreferences().getWebsite());
        sleep(5000L);
        browser.findElement(new By.ByXPath(xpaths.get("login"))).sendKeys(login);
        sleep(1000L);
        browser.findElement(new By.ByXPath(xpaths.get("pass"))).sendKeys(password);
        sleep(90*1000L);
        initialBalance = getBalance();
        log(String.format("Logged as %s", login));
    }

    public void changeBet(ActionType type){
        switch (type){
            case X2:
                bet *= 2;
                break;
            case DIV2:
                bet /= 2;
                break;
            case MIN:
                bet = 4;
                break;
            case MAX:
                bet = Integer.parseInt(findElement("balance").getAttribute("mybalance"));
                break;
        }
        WebElement element = findElement(xpaths.get("BetSize"));
        element.clear();
        element.sendKeys(String.valueOf(bet));
        System.out.println("Bet Size: " + bet);
        sleep(1000L);
    }

    public void changeChance(ActionType type){
        switch (type){
            case X2:
                chance *= 2;
                break;
            case DIV2:
                chance /= 2;
                break;
            case MIN:
                chance = 1;
                break;
            case MAX:
                chance = 95;
                break;
        }
        WebElement element = findElement(xpaths.get("Chance"));
        element.clear();
        element.sendKeys(String.valueOf(bet));
        sleep(1000L);
    }

    public void rollLess(){
        double oldBalance = getBalance();
        findElement(xpaths.get("buttonMin")).click();
        sleep(2000L);
        double newBalance = getBalance();
        double profit = newBalance - oldBalance;
        updateStats(profit);
    }

    public void rollMore(){
        double oldBalance = getBalance();
        findElement(xpaths.get("buttonMax")).click();
        sleep(2000L);
        double newBalance = getBalance();
        double profit = newBalance - oldBalance;
        updateStats(profit);
    }

    public void rollRandom(){
        int number = new Random().nextInt(1_000_000);
        if(number > 500000){
            rollMore();
        } else {
            rollLess();
        }
    }

    public void updateStats(double profit){
        if(profit < 0){
            loses++;
            System.out.println("Lose");
        } else {
            wins++;
            System.out.println("Win");
        }
        this.overallProfit += profit;
        if(overallProfit >= maxProfit){
            end();
        }
        if(bet >= maxBet){
            end();
        }
    }

    public void setBet(int bet) {
        this.bet = bet;
        WebElement element = findElement(xpaths.get("BetSize"));
        element.clear();
        element.sendKeys(String.valueOf(bet));
        sleep(1000L);
    }

    public void setChance(int chance) {
        this.chance = chance;
        WebElement element = findElement(xpaths.get("Chance"));
        element.clear();
        element.sendKeys(String.valueOf(chance));
        sleep(1000L);
    }

    public int getBet() {
        return bet;
    }

    public int getChance() {
        return chance;
    }

    public double getBalance(){
        return Double.parseDouble(findElement(xpaths.get("balance")).getAttribute("mybalance"));
    }

    public double getPossibleProfit(){
        return Double.parseDouble(findElement(xpaths.get("overallProfit")).getText());
    }

    public Map<String, String> getXpaths() {
        return xpaths;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    private void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isWorking() {
        return isWorking;
    }

    /**
     * Closes a browser.
     */
    public void end() {
        browser.quit();
        isWorking = false;
        log("Browser Closed!");
    }

    private WebElement findElement(String xpath){
        return browser.findElement(new By.ByXPath(xpath));
    }

    private void log(String message) {
        logger.log(Level.INFO, String.format("NvutiBot: %s", message));
    }

    private void warn(String warning) {
        logger.log(Level.WARNING, String.format("NvutiBot: %s", warning));
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public void setOverallProfit(double profit) {
        this.overallProfit = profit;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public double getOverallProfit() {
        return overallProfit;
    }
}
