package com.ssafy.farmcu.api.entity.store;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    //필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryCode;

}

