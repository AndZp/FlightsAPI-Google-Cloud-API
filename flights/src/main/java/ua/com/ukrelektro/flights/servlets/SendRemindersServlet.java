package ua.com.ukrelektro.flights.servlets;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.utils.SystemProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.db.models.Reservation;

@SuppressWarnings("serial")
public class SendRemindersServlet extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(SendConfirmationEmailServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Date startDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, 1);
		Date endDate = c.getTime();

		Iterable<Key<Flight>> queryFligths = ofy().load().type(Flight.class).filter("dateDepart >", startDate).filter("dateDepart <", endDate).keys();
		if (queryFligths.iterator().hasNext()) {
			Query<Reservation> queryReservation = ofy().load().type(Reservation.class).filter("flight in", queryFligths);
			for (Reservation reservation : queryReservation) {
				sendRemindMail(reservation);
			}

		}

		response.setStatus(204);
	}

	private void sendRemindMail(Reservation reservation) throws UnsupportedEncodingException {
		String email = reservation.getPassenger().getEmail();
		String reservationInfo = reservation.toString();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String body = "Hi " + reservation.getPassenger().getGivenName() + ",\nWe want to remind you that you have a reservation on the flight:\n"
				+ reservationInfo + "\nEnjoy your flight !";
		try {
			Message message = new MimeMessage(session);
			InternetAddress from = new InternetAddress(String.format("noreply@%s.appspotmail.com", SystemProperty.applicationId.get()),
					"Flights GCE API");
			message.setFrom(from);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, ""));
			message.setSubject("Do you remember about your Reservation?");
			message.setText(body);
			Transport.send(message);

			LOG.log(Level.INFO, String.format("Send reminder message to ", email));

		} catch (MessagingException e) {
			LOG.log(Level.WARNING, String.format("Failed to send an mail to %s", email), e);
			throw new RuntimeException(e);
		}
	}
}
