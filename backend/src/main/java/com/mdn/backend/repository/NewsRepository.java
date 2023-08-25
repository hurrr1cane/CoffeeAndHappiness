package com.mdn.backend.repository;

import com.mdn.backend.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {

}
