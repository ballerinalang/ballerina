package ballerina.lang.errors;

struct Error {
    string msg;
    Error cause;
}

struct StackTrace {
    StackTraceItem[] items;
}

struct StackTraceItem {
    string caller;
    string packageName;
    string fileName;
    int lineNumber;
}

function toString(StackTraceItem item)(string){
    string packageName = "";
    if (item.packageName == ".") {
        packageName = item.packageName + ":";
    }
    return packageName + item.caller + "(" + item.fileName + ":" + item.lineNumber + ")";
}

struct TypeCastError {
    string msg;
    Error cause;
    string sourceType;
    string targetType;
}

struct TypeConversionError {
    string msg;
    Error cause;
    string sourceType;
    string targetType;
}

struct NullReferenceError {
    string msg;
    Error cause;
}