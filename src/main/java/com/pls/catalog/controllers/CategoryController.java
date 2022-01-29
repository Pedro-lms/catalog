package com.pls.catalog.controllers;

import com.pls.catalog.dto.CategoryDTO;
import com.pls.catalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/categories" )
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", required = false,defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction,
           @RequestParam(value = "orderBy", required = false, defaultValue = "moment") String orderBy
    ){
       /* if (page == null) {
            page = 0;
        }
        if(linesPerPage == null)
            linesPerPage = 20;
        if(direction == null)
            direction = "ASC";
        if(orderBy == null)
            orderBy = "moment";*/

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<CategoryDTO> pageList = service.findAllPaged(pageRequest); //Service chama o Repository que chama o find all no banco de dados
        return ResponseEntity.ok().body(pageList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        CategoryDTO dto = service.findById(id); //Service chama o Repository que chama o find all no banco de dados
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    //Pra que o objeto seja reconhecido e case com o objeto dto, passa-se o @RequestBody
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
