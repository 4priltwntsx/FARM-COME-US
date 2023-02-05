package com.ssafy.farmcu.api.service.store;

import com.ssafy.farmcu.api.dto.store.ItemDto;
import com.ssafy.farmcu.api.dto.store.ItemSearchReq;
import com.ssafy.farmcu.api.entity.store.Category;
import com.ssafy.farmcu.api.entity.store.Item;
import com.ssafy.farmcu.api.entity.store.Store;
import com.ssafy.farmcu.api.repository.CategoryRepository;
import com.ssafy.farmcu.api.repository.ItemRepository;
import com.ssafy.farmcu.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;

    @Override
    public boolean saveItem(ItemDto itemDto) {
        try {
            Category category = categoryRepository.findByCategoryCode(itemDto.getCategoryCode());
            Store store = storeRepository.findByStoreId(itemDto.getStoreId()).get();
            Item item = Item.builder()
                    .itemName(itemDto.getItemName())
                    .itemDescription(itemDto.getItemDescription())
                    .itemPrice(itemDto.getItemPrice())
                    .itemDiscount(itemDto.getItemDiscount())
                    .itemStock(itemDto.getItemStock())
                    .category(category)
                    .store(store)
                    .build();

//            item.setCategory(category);
//            item.setStore(store);
            itemRepository.save(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateItem(ItemDto itemDto) {
        try {
            Category category = categoryRepository.findByCategoryCode(itemDto.getCategoryCode());
            Store store = storeRepository.findByStoreId(itemDto.getStoreId()).orElseThrow(NullPointerException::new);
            Item item = itemRepository.findByItemId(itemDto.getItemId()).orElseThrow(NullPointerException::new);

            item.setItemName(itemDto.getItemName());
            item.setItemDescription(itemDto.getItemDescription());
            item.setItemPrice(itemDto.getItemPrice());
            item.setItemDiscount(itemDto.getItemDiscount());
            item.setItemStock(itemDto.getItemStock());
            item.setCategory(category);

            itemRepository.save(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteItem(Long itemId) {
        try {
            itemRepository.deleteByItemId(itemId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ItemDto findOne(Long itemId) {
        Item item = itemRepository.findByItemId(itemId).orElseThrow(NullPointerException::new);
//        Item item = itemRepository.findByItemId(itemId);
//        Category category = categoryRepository.findByCategoryCode(item.getCategory().getCategoryCode());
//        Store store = storeRepository.findByStoreId(item.getStore().getStoreId());

        ItemDto result = new ItemDto(item);
        return result;
    }

    @Override
    public Slice<ItemDto> findItemsByCategoryAndItemNameLike(ItemSearchReq itemSearchReq, Pageable pageable) {
        Slice<Item> items;

        if (itemSearchReq.getCategoryCode() == 0) {
            items = itemRepository.findByItemNameLike(itemSearchReq.getItemName(), pageable);
        } else {
            Category category = categoryRepository.findByCategoryCode(itemSearchReq.getCategoryCode());
            items = itemRepository.findByCategoryAndItemNameLike(category, itemSearchReq.getItemName(), pageable);
        }

        Slice<ItemDto> result = (Slice<ItemDto>) items.stream()
                .map(i -> new ItemDto(i))
                .collect(toList());

        return result;
    }

}
