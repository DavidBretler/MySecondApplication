package com.example.mysecondapplication.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Application;


@Entity (tableName = "travels")

public class Travel {

    /////////////FIELDS
    @NonNull
    @PrimaryKey
    private String travelId = "id";
    private String clientName;
    private String clientPhone;
    private String clientEmail;
    private int   numOfPassenger;
    @TypeConverters(UserLocationConverter.class)
    private UserLocation pickupAddress;
//    @TypeConverters(UserLocationConverter.class)
//    private UserLocation detentionAddress;
    @TypeConverters(RequestType.class)
    private RequestType requestType=RequestType.sent;
    @TypeConverters(DateConverter.class)
    private Date travelDate;
    @TypeConverters(DateConverter.class)
    private Date arrivalDate;
    private  boolean VIPBUS;
    @TypeConverters(CompanyConverter.class)
    private HashMap<String, Boolean> company;

    /////////////GETTERS
    @NonNull
    public String getTravelId(){  return travelId; }

    public String getClientName() {return this.clientName; }

    public String getClientPhone() {return this.clientPhone; }

    public String getClientEmail() {return this.clientEmail; }


    public int getNumOfPassenger() {    return numOfPassenger; }

    public RequestType getRequestType() { return requestType; }

    public Date getTravelDate() { return this.travelDate;}


    public Date getArrivalDate() { return this.arrivalDate;}

    public HashMap<String, Boolean> getCompany() { return this.company;}

    public UserLocation getPickupAddress() { return this.pickupAddress; }

 //   public UserLocation getDetentionAddress() { return detentionAddress; }

    public boolean isVIPBUS() { return this.VIPBUS; }

    //////////////////SETTERS
    public void setTravelId( @NonNull String id) { this.travelId=id; }

    public void setClientName(String clientName) { this.clientName = clientName; }

    public void setClientPhone(String clientPhone) { this.clientPhone = clientPhone; }

    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }

    public void setNumOfPassenger(int numOfPassenger) { this.numOfPassenger = numOfPassenger; }

    public void setPickupAddress(UserLocation pickupAddress) { this.pickupAddress = pickupAddress; }

  //  public void setDetentionAddress(UserLocation detentionAddress) { this.detentionAddress = detentionAddress; }

    public void setRequestType(RequestType requesType) { this.requestType = requesType; }

    public void setTravelDate(Date travelDate) { this.travelDate = travelDate; }

    public void setArrivalDate(Date arrivalDate) { this.arrivalDate = arrivalDate; }

    public void setVIPBUS(boolean VIPBUS) { this.VIPBUS = VIPBUS; }

    public void setCompany(HashMap<String, Boolean> company) { this.company = company; }

    public Travel(String clientName, String clientPhone, String clientEmail, Date departingDate, Date returnDate
            ,int numOfPassenger,UserLocation  pickupAddress , UserLocation destAddress,RequestType requestType,boolean VIPBUS, HashMap<String, Boolean> company) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.clientEmail = clientEmail;
        this.travelDate = departingDate;
        this.arrivalDate = returnDate;
        this.numOfPassenger=numOfPassenger;
        this.pickupAddress=pickupAddress;
   //     this.detentionAddress = destAddress;
        this.requestType=requestType;
        this.VIPBUS=VIPBUS;
        this.company = company;
    }

    public  Travel(){}


    public static class DateConverter {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        @TypeConverter
        public Date fromTimestamp(String date) throws ParseException {
            return (date == null ? null : format.parse(date));
        }

        @TypeConverter
        public String dateToTimestamp(Date date) {
            return date == null ? null : format.format(date);
        }
    }


    public enum RequestType {
        sent(0), accepted(1), run(2), close(3),payed(4);
        private final Integer code;
        RequestType(Integer value) {
            this.code = value;
        }
        public Integer getCode() {
            return code;
        }
        @TypeConverter
        public static RequestType getType(Integer numeral) {
            for (RequestType ds : values())
                if (ds.code.equals(numeral))
                    return ds;
            return null;
        }
        @TypeConverter
        public static Integer getTypeInt(RequestType requestType) {
            if (requestType != null)
                return requestType.code;
            return null;
        }
    }

    public static class CompanyConverter {
        @TypeConverter
        public HashMap<String, Boolean> fromString(String value) {
            if (value == null || value.isEmpty())
                return null;
            String[] mapString = value.split(","); //split map into array of (string,boolean) strings
            HashMap<String, Boolean> hashMap = new HashMap<>();
            for (String s1 : mapString) //for all (string,boolean) in the map string
            {
                if (!s1.isEmpty()) {//is empty maybe will needed because the last char in the string is ","
                    String[] s2 = s1.split(":"); //split (string,boolean) to company string and boolean string.
                    Boolean aBoolean = Boolean.parseBoolean(s2[1]);
                    hashMap.put(/*company string:*/s2[0], aBoolean);
                }
            }
            return hashMap;
        }

        @TypeConverter
        public String asString(HashMap<String, Boolean> map) {
            if (map == null)
                return null;
            StringBuilder mapString = new StringBuilder();
            for (Map.Entry<String, Boolean> entry : map.entrySet())
                mapString.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
            return mapString.toString();
        }
    }
//
    public static class ListuserlocConverter {
//        UserLocationConverter userLocationConverter;
        @TypeConverter
        public String ListToString(List<UserLocation> list) {
//            userLocationConverter =new  Travel.UserLocationConverter();
//            if (list == null )
//                return null;
//            StringBuilder listString = new StringBuilder();
//
//            for (UserLocation loc : list) {
//                listString.append(userLocationConverter.asString(loc)).append(",");
//            }
//            return listString.toString() ;
            return "kuku";
        }

        @TypeConverter
        public List<UserLocation> StringToList(String value) {
//            if (value == null || value.isEmpty())
//                return null;
//            String[] listString = value.split(","); //split list into array of strings
            List<UserLocation> list = new ArrayList<UserLocation>();
            list.add(new UserLocation(10,20));

//            for (String s1 : listString) //for all (string,boolean) in the map string
//                if (!s1.isEmpty()) //is empty maybe will needed because the last char in the string is ","
//                {
//
//                    list.add(userLocationConverter.fromString(s1)); //user location
//                }
            return list;
        }

    }

    public static class UserLocationConverter extends Application {

        @TypeConverter
        public UserLocation fromString(String value) {
            if (value == null || value.equals(""))
                return null;
            double lat = Double.parseDouble(value.split(" ")[0]);
            double lang = Double.parseDouble(value.split(" ")[1]);
            return new UserLocation(lat, lang);
        }

        @TypeConverter
        public String asString(UserLocation warehouseUserLocation) {
            return warehouseUserLocation == null ? "" : warehouseUserLocation.getLat() + " " + warehouseUserLocation.getLon();
        }


    }

}
