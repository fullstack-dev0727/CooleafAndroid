package zkhaider.com.cooleaf.ui.navdrawer;

/**
 * Created by Haider on 2/3/2015.
 */
public class NavMenuItem {

    public String mTitle;
    public int mIcon;

    public NavMenuItem(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }

    public String getTitle()
    {
        return mTitle;
    }
    public int getIcon()
    {
        return mIcon;
    }

}