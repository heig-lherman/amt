# Report

## Exercise 1 - Transaction, Renting a DVD

> **Report**
> * Describe an alternative strategy that could achieve a similar result to the one implemented.

One approach would be to leverage optimistic locking by slightly denormalizing the data model to
include a reference to the active rental directly in the inventory table. By adding an
`active_rental_id` foreign key to the inventory table (as well as a version field annotated with
`@Version`), we could attempt to atomically update this reference using optimistic locking on the
inventory record. When creating a new rental, we would try to set the `active_rental_id` from NULL
to our new rental's ID - if another transaction had succeeded first, the optimistic lock would fail.
While this approach could potentially offer better performance by avoiding explicit locks, it
deliberately denormalizes the data model. The concept of an "active rental" becomes redundantly
stored data that violates third normal form by creating a transitive dependency. This
denormalization requires careful maintenance to ensure consistency, particularly when handling
rental returns, and risks data corruption if these operations aren't handled perfectly.

Another approach would be to lock the rental table completely before inserting (different from
our current approach which only locks insertion of rentals linked to the inventory item),
preventing any other transactions from inserting a rental for the same DVD. This would ensure
that only one rental can be created for a given DVD at a time, but it has the side-effect of
completely locking every write operation (given the lock type used properly allowed for read
operations during the lock) to any other inventory item, thus potentially causing a bottleneck
in the whole system and not only for high-demand DVDs.

> **Report**
> * Explain why managing the concurrency
    using [@Lock](https://quarkus.io/guides/cdi-reference#container-managed-concurrency) or Java
    `synchronized` is not a solution.

Using `@Lock` annotations or Java's synchronized keyword for concurrency control in this rental
system would be inadequate because these mechanisms only operate at the application instance level.
In a production environment, multiple instances of the application typically run behind a load
balancer to provide scalability and high availability.

When multiple application instances are running, `@Lock` and `synchronized` would only prevent
concurrent access within each individual instance, while allowing race conditions between different
instances.

For example, if two rental requests for the same DVD are routed to different application instances,
both instances could simultaneously determine the DVD is available and create rental records,
leading to double-booking.

## Exercise 3 - Implement authentication for staff

> **Report**
> Explain why the password storage in Sakila `Staff` table is insecure. Provide a proposal for a
> more secure password storage.

The main reason is that it uses SHA1, a cryptographic hash function that is considered
cryptographically broken due to successful collision attacks. Additionally, the passwords are stored
using simple hashing without any salt, making them vulnerable to rainbow table attacks and allowing
identical passwords to produce identical hashes.

A more secure approach would involve using a modern password hashing algorithm specifically designed
for this purpose, such as Argon2id or bcrypt. These algorithms are deliberately slow and
memory-intensive to resist brute-force attacks. They also automatically handle salt generation and
incorporation.

The implementation would require modifying the staff table to store longer hash strings (at least 60
characters for bcrypt) and updating the `PasswordType` field to `MCF` (Modular Crypt Format) which
will store the algorithm and parameters used to hash the password directly in the generated string
value as well as use bcrypt for the hashing algorithm.

> **Report**
> Describe the flow of HTTP requests of the above test case and explain:
>
> * What is sent at step 1 to authenticate to the application and how it is transmitted.
> * What is the content of the response at step 2 that his specific to the authentication flow.
> * What is sent at step 3 to authenticate the user to the application and how it is transmitted.
>
> Explain why the above test authentication flow is considered insecure, if it was used in a
> productive environment as is, and what is required to make it secure.

In step 1, when navigating to the login page, no authentication data is transmitted - it's a simple
GET request to retrieve the login form. During step 2, when submitting the login form, the
credentials (username `j_username=Mike` and password `j_password=12345`) are sent as form parameters
in a POST request to `/j_security_check`. The server responds with a redirect and sets a session
cookie named `quarkus-credential` that will be used for subsequent authentication. In step 3, when
accessing the protected endpoint, this cookie is automatically included in the request headers by
the browser, thus allowing the server to identify and authenticate the user for the request.

The current implementation is insecure because credentials are sent in plaintext as form parameters
through an unencrypted HTTP connection, making them susceptible to interception through
man-in-the-middle attacks. Additionally, the session cookie will not be secure if the application
does not enforce HTTPS, as it can be easily intercepted and used to impersonate the user.
Additionally, the user credentials used for testing are very insecure, if the user is meant to be
kept as a testing staff in production, it should have a more secure password.

Another small issue is that the application doesn't yet employ roles, so any authenticated user
will be able to access protected endpoints annotated with `@Authenticated`. While this is fine for
the current state of the application, it would be a security risk if the application were to be
expanded to include more sensitive data or operations that are available to, for example,
customers. This introduces a technical debt that can become an annoyance to fix later on.

## Exercise 5 - Implement a frontend for rentals

> **Report**
> Discuss the pros and cons of using an approach such as the one promoted by htmx.

The hypermedia-driven approach promoted by htmx offers several compelling advantages. By returning
HTML instead of JSON, the server maintains complete control over the UI rendering, ensuring
consistent presentation and reducing client-side complexity. This paradigm also significantly
reduces JavaScript code, leading to simpler, more maintainable applications where the majority of
the logic resides server-side.

However, this approach also presents some limitations. The tight coupling between server and client
can make it challenging to develop multiple client interfaces or third-party integrations that might
require raw data access. Performance may be impacted as full HTML snippets are transferred instead
of minimal JSON payloads, potentially increasing bandwidth usage. The approach may also be less
suitable for highly interactive applications requiring complex client-side state management or
offline functionality.

From a development perspective, the htmx approach requires a mindset shift from the prevalent SPA
model, which affected our productivity initially. However, it excels in rapid application
development for traditional web applications where the benefits of simplified client-side logic and
reduced JavaScript complexity outweigh the need for rich client-side interactivity.
