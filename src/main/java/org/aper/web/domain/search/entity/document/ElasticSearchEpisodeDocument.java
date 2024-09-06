package org.aper.web.domain.search.entity.document;


@Document(indexName = "episodes")  // 인덱스 이름은 적절하게 변경
public class SearchEpisode {

    @Field(name = "episode_title", type = FieldType.Text)
    @JsonProperty("episodeTitle")
    private String episodeTitle;

    @Field(name = "story_id", type = FieldType.Integer)
    @JsonProperty("storyId")
    private Integer storyId;

    @Field(name = "episode_public_date", type = FieldType.Text)  // nullable로 처리된 경우 문자열로 받을 수 있음
    @JsonProperty("episodePublicDate")
    private String episodePublicDate;

    @Field(name = "pen_name", type = FieldType.Text)
    @JsonProperty("penName")
    private String penName;

    @Field(name = "episode_description", type = FieldType.Text)
    @JsonProperty("episodeDescription")
    private String episodeDescription;

    @Field(name = "story_title", type = FieldType.Text)
    @JsonProperty("storyTitle")
    private String storyTitle;

    @Field(name = "user_id", type = FieldType.Integer)
    @JsonProperty("userId")
    private Integer userId;
}
