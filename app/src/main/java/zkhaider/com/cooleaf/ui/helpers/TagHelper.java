package zkhaider.com.cooleaf.ui.helpers;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Tag;

/**
 * Created by Haider on 2/20/2015.
 */
public class TagHelper {

    public static String tagsToString(List<Tag> tagList) {

        StringBuilder sb = new StringBuilder();

        if (tagList.size() != 0) {
            // Currently using this since API rest architecture is not using best practices
            for (int i = 0; i < tagList.size(); i++) {
                if (tagList.get(i).getParentId() == 10) {
                    sb.append(tagList.get(i).getName() + ", ");
                }
            }

            String convert = sb.toString();
            return convert;
        }

        return sb.toString();
    }

}
