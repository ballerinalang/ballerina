import ballerina/log;
import ballerina/rabbitmq;

// Creates a ballerina RabbitMQ connection that allows re-usability if necessary.
rabbitmq:Connection connection = new({ host: "localhost", port: 5672 });

listener rabbitmq:ChannelListener channelListener= new(connection);

@rabbitmq:ServiceConfig {
    queueConfig: {
        queueName: "MyQueue"
    }
}
// Attaches the service to the listener.
service dataBindingConsumer on channelListener {

    // Gets triggered when a message is received by the queue.
    resource function onMessage(rabbitmq:Message message, string stringMessage) {
        // Retrieves the text content of the message.
        var messageContent = message.getIntContent();
        if (messageContent is string) {
            log:printInfo("The message received: " + messageContent);
        } else {
            log:printError("Error occurred while retrieving the message content.");
        }

        // Message content can be accessed using the second parameter
        // of the resource function.
        log:printInfo("The message received from data binding: " + stringMessage);

         // Positively acknowledges a single message.
        var result = message->basicAck();
        if (result is error) {
            log:printError("Error occurred while acknowledging the message.");
        }
    }

    // Gets triggered when an error is encountered.
    resource function onError(rabbitmq:Message message, error err) {
        log:printError("Error from connector: " + err.reason() + " - "
                                                  + <string>err.detail().message);
    }
}
