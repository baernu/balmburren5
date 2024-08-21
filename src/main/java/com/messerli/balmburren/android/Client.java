package com.messerli.balmburren.android;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String name;
    private String geopoint;
    private String milk;
    private String eggs;
    private String isDelivered;
    private String address;
    private String position;
    private String text;
    private String date;
    private String keys;
}
//
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({
//        "name",
//        "geopoint",
//        "milk",
//        "isDelivered",
//        "address",
//        "position",
//        "text"
//})
//public class Client {
//    @JsonProperty("name")
//    public String name;
//    @JsonProperty("geopoint")
//    public String geopoint;
//    @JsonProperty("milk")
//    public String milk;
//    @JsonProperty("eggs")
//    public String eggs;
//    @JsonProperty("isdelivered")
//    public String isdelivered;
//    @JsonProperty("address")
//    public String address;
//    @JsonProperty("position")
//    public String position;
//    @JsonProperty("text")
//    public String text;
//
//    @JsonCreator
//    public Client(
//            @JsonProperty("name") String name,
//            @JsonProperty("geopoint") String geopoint,
//            @JsonProperty("milk") String milk,
//            @JsonProperty("eggs") String eggs,
//            @JsonProperty("isdelivered") String isdelivered,
//            @JsonProperty("address") String address,
//            @JsonProperty("position") String position,
//            @JsonProperty("text") String text) {
//        this.name = name;
//        this.milk = milk;
//        this.eggs = eggs;
//        this.geopoint = geopoint;
//        this.isdelivered = isdelivered;
//        this.address = address;
//        this.position = position;
//        this.text = text;
//    }
//
//    @JsonGetter("name")
//    public String getName() {
//        return this.name;
//    }
//    @JsonSetter("name")
//    public void setName(String name) {
//        this.name = name;
//    }
//    @JsonGetter("geopoint")
//    public String getGeopoint() {
//        return this.geopoint;
//    }
//    @JsonSetter("geopoint")
//    public void setGeopoint(String geopoint) {
//        this.geopoint = geopoint;
//    }
//    @JsonGetter("milk")
//    public String getMilk() {
//        return this.milk;
//    }
//    @JsonSetter("mild")
//    public void setMilk(String milk) {
//        this.milk = milk;
//    }
//    @JsonGetter("eggs")
//    public String getEggs() {
//        return this.eggs;
//    }
//    @JsonSetter("eggs")
//    public void setEggs(String eggs) {
//        this.eggs = eggs;
//    }
//    @JsonGetter("address")
//    public String getAddress() {
//        return this.address;
//    }
//    @JsonSetter("address")
//    public void setAddress(String address) {
//        this.address = address;
//    }
//    @JsonGetter("isdelivered")
//    public String getDelivered() {return this.isdelivered;}
//    @JsonSetter("isdelivered")
//    public void setDelivered(String isdelivered) {
//        this.isdelivered = isdelivered;
//    }
//    @JsonGetter("position")
//    public String getPosition() {return this.position;}
//    @JsonSetter("position")
//    public void setPosition(String position) {
//        this.position = position;
//    }
//    @JsonGetter("text")
//    public String getText() {return this.text;}
//    @JsonSetter("text")
//    public void setText(String text) {
//        this.text = text;
//    }


//}
