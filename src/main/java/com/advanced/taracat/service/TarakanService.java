package com.advanced.taracat.service;

import com.advanced.taracat.dao.entity.Tarakan;
import com.advanced.taracat.dao.entity.User;
import com.advanced.taracat.dao.repository.TarakanRepository;
import com.advanced.taracat.exeptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TarakanService {


    @Autowired
    private TarakanRepository tarakanRepository;

    @Autowired
    private UserService userService;

    public List<Tarakan> getAll() {
        return tarakanRepository.findAll();
    }

    public List<Tarakan> getAllByUsername(String username) {
        return tarakanRepository.findAllByUser_Username(username);
    }

    public Tarakan getTarakanById(Long id) {
        return tarakanRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Tarakan getTarakanByName(String name) {
        return tarakanRepository.findByTarname(name);
    }

    public Tarakan create(String tarname, User user) {

        Tarakan tarakan = new Tarakan();

        tarakan.setUser(user);
        tarakan.setTarname(tarname);
        tarakan.setLevel(1);
        tarakan.setExperience(0);
        tarakan.setStep(3);
        tarakan.setLoss(0);
        tarakan.setWin(0);
        tarakan.setDraw(0);
        tarakan.setRunning(0);
        tarakan.setImgId("tarU");
        return tarakanRepository.save(tarakan);
    }

    public void delete(Long id) {
        Optional<Tarakan> toDelete = tarakanRepository.findById(id);
        if (toDelete.isPresent()) {
            tarakanRepository.delete(toDelete.get());
        }
    }

    public Tarakan updateLevel (Tarakan tarakan, int experience){
        Tarakan tarakanDB = tarakanRepository.findById(tarakan.getId()).get();
        tarakanDB.setExperience(tarakan.getExperience()+experience);
        if (tarakan.getExperience()>=0 && tarakan.getExperience()<=99){
            tarakanDB.setLevel(1);
            tarakanDB.setStep(3);
        }else if (tarakan.getExperience()>=100 && tarakan.getExperience()<=199){
            tarakanDB.setLevel(2);
            tarakanDB.setStep(4);
        }else
        if (tarakan.getExperience()>=200 && tarakan.getExperience()<=299){
            tarakanDB.setLevel(3);
            tarakanDB.setStep(5);
        }else
        if (tarakan.getExperience()>=300 && tarakan.getExperience()<=399){
            tarakanDB.setLevel(4);
            tarakanDB.setStep(6);
        }else
        if (tarakan.getExperience()>=400){
            tarakanDB.setLevel(5);
            tarakanDB.setStep(7);
        }else if (tarakan.getExperience()<0){
            tarakanDB.setExperience(0);
            tarakanDB.setLevel(1);
            tarakanDB.setStep(3);
        }
        tarakanRepository.save(tarakanDB);
        return tarakanDB;
    }

    public Tarakan updateWin (Tarakan tarakan){
        Tarakan tarakanDB = tarakanRepository.findById(tarakan.getId()).get();
        tarakanDB.setWin(tarakan.getWin()+1);
        tarakanRepository.save(tarakanDB);
        return tarakanDB;
    }

    public Tarakan updateLoss (Tarakan tarakan){
        Tarakan tarakanDB = tarakanRepository.findById(tarakan.getId()).get();
        tarakanDB.setLoss(tarakan.getLoss()+1);
        tarakanRepository.save(tarakanDB);
        return tarakanDB;
    }

    public Tarakan updateDraw (Tarakan tarakan){
        Tarakan tarakanDB = tarakanRepository.findById(tarakan.getId()).get();
        tarakanDB.setDraw(tarakan.getDraw()+1);
        tarakanRepository.save(tarakanDB);
        return tarakanDB;
    }

    public Tarakan updateRuning (Tarakan tarakan){
        Tarakan tarakanDB = tarakanRepository.findById(tarakan.getId()).get();
        tarakanDB.setRunning(tarakan.getDraw()+tarakan.getWin()+tarakan.getLoss());
        tarakanRepository.save(tarakanDB);
        return tarakanDB;
    }

    public Tarakan selectBotByUserLevel(Long id){
        Tarakan tarakanBot = new Tarakan();
        Tarakan tarakanUser = tarakanRepository.findById(id).orElseThrow(NotFoundException::new);
        tarakanBot.setStep(tarakanUser.getStep());
        tarakanBot.setTarname("bot Level "+tarakanUser.getLevel());
        return tarakanBot;
    }
    public Tarakan selectRandomBot(){
        Tarakan tarakanBot = new Tarakan();
        Random random = new Random();
        int tarStep;
        tarakanBot.setStep(random.nextInt(5)+3);
        tarStep = tarakanBot.getStep();
        tarakanBot.setTarname("Bot Level "+(tarStep-2));
        return tarakanBot;
    }
    public Tarakan selectRandomBotByUserLevel(Long id){
        Tarakan tarakanBot = new Tarakan();
        Tarakan tarakanUser = tarakanRepository.findById(id).orElseThrow(NotFoundException::new);
        Random random = new Random();
        int tarStep;
        tarakanBot.setStep(random.nextInt(tarakanUser.getLevel())+3);
        tarStep = tarakanBot.getStep();
        tarakanBot.setTarname("Bot Level "+(tarStep-2));
        return tarakanBot;
    }
    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    public List<Tarakan> generateTarakanBotsByUserLevel (Long userTarakanId, int numOfTarakans){
        Tarakan tarakanUser = tarakanRepository.findById(userTarakanId).orElseThrow(NotFoundException::new);
        Random random = new Random();
        int tarStep;
        ArrayList<Tarakan> tarakans = new ArrayList<>();
        for (int i=0; i<numOfTarakans; i++){
            Tarakan tarakan = new Tarakan();
            if (tarakanUser.getLevel()>1){
                tarakan.setStep(getRandomNumberInRange(tarakanUser.getLevel()+1,tarakanUser.getLevel()+2));
            }else {
                tarakan.setStep(random.nextInt(tarakanUser.getLevel()) + 3);
            }
            tarStep = tarakan.getStep();
            tarakan.setTarname("Bot Level "+(tarStep-2));
            tarakan.setImgId("tarB"+i);
            tarakans.add(tarakan);
        }
        return tarakans;
    }
    public List<Integer> numOfBotsByUserLevel(int tarUserLevel){
        tarUserLevel=6-tarUserLevel;
        ArrayList<Integer> roads = new ArrayList<>();
        for (int i=(tarUserLevel*2); i>0; i--){
            roads.add(11-i);
        }
        return roads;
    }

}
