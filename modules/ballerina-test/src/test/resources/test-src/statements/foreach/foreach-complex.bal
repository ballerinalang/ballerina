string output = "";

function concatString (string value) {
    output = output + value;
}

function concatIntString (int i, string v) {
    output = output + i + ":" + v + " ";
}

function concatInt (int i) {
    output = output + i + " ";
}

function concatTwoInts (int i, int j) {
    output = output + i + ":" + j + " ";
}

struct Week {
    string[] days;
}

function testNestedForeach () (string) {
    Week w = {days:["mon", "tue", "wed", "thu", "fri"]};
    string[] people = ["tom", "bob", "sam"];
    output = "";
    foreach i, s in w.days {
        concatIntString(i, s);
        foreach _, k in people {
            concatIntString(i, k);
        }
        concatString("\n");
    }
    return output;
}

function testIntRangeSimple(int a, int b)(string){
    int x = a;
    output = "";
    foreach i in x..b {
        concatInt(i);
    }
    return output;
}

function testIntRangeEmptySet()(string){
    output = "";
    foreach i,j in 5 .. 0  {
        concatTwoInts(i, j);
    }
    return output;
}

function testIntRangeSimpleArity2(int a, int b)(string){
    int x = a;
    output = "";
    foreach i, j in x..b {
        concatTwoInts(i, j);
    }
    return output;
}

int gx = 0;

struct data {
    int sx;
}

function testIntRangeComplex()(string){
    data d = {sx : 10};
    output = "";
    foreach i in gx..d.sx {
        concatInt(i);
    }
    return output;
}

function testIntRangeExcludeStart()(string){
    output = "";
    foreach i,j in ( -10 .. 10 ] {
        concatTwoInts(i, j);
    }
    return output;
}

function testIntRangeExcludeEnd()(string){
    output = "";
    foreach i,j in [ -10 .. 10 ) {
        concatTwoInts(i, j);
    }
    return output;
}

function testIntRangeExcludeBoth()(string){
    output = "";
    foreach i,j in ( -10 .. 10 ) {
        concatTwoInts(i, j);
    }
    return output;
}

function testIntRangeIncludeBoth()(string){
    output = "";
    foreach i,j in [ -10 .. 10 ] {
        concatTwoInts(i, j);
    }
    return output;
}
