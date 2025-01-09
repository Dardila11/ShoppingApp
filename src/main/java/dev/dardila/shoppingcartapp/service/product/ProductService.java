package dev.dardila.shoppingcartapp.service.product;

import dev.dardila.shoppingcartapp.exceptions.ProductNotFoundException;
import dev.dardila.shoppingcartapp.model.Category;
import dev.dardila.shoppingcartapp.model.Product;
import dev.dardila.shoppingcartapp.repository.CategoryRepository;
import dev.dardila.shoppingcartapp.repository.ProductRepository;
import dev.dardila.shoppingcartapp.request.AddProductRequest;
import dev.dardila.shoppingcartapp.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /*
        This method adds a new product to the system.
        1. It first checks if the category of the product already exists in the database by using the category name.
        2. If the category does not exist, it creates a new one and saves it to the database.
        3. The product's category is then set with the found or newly created category.
        4. A new Product instance is created by mapping the fields from the AddProductRequest object.
        5. Finally, the new Product is saved to the database and returned.
    */
    @Override
    public Product addProduct(AddProductRequest product) {
        // check if category exists
        Category category = Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(product.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        product.setCategory(category);
        return productRepository.save(createProduct(product, category));
    }

    //  map product request (DTO) to Product

    private Product createProduct(AddProductRequest productRequest, Category category) {
        return new Product(
                productRequest.getName(),
                productRequest.getBrand(),
                productRequest.getPrice(),
                productRequest.getInventory(),
                productRequest.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not Found"));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, () -> {
                    throw new ProductNotFoundException("Product not Found");
                });

    }

    @Override
    public Product updateProduct(UpdateProductRequest productRequest, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, productRequest ))
                .map(productRepository :: save)
                .orElseThrow(() -> new ProductNotFoundException("Product not Found"));
    }

    private Product updateExistingProduct(Product product, UpdateProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setBrand(productRequest.getBrand());
        product.setPrice(productRequest.getPrice());
        product.setInventory(productRequest.getInventory());
        product.setDescription(productRequest.getDescription());

        Category category = categoryRepository.findByName(productRequest.getCategory().getName());
        product.setCategory(category);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByBrandAndCategory(String brand, String category) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
