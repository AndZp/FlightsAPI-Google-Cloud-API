package ua.com.ukrelektro.flights.model;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import ua.com.ukrelektro.flights.db.models.Passenger;

public class PassengerTest {

	private static final String EMAIL = "example@gmail.com";

	private static final String USER_ID = "123456789";

	private static final String GIVEN_NAME = "Your Given Here";

	private static final String FAMILY_NAME = "Your Family Here";

	private static final String DOCUMENT = "Document test number";

	private static final String PHONE = "Phone test number";

	private static final String AVATAR_BLOB_KEY = "Some blob-key";

	private Passenger passenger;

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

	Closeable session;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		passenger = new Passenger(USER_ID, GIVEN_NAME, FAMILY_NAME, DOCUMENT, PHONE, EMAIL);
		session = ObjectifyService.begin();

	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
		session.close();
	}

	@Test
	public void testGetters() throws Exception {
		assertEquals(USER_ID, passenger.getId());
		assertEquals(GIVEN_NAME, passenger.getGivenName());
		assertEquals(FAMILY_NAME, passenger.getFamilyName());
		assertEquals(DOCUMENT, passenger.getDocumentNumber());
		assertEquals(PHONE, passenger.getPhone());
		assertEquals(EMAIL, passenger.getEmail());
	}

	@Test
	public void testSetters() throws Exception {
		String newEmail = "newEmail";
		String newPhone = "newPhone";

		passenger.setEmail(newEmail);
		passenger.setPhone(newPhone);
		passenger.setAvatarBlobKey(AVATAR_BLOB_KEY);

		assertEquals(newPhone, passenger.getPhone());
		assertEquals(newEmail, passenger.getEmail());
		assertEquals(AVATAR_BLOB_KEY, passenger.getAvatarBlobKey());
	}

	@Test
	public void testUpdate() throws Exception {

		String givenNameNew = "givenNameNew";
		String familyNameNew = "familyNameNew";
		String documentNumberNew = "documentNumberNew";
		String phoneNew = "phoneNew";
		String emailNew = "emailNew";

		passenger.update(givenNameNew, familyNameNew, documentNumberNew, phoneNew, emailNew);

		assertEquals(USER_ID, passenger.getId());
		assertEquals(givenNameNew, passenger.getGivenName());
		assertEquals(familyNameNew, passenger.getFamilyName());
		assertEquals(documentNumberNew, passenger.getDocumentNumber());
		assertEquals(phoneNew, passenger.getPhone());
		assertEquals(emailNew, passenger.getEmail());

	}
}