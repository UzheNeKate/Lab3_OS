package parser;

public class ResponseHeader {
    String httpStandard;
    int httpCode;
    String serverName;
    String contentType;
    int contentLength;

    public ResponseHeader(String httpStandard, int httpCode, String serverName,
                          String contentType, int contentLength) {
        this.httpStandard = httpStandard;
        this.httpCode = httpCode;
        this.serverName = serverName;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    @Override
    public String toString() {
        return String.format("""
                %s %d\r
                Server: %s\r          
                Content-Type: %s; charset=utf-8\r
                Content-Length: %d\r
                        
                """, httpStandard, httpCode, serverName, contentType, contentLength);
    }
}
