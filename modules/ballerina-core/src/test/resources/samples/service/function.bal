package samples.service;

import ballerina.connectors.twitter;
import ballerina.connectors.salesforce as sf;

service HelloService {

  @POST
  @Path ("/tweet")
  resource tweet (message m) {
      int a;
      a = 10;
  }
}

function test(int a) (int) {
    a = a+2;
    return a;
}