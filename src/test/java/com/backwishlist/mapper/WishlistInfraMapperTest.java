package com.backwishlist.mapper;

import com.backwishlist.Util.ProductTestDataFactory;
import com.backwishlist.Util.WishlistTestDataFactory;
import com.backwishlist.domain.Product;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.infrastructure.database.documents.ProductDocument;
import com.backwishlist.infrastructure.database.documents.WishlistDocument;
import com.backwishlist.infrastructure.mappers.WishlistInfraMapper;
import com.backwishlist.infrastructure.mappers.WishlistInfraMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WishlistInfraMapperImpl.class)
class WishlistInfraMapperTest {


    @Autowired
    private WishlistInfraMapper mapper;

    private static final String CUSTOMER_ID = WishlistTestDataFactory.DEFAULT_CUSTOMER_ID;
    private static final String WISHLIST_ID = WishlistTestDataFactory.DEFAULT_WISHLIST_ID;
    private static final Product PRODUCT = WishlistTestDataFactory.createProduct("product-1", "Tênis Nike");

    @Test
    void ShouldMapDomainToDocument() {
        final Wishlist wishlist = WishlistTestDataFactory.wishlistWithProduct(WISHLIST_ID, CUSTOMER_ID, PRODUCT);

        final WishlistDocument document = mapper.toDocument(wishlist);

        assertEquals(wishlist.getId(), ReflectionTestUtils.getField(document, "id"));
        assertEquals(wishlist.getCustomerId(), ReflectionTestUtils.getField(document, "customerId"));

        @SuppressWarnings("unchecked")
        final List<ProductDocument> productDocs = (List<ProductDocument>) ReflectionTestUtils.getField(document, "products");
        assertNotNull(productDocs);
        assertEquals(1, productDocs.size());

        final ProductDocument productDoc = productDocs.get(0);
        assertEquals(PRODUCT.getId(), ReflectionTestUtils.getField(productDoc, "id"));
        assertEquals(PRODUCT.getName(), ReflectionTestUtils.getField(productDoc, "name"));
    }

    @Test
    void ShoulMapDocumentToDomain() {
        final ProductDocument productDoc = ProductTestDataFactory.defaultProductDocument();
        final WishlistDocument wishlistDoc = ProductTestDataFactory.wishlistDocumentWithProduct(
                ProductTestDataFactory.DEFAULT_DOCUMENT_ID,
                ProductTestDataFactory.DEFAULT_CUSTOMER_ID,
                productDoc
        );

        final Wishlist wishlist = mapper.toDomain(wishlistDoc);

        assertEquals(wishlistDoc.getId(), wishlist.getId());
        assertEquals(wishlistDoc.getCustomerId(), wishlist.getCustomerId());
        assertEquals(1, wishlist.getProducts().size());
        assertEquals(productDoc.getId(), wishlist.getProducts().get(0).getId());
    }
}
