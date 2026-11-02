# GameFlix

Spring Boot 4 + Thymeleaf + MySQL. Registration and login against the `users` table (BCrypt).
Optional "Log in with GitHub" (OAuth2).

## Run
1. Start MySQL on port 3306. The `gameflix` database is created automatically.
2. Set `DB_USER` and `DB_PASS`, then:
   `./mvnw spring-boot:run`
3. Open http://localhost:8080

## Optional GitHub OAuth2 login
1. Create a GitHub OAuth App; callback URL: `http://localhost:8080/login/oauth2/code/github`
2. Set `GITHUB_CLIENT_ID` and `GITHUB_CLIENT_SECRET`
3. `./mvnw spring-boot:run -Dspring-boot.run.profiles=oauth`

## Existing database
If you already have a `users` table from the old version, drop it
(`DROP TABLE users;`) so Hibernate can recreate it with the new unique/not-null constraints.
## AI USAGE

I used AI in the following ways:
1. Used to create the dto folder, which contains code related to the Registration Form and User Summary
2. Used to write the code in the security package: This code was written such that it allows the user to be authenticated via the MySQL database
3. Updates to OAuth2 User Services:
   4. Added checks to ensure that the users have an email and being where password encryption, when signing in with Github
5. Updates to UserServiceImpl
   6. Register was majorly overhauled, and now includes checks for things like if the username or email is already registered, ensuring these remain unique (They're also unique on the database level) 
7. Home controller now checks to see if user is logged in. If user is not logged in, it brings them to the login screen, while if user is logged in, it takes them to the main page
8. Updated most of the HTML pages.
9. It wrote the top bits of the readme (everything before the AI usage part)
10. Added the files TokenController and JwtConfig, as well as some things to Application.properties and pom.xml to allow for the creation fo JWT tokens
11. Fixed auth/users/me
## TO DO:

I still admit that I must find a way to allow the user to individually have their information displayed when calling api/auth/me. In the meantime, I have a page that lists all users (which I initially built while watching the lecture video for the springboot project)
