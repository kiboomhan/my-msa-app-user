package com.example.msaappuser.client;

import com.example.msaappuser.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("MSA-APP-ORDER")
@RequestMapping("/order")
public interface OrderServiceClient {

    @GetMapping("/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable("userId") String userId);

}
