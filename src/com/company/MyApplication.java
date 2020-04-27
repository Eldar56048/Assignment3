package com.company;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.InputMismatchException;

public class MyApplication {
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Friends> friends = new ArrayList<Friends>();
    private ArrayList<Message> messages = new ArrayList<Message>();
    private Scanner sc = new Scanner(System.in);
    private boolean signedUser;
    private String username;
    ArrayList<Integer> friendsID = new ArrayList<Integer>();
    private int profileid;


    private void fillusers() throws FileNotFoundException {

        File file = new File("db.txt");
        Scanner sc = new Scanner(file);
        int id;
        String name, surname, username, password;
        User person;
        while (sc.hasNext()) {
            id = sc.nextInt();
            name = sc.next();
            surname = sc.next();
            username = sc.next();
            password = sc.next();
            person = new User(id, name, surname, username, password);
            users.add(person);
        }
    }

    private void fillfriends() throws FileNotFoundException {
        File file = new File("friends.txt");
        Scanner sc = new Scanner(file);
        int senderid, recieverid;
        String status, request;
        Friends friend;
        while (sc.hasNext()) {
            senderid = sc.nextInt();
            recieverid = sc.nextInt();
            status = sc.next();
            request = sc.next();
            friend = new Friends(senderid, recieverid, status, request);
            friends.add(friend);
        }
    }

    private void fillmessages() throws FileNotFoundException {
        File file = new File("messages.txt");
        Scanner sc = new Scanner(file);
        int senderid, recieverid, messageid;
        String message = "", message2, date = "", time;
        Message message1;
        while (sc.hasNext()) {
            message="";

            senderid = sc.nextInt();
            recieverid = sc.nextInt();
            messageid = sc.nextInt();
            while (sc.hasNext()) {
                message2 = sc.next();
                if (!checktext(message2)) {
                    date = message2;
                    break;
                }
                message = message + " " + message2;
            }
            time = sc.next();
            message1 = new Message(senderid, recieverid, messageid, message, date, time);
            messages.add(message1);
        }
    }

    private boolean checktext(String message) {
        if (message.length() == 10 && message.indexOf('/') == 2) {
            return false;
        }
        return true;
    }

    private void menu() throws IOException {
        while (true) {
            if (signedUser == false) {
                System.out.println("You are not signed in.");
                System.out.println("1. Authentication");
                System.out.println("2. Back");
                int choice = sc.nextInt();
                if (choice == 1) authentication();
                else start();
            } else {
                userProfile();
            }
            break;
        }
    }

    private void userProfile() throws IOException {
        profileid = profileid();
        System.out.println("Your profile");
        while (true) {
            System.out.println("1. Exit from profile");
            System.out.println("2. Back");
            System.out.println("3. Change Username");
            System.out.println("4. Change Password");
            System.out.println("5. Delete Account");
            System.out.println("6. Go to the menu");
            System.out.println("7. My friends");
            System.out.println("8. Add friends");
            System.out.println("9. Delete friends");
            System.out.println("10. View applications");
            System.out.println("11. Chat");

            int check = sc.nextInt();
            if (check == 1) {
                logOff();
                menu();
            } else if (check == 2) {
                menu();
            } else if (check == 3) {
                while (true) {
                    System.out.println("Enter the new Username");
                    String newusername = sc.next();
                    if (checkusername(newusername)) {
                        System.out.println("This username is busy!");
                        continue;
                    }
                    changeUsername(username, newusername);
                    System.out.println("Your new username: " + newusername);
                    userProfile();
                }
            } else if (check == 4) {

                while (true) {
                    System.out.println("Enter the new Password");
                    String newpassword = sc.next();
                    if (newpassword.length() < 9) {
                        System.out.println("The length of password should be more than 9 symbols");
                        continue;
                    }
                    changepassword(username, newpassword);
                    System.out.println("Your Password Changed");
                    System.out.println("Your new password: ");
                    System.out.println("1. Profile");
                    System.out.println("2. Exit from profile");
                    check = sc.nextInt();
                    if (check == 1) {
                        userProfile();
                    } else if (check == 2) {
                        logOff();
                        authentication();
                    }
                }

            } else if (check == 5) {
                deleteaccount(username);
                System.out.println("Your account deleted");
                logOff();
                menu();
            } else if (check == 6) {

                start();
            } else if (check == 7) {

                myfriends();
            } else if (check == 8) {

                while (true) {
                    System.out.println("1. Search the user");
                    System.out.println("2. Show all the users");
                    check = sc.nextInt();
                    if (check == 1) {
                        while (true) {

                            System.out.println("Enter the name of the person: ");
                            String name = sc.next();
                            System.out.println("Enter the surname of the person: ");
                            String surname = sc.next();
                            System.out.println("Searching");
                            int count = 0;
                            for (int i = 0; i < users.size(); i++) {
                                if (users.get(i).getName().equals(name) && users.get(i).getSurname().equals(surname) && users.get(i).getId() != profileid) {
                                    count++;
                                }
                            }
                            if (count == 0) {
                                System.out.println("No such person found");
                            } else {
                                System.out.println("Found " + count + " person");
                                for (int i = 0; i < users.size(); i++) {
                                    if (users.get(i).getName().equals(name) && users.get(i).getSurname().equals(surname) && profileid != users.get(i).getId()) {
                                        System.out.println("id: " + users.get(i).getId() + " name: " + users.get(i).getName() + " Surname: " + users.get(i).getSurname());
                                    }
                                }
                                System.out.println("1. Add the person who found");
                                System.out.println("2. Look for another person");
                                System.out.println("3. Back to UserProfile");
                                check = sc.nextInt();
                                if (check == 1) {
                                    System.out.println("Enter the id of person: ");
                                    int recieverid = sc.nextInt();
                                    System.out.println(profileid);
                                    profileid();
                                    System.out.println(profileid);
                                    int senderid = profileid;
                                    String status = "Other", request;
                                    System.out.println("1. Best friend");
                                    System.out.println("2. Namesake");
                                    System.out.println("3. Classmates");
                                    System.out.println("4. Groupmates");
                                    System.out.println("5. Relative");
                                    System.out.println("6. Other");
                                    while (true) {
                                        System.out.println("Choose a status for a friend:");
                                        check = sc.nextInt();
                                        if (check == 1) {
                                            status = "Bestfriend";
                                        } else if (check == 2) {
                                            status = "Namesake";
                                        } else if (check == 3) {
                                            status = "Classmates";
                                        } else if (check == 4) {
                                            status = "Groupmates";
                                        } else if (check == 5) {
                                            status = "Relative";
                                        } else if (check == 6) {
                                            status = "Other";
                                        } else {
                                            continue;
                                        }


                                        request = "notaccepted";

                                        if (checkfriends(profileid, recieverid)) {
                                            System.out.println("You have already sent an application");
                                            continue;
                                        } else if (!checkfriends(profileid, recieverid)) {


                                            Friends friend;


                                            for (int i = 0; i < users.size(); i++) {
                                                if (users.get(i).getId() == recieverid) {
                                                    friend = new Friends(senderid, recieverid, status, request);
                                                    friends.add(friend);
                                                }
                                            }
                                            System.out.println("Your application sended");
                                            clearfriends();
                                            overwritefriends();
                                            userProfile();
                                        }
                                    }
                                } else if (check == 2) {
                                    continue;
                                } else if (check == 3) {
                                    userProfile();
                                } else {

                                }

                            }
                        }
                    } else if (check == 2) {
                        for (int i = 0; i < users.size(); i++) {
                            if (profileid != users.get(i).getId()) {
                                System.out.println(users.get(i).getId() + " " + users.get(i).getName() + " " + users.get(i).getSurname());
                            }
                        }
                        System.out.println("1. Back");
                        System.out.println("2. Add the person");
                        check = sc.nextInt();
                        if (check == 1) {
                            continue;
                        } else if (check == 2) {
                            System.out.println("Enter the id of person: ");
                            int recieverid = sc.nextInt();
                            int senderid = profileid;
                            String status = "Other", request;
                            System.out.println("1. Best friend");
                            System.out.println("2. Namesake");
                            System.out.println("3. Classmates");
                            System.out.println("4. Groupmates");
                            System.out.println("5. Relative");
                            System.out.println("6. Other");
                            while (true) {
                                System.out.println("Choose a status for a friend:");
                                check = sc.nextInt();
                                if (check == 1) {
                                    status = "Bestfriend";
                                } else if (check == 2) {
                                    status = "Namesake";
                                } else if (check == 3) {
                                    status = "Classmates";
                                } else if (check == 4) {
                                    status = "Groupmates";
                                } else if (check == 5) {
                                    status = "Relative";
                                } else if (check == 6) {
                                    status = "Other";
                                } else {
                                    continue;
                                }


                                request = "notaccepted";
                                if (checkfriends(profileid, recieverid)) {
                                    System.out.println("You have already sent an application");
                                } else if (!checkfriends(profileid, recieverid)) {


                                    Friends friend;


                                    for (int i = 0; i < users.size(); i++) {
                                        if (users.get(i).getId() == recieverid) {
                                            friend = new Friends(senderid, recieverid, status, request);
                                            friends.add(friend);
                                        }
                                    }
                                    System.out.println("Your application to friend send");
                                    clearfriends();
                                    overwritefriends();
                                    userProfile();
                                    System.out.println(friends);
                                }
                            }
                        }

                    }
                }
            } else if (check == 9) {
                deletefriend();
            } else if (check == 10) {
                seetheapplication();
            } else if (check == 11) {
                Chat();
            }
        }
    }

    private int profileid() {
        int id = 0;
        for (int i = 0; i < users.size(); i++) {
            if (username.equals(users.get(i).getUsername())) {
                id = users.get(i).getId();
            }
        }
        return id;
    }

    private String aboutUser(int id) {
        String text = null;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                text = "id: " + users.get(i).getId() + " " + users.get(i).getName() + " " + users.get(i).getSurname();
            }
        }
        return text;
    }

    private void Chat() throws IOException {
        int friendid = 0;
        String friendname = "";
        int check = 0;
        while (true) {
            myfriends();
            System.out.println("Your Chats: ");
            for (int i = 0; i < friendsID.size(); i++) {
                System.out.println(aboutUser(friendsID.get(i)));
            }
            System.out.println("1. Go to the chat");
            System.out.println("2. Back");
            check = sc.nextInt();
            if (check == 1) {
                System.out.println("Enter the id of the person you want to talk to: ");
                friendid = sc.nextInt();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId() == friendid) {
                        friendname = users.get(i).getName();
                    }
                }

                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getSenderid() == profileid && messages.get(i).getReceiverid() == friendid) {

                        System.out.println("     " + messages.get(i).getMessageid() + "                You: " + messages.get(i).getMessage() + "  Date: " + messages.get(i).getDate() + " Time: " + messages.get(i).getTime());
                    } else if (messages.get(i).getSenderid() == friendid && messages.get(i).getReceiverid() == profileid) {
                        System.out.println(friendname + ": " + messages.get(i).getMessage() + "  Date: " + messages.get(i).getDate() + " Time: " + messages.get(i).getTime());
                    }
                }
                System.out.println("1. New message");
                System.out.println("2. Edit the message");
                System.out.println("3. Delete the message");
                System.out.println("4. Back");
                while (true) {
                    check = sc.nextInt();
                    if (check == 1) {
                        addMessage(profileid, friendid);
                    } else if (check == 2) {
                        editMessage();
                    } else if (check == 3) {
                        deleteMessage();
                    } else if (check == 4) {
                        Chat();
                    }
                }
            } else if (check == 2) {
                userProfile();
            } else continue;
        }
    }

    private void addMessage(int senderid, int receiverid) throws IOException {
        sc.nextLine();
        System.out.print("Enter the Message: ");
        String Message = sc.nextLine();
        int lastid = messages.get(messages.size() - 1).getMessageid();
        int messageid = ++lastid;
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDate localDate = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        String Time = dtf.format(now);
        String Date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate);
        String acc = "a";
        Message message = new Message(senderid, receiverid, messageid, Message, Date, Time, acc);
        messages.add(message);
        System.out.println("Your message send");
        clearmessage();
        overwritemessages();
        Chat();
    }

    private void editMessage() throws IOException {

        System.out.println("Enter the Message ID: ");
        int messageid = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the new message: ");
        String newMessage = sc.nextLine();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessageid() == messageid) {
                messages.get(i).setMessage(newMessage);
            }
        }
        System.out.println("Your message changed");
        clearmessage();
        overwritemessages();
        Chat();
    }

    private void deleteMessage() throws IOException {
        System.out.println("Enter the Message ID: ");
        int messageid = sc.nextInt();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessageid() == messageid) {
                messages.remove(i);
            }
        }
        System.out.println("Your message deleted");
        clearmessage();
        overwritemessages();
        Chat();
    }

    private void seetheapplication() throws IOException {
        profileid();
        while (true) {
            System.out.println("1. The application that I sent");
            System.out.println("2. Friend requests");
            System.out.println("3. Back");
            int check = sc.nextInt();
            int friendid = 0;
            if (check == 1) {
                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).getSenderid() == profileid && friends.get(i).getRequest().equals("notaccepted")) {
                        friendid = friends.get(i).getRecieverid();
                        System.out.println("You send application to " + aboutUser(friendid));
                    }
                }
                userProfile();
            } else if (check == 2) {
                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).getRecieverid() == profileid) {
                        friendid = friends.get(i).getSenderid();
                        System.out.println(aboutUser(friendid) + " send to you application");
                    }
                }
                System.out.println("1. Accept Application");
                System.out.println("2. Back");
                check = sc.nextInt();
                if (check == 1) {

                    System.out.println("Enter the id of the person: ");
                    friendid = sc.nextInt();
                    for (int i = 0; i < friends.size(); i++) {
                        if (friends.get(i).getSenderid() == friendid && friends.get(i).getRecieverid() == profileid) {
                            friends.get(i).setRequest("accepted");
                            System.out.println("You accept the application");
                        }
                    }
                    clearfriends();
                    overwritefriends();
                    System.out.println("1. Accept application yet");
                    System.out.println("2. Back");
                    while (true) {
                        check = sc.nextInt();
                        if (check == 1) {
                            continue;
                        } else if (check == 2) {
                            userProfile();
                        } else {
                            System.out.println("1. Accept application yet");
                            System.out.println("2. Back");
                        }
                    }

                }
            } else if (check == 3) {
                userProfile();
            } else {
                continue;
            }
        }
    }

    private void deletefriend() throws IOException {
        myfriends();
        profileid();
        while (true) {
            System.out.println("1. Delete the friend");
            System.out.println("2. Back");
            int check = sc.nextInt();
            if (check == 1) {
                System.out.println("Enter the id of the person you want to delete: ");
                int deletefriendid = sc.nextInt();
                for (int i = 0; i < friends.size(); i++) {
                    if ((friends.get(i).getSenderid() == profileid && friends.get(i).getRecieverid() == deletefriendid && friends.get(i).getRequest().equals("accepted")) || (friends.get(i).getRecieverid() == profileid && friends.get(i).getSenderid() == deletefriendid && friends.get(i).getRequest().equals("accepted"))) {
                        friends.remove(i);
                        System.out.println("Your friend deleted");
                    }
                }
                clearfriends();
                overwritefriends();
                userProfile();
            } else if (check == 2) {
                userProfile();
            } else {
                continue;
            }
        }
    }

    private void myfriends() {
        int profileid = 0;
        for (int i = 0; i < users.size(); i++) {
            if (username.equals(users.get(i).getUsername())) {
                profileid = users.get(i).getId();
            }
        }



        for (int i = 0; i < friends.size(); i++) {

            if (profileid == friends.get(i).getSenderid() && friends.get(i).getRequest().equals("accepted")) {
                System.out.println(aboutUser(friends.get(i).getRecieverid()));
            } else if (profileid == friends.get(i).getRecieverid() && friends.get(i).getRequest().equals("accepted")) {
                System.out.println(aboutUser(friends.get(i).getSenderid()));
            }
        }
    }






    private void logOff() {
        signedUser = false;
    }

    private void deleteaccount(String username) throws IOException {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                users.remove(i);
            }
        }
        clear();
        overwrite();
    }

    private boolean checkfriends(int profileid, int recieverid) {
        for (int i = 0; i < friends.size(); i++) {
            if ((friends.get(i).getRecieverid() == recieverid && friends.get(i).getSenderid() == profileid) || (friends.get(i).getSenderid() == recieverid && friends.get(i).getRecieverid() == profileid)) {
                return true;
            }
        }
        return false;
    }

    private void changepassword(String username, String newpassword) throws IOException {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                users.get(i).setPassword(newpassword);
            }
        }
        clear();
        overwrite();
    }

    private void changeUsername(String Username, String newUsername) throws IOException {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                users.get(i).setUsername(newUsername);
                System.out.println(users.get(i).getUsername() + " " + users.get(i).getPassword());
            }
        }
        clear();
        overwrite();
    }

    private void authentication() throws IOException {
        while (true) {
            System.out.println("1. Sign in");
            System.out.println("2. Sign up");
            System.out.println("3. Back");
            int choice = sc.nextInt();
            if (choice == 1) {
                signIn();
            } else if (choice == 2) {
                signUp();
            }
            break;
        }
    }


    private void signIn() throws IOException {
        while (true) {
            System.out.print("Enter your username: ");
            String username = sc.next();

            if (checkusername(username)) {
                while (true) {
                    System.out.println(username);
                    System.out.print("Enter your password");
                    String password = sc.next();
                    if (checkpassword(username, password)) {
                        signedUser = true;
                        this.username = username;
                        userProfile();
                        break;

                    } else {
                        System.out.println("Wrong Password!");
                        continue;

                    }

                }
            } else {
                System.out.println("Wrong Username!");
                continue;
            }

            break;
        }
    }

    private boolean checkusername(String username) {
        int count = 0;
        for (int i = 0; i < users.size(); i++) {
            if (username.equals(users.get(i).getUsername()))
                count++;
        }
        if (count == 0) {
            return false;
        }
        return true;
    }

    private boolean checkpassword(String username, String password) {
        int count = 0;
        for (int i = 0; i < users.size(); i++) {
            if (username.equals(users.get(i).getUsername()) && password.equals(users.get(i).getPassword())) {
                count++;
            }
        }
        if (count == 0) return false;
        return true;
    }


    private void signUp() throws IOException {

        System.out.println("Enter the Name: ");
        String name = sc.next();
        System.out.println("Enter the Surname: ");
        String surname = sc.next();
        while (true) {
            System.out.println("Enter the New Username: ");
            String username = sc.next();
            if (!checkusername(username)) {
                while (true) {
                    System.out.println("Enter the New Password: ");
                    String password = sc.next();
                    if (password.length() < 9) {

                        System.out.println("The length of password should be more than 9 symbols");
                        continue;

                    }
                    System.out.println("Repeat the Password: ");
                    String repassword = sc.next();

                    if (checktwopassword(password, repassword)) {
                        int lastid = users.get(users.size() - 1).getId();
                        String acc = "a";
                        User person = new User(acc, lastid, name, surname, username, password);
                        users.add(person);
                        int size = users.size();
                        addUser(size);
                        System.out.println("Your Account Created");
                        authentication();
                        break;
                    } else {
                        System.out.println("Two passwords do not match");
                        continue;
                    }

                }
            } else {
                System.out.println("A user with this login is already taken!");
                continue;
            }


        }
    }

    private void clear() {

        File file = new File("db.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(" ");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void overwrite() throws IOException {
        int size = users.size();
        File file = new File("db.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            for (int i = 0; i < size; i++) {
                String data = users.get(i).getId() + " " + users.get(i).getName() + " " + users.get(i).getSurname() + " " + users.get(i).getUsername() + " " + users.get(i).getPassword() + "\n";
                writer.write(data);
                writer.flush();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void clearfriends() {
        File file = new File("friends.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(" ");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void overwritefriends() throws IOException {
        int size = friends.size();
        File file = new File("friends.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            for (int i = 0; i < size; i++) {
                String data = friends.get(i).getSenderid() + " " + friends.get(i).getRecieverid() + " " + friends.get(i).getStatus() + " " + friends.get(i).getRequest() + "\n";
                writer.write(data);
                writer.flush();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void clearmessage() {
        File file = new File("messages.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(" ");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void overwritemessages() throws IOException {
        int size = messages.size();
        File file = new File("messages.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            for (int i = 0; i < size; i++) {
                String data = messages.get(i).getSenderid() + " " + messages.get(i).getReceiverid() + " " + messages.get(i).getMessageid() + " "+ messages.get(i).getMessage() +" "+ messages.get(i).getDate()+" "+messages.get(i).getTime()+"\n";
                writer.write(data);
                writer.flush();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void database() {
        for (int i = 0; i < users.size(); i++) {
            String data = users.get(i).getId() + " " + users.get(i).getName() + " " + users.get(i).getSurname() + " " + users.get(i).getUsername() + " " + users.get(i).getPassword();
            System.out.println(data);
        }
    }

    private void addUser(int size) throws IOException {
        File file = new File("db.txt");
        try {
            String data = users.get(size - 1).getId() + " " + users.get(size - 1).getName() + " " + users.get(size - 1).getSurname() + " " + users.get(size - 1).getUsername() + " " + users.get(size - 1).getPassword() + "\n";

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(data);
            writer.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private boolean checktwopassword(String password, String repassword) {
        if (!password.equals(repassword)) {
            return false;
        }
        return true;
    }

    public void begin() throws IOException {
        fillusers();
        fillfriends();
        fillmessages();
        start();
    }

    private void start() throws IOException {
        Date date = new Date();
        while (true) {
            System.out.println("Welcome to my application!");
            System.out.println("Select command:");
            System.out.println("1. My profile");
            System.out.println("2. Exit");
            int choice = sc.nextInt();
            if (choice == 1) {
                menu();
            } else if (choice == 2) {
                break;
            }
        }

    }

}
