import testorg/foo;
import ballerina/reflect;
import ballerina/http;


@foo:ConfigAnnotation {
    numVal: 10,
    textVal: "text",
    conditionVal: false,
    recordVal: { nestNumVal: 20, nextTextVal: "nestText" }
}
function someFunction(string arg) returns int {
    return 10;
}

function testNonBallerinaAnnotations() returns foo:SomeConfiguration? {
    var tDesc = typeof someFunction;
    return tDesc.@foo:ConfigAnnotation;
}

@http:ServiceConfig {
    basePath: "/myService"
}
service MyService on new http:MockListener(9090) {

    @http:ResourceConfig {
        path: "/bar"
    }
    resource function foo(http:Caller caller, http:Request req) {

    }
}

function testBallerinaServiceAnnotations() returns any {
    var a = reflect:getServiceAnnotations(MyService, "ServiceConfig");
    if (a is ()) {
        return "1";
    }
    return reflect:getServiceAnnotations(MyService, "ServiceConfig");
}

function testBallerinaResourceAnnotations() returns any {
    return reflect:getResourceAnnotations(MyService, "foo", "ResourceConfig");
}
