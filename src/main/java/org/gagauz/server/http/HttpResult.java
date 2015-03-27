package org.gagauz.server.http;

public enum HttpResult {
    Continue_100(100, "Continue"),
    Switching_Protocols_101(101, "Switching Protocols"),
    OK_200(200, "OK"),
    Created_201(201, "Created"),
    Accepted_202(202, "Accepted"),
    No_Content_204(204, "No Content"),
    Reset_Content_205(205, "Reset Content"),
    Partial_Content_206(206, "Partial Content"),
    Multiple_Choices_300(300, "Multiple Choices"),
    Moved_Permanently_301(301, "Moved Permanently"),
    Found_302(302, "Found"),
    See_Other_303(303, "See Other"),
    Use_Proxy_305(305, "Use Proxy"),
    Unused_306(306, "(Unused)"),
    Temporary_Redirect_307(307, "Temporary Redirect"),
    Bad_Request_400(400, "Bad Request"),
    Unauthorized_401(401, "Unauthorized"),
    Payment_Required_402(402, "Payment Required"),
    Forbidden_403(403, "Forbidden"),
    Not_Found_404(404, "Not Found"),
    Method_Not_Allowed_405(405, "Method Not Allowed"),
    Not_Acceptable_406(406, "Not Acceptable"),
    Proxy_Authentication_Required_407(407, "Proxy Authentication Required"),
    Request_Timeout_408(408, "Request Timeout"),
    Conflict_409(409, "Conflict"),
    Gone_410(410, "Gone"),
    Length_Required_411(411, "Length Required"),
    Precondition_Failed_412(412, "Precondition Failed"),
    Request_Entity_Too_Large_413(413, "Request Entity Too Large"),
    Request_URI_Too_Long(414, "Request-URI Too Long"),
    Unsupported_Media_Type_415(415, "Unsupported Media Type"),
    Requested_Range_Not_Satisfiable_416(416, "Requested Range Not Satisfiable"),
    Expectation_Failed_417(417, "Expectation Failed"),
    Internal_Server_Error_500(500, "Internal Server Error"),
    Not_Implemented_501(501, "Not Implemented"),
    Bad_Gateway_502(502, "Bad Gateway"),
    Service_Unavailable_503(503, "Service Unavailable"),
    Gateway_Timeout_504(504, "Gateway Timeout"),
    HTTP_Version_Not_Supported_505(505, "HTTP Version Not Supported");

    private int code;
    private String message;

    HttpResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
