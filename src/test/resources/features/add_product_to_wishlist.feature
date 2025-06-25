Feature: Adicionar produto à wishlist

  Scenario: Adicionar produto com sucesso
    Given que o cliente "customer-1" tem uma wishlist vazia
    When eu adicionar o produto "product-1" com nome "Tênis Nike"
    Then o produto "product-1" deve estar na wishlist do cliente "customer-1"

  Scenario: Tentar adicionar produto duplicado
    Given que o cliente "customer-1" já tem o produto "product-1" na wishlist
    When eu tentar adicionar o mesmo produto novamente
    Then deve ocorrer um erro de produto já existente

  Scenario: Exceder limite de produtos na wishlist
    Given que o cliente "customer-1" já tem 20 produtos na wishlist
    When eu tentar adicionar o produto "product-21" com nome "Produto Extra"
    Then deve ocorrer um erro de limite de produtos excedido
