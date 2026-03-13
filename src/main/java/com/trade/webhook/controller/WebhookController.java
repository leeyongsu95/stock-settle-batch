package com.trade.webhook.controller;

import com.trade.common.dto.ApiResponse;
import com.trade.webhook.dto.ExecutionWebhookRequest;
import com.trade.webhook.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    @Autowired
    private WebhookService webhookService;

    @RequestMapping(value = "/execution", method = RequestMethod.POST)
    public ApiResponse<String> receiveExecution(@RequestBody ExecutionWebhookRequest request) {
        webhookService.processExecution(request);
        return ApiResponse.ok("처리 완료");
    }
}
