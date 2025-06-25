package com.backwishlist.Util;

import com.backwishlist.infrastructure.database.documents.ProductDocument;
import com.backwishlist.infrastructure.database.documents.WishlistDocument;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public class ProductTestDataFactory {

    public static final String DEFAULT_DOCUMENT_ID = "wishlist-1";
    public static final String DEFAULT_CUSTOMER_ID = "customer-1";
    public static final String DEFAULT_PRODUCT_ID = "product-1";
    public static final String DEFAULT_PRODUCT_NAME = "Tênis Nike";

    public static ProductDocument createProductDocument(String id, String name) {
        ProductDocument doc = new ProductDocument();
        ReflectionTestUtils.setField(doc, "id", id);
        ReflectionTestUtils.setField(doc, "name", name);
        return doc;
    }

    public static ProductDocument defaultProductDocument() {
        return createProductDocument(DEFAULT_PRODUCT_ID, DEFAULT_PRODUCT_NAME);
    }

    public static WishlistDocument wishlistDocumentWithProduct(String id, String customerId, ProductDocument product) {
        WishlistDocument doc = new WishlistDocument();
        ReflectionTestUtils.setField(doc, "id", id);
        ReflectionTestUtils.setField(doc, "customerId", customerId);
        ReflectionTestUtils.setField(doc, "products", List.of(product));
        return doc;
    }

    public static WishlistDocument emptyWishlistDocument(String id, String customerId) {
        WishlistDocument doc = new WishlistDocument();
        ReflectionTestUtils.setField(doc, "id", id);
        ReflectionTestUtils.setField(doc, "customerId", customerId);
        ReflectionTestUtils.setField(doc, "products", List.of());
        return doc;
    }

}
