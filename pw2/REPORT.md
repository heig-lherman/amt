# Report

## Exercice 1 - Entity mappings

**Report** The tests provided that validate updating entities are using `EntityManager.flush()` and
`EntityManager.clear()`) (e.g. `ActorRepositoryTest.testUpdateActor`).

* Describe precisely from the perspective of SQL statements sent to the database, the difference between:
    * `ActorRepositoryTest.testUpdateActor`
    * `ActorRepositoryTest.testUpdateActorWithoutFlushAndClear`
    * `ActorRepositoryTest.testUpdateActorWithoutClear`

* Explain the behavior differences and why it occurs.

Hints: run the tests using the debugger, look at the SQL statements in the log.

### Answer

We can see the number of queries differ from case to case. When `flush()` and `clear()` are called three statements are
created. When only `flush()` is called, two statements are created. When none of them are called, only one statement is
created. Further discussion on the behavior differences and why it occurs can be found in the following paragraphs.

First let's take a look at what `flush()` and `clear()` do.

##### `flush()`

The `flush()` method is used to synchronize the changes made to the entities with the database. This means that the
changes made to the entities are persisted to the database. However, the entities are not detached from the
`EntityManager`. This means that the `EntityManager` will still track the entities. In the configuration of the
`EntityManager` we can either set it to automatically (`AUTO`) flush the changes to the database or manually (`COMMIT`)
flush the changes to the database. When the `EntityManager` is set to automatically flush the changes to the database,
the `flush()` method is called automatically when the transaction is committed. When the `EntityManager` is set to
manually flush the changes to the database, the `flush()` method must be called manually.

##### `clear()`

The `clear()` method is used to detach all entities from the persistence context. This means that the `EntityManager`
will no longer track the entities. If a new statement concerning an entity that was previously tracked by the
persistence context is made, the `EntityManager` will retrieve fresh data from the database, thus creating a `SELECT`
statement.

#### 1. `flush()` and `clear()`

When `flush()` is called, the `EntityManager` synchronizes the changes made to the entities with the database. The
`clear()` method is used to detach all entities from the `EntityManager`. This means that the `EntityManager` will no
longer track the entities. When `clear()` is called, the `EntityManager` will not be able to detect changes made to the
entities. This is why we need to call `flush()` before `clear()`. The `flush()` method will synchronize the changes made
to the entities with the database, and then `clear()` will detach the entities from the `EntityManager`.

The third statement is a `select` query. This is made by Hibernate to fetch the updated state of the entity from the
database after the `flush()` and `clear()` method are called. Since `clear()` detaches all entities, Hibernate needs to
re-fetch the entity to ensure it has the latest state when accessed again.

We can see here the three statements thus created :

```sql
[Hibernate] 
    insert 
    into
        actor
        (first_name, last_name) 
    values
        (?, ?) 
    returning actor_id
2024-10-18 15:56:09,274 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (1:VARCHAR) <- [ALICE]
2024-10-18 15:56:09,275 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (2:VARCHAR) <- [BOB]
[Hibernate]
update
    actor
set first_name=?,
    last_name=?
where actor_id = ?
2024 - 10 - 18 15:56:28,738 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (1:VARCHAR) <- [FOO]
2024-10-18 15:56:28,738 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (2:VARCHAR) <- [BAR]
2024-10-18 15:56:28,739 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (3:INTEGER) <- [201]
[Hibernate]
select a1_0.actor_id,
       a1_0.first_name,
       a1_0.last_name
from actor a1_0
where a1_0.actor_id = ?
2024 - 10 - 18 15:56:37,284 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (1:INTEGER) <- [201]
```

#### 2. Only`flush()`

When only `flush()` is called, the `EntityManager` synchronizes the changes made to the entities with the database.
However, the entities are not detached from the `EntityManager`. This means that the `EntityManager` will still track
the entities. This is why we can see only two statements created. The first statement is the `insert` statement, and the
second statement is the `update` statement. The third statement is thus not necessary.

Here we do not see the `select` statement because the entities are still within the persistence context.

```sql
[Hibernate] 
    insert 
    into
        actor
        (first_name, last_name) 
    values
        (?, ?) 
    returning actor_id
2024-10-18 16:12:21,786 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (1:VARCHAR) <- [ALICE]
2024-10-18 16:12:21,786 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (2:VARCHAR) <- [BOB]
[Hibernate]
update
    actor
set first_name=?,
    last_name=?
where actor_id = ?
2024 - 10 - 18 16:12:24,928 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (1:VARCHAR) <- [FOO]
2024-10-18 16:12:24,928 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (2:VARCHAR) <- [BAR]
2024-10-18 16:12:24,929 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (3:INTEGER) <- [201]
```

#### 3. No `flush()` and `clear()`

When neither `flush()` nor `clear()` is called, the `EntityManager` will not synchronize the changes made to the
entities with the database. This means that the changes will not be persisted to the database until the transaction is
commited. This is why we can see only one statement created. The statement is the `insert` statement. The `update`
statement is not created because the changes were not yet synchronized with the database.

Hibernate was able to optimize the query by persisting only the updated values to the database.

```sql
[Hibernate] 
    insert 
    into
        actor
        (first_name, last_name) 
    values
        (?, ?) 
    returning actor_id
2024-10-18 16:15:27,715 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (1:VARCHAR) <- [ALICE]
2024-10-18 16:15:27,716 TRACE [org.hib.orm.jdb.bind] (main) binding parameter (2:VARCHAR) <- [BOB]
```

## Exercice 2 - Querying

**Report** on the query language that you prefer and why.

Foe us, choosing a query language in JPA often depends on specific requirements and the development context.

## 1. Native SQL

- **Advantages**: Native SQL is powerful when you want precise control over the database interactions and have complex,
  custom queries that may not map easily to JPA. Since it uses plain SQL, developers with SQL experience may find it
  straightforward, and it can be more performant in certain cases because the query is directly executed on the
  database.
- **Disadvantages**: It bypasses some of the type safety and automatic transformations JPA offers, which may lead to
  errors if not carefully managed.

**Preference**: Native SQL can be useful for fine-tuned queries and performance optimization, but is overall less
useful due to the impendence mismatch between the object model and the relational model (e.g. many-to-many tables).
Therefore, one should generally prefer JPQL to have queries that more closely match the modeled object domain.

## 2. JPQL (Java Persistence Query Language)

- **Advantages**: JPQL abstracts away the direct database schema by focusing on entities and their relationships,
  aligning with an object-oriented approach. This allows developers to work within the Java domain model, providing some
  level of flexibility if the database schema changes. JPQL is quite similar to SQL, making it relatively easy to learn
  and write for those with SQL experience.
- **Disadvantages**: Although JPQL provides a high-level abstraction, it might not support all SQL features. This limits
  its flexibility in advanced use cases. Additionally, for beginners, the similarity to SQL might lead to confusion as
  JPQL has its unique syntax and constraints.

**Preference**: JPQL is a good middle-ground solution, as it combines abstraction with familiarity. This approach should
be preferred for applications where an object-oriented mapping is beneficial and where the queries are relatively simple
to moderate in complexity.

## 3. Criteria API (String-Based)

- **Advantages**: The Criteria API is a programmatic somewhat typed way of building dynamic queries.
  It is especially powerful in situations where query conditions depend on runtime parameters. However, using string
  based Criteria queries doesn't offer any type safety as to what attributes are being referenced.
- **Disadvantages**: String-based Criteria queries can become verbose and challenging to read, especially for complex
  queries. They may also be more error-prone due to typos or incorrect property references, especially when refactoring.

**Preference**: The Criteria API offers mostly flexibility and ability to build requests through complex chains of
calls, making it highly valuable for building dynamic queries (in scenarios where the query could be built in a couple
of places, for example in a search service).

## 4. Criteria API (MetaModel)

- **Advantages**: The Metamodel-based Criteria API provides the strongest type safety of all approaches by generating
  static metamodel classes at compile time. These classes contain type-safe references to entity attributes, preventing
  runtime errors from typos or incorrect property references. It enables IDE features like auto-completion and
  refactoring support. When entities are modified, the metamodel is regenerated, forcing you to update affected queries.
- **Disadvantages**: Requires additional setup for metamodel generation and slightly increases the build complexity. The
  syntax can be verbose and may have a steeper learning curve compared to JPQL or native SQL. Some developers find the
  resulting code less readable than equivalent JPQL queries.

**Preference**: The Metamodel-based Criteria API is ideal for large enterprise applications where type safety and
maintainability are crucial. This approach should be chosen when working with complex domain models that undergo
frequent changes, as it helps catch errors early in the development cycle and makes refactoring safer.

## Overall Preference

For most applications, we would recommend using JPQL as the primary query language. It provides a good balance between
abstraction and flexibility, aligning well with the object-oriented nature of Java applications. JPQL is relatively easy
to learn and write, making it suitable for developers of all skill levels.

When dealing with complex queries or dynamic conditions, the Criteria API (MetaModel) is a good choice. It offers the
strongest type safety and refactoring support, making it ideal for large applications with complex domain models.
String-based Criteria queries should be used sparingly, as they lack type safety and can be challenging to maintain,
especially in large codebases.
