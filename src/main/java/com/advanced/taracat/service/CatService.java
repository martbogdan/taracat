package com.advanced.taracat.service;

import com.advanced.taracat.dao.entity.Cat;
import com.advanced.taracat.dao.entity.User;
import com.advanced.taracat.dao.repository.CatRepository;
import com.advanced.taracat.exeptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatService {

    @Autowired
    private CatRepository catRepository;
    @Autowired
    private UserService userService;

    // Витяг всіх котів юзера
    public List<Cat> getAllByUsername(String userName){

        return catRepository.findAllByUser_Username(userName);
    }

    // Замінити по принципу getAllByUsername
    public Cat getCatByName (String catName){
        return catRepository.findCatByName(catName);
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------

    public void addCat (String catName, String userName){

        User currentUser = userService.getUserByUsername(userName);

        Cat cat = new Cat();

        cat.setUser(currentUser);
        cat.setName(catName);
        cat.setCat_level(1);
        cat.setCat_expirience(0);
        cat.setCat_maxexpirience(1000);
        catRepository.save(cat);

    }

    public Cat updateExpirience (Cat cat){

        Cat catDB = catRepository.findById(cat.getId()).get();

        int newExp = cat.getCat_expirience();
        int lastExp = cat.getCat_maxexpirience();
        int catLevel = cat.getCat_level();

        if (newExp >= lastExp) {
            cat.setCat_expirience(0);
            cat.setCat_level(catLevel+1);
        } else {
            cat.setCat_expirience(newExp);
            cat.setCat_level(catLevel);
        }

        catRepository.save(cat);

        return cat;

    }

    public void delete (Long id){
        Optional<Cat> toDelete = catRepository.findById(id);
        if (toDelete.isPresent()){
            catRepository.delete(toDelete.get());
        }
    }


}
