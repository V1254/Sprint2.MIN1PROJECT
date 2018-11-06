package eMarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eMarket.EMarketApp;
import eMarket.domain.Order;

@Controller
@RequestMapping("/order")
public class OrderController {

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("orderList", EMarketApp.getStore().getOrderList());
		return "form/orderMaster";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addOrder(@RequestParam(value = "orderId", required = false, defaultValue = "-1") int orderId,
			Model model) {
		if (orderId >= 0) {
			// display order items with the provided orderId;
			Order original = EMarketApp.getStore().getOrderList().stream().filter(o -> (o.getId() == orderId)).findAny()
																											  .get();
			model.addAttribute(original);
		} else {
			Order newOrder = new Order();
			newOrder.setId();
			model.addAttribute(newOrder);
			EMarketApp.getStore().getOrderList().add(newOrder);
		}
		return "form/orderDetail";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteOrder(@RequestParam(value = "orderId", required = true) int orderId, Model model) {
		EMarketApp.getStore().getOrderList().removeIf(o -> o.getId() == orderId);
		// update view with the new contents of the orderList.
		model.addAttribute("orderList", EMarketApp.getStore().getOrderList());
		return "form/orderMaster";
	}

//	private void deleteEmptyOrders() {
//		EMarketApp.getStore().getOrderList().removeIf(o -> o.getItemList().size() == 0);
//	}

}
