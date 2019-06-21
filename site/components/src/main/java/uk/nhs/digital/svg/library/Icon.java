package uk.nhs.digital.svg.library;

import java.util.ArrayList;
import java.util.List;

public class Icon {

    private String id;
    private String name;
    private String path;
    private String category;
    private List<String> keywords = new ArrayList<>();

    public Icon(String id, String name, String path, String category) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public List<String> getKeywords() {
        return new ArrayList<>(keywords);
    }

    public String getCategory() {
        return category;
    }

    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    public void addKeywords(List<String> keywords) {
        this.keywords.addAll(keywords);
    }
}
