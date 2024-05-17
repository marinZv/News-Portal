package rs.raf.demo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Tag {

    private Integer id;

    @NotNull(message = "tag_name required")
    @NotEmpty(message = "tag_name required")
    @JsonProperty("tag_name")
    private String tagName;

    public Tag(){
    }

    public Tag(Integer id, String tagName){
        this.id = id;
        this.tagName = tagName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
