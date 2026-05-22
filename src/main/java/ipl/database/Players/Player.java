package ipl.database.Players;

import ipl.database.Clubs.Club;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    String name, country, club, position;
    int age, number, weeklySalary;
    double height;

    public Player(String name, String country, int age, double height, String club, String position, int number, int weeklySalary) {
        this.height = height;
        this.weeklySalary = weeklySalary;
        this.number = number;
        this.age = age;
        this.position = position;
        this.club = club;
        this.country = country;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club){
        this.club = club;
    }

    public String getPosition() {
        return position;
    }

    public int getAge() {
        return age;
    }

    public int getWeeklySalary() {
        return weeklySalary;
    }

    public int getNumber() {
        return number;
    }

    public double getHeight() {
        return height;
    }

    public void printPlayerDetails(){
        System.out.println("Name           : " + name);
        System.out.println("Age            : " + age);
        System.out.println("Country        : " + country);
        System.out.println("Club           : " + club);
        System.out.println("Position       : " + position);
        System.out.println("Number         : " + ((number > 0)?number:"N/A"));
        System.out.println("Weekly Salary  : " + weeklySalary);
        System.out.println("Height         : " + height + " meters");
        System.out.println();
    }

    @Override
    public String toString(){
        return name+","+country+","+ age +","+height+","+club+","+position+","+((number!=-1)?number:"")+","+weeklySalary;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
