function testFPInvocation() returns int {
    function (int, int) returns int pow = calculatePow;
    return pow.call(10, 20);
}

function testFPInObjectField() returns int {
    Test1 t1 = new;
    return t1.pow.call(10, 20);
}

function testFPInNestedObjectField() returns int {
    Test2 t2 = new;
    return t2.t1.pow.call(10, 20);
}

//function testCallMethodInObject() returns int {
//    Test3 t3 = new;
//    return t3.call().call(10, 20);
//}

function calculatePow(int i, int j) returns int {
    return i * j;
}

type Test1 object {
    function (int, int) returns int pow = calculatePow;
};

type Test2 object {
    Test1 t1 = new;
};

//type Test3 object {
//    function call() returns function (int, int) returns int {
//        return calculatePow;
//    }
//};
