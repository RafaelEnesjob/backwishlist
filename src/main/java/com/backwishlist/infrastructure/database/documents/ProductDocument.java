package com.backwishlist.infrastructure.database.documents;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDocument {

    private String id;
    private String name;

}
