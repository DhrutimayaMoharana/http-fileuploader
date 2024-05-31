package com.https.fileuploader.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.https.fileuploader.entity.RequestResponseEntity;

public interface RequestResponseRepository extends JpaRepository<RequestResponseEntity, Long>{

}
