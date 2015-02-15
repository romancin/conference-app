package org.ale.thot.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ale.thot.dao.SessionDao;
import org.ale.thot.domain.Session;
import org.ale.thot.domain.SessionType;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("sessionDao")
@Transactional
public class JpaSessionDao implements SessionDao {

    @PersistenceContext
    private EntityManager em;

    public JpaSessionDao() {
    }

    // test constructor
    public JpaSessionDao(EntityManager em) {
	this.em = em;
    }

    public List<Session> getSessionsByDate(String date) {
	return em.createNamedQuery("findSessionsForDate", Session.class).setParameter("date", date).getResultList();
    }

    public List<Session> getAllSessions() {
	return em.createNamedQuery("findAllSessions", Session.class).getResultList();
    }

    public Session saveSession(Session session) {
	return em.merge(session);
    }

    public Session getSessionById(String id) {
	return em.find(Session.class, Integer.parseInt(id));
    }

    public List<Session> getAllStaticSessions() {
	return em.createNamedQuery("findAllStaticSessions", Session.class).getResultList();
    }

    public List<Session> getAllSessionsByDateAndType(String date, SessionType... type) {
	return em.createNamedQuery("findSessionsForDate", Session.class).setParameter("date", date).setParameter("type", Arrays.asList(type)).getResultList();
    }

    public List<Session> getStaticSessionsByDate(String date) {
	return em.createNamedQuery("findStaticSessionsForDate", Session.class).setParameter("date", date).getResultList();
    }

    public List<Session> getCurrentSessions() {
	List<Session> todaySessions = getStaticSessionsByDate(getNowAsString());
	List<Session> currentSessions = new ArrayList<Session>();
	for (Session session : todaySessions) {
	    if (session.isInProgress(Calendar.getInstance())) {
		currentSessions.add(session);
	    }
	}

	return currentSessions;
    }

    public List<Session> getAllCurrentSessions() {
	List<Session> todaySessions = getAllSessionsByDate(getNowAsString());
	List<Session> currentSessions = new ArrayList<Session>();
	for (Session session : todaySessions) {
	    if (session.isInNearProgress(DateTime.now())) {
		currentSessions.add(session);
	    }
	}

	return currentSessions;
    }

    String getNowAsString() {
	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	return format.format(new Date());
    }

    @Override
    public List<String> getListOfConferenceDays() {
	return em.createNamedQuery("findListOfConferenceDays", String.class).getResultList();
    }

    @Override
    public List<Session> getAllSessionsByDate(String now) {
	return em.createNamedQuery("findAllSessionsForDate", Session.class).setParameter("date", now).getResultList();
    }

    @Override
    public List<Session> getAllSessionsByAuthor(String author) {
	return em.createNamedQuery("findAllSessionsForAuthor", Session.class).setParameter("author", "%" + author + "%").getResultList();
    }

}
