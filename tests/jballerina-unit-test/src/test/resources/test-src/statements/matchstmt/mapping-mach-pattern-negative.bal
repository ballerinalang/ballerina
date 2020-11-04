const string CONST1 = "str";

function testMappingMatchPatternNegative() returns string {

    boolean v1 = true;
    match v1 {
        { a : true } => { // pattern will not be matched
            return "Match";
        }
    }

    map<int> v2 = { a : 2 };
    match v2 {
        { a : "2" } => { // pattern will not be matched
            return "Match";
        }
        { x : 2, y : "2" } | { x : 3, y : "3"} => { // pattern will not be matched
            return "Match";
        }
        { c1 : CONST1, c2 : 2 } => { // pattern will not be matched
            return "Match";
        }
    }

    map<int|string> v3 = { a : 2, b : "2" };
    match v3 {
        { a : 2 , b : true } => { // pattern will not be matched
            return "Match";
        }
        { a : CONST1 , b : true } => { // pattern will not be matched
            return "Match";
        }
    }

    return "No match";

}

const X = 2;
const Y = 4;
function testSameMatchPatternsNegative1() {
    map<int> v = { x : 1, y : 2 };
    match v {
        { x : var a, y : 2 } | { x : var a, y : 2 } => { // unreachable pattern
        }
        { x : var a, y : 3 } => {
        }
        { x : var a, y : 3 } => { // unreachable pattern
        }
        { x : 31, y : 21 } | { x : 31, y : 21 } => { // unreachable pattern
        }
        { x : 1, y : X } | { x : 1, y : X } => { // unreachable pattern
        }
        { x : 11, y : Y } => {
        }
        { x : 11, y : Y } => { // unreachable pattern
        }
        { x : var a, y : 21 } if a is int => {
        }
        { x : var a, y : 21 } if a is int => { // unreachable pattern
        }
    }
}

function testSameMatchPatternsNegative2(any v) {
    match v {
        { x : var a, y : var b } => {}
        { x : var c, y : var d } => {} // unreachable pattern
    }
}

function testSameMatchPatternsNegative3(any v) {
    match v {
        { x : var a, ...var b } => {}
        { x : var c, ...var d } => {} // unreachable pattern
    }
}

function testSameMatchPatternsNegative4(any v) {
    match v {
        { x : var a, ...var b } => {}
        { x : var a, y : var b, ...var c } => {} // unreachable pattern
    }
}
