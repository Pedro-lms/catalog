package com.pls.catalog.services;

import com.pls.catalog.dto.CategoryDTO;
import com.pls.catalog.models.Category;
import com.pls.catalog.repositories.CategoryRepository;
import com.pls.catalog.services.exceptions.ControllerNotFoundException;
import com.pls.catalog.services.exceptions.DataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ControllerNotFoundException("Objeto não encontrado"));
        return new CategoryDTO(entity); //Transforma o objeto da Category com nome obj em CategoryDTO
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
      try {
          Category entity = repository.getOne(id);
          entity.setName(dto.getName());
          entity = repository.save(entity);
          return new CategoryDTO(entity);
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
}
