package com.cricket.api.serviceImpl;

import com.cricket.api.entities.Match;
import com.cricket.api.repositories.MatchRepository;
import com.cricket.api.service.MatchService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;

    //match history of all matches from our databases
    @Override
    public List<Match> getAllMatches() {
        return this.matchRepository.findAll();
    }

    @Override
    public List<Match> getLiveMatchescore() {
        List<Match> matches = new ArrayList<>();

        try {
            String url = "https://www.cricbuzz.com/cricket-match/live-scores";
            Document document = Jsoup.connect(url).get();
            Elements liveScoresElements = document.select("div.cb-mtch-lst.cb-tms-itm");
            for (Element match : liveScoresElements) {
                HashMap<String, String> liveMatchInfo = new LinkedHashMap<>();
                String teamHeading = match.select("h3.cb-lv-scr-mtch-hdr").select("a").text();
                String matchNumberVenue = match.select("span").text();
                Elements matchBatTeamInfo = match.select("div.cb-hmscg-bat-txt");
                String battingTeam = matchBatTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String score = matchBatTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                Elements bowlTeamInfo = match.select("div.cb-hmscg-bwl-txt");
                String bowlTeam = bowlTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String bowlTeamScore = bowlTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                String textLive = match.select("div.cb-text-live").text();
                String textComplete = match.select("div.cb-text-complete").text();
                //getting match link
                String matchLink = match.select("a.cb-lv-scrs-well.cb-lv-scrs-well-live").attr("href").toString();

                Match match1 = new Match();
                match1.setTeamHeading(teamHeading);
                match1.setMatchNumberVenue(matchNumberVenue);
                match1.setBattingTeam(battingTeam);
                match1.setBattingTeamScore(score);
                match1.setBowlTeam(bowlTeam);
                match1.setBowlTeamScore(bowlTeamScore);
                match1.setLiveText(textLive);
                match1.setMatchLink(matchLink);
                match1.setTextComplete(textComplete);
                match1.setMatchStatus();

                matches.add(match1);
                //update match info in the database
                 updateMatchInDb(match1);

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return matches;
    }

    private void updateMatchInDb(Match match1) {
        Match match = this.matchRepository.findByTeamHeading(match1.getTeamHeading()).orElse(null);
        if (match == null) {
            //save into db
            matchRepository.save(match1);
        } else {
            match1.setMatchId(match.getMatchId());
            //update into db
            matchRepository.save(match1);
        }
    }


    @Override
    public List<List<String>> getWC2023PointTables() {
        List<List<String>> pointTable = new ArrayList<>();
        String tableURL = "https://www.cricbuzz.com/cricket-series/6732/icc-cricket-world-cup-2023/points-table";
        try {
            Document document = Jsoup.connect(tableURL).get();
            Elements table = document.select("table.cb-srs-pnts");
            Elements tableHeads = table.select("thead>tr>*");
            List<String> headers = new ArrayList<>();
            tableHeads.forEach(element -> {
                headers.add(element.text());
            });
            pointTable.add(headers);
            Elements bodyTrs = table.select("tbody>*");
            bodyTrs.forEach(tr -> {
                List<String> points = new ArrayList<>();
                if (tr.hasAttr("class")) {
                    Elements tds = tr.select("td");
                    String team = tds.get(0).select("div.cb-col-84").text();
                    points.add(team);
                    tds.forEach(td -> {
                        if (!td.hasClass("cb-srs-pnts-name")) {
                            points.add(td.text());
                        }
                    });
//                    System.out.println(points);
                    pointTable.add(points);
                }


            });

            System.out.println(pointTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointTable;

    }

    @Override
    public List<List<String>> getWTC2325PointTables() {
        List<List<String>> pointTable = new ArrayList<>();
        String tableURL = "https://www.cricbuzz.com/cricket-stats/points-table/test/icc-world-test-championship";
        try {
            Document document=Jsoup.connect(tableURL).get();
            Elements table = document.select("table.cb-srs-pnts");
            System.out.println("Table is"+table.text());
            Elements tableHeads = table.select("thead>tr>*");
            List<String> headers = new ArrayList<>();
            tableHeads.forEach(element -> {
                headers.add(element.text());

            });
            System.out.println("headers is"+headers);
            pointTable.add(headers);
            Elements bodyTrs = table.select("tbody>*");
            bodyTrs.forEach(tr -> {
                List<String> points = new ArrayList<>();
                System.out.println("in for-lop");
                if (tr.hasAttr("class")) {
                    System.out.println("In the if -conditin");
                    Elements tds = tr.select("td");
                    String team = tds.get(0).select("div.cb-col-84").text();
                    System.out.println("Team is.."+team);
                    points.add(team);
                    tds.forEach(td -> {
                        if (!td.hasClass(" cb-srs-pnts-th")) {
                            points.add(td.text());
                        }
                    });
                  System.out.println("points is.."+ points);
                    pointTable.add(points);
                }


            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pointTable;
    }
}
