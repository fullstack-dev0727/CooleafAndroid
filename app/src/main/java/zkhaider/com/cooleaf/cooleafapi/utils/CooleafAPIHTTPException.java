package zkhaider.com.cooleaf.cooleafapi.utils;


import zkhaider.com.cooleaf.cooleafapi.entities.APIError;

/**
 * Created by kcoleman on 2/2/15.
 */
public class CooleafAPIHTTPException extends Exception {
    private APIError mError;
    private boolean mAuthentication;
    private int HTTPCode;

    public CooleafAPIHTTPException(APIError error, boolean authentication, int httpCode) {
        mError = error;
        mAuthentication = authentication;
        HTTPCode = httpCode;
    }

    public boolean getAuthentication() {
        return mAuthentication;
    }

    public APIError getHTTPError() {
        return mError;
    }

    public int getHTTPCode() { return HTTPCode;}

}
