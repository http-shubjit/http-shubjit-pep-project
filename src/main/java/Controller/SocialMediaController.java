package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.sql.SQLException;
import java.util.List;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginAccountHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        return app;
    }

    private void registerAccountHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account.username, account.password);
        if (registeredAccount != null) {
            ctx.json(mapper.writeValueAsString(registeredAccount));
            ctx.status(200);

        } else {
            ctx.status(400);
        }
    }

    private void loginAccountHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.login(account.username, account.password);
        if (loggedInAccount != null) {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
        } else {
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message.posted_by, message.message_text,
                message.time_posted_epoch);
        if (createdMessage != null) {
            ctx.json(mapper.writeValueAsString(createdMessage));
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException, SQLException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException, SQLException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200);
        }
    }

    private void deleteMessageHandler(Context ctx) throws SQLException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
            ctx.status(200);
        } else {
            ctx.status(200);
        }
    }

    private void updateMessageHandler(Context ctx) throws SQLException, JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message updatedMessageData = mapper.readValue(ctx.body(), Message.class);

            Message updatedMessage = messageService.updateMessage(message_id, updatedMessageData.message_text);

            if (updatedMessage != null) {
                ctx.json(updatedMessage);
            } else {
                ctx.status(400);
            }
       
    }

    private void getMessagesByUserHandler(Context ctx) throws JsonProcessingException, SQLException{
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUserId(accountId);

        if (messages != null) {
            ctx.json(messages);
        } else {
            ctx.status(400);
        }
    }
}
