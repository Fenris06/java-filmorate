--DROP ALL OBJECTS DELETE FILES;

CREATE TABLE IF NOT EXISTS rate
 (
 rate_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 rate_name VARCHAR(20) NOT NULL
 );

CREATE TABLE IF NOT EXISTS films
 (
 film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 name VARCHAR(60) NOT NULL,
 description VARCHAR(200) NOT NULL,
 release_date DATE NOT NULL,
 duration INTEGER NOT NULL,
 rate_id INTEGER REFERENCES rate (rate_id) ON DELETE CASCADE
 );

CREATE TABLE IF NOT EXISTS users
 (
 user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 email VARCHAR(60) NOT NULL,
 login VARCHAR(60) NOT NULL,
 name VARCHAR(60) NOT NULL,
 birthday DATE NOT NULL
 );

 CREATE TABLE IF NOT EXISTS genres
 (
 genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 genre_name VARCHAR(60) NOT NULL
 );

 CREATE TABLE IF NOT EXISTS films_genres
 (
 genre_id INTEGER REFERENCES genres (genre_id) ON DELETE CASCADE,
 film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
 PRIMARY KEY (film_id, genre_id)
 );

 CREATE TABLE IF NOT EXISTS likes
 (
 film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
 user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE
 );

 CREATE TABLE IF NOT EXISTS friends
 (
 user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
 friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE
 );