package zkhaider.com.cooleaf.utils;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.Participant;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchGroup;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchQuery;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchResult;

/**
 * Created by ZkHaider on 6/10/15.
 */
public class AutoCompleteHelper {


    public static List<String> getAutoCompleteResults(SearchResult searchResult) {

        List<Participant> people = searchResult.getPeople();
        List<SearchGroup> groups = searchResult.getGroups();
        List<Event> events = searchResult.getEvents();
        List<Post> posts = searchResult.getPosts();

        List<String> resultArray = new ArrayList<>();

        String personName;
        if (!people.isEmpty()) {
            personName = people.get(0).getName();
            resultArray.add(personName);
        }

        String groupName;
        if (!groups.isEmpty()) {
            groupName = groups.get(0).getName();
            resultArray.add(groupName);
        }

        String eventName;
        if (!events.isEmpty()) {
            eventName = events.get(0).getName();
            resultArray.add(eventName);
        }

        String postContent;
        if (!posts.isEmpty()) {
            postContent = posts.get(0).getContent();
            resultArray.add(postContent);
        }

        return resultArray;
    }

    public static List<String> getAutoCompleteQueries(List<SearchQuery> queries) {
        List<String> results = new ArrayList<>(4);
        int size = queries.size();
        if (size < 4) {
            for (int i = 0; i < size; i++) {
                SearchQuery query = queries.get(i);
                results.add(query.getName());
            }
        } else {
            for (int i = 0; i < 4; i++) {
                SearchQuery query = queries.get(i);
                results.add(query.getName());
            }
        }
        return results;
    }

}
