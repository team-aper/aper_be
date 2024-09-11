package org.aper.web.domain.elasticsearch.entity.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "#{@environment.getProperty('kafka.user-topic')}")
public class ElasticSearchUserDocument {
    @Id
    @Field(name = "_id", type = FieldType.Text)
    @JsonProperty("_id")
    private String elasticId;

    @Field(name = "userId", type = FieldType.Long)
    @JsonProperty("userId")
    private Long userId;

    @Field(name = "penName", type = FieldType.Text)
    @JsonProperty("penName")
    private String penName;
}
