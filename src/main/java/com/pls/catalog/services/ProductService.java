package com.pls.catalog.services;

import com.pls.catalog.dto.CategoryDTO;
import com.pls.catalog.dto.ProductDTO;
import com.pls.catalog.models.Category;
import com.pls.catalog.models.Product;
import com.pls.catalog.repositories.CategoryRepository;
import com.pls.catalog.repositories.ProductRepository;
import com.pls.catalog.services.exceptions.ControllerNotFoundException;
import com.pls.catalog.services.exceptions.DataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    public ProductRepository repository;

    @Autowired
    public CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll(){
        List<Product> list= repository.findAll();
        /**
         * Usando lambda pra pegar cada elemento da lista de Categoria e levando para um elemento da lista CategoriaDTO
         */
        return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ControllerNotFoundException("Objeto não encontrado"));
        return new ProductDTO(entity); //Transforma o objeto da Product com nome obj em ProductDTO
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto,entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
      try {
          Product entity = repository.getOne(id);
          copyDtoToEntity(dto,entity);
          entity = repository.save(entity);
          return new ProductDTO(entity);
      }catch (EntityNotFoundException e){
         throw new ControllerNotFoundException("Id não encontrado" + id);
      }
    }



    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ControllerNotFoundException("Id não Encontrado" + id);
        }catch (DataIntegrityViolationException d) {
            throw new DataBaseException("Violação de Integridade");
        }
    }

    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list= repository.findAll(pageRequest);
        return list.map(x -> new ProductDTO(x));//Pagable já é uma stream
    }
    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setimageUrl(dto.getimageUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for(CategoryDTO categoryDTO : dto.getCategories()){
            Category category = categoryRepository.getOne(categoryDTO.getId());
            entity.getCategories().add(category);
        }
    }
}
