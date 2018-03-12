package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.onehippo.taxonomy.api.Category;

import uk.nhs.digital.ps.beans.BaseDocument;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

import java.util.ArrayList;
import java.util.List;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:nihub")
@Node(jcrType = "nationalindicatorlibrary:nihub")
public class NiHub extends BaseDocument {

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:title")
    public String getTitle() {
        return getProperty("nationalindicatorlibrary:title");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:summary")
    public String getSummary() {
        return getProperty("nationalindicatorlibrary:summary");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:populateTopicLinks")
    public List<ActionLink> getPopularTopicLinks() {
        return getChildBeansByName("nationalindicatorlibrary:populateTopicLinks", ActionLink.class);
    }
    
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:nihublink")
    public List<NiHubLink> getNiHubLink() {
        return getChildBeansByName("nationalindicatorlibrary:nihublink", NiHubLink.class);
    }    

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:igbHubLink")
    public NiHubLink getIgbHubLink() {
        return getBean("nationalindicatorlibrary:igbHubLink", NiHubLink.class);
    }

    public List<String[]> getAzTaxonomySearchString() {
        ArrayList<Category> fullTaxonomyList = HippoBeanHelper.getFullTaxonomyList();
        String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        ArrayList<String> filteredList = new ArrayList<String>();
        ArrayList<String[]> searchStrings = new ArrayList<String[]>();

        for (int i = 0; i < 26 ; i++) {
            filteredList = filterByFirstLetter(fullTaxonomyList, letters[i]);
            String[] letterLink = {letters[i], ("/site/search/document-type/indicator?query=" + String.join(" ", filteredList).toLowerCase())};
            if (filteredList.size() > 0) {
                searchStrings.add(letterLink);
            }
        }

        return searchStrings;
    }

    private ArrayList<String> filterByFirstLetter(List<Category> list, String letter) {
        ArrayList<String> filteredList = new ArrayList<String>();
        for (int i = 0; i < list.size() ; i++) {
            if (String.valueOf(list.get(i).getKey().charAt(0)).toUpperCase().equals(letter)) {
                filteredList.add(list.get(i).getKey());
            }
        }
        return filteredList;
    }
}
