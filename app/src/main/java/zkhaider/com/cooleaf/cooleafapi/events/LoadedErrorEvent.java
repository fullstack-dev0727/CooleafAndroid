package zkhaider.com.cooleaf.cooleafapi.events;

import retrofit.RetrofitError;

/**
 * Created by Haider on 2/11/2015.
 */
public class LoadedErrorEvent {

    private RetrofitError mError;

    public RetrofitError getError()
    {
        return mError;
    }

    public LoadedErrorEvent(RetrofitError error) {
        mError = error;
    }

}
