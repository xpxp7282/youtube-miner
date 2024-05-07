package com.youtubeMiner.youtubeMiner.youtubeminer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.caption.Caption;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.caption.CaptionSnippet;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.channel.Channel;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.channel.ChannelSnippet;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.comment.AuthorChannelId;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.comment.Comment;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.comment.CommentSnippet__1;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.videoSnippet.VideoSnippet;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.videoSnippet.VideoSnippetDetails;
import com.youtubeMiner.youtubeMiner.youtubeminer.model.videoSnippet.VideoSnippetId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/youtube")
public class youtubeRestController {
    @Autowired
    RestTemplate restTemplate;
    public String getApiKey() {
        return System.getenv("YOUTUBE_API_KEY");
    }

    String apiKey = "AIzaSyAqoVB-FghRdQlgPUVuyjtGoggcI4qpmOQ";




    String baseLink = "https://www.googleapis.com/youtube/v3";

    HttpHeaders headers = new HttpHeaders();
    ObjectMapper objectMapper = new ObjectMapper();
    public HttpEntity<Void> authorisedEntity(){
        String BearerToken = "Bearer "+apiKey;
        headers.set("Authorization", "Bearer " + apiKey);
        // Create an HttpEntity with the headers
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        return requestEntity;
    }


    // First of all, we will implement all the GET operations for the youtube API without using it directly since we don't import the youtube API

    // Get one channel by id
    @GetMapping("/channels/{id}")
    public Channel findOne(@PathVariable String id) throws JsonProcessingException {
        assert id != null;
        String link = baseLink + "/channels?part=snippet&id=" + id + "&key=" + apiKey ;
        // get the channels
        ResponseEntity<String> responseEntity = restTemplate.exchange(link, HttpMethod.GET, null, String.class);

        assert responseEntity != null;
        Map<String, Object> jsonMap = objectMapper.readValue(responseEntity.getBody(), Map.class);


        //get the channel correspind to the id which is located in the jsonMap in the item[....] part
        String channelID = (String) ((Map<String, Object>) ((List<Object>) jsonMap.get("items")).get(0)).get("id");
        // get the snippet which is also located in the item part of the jsonMap
        ChannelSnippet snippet = objectMapper.convertValue(((Map<String, Object>) ((List<Object>) jsonMap.get("items")).get(0)).get("snippet"), ChannelSnippet.class);

        List<VideoSnippet> videos = (List<VideoSnippet>) jsonMap.get("videos");
        Channel channel = new Channel(channelID,snippet,videos);

        return channel;
    }

    // Get one video by id
    @GetMapping("/videos/{id}")
    public  VideoSnippet findOneVideo(@PathVariable String id) throws JsonProcessingException {
        assert id != null;
        String link = baseLink + "/videos?part=snippet&id=" + id + "&key=" + apiKey ;
        // get the video all the info are in the item part first we get the id and the snippet
        ResponseEntity<String> responseEntity = restTemplate.exchange(link, HttpMethod.GET, null, String.class);
        assert responseEntity != null;
        Map<String, Object> jsonMap = objectMapper.readValue(responseEntity.getBody(), Map.class);

        // now i need to get the info of the video in the item part of the jsonMap
        // first the id which is located in the id part of the item
        String videoSnippetId = (String) ((Map<String, Object>) ((List<Object>) jsonMap.get("items")).get(0)).get("id");
        VideoSnippetId  videoID = new VideoSnippetId();
        videoID.setVideoId(videoSnippetId);

        // then the snippet which is located in the snippet part of the item and its type is VideoSnippetDetails
        VideoSnippetDetails videoSnippetDetails = objectMapper.convertValue(((Map<String, Object>) ((List<Object>) jsonMap.get("items")).get(0)).get("snippet"), VideoSnippetDetails.class);

        // finally the comments which are located in the comments part of the item and its type is List<Comment>
       // List<Comment> comments = (List<Comment>) ((Map<String, Object>) ((List<Object>) jsonMap.get("items")).get(0)).get("comments");
        // finally the captions which are the result of the getCaptions method but i need to pass the id of the video and get a List<Caption> with a setter

        // now i will create the video object
        // now i will create the jsonMap that will be returned
        VideoSnippet video = new VideoSnippet();
        video.setId(videoID);
        video.setSnippet(videoSnippetDetails);
        video.setComments(videoComment(id));
        video.setCaptions(getCaptions(id));


        return video;
    }

    // Get one caption by id
    @GetMapping("/captions/{id}")
    public List<Caption> getCaptions(@PathVariable String id) throws JsonProcessingException {
        assert id != null;
        String link = baseLink + "/captions?part=snippet&videoId=" + id + "&key=" + apiKey ;
        // get the captions
        ResponseEntity<String> responseEntity = restTemplate.exchange(link, HttpMethod.GET, null, String.class);
        assert responseEntity != null;
        Map<String, Object> jsonMap = objectMapper.readValue(responseEntity.getBody(), Map.class);
        // get the id
        String captionId = (String) ((Map<String, Object>) ((List<Object>) jsonMap.get("items")).get(0)).get("id");

        // get the snippet
        CaptionSnippet snippet = objectMapper.convertValue(((Map<String, Object>) ((List<Object>) jsonMap.get("items")).get(0)).get("snippet"), CaptionSnippet.class);
        Caption response = new Caption(captionId,snippet);
        // now i need  to add it to a list<Caption>
        List<Caption> caption = new ArrayList<>();
        caption.add(response);
        return caption;
    }

    // Get the comments and its replies
    @GetMapping("/comments/{id}")
    public List<Comment> getComments(@PathVariable String id) throws JsonProcessingException {
        assert id != null;
        String link = baseLink + "/comments?part=snippet&maxResults=10&parentId=" + id + "&key=" + apiKey ;
        // get the comments
        ResponseEntity<String> responseEntity = restTemplate.exchange(link, HttpMethod.GET, null, String.class);
        assert responseEntity != null;
        Map<String, Object> jsonMap = objectMapper.readValue(responseEntity.getBody(), Map.class);
        // the jsonMap is a list of comments
        List<Comment> comments = new ArrayList<>();
        for (Object comment : (List<Object>) jsonMap.get("items")) {
            // for the authorChannelId i need to get the value parameter which is inside the authorChannelId part of the snippet but we have like snippet{authorChannelId{value}} so we need to get the value and not authorChannelId
            //com.youtubeMiner.youtubeMiner.youtubeminer.model.comment.AuthorChannelId@614fd4c8[value=UCpbrLQkx3N4xQv6N2x3uEWg] this is the value for now i just want UCpbrLQkx3N4xQv6N2x3uEWg as a value
            // still that as a result authorChannelId: com.youtubeMiner.youtubeMiner.youtubeminer.model.comment.AuthorChannelId@597e1256[value=UChP1JCRJvapMWt2vgFxJ9Pw] so i need to get the value
            //maybe we first get the AuthorChannelId not the value yet
            Map<String, Object> snippetMap = objectMapper.convertValue(((Map<String, Object>) comment).get("snippet"), Map.class);
            Map<String, Object> authorChannelIdMap = (Map<String, Object>) snippetMap.get("authorChannelId");
            String value = (String) authorChannelIdMap.get("value");
            AuthorChannelId authorChannelId = new AuthorChannelId();
            authorChannelId.setValue(value);

            //Snippet part of the comment
            // first the original text
            String textOriginal = (String) snippetMap.get("textOriginal");

            //Then the author display name
            String authorDisplayName = (String) snippetMap.get("authorDisplayName");

            //Then the author profile image url
            String authorProfileImageUrl = (String) snippetMap.get("authorProfileImageUrl");

            //Then the author channel url
            String authorChannelUrl = (String) snippetMap.get("authorChannelUrl");

            //Then the published at
            String publishedAt = (String) snippetMap.get("publishedAt");

            CommentSnippet__1 snippet = new CommentSnippet__1(textOriginal,authorDisplayName,authorProfileImageUrl,authorChannelUrl,authorChannelId,publishedAt);

            Comment response = new Comment(snippet);


            comments.add(response);
        }
        return comments;
    }

    // Get the 10 first comments on a video
    @GetMapping("/commentsOfVideo/{videoID}")
    public List<Comment> videoComment(@PathVariable String videoID) throws JsonProcessingException {
        String link = baseLink + "/commentThreads?part=snippet&maxResults=10&videoId=" + videoID + "&key=" + apiKey;
        // get the comments
        ResponseEntity<String> responseEntity = restTemplate.exchange(link, HttpMethod.GET, null, String.class);
        assert responseEntity != null;
        Map<String, Object> jsonMap = objectMapper.readValue(responseEntity.getBody(), Map.class);

        // the jsonMap is a list of comments
        List<Comment> comments = new ArrayList<>();
        for (Object comment : (List<Object>) jsonMap.get("items")) {
            // for the authorChannelId i need to get the value parameter which is inside the authorChannelId part of the snippet but we have like snippet{authorChannelId{value}} so we need to get the value and not authorChannelId
            //com.youtubeMiner.youtubeMiner.youtubeminer.model.comment.AuthorChannelId@614fd4c8[value=UCpbrLQkx3N4xQv6N2x3uEWg] this is the value for now i just want UCpbrLQkx3N4xQv6N2x3uEWg as a value
            // still that as a result authorChannelId: com.youtubeMiner.youtubeMiner.youtubeminer.model.comment.AuthorChannelId@597e1256[value=UChP1JCRJvapMWt2vgFxJ9Pw] so i need to get the value
            //maybe we first get the AuthorChannelId not the value yet
            Map<String, Object> snippetMap = objectMapper.convertValue(((Map<String, Object>) comment).get("snippet"), Map.class);

            Map<String, Object> topLevelCommentMap = (Map<String, Object>) snippetMap.get("topLevelComment");

            Map<String, Object> snippetTLCMap = (Map<String, Object>) topLevelCommentMap.get("snippet");
            Map<String, Object> authorChannelIdMap = (Map<String, Object>) snippetTLCMap.get("authorChannelId");

            String value = (String) authorChannelIdMap.get("value");
            AuthorChannelId authorChannelId = new AuthorChannelId();
            authorChannelId.setValue(value);

            //Snippet part of the comment
            // first the original text
            String textOriginal = (String) snippetTLCMap.get("textOriginal");

            //Then the author display name
            String authorDisplayName = (String) snippetTLCMap.get("authorDisplayName");

            //Then the author profile image url
            String authorProfileImageUrl = (String) snippetTLCMap.get("authorProfileImageUrl");

            //Then the author channel url
            String authorChannelUrl = (String) snippetTLCMap.get("authorChannelUrl");

            //Then the published at
            String publishedAt = (String) snippetTLCMap.get("publishedAt");

            CommentSnippet__1 snippet = new CommentSnippet__1(textOriginal, authorDisplayName, authorProfileImageUrl, authorChannelUrl, authorChannelId, publishedAt);

            Comment response = new Comment(snippet);

            comments.add(response);
        }
        return comments;
        }

    }

