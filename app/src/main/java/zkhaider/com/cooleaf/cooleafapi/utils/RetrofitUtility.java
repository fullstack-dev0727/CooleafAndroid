package zkhaider.com.cooleaf.cooleafapi.utils;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.client.Response;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import zkhaider.com.cooleaf.cooleafapi.entities.APIError;

public class RetrofitUtility {

    private static final int BUFFER_SIZE = 0x1000;

    public static APIError getAPIError(Response response) {
        if (response != null) {
            try {
                Gson gson = new Gson();
                String body = getBodyString(response);
                APIError error = gson.fromJson(body, APIError.class);
                if (error != null) {
                    return gson.fromJson(body, APIError.class);
                }
            } catch (IOException ex) {
                return new APIError("Unknown Error", response.getStatus());
            }
        }
        return new APIError("Unable to reach cooleaf.com", 0);
    }

    public static String getBodyString(Response response) throws IOException {
        TypedInput body = response.getBody();

        if (body!= null) {
            if (!(body instanceof TypedByteArray)) {
                // Read the entire response body to we can log it and replace the original response
                response = readBodyToBytesIfNecessary(response);
                body = response.getBody();
            }

            byte[] bodyBytes = ((TypedByteArray) body).getBytes();
            String bodyMime = body.mimeType();
            String bodyCharset = MimeUtil.parseCharset(bodyMime);
            return new String(bodyBytes, bodyCharset);
        }
        return null;

    }

    static Response readBodyToBytesIfNecessary (Response response) throws IOException {
        TypedInput body = response.getBody();
        if (body == null || body instanceof TypedByteArray) {
            return response;
        }
        String bodyMime = body.mimeType();
        byte[] bodyBytes = streamToBytes(body.in());
        body = new TypedByteArray(bodyMime, bodyBytes);

        return replaceResponseBody(response, body);
    }

    static Response replaceResponseBody(Response response, TypedInput body) {
        return new Response(response.getUrl(), response.getStatus(), response.getReason(), response.getHeaders(), body);
    }


    static byte[] streamToBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (stream != null) {
            byte[] buf = new byte[BUFFER_SIZE];
            int r;
            while ((r = stream.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
        }
        return baos.toByteArray();
    }


}