package net.scraplls.nvutibot.model;

import net.scraplls.nvutibot.util.RollType;

public abstract class Strategy {

    private boolean isRunning;

    public abstract void play(RollType rollType) throws Exception;

    public void stop(){
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
