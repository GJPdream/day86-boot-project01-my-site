package cn.itcast.constant;

public enum Types {

    TAG("tag"),
    CATEGORY("category"),
    ARTICLE("post"),
    PUBLISH("publish"),
    PAGE("page"),
    DRAFT("draft"),
    LINK("link"),
    IMAGE("image"),
    FILE("file"),
    CSRF_TOKEN("csrf_token"),
    COMMENTS_FREQUENCY("comments:frequency"),
    PHOTO("photo"),

    ;
    private String type;
    Types(String type) {
    this.type=type;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }

}
