# C02006 - Coursework(20% of Module) - Spring MVC and Validations.
# Graded Score : 100/100

## Topics Studied
- Controllers(Request mappings)
- Views(JSP,JSTL)
- Controllers(Forms)
- Spring Architecture/Validation

## Task For this Courswork

The system had to be completed by developing code for the business logic/validation requirements stated below in the following classes
* Controller class [eMarket.controller.OrderController](/src/main/java/eMarket/controller/OrderController.java)
* Controller class [eMarket.controller.ItemController](/src/main/java/eMarket/controller/ItemController.java)
* Validator class [eMarket.controller.ItemValidator](/src/main/java/eMarket/controller/ItemValidator.java)
     

### Task For this Courswork

1. When clicking on `Order` in the view `index`, the user must be redirected to the view `orderMaster`. 
2. When clicking on `Main Page` in the view `orderMaster`, the user must be redirected to the view `index`. 
3. When clicking on `Add new order` in the view `orderMaster`, the user must be redirected to the view `orderDetail`. 
4. When clicking on `Edit` for a specific order in the view `orderMaster`, the user must be redirected to the view `orderDetail`. 
5. When clicking on `Delete` for a specific order in the view `orderMaster`, the user must be redirected to the view `orderMaster`. 
6. When clicking on `Add new order` in the view `orderMaster`, a new order must be created and added in the store `EMarketApp.store.orderList`. We are using `EMarketApp.store` as an abstraction of a data store that we will replace with a real database in sprint 4. The date of the new order must be the system date, the one that appears in the view `index`.  
7. When the list of orders is displayed in the view `orderMaster`, the following information must be displayed for each order in `EMarketApp.store.orderList`:
    * `id`: identifier
    * `date`: date of creation
    * `description`: description
    * `cost`: sum of the cost for each item in the order (`item.product.price * item.amount`)
8. When clicking on `Edit` for a specific order in the view `orderMaster`, the view `orderDetail` must display the details of the chosen order.
9. When clicking on `Delete` for a specific order in the view `orderMaster`, the selected order must be deleted from `EMarketApp.store.orderList`.

10. When clicking on `Show all orders` in the view `orderDetail`, the user must be redirected to the view `orderMaster`. 
11. When clicking on `Add new item` in the view `orderDetail`, the user must be redirected to the view `itemDetail`. 
12. When clicking on `Submit` in the view `itemDetail`, and there are no validation errors, the user must be redirected to the view `orderDetail`. 
13. When clicking on `Submit` in the view `itemDetail`, and there are validation errors, the user must be redirected to the view `itemDetail`. 
14. When clicking on `Cancel` in the view `itemDetail`, the user must be redirected to the view `orderDetail`. 
15. When adding a new item by clicking on `Add new item` in view `orderDetail`, a new object `OrderItem` must be created and linked to the form in the view `itemDetail`. 
16. When editing an existing item by clicking on `Edit` in view `orderDetail`, the corresponding `OrderItem` must be fetched from its corresponding order, which is stored in `EMarketApp.store.orderList`.
17. When deleting an existing item by clicking on `Delete` in view `orderDetail`, the corresponding `OrderItem` must be deleted from its corresponding order, which is stored in `EMarketApp.store.orderList`.
18. When adding a new item by clicking on `Submit` in view `itemDetail`, a new `OrderItem` will be created and appended to the `itemList` of the selected order (its identifier needs to be remembered when going from `orderDetail` to `itemDetail`):
    * the `product` must reference the corresponding product in `EMarketApp.store.productList`;
    * the `amount` must be set with the amount in the form in view `itemDetail`;
    * the `cost` must correspond to `amount * product.price`.

19. In view `itemDetail`, when submitting a new item, if the field `amount` is empty an error must be displayed next to the field `amount`.
20. In view `itemDetail`, when submitting a new item, if the `amount` provided is negative an error must be displayed next to the field `amount`.

21. When adding a new item by clicking on `Submit` in view `itemDetail`, the system should check if there is an active deal for the selected product in `EMarketApp.store.dealList`. In that case the discounted cost should be computed by applying the discount with the formula `(product.price - deal.discount * product.price) * amount`. Feel free to refactor the formula using your algebra skills. A deal is active for an order when:
    * the start date of the deal is before or equal to the order creation date, and there is no end date for the deal;
    * the start date of the deal is before or equal to the order date, and the end date of the deal is after or equal to the order creation date.
22. In the view `orderDetail`, display the cost of each item after applying discounts if there are active deals for the product of the order item.
23. In the view `orderMaster`, display the cost of the deal after applying discounts to each line item.
