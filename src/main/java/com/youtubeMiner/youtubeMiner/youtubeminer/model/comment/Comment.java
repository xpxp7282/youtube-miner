
package com.youtubeMiner.youtubeMiner.youtubeminer.model.comment;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    @JsonProperty("snippet")
    private CommentSnippet__1 snippet;

    public Comment(CommentSnippet__1 snippet) {
        this.snippet = snippet;
    }


    @JsonProperty("snippet")
    public CommentSnippet__1 getCommentSnippet() {
        return snippet;
    }

    @JsonProperty("snippet")
    public void setCommentSnippet(CommentSnippet__1 snippet) {
        this.snippet = snippet;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Comment.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("snippet");
        sb.append('=');
        sb.append(((this.snippet == null)?"<null>":this.snippet));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
