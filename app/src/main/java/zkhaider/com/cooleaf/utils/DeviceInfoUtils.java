package zkhaider.com.cooleaf.utils;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;

/**
 * Created by ZkHaider on 7/13/15.
 */
public class DeviceInfoUtils {

    /***
     *  Get the device's manufacturer name string.
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        return (model.startsWith(manufacturer)) ? capitalize(model) : capitalize(manufacturer) + " " + model;
    }

    /***
     *  Get the device's UUID
     */
    public static String getDeviceUUID(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter.getAddress();
    }

    /***
     *  Is this application an emulator?
     */
    public static boolean isEmulator() {
        String brand = Build.BRAND;
        return (brand.equalsIgnoreCase("generic"));
    }

    /***
     *  Internal helper to capitalize the string properly.
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        return (Character.isUpperCase(first)) ? s : Character.toUpperCase(first) + s.substring(1);
    }

}
