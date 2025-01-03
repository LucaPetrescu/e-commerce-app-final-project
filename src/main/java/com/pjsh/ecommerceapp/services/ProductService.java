package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.datamodels.Category;
import com.pjsh.ecommerceapp.datamodels.Product;

import com.pjsh.ecommerceapp.exceptions.CategoryDoesNotExistException;
import com.pjsh.ecommerceapp.exceptions.ProductDoesNotExistException;
import com.pjsh.ecommerceapp.repositories.CategoryRepository;
import com.pjsh.ecommerceapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${order.eligible.expression}")
    private String eligibilityExpression;


    public Product addProduct(Product product){
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            throw new RuntimeException("Category must be specified for the product");
        }

        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new CategoryDoesNotExistException("No category with such id exists"));

        product.setCategory(category);

        category.getProducts().add(product);

        categoryRepository.save(category);

        return productRepository.save(product);
    }

    public Product getProductByName(String productName){
        Product foundProduct = productRepository.findByName(productName);

        if(foundProduct == null){
            throw new ProductDoesNotExistException("No product with such name exists");
        }
        return foundProduct;
    }

    public Product getProductById(Integer id){
        Optional<Product> foundProduct = productRepository.findById(id);
        return foundProduct.orElseThrow(() -> new ProductDoesNotExistException("No product with such Id exists"));
    }

    public String deleteById(Integer id){
        this.productRepository.deleteById(id);
        return "Product deleted successfully!";
    }

    //Added SpEL

    public boolean isProductEligibleForDiscount(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductDoesNotExistException("No product with such id exists"));

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("price", product.getPrice());

        return parser.parseExpression(eligibilityExpression).getValue(context, Boolean.class);
    }

    public Product updateProduct(Integer productId, String name, String description, Integer stock, BigDecimal price) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductDoesNotExistException("No product with such id exists"));

        BigDecimal zero = new BigDecimal(0);


        if (name != null && !name.isEmpty()) {
            product.setName(name);
        }

        if (description != null && !description.isEmpty()) {
            product.setDescription(description);
        }

        if (stock != null) {
            if (stock < 0) {
                throw new RuntimeException("Stock cannot be negative.");
            }
            product.setAvailableQuantity(stock);
        }

        if (price != null) {
            if (price.compareTo(zero) < 0) {
                throw new RuntimeException("Price cannot be negative.");
            }
            product.setPrice(price);
        }

        return productRepository.save(product);
    }

}
