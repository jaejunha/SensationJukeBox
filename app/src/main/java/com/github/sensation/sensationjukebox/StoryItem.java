package com.github.sensation.sensationjukebox;

import android.net.Uri;

import java.net.URI;

/**
 * Created by Taek on 2017. 9. 16..
 */

public class StoryItem {
    String storyTitle;
    String storyContent;
    String songName;
    Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
