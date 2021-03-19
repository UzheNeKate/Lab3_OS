package parser;

public class ResponseHeader {
    String httpStandard;
    int httpCode;
    String serverName;
    String contentType;
    int contentLength;
    String connection;

    public ResponseHeader(String httpStandard, int httpCode, String serverName,
                          String contentType, int contentLength, String connection) {
        this.httpStandard = httpStandard;
        this.httpCode = httpCode;
        this.serverName = serverName;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.connection = connection;
    }

    @Override
    public String toString() {
        return String.format("""
            %s %d\r
            Server: %s\r
            Content-Type: %s\r
            Content-Length: %d\r
            Connection: %s\r
            """, httpStandard, httpCode, serverName, contentType, contentLength, connection);
    }
}
