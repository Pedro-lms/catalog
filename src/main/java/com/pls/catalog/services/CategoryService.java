package com.pls.catalog.services;

import com.pls.catalog.dto.CategoryDTO;
import com.pls.catalog.models.Category;
import com.pls.catalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    public CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> list= repository.findAll();

        /**
         * Usando lambda pra pegar cada elemento da lista de Categoria e levando para um elemento da lista CategoriaDTO
         */
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

    }
}
