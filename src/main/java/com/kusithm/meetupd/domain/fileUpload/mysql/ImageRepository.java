package com.kusithm.meetupd.domain.fileUpload.mysql;


import com.kusithm.meetupd.domain.fileUpload.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
