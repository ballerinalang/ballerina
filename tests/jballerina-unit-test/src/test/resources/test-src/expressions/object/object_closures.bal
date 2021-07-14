function foo() {
    final int i = 10;
    object {
        int x;
        function bar(int b) returns int;
    } b = object {
              int x = i;
              function bar(int b) returns int {
                  return b + self.x;
              }
          };
    var lambda = b.bar;
    int y = lambda(45);
    // assertTrue(y == 145);
}

//type AssertionError distinct error;
// const ASSERTION_ERROR_REASON = "AssertionError";
//
// function assertTrue(any|error actual) {
//     if actual is boolean && actual {
//         return;
//     }
//     string actualValAsString = actual is error ? actual.toString() : actual.toString();
//     panic error(ASSERTION_ERROR_REASON, message = "expected 'true', found '" + actualValAsString + "'");
// }
//
// function assertFalse(any|error actual) {
//     if actual is boolean && !actual {
//         return;
//     }
//     string actualValAsString = actual is error ? actual.toString() : actual.toString();
//     panic error(ASSERTION_ERROR_REASON, message = "expected 'false', found '" + actualValAsString + "'");
// }
//
// function assertValueEquality(anydata expected, anydata actual) {
//     if expected == actual {
//         return;
//     }
//     panic error(ASSERTION_ERROR_REASON,
//                 message = "expected '" + expected.toString() + "', found '" + actual.toString () + "'");
// }
