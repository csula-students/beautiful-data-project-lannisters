package edu.csula.datascience.acquisition;

/**
 * A simple model for testing
 */
public class Simple {
    private final String id;
    private final String content;

    public Simple(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public static Simple build(Mock data) {
        return new Simple(data.getId(), data.getContent());
    }
}
