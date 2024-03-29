# java-filmorate
Template repository for Filmorate project.
# Database diagram

![database diagram](https://github.com/Fenris06/java-filmorate/blob/main/updatedatabase.png)

### Database diogram description
**geners:**
содержит названия жанров фильмов.
* **gener_id** - первичный ключ и идентификатор жанра;
* **genre_name** - название жанра;

**films_geners:**
содержит иформацию о том какой жанр к какому фильму относится.
* **gener_id**  - первичный ключь и идентификатор жанра отсылающий к таблице **geners**;
* **film_id** - первичный ключ и идентификатор фильма отсылающий к таблице **films**;

**films:**
содержит основную информацию о фильмах.
* **film_id** - первичный ключ и идентификатор фильма;
* **description** - описание фильма;
* **releasedate** - дата выходва фильма;
* **duration** - продолжительность фильма;
* **rate_id** - идентификатор возрастного рейтинга фильма;

**likes:** 
содержит информацию о лакайх которые пользователи ставят фильмам.
* **film_id** - первичный ключ и идентификатор фильма отсылающий к таблице **films**;
* **user_id** - первичный ключ и идентификатор юзера отсылающий к таблице **users**;

**users:**
содержит информацию о юзерах.
* **user_id** - первичный ключ и идентификатор юзера;
* **email** - имеил юзера;
* **login** - логин пользователя;
* **name** - имя пользователя;
* **birthday** - день рождения пользователя;

**friends:**
содержит информацию о друзьях пользователя.
* **user_id** - первичный ключ и идентификатор пользователя отсылающий к таблице **users**;
* **friend_id** - идентификатор друга отсыдающий к таблице **users**;

**rate:**
содержит информацию о возрастном рейтинге фильма.
* **rate_id** - первичный ключ и идентификатор рейтинга отсылающий к таблице **films**;
* **rate_name** - имя рейтинга;

# sql requests

**Запрос на получения таблици юзеров:**
```sql
SELECT*
FROM users;
```
**Запрос на получение таблици фильмов:**
```sql
SELECT*
FROM films;
```
**Запрос на получение таблици 10 самых популярных фильмов:**
```sql
SELECT f.name,
       f.discription,
       f.relesedate,
       f.duration,
       f.rate,
       COUNT(l.user_id)
FROM films as f
LEFT OUTER JOIN likes AS l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY  COUNT(l.user_id) DESC
LIMIT 10;
```

