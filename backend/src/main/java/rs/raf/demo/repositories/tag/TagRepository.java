package rs.raf.demo.repositories.tag;

import rs.raf.demo.entities.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> allTags();
    Tag addTag(Tag tag);
    Tag findTagByName(String tagName);
    Tag findTagById(Integer id);
    void deleteTag(Integer id);
}
