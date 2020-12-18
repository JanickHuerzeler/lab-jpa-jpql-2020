package ch.fhnw.edu.jpa.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

import ch.fhnw.edu.jpa.model.Movie;
import ch.fhnw.edu.jpa.model.User;

@SpringBootApplication
@EntityScan(basePackageClasses = Movie.class)
public class Test1 implements CommandLineRunner {

	@PersistenceContext
	EntityManager em;

	public static void main(String[] args) {
		new SpringApplicationBuilder(Test1.class).run(args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// test1();
		// testReturnValue();
		testPrimitiveDistinct();
		System.out.println("done");
		// System.exit(0); // if you do not want to inspect the DB using the h2 console
	}

	private void testReturnValue() {
		Query query = em.createQuery("SELECT u.firstName, u.lastName FROM User u");
		List<Object[]> result = query.getResultList();

		for (Object[] names : result) {
			System.out.println(names[0] + ", " + names[1]);
		}
	}

	private void testPrimitiveDistinct() {
		TypedQuery<String> query = em.createQuery("SELECT DISTINCT u.lastName FROM User u", String.class);
		List<String> result = query.getResultList();
		for (String lastName : result) {
			System.out.println(lastName);
		}

	}

	private void test1() {
		Movie movie = em.find(Movie.class, 1L);
		System.out.println(movie);
		if (movie != null)
			System.out.println(movie.getTitle());

		TypedQuery<User> query = em.createQuery("SELECT u FROM User u JOIN u.rentals r WHERE r.movie.title = :movie",
				User.class);
		// Aufgabe 2, bidirektionales Query: "SELECT r.user FROM Rental r WHERE
		// r.movie.title =:movie"
		query.setParameter("movie", movie.getTitle());		

		List<User> users = query.getResultList();
		if (users.size() == 0) {
			System.out.println("nicht ausgeliehen");
		} else {
			System.out.println("ausgeliehen an " + users.get(0).getEmail());
		}

	}

}
