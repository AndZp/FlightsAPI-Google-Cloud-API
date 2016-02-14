package ua.com.ukrelektro.flights.db.helpers.impl;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import com.google.api.server.spi.response.NotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

public abstract class AbstractBaseDB<T> {

	protected <T> Key<T> create(T t) {
		return ofy().save().entity(t).now();
	}

	protected <T> String createWithKey(T t) {
		Key<T> key = ofy().save().entity(t).now();
		return key.getString();
	}

	protected <T> Long createWithID(T t) {
		Key<T> key = ofy().save().entity(t).now();
		return key.getId();
	}

	protected <T> void update(Class<T> clazz, Long id, T t) throws NullPointerException {
		if (id == null) {
			throw new NullPointerException("ID cannot be null");
		}
		T tnew = ofy().load().type(clazz).id(id).now();
		ofy().save().entity(tnew).now();
	}

	protected <T> void update(Class<T> clazz, String key, T t) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException("ID cannot be null");
		}
		T tnew = ofy().load().type(clazz).id(key).now();
		ofy().save().entity(tnew).now();

	}

	protected <T> T getById(Class<T> clazz, Long id) throws NullPointerException {
		if (id == null) {
			throw new NullPointerException("ID cannot be null");
		}
		return ofy().load().type(clazz).id(id).now();
	}

	protected <T> T getByKey(Class<T> clazz, String key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException("ID cannot be null");
		}
		return ofy().load().type(clazz).id(key).now();
	}

	protected <T> Query<T> getQueryAll(Class<T> clazz) {
		Query<T> query = ofy().load().type(clazz);
		return query;
	}

	/**
	 * Check simple Key<T> without @Parent
	 * 
	 * @param clazz
	 *            .class
	 * @param id
	 *            value
	 * @return Object by Key
	 * @throws NotFoundException
	 */
	protected T checkSimpleKey(Class<T> clazz, String id) throws NotFoundException {
		Key<T> key = Key.create(clazz, id);
		T obj = checkKeyInDB(clazz, id, key);
		return obj;
	}

	protected T checkKeyInDB(Class<T> clazz, String id, Key<T> key) throws NotFoundException {
		T obj = ofy().load().key(key).now();
		if (obj == null) {
			throw new NotFoundException("No found " + clazz.getSimpleName() + " with id: " + id);
		}
		return obj;
	}

	protected T getByWebSafeKey(String websafeCityKey, Class<T> clazz) throws NotFoundException {
		Key<T> key = Key.create(websafeCityKey);
		T obj = ofy().load().key(key).now();
		if (obj == null) {
			throw new NotFoundException("No " + clazz.getSimpleName() + " found with key: " + websafeCityKey);
		}
		return obj;
	}

}
