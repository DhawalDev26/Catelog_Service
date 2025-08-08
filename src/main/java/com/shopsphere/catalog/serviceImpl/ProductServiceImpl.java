package com.shopsphere.catalog.serviceImpl;

import com.shopsphere.catalog.dto.APIResponse;
import com.shopsphere.catalog.dto.CreateProductRequest;
import com.shopsphere.catalog.entity.Category;
import com.shopsphere.catalog.entity.Product;
import com.shopsphere.catalog.repository.CategoryRepository;
import com.shopsphere.catalog.repository.ProductRepository;
import com.shopsphere.catalog.service.ProductService;
import com.shopsphere.catalog.utils.SKUUtil;
import com.shopsphere.catalog.utils.SlugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public APIResponse addProduct(CreateProductRequest productRequest) {
        try {
            if (productRepository.existsBySku(productRequest.getSku())) {
                return APIResponse.builder()
                        .status(HttpStatus.CONFLICT)
                        .message("Product with SKU '" + productRequest.getSku() + "' already exists")
                        .build();
            }

            Optional<Product> existingProduct = productRepository.findByTenantIdAndNameAndBrandAndAttributes(
                    productRequest.getTenantId(),
                    productRequest.getName(),
                    productRequest.getBrand(),
                    productRequest.getAttributes()
            );

            if (existingProduct.isPresent()) {
                return APIResponse.builder()
                        .status(HttpStatus.CONFLICT)
                        .message("Product already exists for this tenant with same name, brand and attributes")
                        .build();
            }

            Optional<Category> optionalCategory = categoryRepository.findById(productRequest.getCategoryId());
            Optional<Category> optionalSubCategory = categoryRepository.findById(productRequest.getSubCategoryId());

            if (optionalCategory.isPresent() && optionalSubCategory.isPresent()) {
                Product product = Product.builder()
                        .name(productRequest.getName())
                        .slug(SlugUtil.generateSlug(productRequest.getName()))
                        .description(productRequest.getDescription())
                        .brand(productRequest.getBrand())
                        .categoryId(productRequest.getCategoryId())
                        .subCategoryId(productRequest.getSubCategoryId())
                        .price(productRequest.getPrice())
                        .stock(productRequest.getStock())
                        .sku(SKUUtil.generateSKU(productRequest.getName(), productRequest.getBrand()))
                        .images(productRequest.getImages())
                        .attributes(productRequest.getAttributes())
                        .tags(productRequest.getTags())
                        .tenantId(productRequest.getTenantId())
                        .isActive(Boolean.TRUE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                Product savedProduct = productRepository.save(product);

                return APIResponse.builder()
                        .status(HttpStatus.CREATED)
                        .message("Product added successfully")
                        .body(savedProduct)
                        .build();

            } else {
                return APIResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Invalid category or subcategory ID")
                        .build();
            }
        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error while adding product: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse getProductById(String id) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);

            if (optionalProduct.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Product with ID " + id + " not found")
                        .build();
            }

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Product fetched successfully")
                    .body(optionalProduct.get())
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error fetching category: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse getAllProducts(String tenantId, String categoryId, String subCategoryId, String brand,
                                      String name, Double minPrice, Double maxPrice, Boolean inStock) {
        try {
            Query query = new Query();
            List<Criteria> criteriaList = new ArrayList<>();

            if (tenantId != null) criteriaList.add(Criteria.where("tenantId").is(tenantId));
            if (categoryId != null) criteriaList.add(Criteria.where("categoryId").is(categoryId));
            if (subCategoryId != null) criteriaList.add(Criteria.where("subCategoryId").is(subCategoryId));
            if (brand != null) criteriaList.add(Criteria.where("brand").is(brand));
            if (name != null) criteriaList.add(Criteria.where("name").regex(name, "i")); // case-insensitive name search

            if (minPrice != null && maxPrice != null) {
                criteriaList.add(Criteria.where("price").gte(minPrice).lte(maxPrice));
            } else if (minPrice != null) {
                criteriaList.add(Criteria.where("price").gte(minPrice));
            } else if (maxPrice != null) {
                criteriaList.add(Criteria.where("price").lte(maxPrice));
            }

            if (inStock != null) {
                if (inStock) {
                    criteriaList.add(Criteria.where("stock").gt(0));
                } else {
                    criteriaList.add(Criteria.where("stock").lte(0));
                }
            }

            if (!criteriaList.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
            }

            List<Product> filteredProducts = mongoTemplate.find(query, Product.class);

            if (filteredProducts.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("No products found for given filters!")
                        .build();
            }

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Products fetched successfully")
                    .body(filteredProducts)
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error fetching products: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse updateProductStatus(String id, Boolean status) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Product not found with ID: " + id)
                        .build();
            }

            Product product = optionalProduct.get();
            product.setIsActive(status);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Product status updated successfully")
                    .body(product)
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error updating product status: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse updateProductStocks(String id, Integer stocks) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Product not found with ID: " + id)
                        .build();
            }

            Product product = optionalProduct.get();
            product.setStock(stocks);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Product stock updated successfully")
                    .body(product)
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error updating product stock: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse updateProduct(String id, CreateProductRequest productRequest) {
        try {
            Optional<Product> optionalProduct = productRepository.findByIdAndTenantId(id, productRequest.getTenantId());

            if (optionalProduct.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Product not found for the given tenant")
                        .build();
            }

            Product existingProduct = optionalProduct.get();

            // Optional: check for duplicate name in same tenant
            Optional<Product> duplicate = productRepository.findByNameAndTenantId(productRequest.getName(), productRequest.getTenantId());
            if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
                return APIResponse.builder()
                        .status(HttpStatus.CONFLICT)
                        .message("Another product with this name already exists for the tenant")
                        .build();
            }

            // Update fields
            if (productRequest.getName() != null) {
                existingProduct.setName(productRequest.getName());
            }
            if (productRequest.getDescription() != null) {
                existingProduct.setDescription(productRequest.getDescription());
            }
            if (productRequest.getBrand() != null) {
                existingProduct.setBrand(productRequest.getBrand());
            }
            if (productRequest.getPrice() != null) {
                existingProduct.setPrice(productRequest.getPrice());
            }
            if (productRequest.getImages() != null) {
                existingProduct.setImages(productRequest.getImages());
            }
            if (productRequest.getAttributes() != null) {
                existingProduct.setAttributes(productRequest.getAttributes());
            }
            if (productRequest.getTags() != null) {
                existingProduct.setTags(productRequest.getTags());
            }

            Product updatedProduct = productRepository.save(existingProduct);

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Product updated successfully")
                    .body(updatedProduct)
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error while updating product: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse deleteProduct(String id, String tenantId) {
        try {
            Optional<Product> optionalProduct = productRepository.findByIdAndTenantId(id, tenantId);

            if (optionalProduct.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Product not found for the given tenant")
                        .build();
            }

            Product product = optionalProduct.get();
            product.setIsActive(Boolean.FALSE);
            product.setUpdatedAt(LocalDateTime.now());

            productRepository.save(product);

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Product deleted successfully (soft delete)")
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error while deleting product: " + e.getMessage())
                    .build();
        }
    }

}
