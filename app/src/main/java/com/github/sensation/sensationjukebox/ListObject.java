package com.github.sensation.sensationjukebox;

/**
 * Created by dream on 2017-09-16.
 */

public class ListObject {

    private String story;
    private String music;

    public ListObject(String story, String music){
        this.story = story;
        this.music = music;
    }

    public String getStory(){
        return story;
    }

    public String getMusic(){
        return music;
    }
}
