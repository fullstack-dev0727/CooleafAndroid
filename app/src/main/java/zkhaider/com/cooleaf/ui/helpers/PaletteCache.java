package zkhaider.com.cooleaf.ui.helpers;

import java.util.HashMap;

/**
 * Created by ZkHaider on 8/23/15.
 */
public class PaletteCache {

    private static PaletteCache sPaletteCache;
    private static HashMap<String, Integer> mVibrantColorCache = new HashMap<>();
    private static HashMap<String, Integer> mDarkVibrantColorCache = new HashMap<>();

    private PaletteCache() {

    }

    public static PaletteCache getInstance() {
        if (sPaletteCache == null)
            sPaletteCache = new PaletteCache();
        return sPaletteCache;
    }

    public HashMap<String, Integer> getVibrantColorCache() {
        return mVibrantColorCache;
    }

    public HashMap<String, Integer> getDarkVibrantColorCache() {
        return mDarkVibrantColorCache;
    }

    public static int getVibrantColor(String itemName) {
        return mVibrantColorCache.get(itemName);
    }

    public static void setVibrantColor(String itemName, int color) {
        mVibrantColorCache.put(itemName, color);
    }

    public static int getDarkVibrantColor(String itemName) {
        return mDarkVibrantColorCache.get(itemName);
    }

    public static void setDarkVibrantColor(String itemName, int color) {
        mDarkVibrantColorCache.put(itemName, color);
    }

}