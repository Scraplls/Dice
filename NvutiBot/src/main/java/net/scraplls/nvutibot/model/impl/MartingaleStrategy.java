package net.scraplls.nvutibot.model.impl;

import net.scraplls.nvutibot.model.NvutiBot;
import net.scraplls.nvutibot.model.Strategy;
import net.scraplls.nvutibot.util.ActionType;
import net.scraplls.nvutibot.util.RollType;

public class MartingaleStrategy extends Strategy {

    private NvutiBot bot;

    public MartingaleStrategy(NvutiBot bot){
        this.bot = bot;
    }

    @Override
    public void play(RollType rollType) {
        switch (rollType){
            case RANDOM:
                while (bot.getBalance() >= bot.getBet()){
                    double oldBalance = bot.getBalance();
                    bot.rollRandom();
                    double newBalance = bot.getBalance();
                    double profit = newBalance - oldBalance;
                    if(profit < 0){
                        bot.changeBet(ActionType.X2);
                    } else {
                        bot.changeBet(ActionType.MIN);
                    }
                }
                break;
            case MORE:
                while (bot.getBalance() >= bot.getBet()){
                    double oldBalance = bot.getBalance();
                    bot.rollMore();
                    double newBalance = bot.getBalance();
                    double profit = newBalance - oldBalance;
                    if(profit < 0){
                        bot.changeBet(ActionType.X2);
                    } else {
                        bot.changeBet(ActionType.MIN);
                    }
                }
                break;
            case LESS:
                while (bot.getBalance() >= bot.getBet()){
                    double oldBalance = bot.getBalance();
                    bot.rollLess();
                    double newBalance = bot.getBalance();
                    double profit = newBalance - oldBalance;
                    if(profit < 0){
                        bot.changeBet(ActionType.X2);
                    } else {
                        bot.changeBet(ActionType.MIN);
                    }
                }
                break;
        }

    }
}
