package rs.raf.demo.services;

import rs.raf.demo.entities.Tag;
import rs.raf.demo.repositories.tag.TagRepository;

import javax.inject.Inject;
import java.util.List;

public class TagService {

    @Inject
    private TagRepository tagRepository;

    public List<Tag> allTags(){
        return this.tagRepository.allTags();
    }


    public Tag findTagById(Integer id){
        return this.tagRepository.findTagById(id);
    }

    public Tag addTag(Tag tag){
        return this.tagRepository.addTag(tag);
    }

    public void deleteTag(Integer id){
        this.tagRepository.deleteTag(id);
    }

    public Tag findTagByName(String name){
        return this.tagRepository.findTagByName(name);
    }

}
