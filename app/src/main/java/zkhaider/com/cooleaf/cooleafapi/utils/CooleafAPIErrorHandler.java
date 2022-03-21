package zkhaider.com.cooleaf.cooleafapi.utils;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.cooleafapi.entities.APIError;

/**
 * Created by kcoleman on 2/1/15.
 */
public class CooleafAPIErrorHandler implements ErrorHandler {


        @Override public Throwable handleError(RetrofitError cause) {
            Response response = cause.getResponse();
            if (response != null) {
                APIError error;
                try {
                    Gson gson = new Gson();
                    String body = RetrofitUtility.getBodyString(response);
                    error = gson.fromJson(body, APIError.class);
                }
                catch(IOException ex) {
                    error = new APIError("Unknown Error", response.getStatus());
                }

                boolean reauthentication = false;

                if (response.getStatus() == 401) {
                    reauthentication = true;
                }

                return new CooleafAPIHTTPException(error, reauthentication, response.getStatus());
            }
            return new CooleafAPIHTTPException(new APIError("Unknown Error", 500), true, 500);
        }

}
