import ballerina.lang.system;
import ballerina.lang.message;

function testworker(message msg)(message) {
  double aa;
  message result;
  worker sampleWorker (message m)  {
    double amount;
    double sumD;
    int quantity;
    double a;
    json j;

    j = `{"name":"chanaka"}`;
    message:setJsonPayload(m, j);
    sumD = 123d;
    amount = 222d;
    quantity = 12;
    a = 123d;
    sumD = sumD + ( amount * quantity );
    system:println(sumD);
    reply m;
  }
  aa = 13;
  system:println(aa);
  msg -> sampleWorker;
  system:println("After worker");
  result <- sampleWorker;
  return result;

}




