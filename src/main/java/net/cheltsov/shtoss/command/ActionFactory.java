package net.cheltsov.shtoss.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionFactory {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * @param command String representation of command type
     * @return CommandType defined in String representation
     */
    public static CommandType defineCommand(String command) {
        if (command == null) {
            return CommandType.UNDEFINED;
        }
        CommandType type;
        try{
            type = CommandType.valueOf(command.replace('-', '_').toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.catching(e);
            return CommandType.UNDEFINED;
        }
        return type;
    }
}
