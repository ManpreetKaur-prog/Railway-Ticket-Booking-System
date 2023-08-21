package org.example;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class RailwayTicketBookingSystem {
    private static final String BOOKINGS_FILE_PATH = "railway_bookings.txt";
    private static int bookingIdCounter = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenu();
            int choice = getUserChoice(scanner);

            MenuChoice selectedChoice = MenuChoice.valueOf(choice);
            if (selectedChoice != null) {
                handleUserChoice(selectedChoice, scanner);
            } else {
                System.out.println("Invalid choice. Please select a valid option.");
            }
        }}
    enum MenuChoice {
        BOOK_TICKET(1),
        VIEW_BOOKINGS(2),
        EXIT(3);

        private final int value;

        MenuChoice(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }public static MenuChoice valueOf(int value) {
            for (MenuChoice choice : MenuChoice.values()) {
                if (choice.value == value) {
                    return choice;
                }
            }
            return null;
        }}



        private static void displayMenu() {
            System.out.println("1. Book Ticket");
            System.out.println("2. View Bookings");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
    }
    private static int getUserChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    private static void handleUserChoice(MenuChoice choice, Scanner scanner) {
        switch (choice) {
            case BOOK_TICKET:
                bookTicket(scanner);
                break;
            case VIEW_BOOKINGS:
                viewBookings();
                break;
            case EXIT:
                System.out.println("Exiting...");
                scanner.close();
                System.exit(0);
                break;
        }
    }
//        while (true) {
//            System.out.println("1. Book Ticket");
//            System.out.println("2. View Bookings");
//            System.out.println("3. Exit");
//            System.out.print("Enter your choice: ");
//            try {
//                int choice = Integer.parseInt(scanner.nextLine());
//            switch (choice) {
//                case 1:
//                    bookTicket(scanner);
//                    break;
//                case 2:
//                    viewBookings();
//                    break;
//                case 3:
//                    System.out.println("Exiting...");
//                    scanner.close();
//                    System.exit(0);
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please select a valid option.");
//            }}
//            catch (NumberFormatException e) {
//                System.out.println("Invalid input. Please enter a numeric choice.");
//            }}
//    }

    private static String getValidTravelDate(Scanner scanner) {
        boolean validDate = false;
        String travelDate = null;

        while (!validDate) {
            System.out.print("Enter travel date (DD/MM/YY): ");
            travelDate = scanner.nextLine();
            validDate = validateDate(travelDate);

            if (!validDate) {
                System.out.println("Invalid date format. Please use DD/MM/YY format.");
            }
        }

        return travelDate;
    }
    private static void getValidTrainAndSeat(Scanner scanner, String travelDate, String passengerName) {
        boolean seatNumberTaken = true;
        String trainNumber = null;
        String seatNumber = null;

        while (seatNumberTaken) {
            System.out.print("Enter Train number: ");
            trainNumber = scanner.nextLine();
            System.out.print("Enter seat number: ");
            seatNumber = scanner.nextLine();

            if (isSeatNumberAlreadyTaken(seatNumber, trainNumber, travelDate)) {
                System.out.println("Seat number " + seatNumber + " is already taken on train " + trainNumber + ". Please choose another seat.");
                // Return without creating the booking
            } else {
                seatNumberTaken = false;
            }
        }

        Booking booking = new Booking(bookingIdCounter, passengerName, travelDate, seatNumber, trainNumber);
        saveBookingToFile(booking);

        System.out.println("Booking successful. Your booking ID is: " + bookingIdCounter);
        bookingIdCounter++;
    }


    private static void bookTicket(Scanner scanner) {
        System.out.print("Enter your name: ");
        // Consume the newline character
        String passengerName = scanner.nextLine();

        String travelDate = getValidTravelDate(scanner);
        getValidTrainAndSeat(scanner, travelDate, passengerName);
    }

    private static boolean isSeatNumberAlreadyTaken(String seatNumber, String trainNumber, String travelDate) {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookingData = line.split("\\|");
                String storedTrainNumber = bookingData[3]; // Index should be 2, since it's zero-indexed
                String storedSeatNumber = bookingData[4]; // Index should be 3
                String storedTravelDate = bookingData[2]; // Assuming seat number is stored at index 4
                if (trainNumber.equals(storedTrainNumber) && seatNumber.equals(storedSeatNumber) && travelDate.equals(storedTravelDate) ) {
                    System.out.println("Please try again later or this seat is booked.");
                    return true; // Seat number is already taken on the specified train
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while checking seat availability: " + e.getMessage());

        }
        return false; // Seat number is available
    }
    private static boolean validateDate(String date) {
        if (date.isEmpty()) {
            System.out.println("Date cannot be empty.");
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);

        try {
            Date enteredDate = dateFormat.parse(date);
            Date currentDate = new Date(); // Get today's date

            if (enteredDate.equals(currentDate)) {
                System.out.println("Can't make a reservation for today.");
                return false;
            } else if (enteredDate.before(currentDate)) {
                System.out.println("Past date is not acceptable.");
                return false;
            }

            return true;
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use DD/MM/YY format.");

            return false;
        }
    }

    private static void saveBookingToFile(Booking booking) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKINGS_FILE_PATH, true))) {
            writer.write(
                    booking.getBookingId() + "|" +
                            booking.getPassengerName() + "|" +
                            booking.getTravelDate() + "|" +
                            booking.getSeatNumber() + "|" +
                            booking.getTrainNumber() + "\n"
            );
        } catch (IOException e) {
            System.out.println("An error occurred while saving the booking: " + e.getMessage());
        }
    }

    private static void viewBookings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE_PATH))) {
            String line;
            boolean hasBookings = false;
            while ((line = reader.readLine()) != null) {
                hasBookings = true;
                String[] bookingData = line.split("\\|");
                Booking booking = new Booking(
                        Integer.parseInt(bookingData[0]),
                        bookingData[1],
                        bookingData[2],
                        bookingData[3],
                        bookingData[4]);

                System.out.println(booking);
            }
            if (!hasBookings) {
                System.out.println("No bookings done yet!");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while viewing bookings: " + e.getMessage());
        }
    }}


