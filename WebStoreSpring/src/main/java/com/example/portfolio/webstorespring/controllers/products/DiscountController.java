package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.models.dtos.products.requests.DiscountRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.DiscountAdminResponse;
import com.example.portfolio.webstorespring.models.dtos.products.responses.DiscountUserResponse;
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
        return discountService.getByCode(code);
    }

    @PostMapping("/admin/discounts")
    @ResponseStatus(HttpStatus.CREATED)
    public DiscountAdminResponse saveDiscount(@Valid @RequestBody DiscountRequest request) {
        return discountService.save(request);
    }

    @DeleteMapping("/admin/discounts")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserOrExpiredDiscount() {
        discountService.deleteUsedOrExpiredDiscount();
    }
}
