package com.example.ecommerce.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.example.ecommerce.entity.Country;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductCategory;
import com.example.ecommerce.entity.State;

import jakarta.persistence.EntityManager;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer{

	@Value("${allowed.origins}")
	private String[] theAllowedOrigins;
	
	private EntityManager entityManager;
	
	@Autowired
	public MyDataRestConfig(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}
	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		// TODO Auto-generated method stub
		RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
		
		HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH};
		
//		disable HTTP methods for Entity class: PUT, POST and DELETE
//		disableHttpMethods(Product.class, config, theUnsupportedActions);
//		disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);
//		disableHttpMethods(Country.class, config, theUnsupportedActions);
//		disableHttpMethods(State.class, config, theUnsupportedActions);
//		disableHttpMethods(Order.class, config, theUnsupportedActions);
		
		List.of(Product.class, ProductCategory.class, Country.class, State.class, Order.class)
				.forEach(entityClass->disableHttpMethods(entityClass, config, theUnsupportedActions));

//		call an internal helper method to expose IDs
		exposeIds(config);
		
		cors.addMapping(config.getBasePath()+"/**").allowedOrigins(theAllowedOrigins);
	}

//	@SuppressWarnings("rawtypes")
	private void disableHttpMethods(Class<?> theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
		config.getExposureConfiguration()
					.forDomainType(theClass)
					.withItemExposure((metadata, httpMethods)->httpMethods.disable(theUnsupportedActions))
					.withCollectionExposure((metadata, httpMethods)->httpMethods.disable(theUnsupportedActions));
	}

	private void exposeIds(RepositoryRestConfiguration config) {
		// TODO Auto-generated method stub
//		expose entity ids

//		get a list of all entity class from entity manager
//		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		
//		create an array of entity types
//		List<Class> entityClasses = new ArrayList<>();
		
//		get the entity types for entities
//		for (EntityType tempEntityType : entities) {
//			entityClasses.add(tempEntityType.getJavaType());
//		}
		
//		expose the entity ids for the array of entity/domain types
//		Class[] domainTypes = entityClasses.toArray(new Class[0]);
//		config.exposeIdsFor(domainTypes);
		
//		Manually adding class
//		config.exposeIdsFor(Product.class, ProductCategory.class);
		
//		dynamically adding class
		config.exposeIdsFor(entityManager.getMetamodel()
				.getEntities()
				.stream()
				.map(e -> e.getJavaType())
				.collect(Collectors.toList())
				.toArray(new Class[0]));
	}
}
