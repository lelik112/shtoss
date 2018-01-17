package net.cheltsov.shtoss.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionFactory {
    private static final Logger LOGGER = LogManager.getLogger();

    public static CommandType defineCommand(String command) {
        CommandType type;
        try{
            type = CommandType.valueOf(command.replace('-', '_').toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) { // TODO: 21.12.2017 Не нравится налл поин
            LOGGER.catching(e);
            return CommandType.UNDEFINED;
        }
        return type;
    }
}
