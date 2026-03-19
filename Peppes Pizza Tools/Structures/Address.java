package Structures;

public class Address {
    private String streetName;
    private Integer streetNumber;
    private Integer areaCode;
    private String areaName;
    private MapCoordinate coordinate;


    /**
     * Stores the address of a customer
     * @param streetName
     * @param streetNumber
     * @param areaCode
     * @param areaName
     * @param coordinate
     */
    public Address(String streetName, Integer streetNumber, Integer areaCode, String areaName, MapCoordinate coordinate) {
        this.streetName = streetName;
        this. streetNumber = streetNumber;
        this.areaCode = areaCode;
        this.areaName = areaName;
        this.coordinate = coordinate;
    }

    //TODO: Maybe override ToString method to make the visual representation better

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public MapCoordinate getMapCoordinate() {
        return coordinate;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
