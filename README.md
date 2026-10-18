# BEST Local Area

A JavaFX desktop application for managing the members and structure of a local student organisation (a BEST local group): volunteers, core teams, departments, projects and partner companies. It was my object-oriented programming course project at the Technical University of Cluj-Napoca.

## Features

- **Login** with per-user accounts and a home screen.
- **Volunteers**: add, edit and delete volunteers and view their details.
- **Core teams**: create teams, set the main organiser, add and remove members.
- **Departments**, **projects** and **companies**: full add / edit / delete screens.
- **Profile**: users can view and edit their own profile and add a photo.

## Architecture

- **Controllers** (`controllers/`): one JavaFX controller per screen.
- **Model** (`model/`): `Volunteer`, `CoreTeam`, `Department`, `Project`, `Company`, `User`.
- **Repository / DAO** (`repository/`): plain JDBC with prepared statements for each entity, connecting to PostgreSQL.
- Many-to-many relations are stored in link tables (`volunteerscoreteams`, `volunteersdepartments`).

Database tables used by the code: `users`, `volunteers`, `coreteams`, `departments`, `projects`, `companies`, `volunteerscoreteams`, `volunteersdepartments`.

## Run

Requirements: JDK 17+, Maven, PostgreSQL.

1. Create a database named `best_la` and the tables listed above.
2. Provide the connection settings as environment variables: `DB_URL`, `DB_USER`, `DB_PASSWORD`.
3. Start the app:

```bash
./mvnw javafx:run
```

## Known limitations

- The database schema is not included in the repository.
- Passwords are compared as plain text, so this is not suitable for real use without hashing.
