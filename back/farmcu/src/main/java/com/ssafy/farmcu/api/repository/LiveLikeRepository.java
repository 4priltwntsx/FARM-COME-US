package com.ssafy.farmcu.api.repository;

import com.ssafy.farmcu.api.entity.live.LiveLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveLikeRepository extends JpaRepository<LiveLike, Long> {
}
