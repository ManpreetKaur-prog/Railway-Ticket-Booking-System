package org.example;

import java.io.*;
import java.util.*;
//My logic:
//1) Give error msg if no bookings done if we try to view bookings!
//2) Validate date of booking
//3) File exception handling in case of IO error
//4) Check if date is of past which is not accdeptable.
//5) Exception handling in choice
class Booking {
    private int bookingId;
    private String passengerName;
    private String travelDate;
    private String seatNumber;
    private String trainNumber;


    public Booking(int bookingId, String passengerName, String travelDate, String seatNumber,String trainNumber) {
        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.travelDate = travelDate;
        this.seatNumber = seatNumber;
        this.trainNumber = trainNumber;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getTrainNumber() {
        return trainNumber;
    }
    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
                "\nPassenger Name: " + passengerName +
                "\nTravel Date: " + travelDate +
                "\nSeat Number: " + seatNumber +
                "\nTrain Number: " + trainNumber +"\n";
    }
}
