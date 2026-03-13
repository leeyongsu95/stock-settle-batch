package com.trade.order.controller;

import com.trade.common.dto.ApiResponse;
import com.trade.order.dto.OrderRequest;
import com.trade.order.dto.OrderResponse;
import com.trade.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public ApiResponse<OrderResponse> buy(@RequestBody OrderRequest request) {
        return ApiResponse.ok(orderService.placeBuyOrder(request));
    }
}
