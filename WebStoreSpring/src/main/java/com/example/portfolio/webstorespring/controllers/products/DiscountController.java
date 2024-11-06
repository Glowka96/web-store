package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.DiscountRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.DiscountAdminResponse;
import com.example.portfolio.webstorespring.model.dto.products.response.DiscountUserResponse;
import com.example.portfolio.webstorespring.services.products.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping("/discounts/{code}")
    public DiscountUserResponse getDiscountByCode(@PathVariable("code") String code) {
        return discountService.getDiscountByDiscountCode(code);
    }

    @PostMapping("/admin/discounts")
    @ResponseStatus(HttpStatus.CREATED)
    public DiscountAdminResponse saveDiscount(@Valid @RequestBody DiscountRequest discountRequest) {
        return discountService.saveDiscount(discountRequest);
    }

    @DeleteMapping("/admin/discounts")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserOrExpiredDiscount() {
        discountService.deleteUsedOrExpiredDiscount();
    }
}
