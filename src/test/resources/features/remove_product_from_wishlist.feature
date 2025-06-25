Feature: Remoção de produto da wishlist

  Scenario: Remover produto existente
    Given que o cliente "customer-1" já salvou o produto "product-1" na wishlist
    When ele remover o produto "product-1" da sua wishlist
    Then o produto não deve estar mais presente na wishlist

  Scenario: Remover produto que não está na wishlist
    Given que o cliente "customer-1" possui uma wishlist vazia para remoção
    When ele tentar remover o produto "product-99"
    Then deve ocorrer um erro informando que o produto não foi encontrado

  Scenario: Remover produto de uma wishlist que não existe
    Given que o cliente "customer-999" não possui uma wishlist cadastrada
    When ele tentar remover o produto "product-1"
    Then deve ocorrer um erro informando que a wishlist não existe