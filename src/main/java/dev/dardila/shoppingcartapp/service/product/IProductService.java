package dev.dardila.shoppingcartapp.service.product;

import dev.dardila.shoppingcartapp.model.Product;
import dev.dardila.shoppingcartapp.request.AddProductRequest;
import dev.dardila.shoppingcartapp.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);

    Product getProductById(Long id);
    void deleteProduct(Long id);
    Product updateProduct(UpdateProductRequest product, Long productId);

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByBrandAndCategory(String brand, String category);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);


}
