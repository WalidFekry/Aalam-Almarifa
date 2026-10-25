package com.walid.knowledgeworld.favorites;

public class FavoriteItem {
    public static final String TYPE_QUOTE = "QUOTE";
    public static final String TYPE_ARTICLE = "ARTICLE";

    private String id;
    private String type;
    private String title;
    private String content;
    private String url;
    private long timestamp;

    public FavoriteItem() {
    }

    public FavoriteItem(String id, String type, String title, String content, String url, long timestamp) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
