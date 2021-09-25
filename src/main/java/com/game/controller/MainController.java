package com.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.config.AppConfig;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/players")
public class MainController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;


    @GetMapping//(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Player> getPlayersList(Model model,
        @RequestParam(name = "name", required = false) String name, @RequestParam(name = "title", required = false) String title,
        @RequestParam(name = "race", required = false) Race race, @RequestParam(name = "profession", required = false) Profession profession,
        @RequestParam(name = "after", required = false) Long after, @RequestParam(name = "before", required = false) Long before,
        @RequestParam(name = "banned", required = false) Boolean banned, @RequestParam(name = "minExperience", required = false) Integer minExperience,
        @RequestParam(name = "maxExperience", required = false) Integer maxExperience, @RequestParam(name = "minLevel", required = false) Integer minLevel,
        @RequestParam(name = "maxLevel", required = false) Integer maxLevel, @RequestParam(name = "order", required = false, defaultValue = "ID") PlayerOrder order,
        @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber, @RequestParam(name = "pageSize", required = false, defaultValue = "3") Integer pageSize
        ) throws IOException {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        Specification<Player> specification = Specification
                .where(playerService.filterByName(name))
                .and(playerService.filterByTitle(title))
                .and(playerService.filterByRace(race))
                .and(playerService.filterByProfession(profession))
                .and(playerService.filterByDate(after, before))
                .and(playerService.filterByDate(after, before))
                .and(playerService.filterByBanned(banned))
                .and(playerService.filterByExperience(minExperience, maxExperience))
                .and(playerService.filterByLevel(minLevel, maxLevel));

        //не подключил преобразоваие енум-ов в строки - вызывалась ошибка обратной конвертации...

        return playerService.findAllPlayers(specification, pageable).getContent();
        }



    @GetMapping("/count")
    public Integer getPlayersCount(
        @RequestParam(name = "name", required = false) String name, @RequestParam(name = "title", required = false) String title,
        @RequestParam(name = "race", required = false) Race race, @RequestParam(name = "profession", required = false) Profession profession,
        @RequestParam(name = "after", required = false) Long after, @RequestParam(name = "before", required = false) Long before,
        @RequestParam(name = "banned", required = false) Boolean banned, @RequestParam(name = "minExperience", required = false) Integer minExperience,
        @RequestParam(name = "maxExperience", required = false) Integer maxExperience, @RequestParam(name = "minLevel", required = false) Integer minLevel,
        @RequestParam(name = "maxLevel", required = false) Integer maxLevel
        ) throws IOException {

        Specification<Player> specification = Specification
                .where(playerService.filterByName(name))
                .and(playerService.filterByTitle(title))
                .and(playerService.filterByRace(race))
                .and(playerService.filterByProfession(profession))
                .and(playerService.filterByDate(after, before))
                .and(playerService.filterByDate(after, before))
                .and(playerService.filterByBanned(banned))
                .and(playerService.filterByExperience(minExperience, maxExperience))
                .and(playerService.filterByLevel(minLevel, maxLevel));

        return playerService.findAllPlayers(specification).size();
    }


    @PostMapping
    public Player createPlayer(@RequestBody Player inPlayer) throws IOException {
        return playerService.createPlayer(inPlayer);
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Long id) throws IOException {
        return playerService.findPlayerByID(id);
    }

    @PostMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player player) throws IOException {
        return playerService.updatePlayer(id, player);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) throws IOException {
        playerService.deletePlayer(id);
    }




}
