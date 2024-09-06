package org.aper.web.domain.search.entity.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document(indexName = "#{@environment.getProperty('kafka.topic')}")
public class ElasticSearchEpisodeDocument {
    @Id
    @Field(name = "_id", type = FieldType.Text)
    @JsonProperty("_id")
    private String elasticId;


    @Field(name = "episodeId", type = FieldType.Text)
    @JsonProperty("episodeId")
    private String episodeId;

    @Field(name = "episodeChapter", type = FieldType.Long)
    @JsonProperty("episodeChapter")
    private Long episodeChapter;

    @Field(name = "episodeTitle", type = FieldType.Text)
    @JsonProperty("episodeTitle")
    private String episodeTitle;

    @Field(name = "episodeDescription", type = FieldType.Text)
    @JsonProperty("episodeDescription")
    private String episodeDescription;

    @Field(name = "episodePublic_date", type = FieldType.Date)
    @JsonProperty("episodePublicDate")
    private LocalDateTime episodePublicDate;

    @Field(name = "episodeOnDisplay", type = FieldType.Boolean)
    @JsonProperty("episodeOnDisplay")
    private Boolean episodeOnDisplay;

    @Field(name = "storyId", type = FieldType.Long)
    @JsonProperty("storyId")
    private Long storyId;

    @Field(name = "storyGenre", type = FieldType.Text)
    @JsonProperty("storyGenre")
    private String storyGenre;

    @Field(name = "storyTitle", type = FieldType.Text)
    @JsonProperty("storyTitle")
    private String storyTitle;

    @Field(name = "storyOnDisplay", type = FieldType.Boolean)
    @JsonProperty("storyOnDisplay")
    private Boolean storyOnDisplay;

    @Field(name = "userId", type = FieldType.Long)
    @JsonProperty("userId")
    private Long userId;

    @Field(name = "penName", type = FieldType.Text)
    @JsonProperty("penName")
    private String penName;

    @Field(name = "fieldImage", type = FieldType.Text)
    @JsonProperty("fieldImage")
    private String fieldImage;

    @JsonProperty("highlight")
    private Map<String, List<String>> highlight;
}
