# CampusTix — College Event Ticketing System

A Java Swing mini project for demonstrating a college event ticket booking workflow.

## Features
- Browse six sample events with date, venue, description, price, and availability.
- Book tickets using a student name and email.
- Generate a booking ID and calculate total cost automatically.
- View booking records and cancel confirmed bookings.
- Recalculate available seats after booking/cancellation.
- Save bookings locally in `bookings.dat`.

## Requirements
- JDK 8 or newer
- VS Code with Java Extension Pack (optional)

## Run in VS Code
1. Extract the ZIP file.
2. Open the `CollegeEventTicketingSystem` folder in VS Code.
3. Open `CollegeEventTicketingSystem.java`.
4. Click **Run** above the `main` method.

## Run in terminal
Open a terminal in this folder:
```bash
javac CollegeEventTicketingSystem.java
java CollegeEventTicketingSystem
```
On Windows, you can double-click `run.bat`.

## Demonstration flow
1. Show the event cards.
2. Book 2 tickets for an event using a valid name and email.
3. Show the generated booking ID and total amount.
4. Open My Bookings to show the record.
5. Cancel the booking and show that seats become available again.
6. Close and reopen the app to demonstrate saved records.

## Java concepts demonstrated
Classes and objects, ArrayList, Swing GUI, event listeners, input validation, methods, file handling, and Java serialization.

## Scope note
This is an educational local demo, not a live ticketing platform. It does not include online payments, user accounts, or a remote database. Sample events are defined in `loadEvents()`.
