package org.ale.thot.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ale.thot.dao.LocationDao;
import org.ale.thot.dao.SessionDao;
import org.ale.thot.dao.TimeslotDao;
import org.ale.thot.domain.Day;
import org.ale.thot.domain.Location;
import org.ale.thot.domain.Session;
import org.ale.thot.domain.SessionType;
import org.ale.thot.domain.Timeslot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/allSessions")
public class AllSessionsController {

    @Autowired
    private SessionDao sessionDao;
    @Autowired
    private TimeslotDao timeslotDao;
    @Autowired
    private LocationDao locationDao;

    public AllSessionsController() {
	super();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void setupForm(ModelMap modelMap) {

	List<Day> conferenceDays = timeslotDao.getConferenceDays();
	List<Location> locations = locationDao.getLocations();

	Map<String, Map<String, Map<String, Session>>> allSessions = new HashMap<String, Map<String, Map<String, Session>>>();
	Map<String, List<Timeslot>> allTimeslots = new HashMap<String, List<Timeslot>>();

	for (Day day : conferenceDays) {
	    List<Session> daySessions = sessionDao.getAllSessionsByDateAndType(day.getShortName(), SessionType.openspace);
	    allSessions.put(day.getShortName(), groupSessionsByLocationsSlots(locations, daySessions));
	    allTimeslots.put(day.getShortName(), timeslotDao.getTimeslots(day.getShortName()));

	    List<Session> openspaceAnywhereSessions = sessionDao.getAllSessionsByDateAndType(day.getShortName(), SessionType.openspaceanywhere);
	    modelMap.put("openspaceAnywhereSessions", openspaceAnywhereSessions);
	}

	modelMap.put("allSessions", allSessions);
	modelMap.put("allTimeslots", allTimeslots);
	modelMap.put("days", conferenceDays);
	modelMap.put("today", getToday());
    }

    public static Map<String, Map<String, Session>> groupSessionsByLocationsSlots(List<Location> locations, List<Session> sessions) {
	HashMap<String, Map<String, Session>> transformedSessions = new HashMap<String, Map<String, Session>>();
	for (Location loc : locations) {
	    transformedSessions.put(loc.getShortName(), new HashMap<String, Session>());
	}
	for (Session session : sessions) {
	    Map<String, Session> sessionOfLocation = new HashMap<String, Session>();
	    sessionOfLocation.put(session.getStart(), session);
	    if (transformedSessions.containsKey(session.getLocation()))
		transformedSessions.get(session.getLocation()).put(session.getStart(), session);
	    else
		transformedSessions.put(session.getLocation(), sessionOfLocation);
	}
	return transformedSessions;
    }

    private String getToday() {
	Date today = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	String formatted = sdf.format(today);
	return formatted;
    }

}
