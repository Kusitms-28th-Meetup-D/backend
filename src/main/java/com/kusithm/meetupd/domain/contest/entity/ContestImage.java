package com.kusithm.meetupd.domain.contest.entity;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class ContestImage {

    @Id
    private ObjectId contestImageId;

    @Field(name = "image_url")
    private String imageUrl;
}
