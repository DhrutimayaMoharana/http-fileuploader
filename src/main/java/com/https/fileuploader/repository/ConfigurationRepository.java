package com.https.fileuploader.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.https.fileuploader.entity.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

	Optional<Configuration> findByKey(String string);

}
