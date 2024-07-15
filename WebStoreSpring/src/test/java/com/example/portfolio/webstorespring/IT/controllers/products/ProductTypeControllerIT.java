package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.example.portfolio.webstorespring.repositories.products.ProductTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductType;
class ProductTypeControllerIT extends AbstractBaseControllerIT<ProductTypeRequest, ProductTypeResponse, ProductType> {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    protected void setup() {
        productTypeRepository.deleteAll();

        ProductType savedProductType = productTypeRepository.save(createProductType());
        id = savedProductType.getId();
    }

    @Override
    protected String getUri() {
        return "/product-types";
    }

    @Override
    protected ProductTypeRequest createRequest() {
        return ProductTypeBuilderHelper.createProductTypeRequest("Test product type");
    }

    @Override
    protected Class<ProductTypeResponse> getResponseTypeClass() {
        return ProductTypeResponse.class;
    }

    @Override
    protected List<ProductType> getAllEntities() {
        return productTypeRepository.findAll();
    }

    @Override
    protected Optional<ProductType> getOptionalEntityById() {
        return productTypeRepository.findById(id);
    }

    @Test
    void shouldGetAllProductTypes_thenStatusOk() {
        shouldGetAllEntities();
    }

    @Test
    void shouldSaveProductType_forAuthenticatedAdmin_thenStatusCreated() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated();
    }

    @Test
    void shouldNotSaveProductType_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldUpdateProductType_forAuthenticatedAdmin_thenStatusAccepted() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted();
    }

    @Test
    void shouldNotUpdateProductType_forAuthenticatedUser_thenStatusForbidden() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        shouldNotUpdateEntityForAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteProductType_forAuthenticatedAdmin_thenStatusNoContent(){
        shouldDeleteEntityForAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteProductType_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotDeleteEntityForAuthenticatedUser_thenStatusForbidden();
    }
}
