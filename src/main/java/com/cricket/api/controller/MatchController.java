package com.cricket.api.controller;


import com.cricket.api.entities.Match;
import com.cricket.api.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cricket-matches")
@CrossOrigin("*")
public class MatchController {

    @Autowired
    private MatchService matchService;

    //get live matches score
    @GetMapping("/livescores")
    public ResponseEntity<List<Match>> getLiveMatchesScore() {
        return new ResponseEntity<>(this.matchService.getLiveMatchescore(), HttpStatus.OK);

    }

    @GetMapping("/allMatches")
    public ResponseEntity<List<Match>> getAllMatches()
    {
        return new ResponseEntity<>(this.matchService.getAllMatches(),HttpStatus.OK);
    }

    @GetMapping("/point-table")
    public ResponseEntity<?> getPointTable()
    {
        return new ResponseEntity<>(this.matchService.getWC2023PointTables(),HttpStatus.OK);
    }

    @GetMapping("/WTC2325-point-table")
    public ResponseEntity<?> getWTC2325PointTable()
    {
        return new ResponseEntity<>(this.matchService.getWTC2325PointTables(),HttpStatus.OK);
    }

}
