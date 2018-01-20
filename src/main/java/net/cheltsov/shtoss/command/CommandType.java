package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.command.admin.*;

public enum CommandType {
    UNDEFINED(new UndefinedCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    REGISTER(new RegisterCommand()),
    REDIRECT(new RedirectCommand()),
    PAYMENT(new AddFundsCommand()),
    GAME(new GameCommand()),
    CHANGE_NAME(new ChangeNameCommand()),
    CHANGE_PASSWORD(new ChangePasswordCommand()),
    INBOX(new InboxCommand()),
    CREATE_TOPIC (new CreateTopicCommand()),
    SHOW_MESSAGES(new ShowMessagesCommand()),
    ADD_MESSAGE(new AddMessageCommand()),
    CHANGE_LANGUAGE(new ChangeLanguageCommand()),
    SHOW_OPERATIONS(new ShowOperationsCommand()),
    SHOW_RATING(new ShowRatingCommand()),
    ADD_NEWS(new AddNewsCommand()),
    EDIT_NEWS(new UpdateNewsCommand()),
    DELETE_NEWS(new DeleteNewsCommand()),
    ADMIN_INBOX(new AdminInboxCommand()),
    ADMIN_CREATE_TOPIC(new AdminCreateTopicCommand()),
    SHOW_USERS(new ShowUsersCommand()),
    SHOW_STATUS(new ShowStatusCommand()),
    CHANGE_ROLE(new ChangeStatusCommand()),
    CHANGE_SUIT(new ChangeSuitCommand()),
    CHANGE_DECK(new ChangeDeckCommand()),
    CHANGE_EMAIL(new ChangeEmailCommand());

    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
