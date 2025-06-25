Feature: Buscar produtos da wishlist

  Scenario: Cliente possui produtos na wishlist
    Given que o cliente "customer-1" já tem produtos na wishlist
    When eu buscar todos os produtos da wishlist do cliente "customer-1"
    Then os produtos da wishlist do cliente "customer-1" devem ser retornados

  Scenario: Cliente não possui wishlist
    Given que o cliente "customer-404" não possui wishlist cadastrada
    When eu buscar todos os produtos da wishlist do cliente "customer-404"
    Then deve ocorrer um erro de wishlist não encontrada

  Scenario: Cliente com wishlist vazia
    Given que o cliente "customer-2" possui uma wishlist completamente vazia
    When eu buscar todos os produtos da wishlist
    Then a lista de produtos deve estar vazia