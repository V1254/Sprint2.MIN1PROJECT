/**
 * (C) Artur Boronat, 2015
 */
package eMarket.controller;

import java.util.Comparator;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eMarket.EMarketApp;
import eMarket.domain.Deal;
import eMarket.domain.Order;
import eMarket.domain.OrderItem;
import eMarket.domain.Product;

@Controller
@RequestMapping("/item")
public class ItemController {

    @InitBinder("itemFormDto")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new ItemValidator());
    }

    @RequestMapping("/")
    public String index(Model model) {
        return "form/itemDetail";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String ItemDetail(@ModelAttribute("itemFormDto") ItemFormDto ifd,
            				 @RequestParam(value = "itemId", required = false, defaultValue = "-1") int itemId,
            				 @RequestParam(value = "orderId", required = true) int orderId) {

        Order currentOrder = getOrderById(orderId);

        // populate drop down.
        ifd.setProductList(EMarketApp.getStore().getProductList());
        ifd.setOrderId(orderId);

        // if item is present populate the fields
        if (itemId >= 0) {
            ifd.setId(itemId);
            // get the item
            OrderItem oi = getOrderItemById(currentOrder, itemId);

            // set the fields.
            ifd.setProductId(oi.getProduct().getId());
            ifd.setAmount(oi.getAmount());
        }

        return "form/itemDetail";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addItem(@Valid @ModelAttribute("itemFormDto") ItemFormDto ifd, BindingResult result,
            					 @RequestParam String action, Model model) {

        // no need to validate if its cancelled
        if (action.equals("Cancel")) {
            return "redirect:/order/add?orderId=" + ifd.getOrderId();
        }

        // check no errors before doing anything else.
        if (result.hasErrors()) {
            ifd.setProductList(EMarketApp.getStore().getProductList());
            model.addAttribute("itemFormDto", ifd);
            return "form/itemDetail";
        }

        Order currentOrder = getOrderById(ifd.getOrderId());

        OrderItem newItem = new OrderItem();

        if (ifd.getId() >= 0) {
            // item already exists so remove it and use its id for the new orderItem
            OrderItem oldItem = getOrderItemById(currentOrder, ifd.getId());
            removeOrderItem(currentOrder, oldItem.getId());
            newItem.setId(oldItem.getId());
        }

        // orderItem fields to create
        int productId = ifd.getProductId();
        int amount = ifd.getAmount();
        double discount = getDiscount(productId);
        double productPrice = getProductById(productId).getPrice();
        double cost = calculateCost(productPrice, discount, amount);

        // setting those fields
        newItem.setProduct(getProductById(productId));
        newItem.setAmount(amount);
        newItem.setDiscount(discount);
        newItem.setCost(cost);
        currentOrder.getItemList().add(newItem);
        currentOrder.updateCost();
        model.addAttribute(currentOrder);

        return "redirect:/order/add?orderId=" + ifd.getOrderId();

    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteItem(@RequestParam(value = "itemId", required = true) int itemId,
            				 @RequestParam(value = "orderId", required = true) int orderId, Model model) {

        Order currentOrder = getOrderById(orderId);
        removeOrderItem(currentOrder, itemId);
        currentOrder.updateCost();
        model.addAttribute(currentOrder);
        return "redirect:/order/add?orderId=" + orderId;
    }

    /**
     * @param productPrice The Price of a particular product
     * @param discount     The discount to apply
     * @param amount       The amount of product items.
     * @return The Cost after the discount is applied.
     */
    private double calculateCost(double productPrice, double discount, int amount) {
        if (discount >= 1.) {
            return productPrice * amount;
        }
        return (productPrice - (productPrice * discount)) * amount;
    }

    /**
     * @param id The id of the Product object to find from the Store.
     * @return The Product with the specified id.
     */

    private Product getProductById(int id) {
        return EMarketApp.getStore().getProductList().stream().filter(p -> p.getId() == id).findAny().get();
    }

    /**
     * @param id The id of the Order Object to Find from the Store.
     * @return The Order with the specified id.
     */

    private Order getOrderById(int id) {
        return EMarketApp.getStore().getOrderList().stream().filter(o -> o.getId() == id).findAny().get();
    }

    /**
     * @param order The Order object to fetch the OrderItem from.
     * @param id    The id of the orderItem to find.
     * @return The OrderItem from order with the specified id.
     */
    private OrderItem getOrderItemById(Order order, int id) {
        return order.getItemList().stream().filter(o -> o.getId() == id).findAny().get();
    }

    /**
     * @param order The order which contains the orderItem you want to delete.
     * @param id    The id of the orderItem to be deleted from Order.
     */
    private void removeOrderItem(Order order, int id) {
        order.getItemList().removeIf(o -> o.getId() == id);
    }

    /**
     * @param productId
     * @return
     */

    private Double getDiscount(int productId) {
    	
    	// TODO: multiple deals try and find all of them then see if there is an active deal on.
        Optional<Deal> productDeal = EMarketApp.getStore().getDealList().stream()
                .filter(d -> d.getProduct().getId() == productId).findAny();
        if (!productDeal.isPresent()) {
            return 0.;
        }

        Deal currentDeal = productDeal.get();
        if (!currentDeal.isActive()) {
            return 0.;
        }

        return currentDeal.getDiscount();

    }
}
