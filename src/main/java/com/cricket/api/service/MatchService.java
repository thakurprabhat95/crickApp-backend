package com.cricket.api.service;

import com.cricket.api.entities.Match;

import java.util.List;
import java.util.Map;

public interface MatchService {

    //get all matches
    List<Match> getAllMatches();

    //get all live matches
    List<Match> getLiveMatchescore();

    //get CWC2023 points table
    List<List<String>> getWC2023PointTables();

    //get 2023-2025 world test championship point-table
    List<List<String>> getWTC2325PointTables();
}
