import ballerina/email;
import ballerina/io;

public function main() {
    // Create the client with the connection parameters, host, username, and password. An error is received in failure.
    // Default port number `993` is used over SSL with these configurations.
    email:ImapClient|email:Error imapClient = new ("imap.email.com", "reader@email.com", "pass456");
    // Check whether the client is created successfully. Otherwise, print the error message.
    if (imapClient is email:ImapClient) {
        // Read the first unseen email received by the IMAP4 server. Nil is returned when there are no new unseen emails.
        // In error cases an error is returned.
        email:Email|email:Error? emailResponse = imapClient->read();
        // Check whether the email is received successfully. Otherwise, print the error message.
        if(emailResponse is email:Email) {
            // Print the subject and the body of the email.
            io:println("Email Subject: ", emailResponse.subject);
            io:println("Email Body: ", emailResponse.body);
        // When no emails are available in the server, nil is returned.
        } else if (emailResponse is ()) {
            io:println("There are no emails in the INBOX.");
        // Print the error message when an error occurred while receiving the email.
        } else {
            io:println("Error while getting getting response: " + <string> emailResponse.detail()["message"]);
        }
    // Print the error message when an error occurred while creating the client.
    } else {
        io:println("Error while creating client: " + <string> imapClient.detail()["message"]);
    }
}
