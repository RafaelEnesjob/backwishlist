Feature: Buscar produto específico na wishlist

  Scenario: Produto existente na wishlist
    Given que o cliente "customer-1" possui o produto "product-1" na wishlist
    When eu buscar o produto "product-1" na wishlist
    Then o produto retornado deve ter o nome "Tênis Nike"

  Scenario: Produto inexistente na wishlist
    Given que o cliente "customer-1" possui uma wishlist sem produtos
    When eu buscar o produto "product-99" na wishlist
    Then deve ocorrer um erro de produto não encontrado