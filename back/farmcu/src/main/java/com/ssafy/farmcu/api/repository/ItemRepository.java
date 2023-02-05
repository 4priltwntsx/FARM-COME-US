package com.ssafy.farmcu.api.repository;

import com.ssafy.farmcu.api.entity.store.Category;
import com.ssafy.farmcu.api.entity.store.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByItemId(Long itemId);

    //    List<Item> findByItemName(String itemName);
//    List<Item> findByCategory(Long categoryCode);
    Slice<Item> findByItemNameLike(String itemName, Pageable pageable);
    Slice<Item> findByCategoryAndItemNameLike(Category category, String itemName, Pageable pageable);
    void deleteByItemId(Long itemId);

}
